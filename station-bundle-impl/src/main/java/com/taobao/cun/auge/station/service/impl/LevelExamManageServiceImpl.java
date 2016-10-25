package com.taobao.cun.auge.station.service.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.convert.LevelExamUtil;
import com.taobao.cun.auge.station.convert.LevelExamUtil.ExamLevelExtendInfo;
import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;
import com.taobao.cun.auge.station.service.LevelExamManageService;
import com.taobao.cun.auge.station.service.LevelExamResultQueryService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;
import com.taobao.cun.crius.exam.dto.UserDispatchDto;
import com.taobao.cun.crius.exam.enums.ExamDispatchSourceEnum;
import com.taobao.cun.crius.exam.enums.ExamInstanceStatusEnum;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;

public class LevelExamManageServiceImpl implements LevelExamManageService, LevelExamResultQueryService {

    private static final Logger logger = LoggerFactory.getLogger(LevelExamManageServiceImpl.class);

    private static final LevelExamConfigurationDto EMPTY_CONFIG_OBJECT = new LevelExamConfigurationDto(); 
    private static final String LEVEL_EXAM_CONFIG = "level_exam_config";
    private static final String LEVEL_EXAM_KEY = "level_to_exam";
    
    @Autowired
    private AppResourceBO appResourceBO;
    
    @Autowired
    ExamUserDispatchService  examUserDispatchService;
    
    @Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;

    @Override
    public boolean configure(LevelExamConfigurationDto configurationDto, String configurePerson) {
        if(configurationDto == null || configurationDto.getLevelExamMap() == null || configurePerson == null){
            logger.error("invalid configuration, dto:{}, configurePerson:{}", configurationDto, configurePerson);
            return false;
        }
        LevelConfiguration configuration = new LevelConfiguration(configurationDto.isOpenEvaluate(), configurationDto.isDispatch(), configurationDto.getLevelExamMap());
        String value = JSON.toJSONString(configuration);
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY, value, false, configurePerson);
    }

    @Override
    public LevelExamConfigurationDto queryConfigure() {
        AppResource appResource =  appResourceBO.queryAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY);
        if(appResource==null || StringUtils.isBlank(appResource.getValue())){
            return EMPTY_CONFIG_OBJECT; 
        }
        LevelConfiguration configuration =  JSON.parseObject(appResource.getValue(), LevelConfiguration.class);
        return new LevelExamConfigurationDto().setLevelExamMap(configuration.getLevelExamPaperIdMap())
                .setDispatch(configuration.isDispatch())
                .setOpenEvaluate(configuration.isOpenEvaluate());
    }
    
    
    /**
     * 根据合伙人当前预估层级分发试卷:
     * 1.获取不到层级试卷配置或者配置失效则不分发试卷
     * 2.查询历史晋升试卷分发记录失败或者其他错误也部分发
     */
    @Override
    public boolean dispatchExamPaper(Long taobaoUserId, String nickName, PartnerInstanceLevel level) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(level);
        DispatchedLevelExamResult examResult  = computeDispatchExamResult(taobaoUserId, level);
        if(examResult==null) {
            logger.error("LevelExamDispatchServiceImpl query dispatch record error, taobaoUserId:{}", taobaoUserId);
            return false;
        }
        return dispatchExamPapers(taobaoUserId, nickName, examResult.getToDispatchExamLevels());
    }
    
    /**
     * 判断是否通过了本层级所有晋升考试
     * 如果没有那么打印日志
     */
    @Override
    public boolean isPassAllLevelExam(Long taobaoUserId, PartnerInstanceLevel level) {
        LevelExamConfigurationDto configurationDto = this.queryConfigure();
        if(configurationDto==null || configurationDto.isOpenEvaluate()){
            return true;
        }
        
        DispatchedLevelExamResult examResult = computeDispatchExamResult(taobaoUserId, level);
        if(examResult==null) {
            logger.error("LevelExamDispatchServiceImpl query dispatch record error, taobaoUserId:{}", taobaoUserId);
            return false;
        }
        if(!examResult.isPassAllExams){
            logger.error(" LevelExamDispatchServiceImpl taobaoUserId:{}, level:{}, not pass level exam are:{}", taobaoUserId,  level, examResult.getNotPassExamLevels());
        }
        return examResult.isPassAllExams;
    }
    
    /**
     * 根据试卷配置信息给合伙人分发指定层级的试卷
     * @param taobaoUserId 合伙人id
     * @param nickName
     * @param dispatchLevels 需要分发试卷的层级
     * @param configurationDto 试卷配置
     * @return
     */
    private boolean dispatchExamPapers(Long taobaoUserId, String nickName, List<PartnerInstanceLevel>dispatchLevels){
        if(CollectionUtils.isEmpty(dispatchLevels)){
            return true;
        }
        LevelExamConfigurationDto configurationDto = this.queryConfigure();
        if(configurationDto==null || configurationDto.isDispatch()){
            logger.error("LevelExamDispatchServiceImpl LevelExamConfigurationDto is null or is deleted, configurationDto:{}", configurationDto);
            return false;
        }
        for(PartnerInstanceLevel level:dispatchLevels){
            Long paperId = configurationDto.getExamPaperId(PartnerInstanceLevel.valueOf(level.name()));
            String extendInfo = JSON.toJSONString(new ExamLevelExtendInfo(level.name()));
            ExamDispatchDto newExamDispatchDto = newExamDispatchDto(taobaoUserId, nickName, paperId, extendInfo, "PartnerServicingLevelExamDispatchStrategy");
            ResultModel<Boolean> result = examUserDispatchService.dispatchExam(newExamDispatchDto);
            if (result == null || !result.isSuccess() || result.getResult() == Boolean.FALSE) {
                logger.error("PartnerServicingLevelExamDispatchStrategy taobaoUserId:{}, dispatch paper:{}",taobaoUserId, paperId);
                return false;
            }
        }
        return true;
    }
    
    /**
    * 根据当前层级应该通过哪些晋升考试 以及已经分发试卷考试情况计算出
    * 1.是否通过所有level层级晋升需要通过的所有考试
    * 2.还没有分发试卷的晋升层级
    */
   private DispatchedLevelExamResult computeDispatchExamResult (Long taobaoUserId, PartnerInstanceLevel level){
       ResultModel<List<ExamDispatchDto>>result = examUserDispatchService.listExamDispatchDto(taobaoUserId, ExamDispatchSourceEnum.promotion, 0, 100);
       if(result==null || !result.isSuccess()){
           return null;
       }
       List<ExamDispatchDto> dispatchedExamList = result.getResult();
       if(CollectionUtils.isEmpty(dispatchedExamList)){
           return new DispatchedLevelExamResult(true, true);
       }
       List<PartnerInstanceLevel> shouldPassExamLevels = LevelExamUtil.computeShouldTakeExamList(level);
       List<PartnerInstanceLevel> passedLevels = Lists.newArrayList(), notPassExamLevels = Lists.newArrayList();
       Map<PartnerInstanceLevel, Long> dispatchedExamLevelAndPaper = LevelExamUtil.parseDispatchedExamLevels(dispatchedExamList);
       for(Map.Entry<PartnerInstanceLevel, Long>entry:dispatchedExamLevelAndPaper.entrySet()){
           ResultModel<UserDispatchDto> resultModel = examUserDispatchService.queryExamUserDispatch(entry.getValue(), taobaoUserId);
           if(isPassExam(resultModel)){
               passedLevels.add(entry.getKey());
           }else {
               notPassExamLevels.add(entry.getKey());
           }
       }
       boolean isPassAllExams  = passedLevels.containsAll(shouldPassExamLevels);
       List<PartnerInstanceLevel> toDispatchLevelList = Lists.newArrayList(shouldPassExamLevels);
       toDispatchLevelList.removeAll(dispatchedExamLevelAndPaper.keySet());
       return new DispatchedLevelExamResult(isPassAllExams, toDispatchLevelList, notPassExamLevels);
   }
   
   /**
    * 是否通过此考试
    * @param resultModel
    * @return
    */
   private boolean isPassExam(ResultModel<UserDispatchDto> resultModel){
       if(resultModel!=null 
               && resultModel.isSuccess() 
               && resultModel.getResult()!=null 
               && resultModel.getResult().getCurrentInstance()!=null){
           return resultModel.getResult().getCurrentInstance().getStatus()!=null 
                   && ExamInstanceStatusEnum.PASS.getCode().equals( resultModel.getResult().getCurrentInstance().getStatus().getCode());
       }
       return false;
   }
   
    private ExamDispatchDto newExamDispatchDto(Long userId, String nickName, Long paperId, String extendInfo, String dispatcher){
        ExamDispatchDto edd =  new ExamDispatchDto();
        edd.setUserId(userId);
        edd.setTaobaoNick(nickName);
        edd.setPaperId(paperId);
        edd.setDispatcher(dispatcher);
        edd.setDispatchSource(ExamDispatchSourceEnum.promotion);
        edd.setDispatchExtendInfo(extendInfo);
        return edd;
    }
    
    static class DispatchedLevelExamResult {
        /**
         * 是否通过所在层级的所有晋升考试,存在两种情况:
         * 1.没有分发试卷,这种也算通过
         * 2.分发试卷而且考试全部通过了
         */
        private boolean isPassAllExams;
        
        private boolean notDispatchExam;
        
        /**
         * 还未分发试卷的层级
         */
        private List<PartnerInstanceLevel> toDispatchExamLevels;
        
        /**
         * 没有通过的考试层级
         */
        private List<PartnerInstanceLevel> notPassExamLevels;
        
        public DispatchedLevelExamResult(boolean notDispatchExam, boolean isPassAllExams){
            super();
            this.notDispatchExam = notDispatchExam;
            this.isPassAllExams = isPassAllExams;
        }
        
        public DispatchedLevelExamResult(boolean isPassAllExams, List<PartnerInstanceLevel> toPassExamLevels, List<PartnerInstanceLevel> notPassExamLevels) {
            super();
            this.isPassAllExams = isPassAllExams;
            this.toDispatchExamLevels = toPassExamLevels;
            this.notPassExamLevels = notPassExamLevels;
        }

        public boolean isPassAllExams() {
            return isPassAllExams;
        }
        
        public void setPassAllExams(boolean isPassAllExams) {
            this.isPassAllExams = isPassAllExams;
        }
        
        public List<PartnerInstanceLevel> getToDispatchExamLevels() {
            return toDispatchExamLevels;
        }
        
        public void setToDispatchExamLevels(List<PartnerInstanceLevel> toDispatchExamLevels) {
            this.toDispatchExamLevels = toDispatchExamLevels;
        }
        
        public List<PartnerInstanceLevel> getNotPassExamLevels() {
            return notPassExamLevels;
        }
        
        public void setNotPassExamLevels(List<PartnerInstanceLevel> notPassExamLevels) {
            this.notPassExamLevels = notPassExamLevels;
        }
        
        public boolean isNotDispatchExam() {
            return notDispatchExam;
        }

        public void setNotDispatchExam(boolean notDispatchExam) {
            this.notDispatchExam = notDispatchExam;
        }
        
    }
    
    public static class LevelConfiguration implements Serializable {

        private static final long serialVersionUID = 5209263287154537277L;
        /**
         * 是否打开开关 晋升时必须通过考试
         */
        private boolean openEvaluate;
        /**
         * 是否分发晋升试卷
         */
        private boolean dispatch;
        private Map<String, Long> levelExamPaperIdMap = new HashMap<String, Long>();
        
        public LevelConfiguration(){};
        
        public LevelConfiguration(boolean isOpenEvaluate, boolean isDispatch, Map<String, Long> levelExamPaperIdMap) {
            super();
            this.openEvaluate = isOpenEvaluate;
            this.dispatch = isDispatch;
            this.levelExamPaperIdMap = levelExamPaperIdMap;
        }

        public boolean isOpenEvaluate() {
            return openEvaluate;
        }
        
        public void setOpenEvaluate(boolean isOpenEvaluate) {
            this.openEvaluate = isOpenEvaluate;
        }
        
        public boolean isDispatch() {
            return dispatch;
        }
        
        public void setDispatch(boolean isDispatch) {
            this.dispatch = isDispatch;
        }
        
        public Map<String, Long> getLevelExamPaperIdMap() {
            return levelExamPaperIdMap;
        }
        
        public void setLevelExamPaperIdMap(Map<String, Long> levelExamPaperIdMap) {
            this.levelExamPaperIdMap = levelExamPaperIdMap;
        }
        
    }

}
