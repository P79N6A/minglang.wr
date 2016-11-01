package com.taobao.cun.auge.station.service.impl;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.convert.LevelExamUtil;
import com.taobao.cun.auge.station.convert.LevelExamUtil.ExamLevelExtendInfo;
import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.dto.LevelExamingResult;
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
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("levelExamManageService")
@HSFProvider(serviceInterface = LevelExamManageService.class)
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
    public boolean dispatchExamPaper(Long taobaoUserId, String nickName, PartnerInstanceLevel partnerLevel) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(partnerLevel);
        Map<PartnerInstanceLevel, Long> dispatchedResult  = getDispatchedPaperInfo(taobaoUserId);
        if(dispatchedResult==null) {
            logger.error("LevelExamDispatchServiceImpl query dispatch record error, taobaoUserId:{}", taobaoUserId);
            return false;
        }
        List<PartnerInstanceLevel> shouldPassExamLevels = LevelExamUtil.computeShouldTakeExamList(partnerLevel);
        shouldPassExamLevels.removeAll(dispatchedResult.keySet());
        return dispatchExamPapers(taobaoUserId, nickName, shouldPassExamLevels);
    }
    
    /**
     * 判断是否通过了本层级所有晋升考试
     * 如果没有那么打印日志
     */
    @Override
    public LevelExamingResult queryLevelExamResult(Long taobaoUserId, PartnerInstanceLevel level) {
        Map<PartnerInstanceLevel, Long> dispatchedExamLevelAndPaper  = getDispatchedPaperInfo(taobaoUserId);
        if(dispatchedExamLevelAndPaper==null) {
            logger.error("LevelExamDispatchServiceImpl query dispatch record error, taobaoUserId:{}", taobaoUserId);
            return new LevelExamingResult(false, Lists.newArrayList());
        }
        List<PartnerInstanceLevel> passedLevels = Lists.newArrayList();
        List<String> notPassExamLevels = Lists.newArrayList();
        for(Map.Entry<PartnerInstanceLevel, Long>entry:dispatchedExamLevelAndPaper.entrySet()){
            ResultModel<UserDispatchDto> resultModel = examUserDispatchService.queryExamUserDispatch(entry.getValue(), taobaoUserId);
            if(isPassExam(resultModel)){
                passedLevels.add(entry.getKey());
            }else {
                notPassExamLevels.add(entry.getKey().name());
            }
        }
        boolean isPassAllDispatchedExam = passedLevels.containsAll(dispatchedExamLevelAndPaper.keySet());
        List<PartnerInstanceLevel> allLevelExams = LevelExamUtil.computeShouldTakeExamList(level);
        boolean isPassAllLevelExam = passedLevels.containsAll(allLevelExams);
        if(!isPassAllDispatchedExam || !isPassAllLevelExam){
            allLevelExams.removeAll(passedLevels);
            logger.error(" LevelExamDispatchServiceImpl taobaoUserId:{}, level:{}, not pass dispatched level:{}, not dispatch levels:{}", taobaoUserId,  level, notPassExamLevels, allLevelExams);
        }
        return new LevelExamingResult(isPassAllDispatchedExam, notPassExamLevels);
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
        LevelExamConfigurationDto configurationDto = this.queryConfigure();
        if(configurationDto==null){
            logger.error("LevelExamDispatchServiceImpl LevelExamConfigurationDto is null");
            return false;
        }
        if(CollectionUtils.isEmpty(dispatchLevels) || !configurationDto.isDispatch()){
            return true;
        }
        for(PartnerInstanceLevel level:dispatchLevels){
            Long paperId = configurationDto.getExamPaperId(PartnerInstanceLevel.valueOf(level.name()));
            if(paperId == null){
                continue;
            }
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
    * 查询某个合伙人分发的晋升试卷<层级,paperId>
    */
   private Map<PartnerInstanceLevel, Long> getDispatchedPaperInfo (Long taobaoUserId){
       ResultModel<List<ExamDispatchDto>>result = examUserDispatchService.listExamDispatchDto(taobaoUserId, ExamDispatchSourceEnum.promotion, 0, 100);
       if(result==null || !result.isSuccess()){
           return null;
       }
       List<ExamDispatchDto> dispatchedExamList = result.getResult();
       if(CollectionUtils.isEmpty(dispatchedExamList)){
           return Collections.emptyMap();
       }
       return LevelExamUtil.parseDispatchedExamLevels(dispatchedExamList);
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
