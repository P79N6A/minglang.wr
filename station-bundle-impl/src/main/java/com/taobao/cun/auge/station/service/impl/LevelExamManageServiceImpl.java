package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
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
import com.alibaba.fastjson.TypeReference;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.convert.LevelExamUtil;
import com.taobao.cun.auge.station.convert.LevelExamUtil.ExamLevelExtendInfo;
import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;
import com.taobao.cun.auge.station.service.LevelExamManageService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;
import com.taobao.cun.crius.exam.enums.ExamDispatchSourceEnum;
import com.taobao.cun.crius.exam.service.ExamUserDispatchService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("levelExamManageService")
@HSFProvider(serviceInterface = LevelExamManageService.class, clientTimeout=10000)
public class LevelExamManageServiceImpl implements LevelExamManageService {

    private static final Logger logger = LoggerFactory.getLogger(LevelExamManageServiceImpl.class);

    private static final LevelExamConfigurationDto EMPTY_CONFIG_OBJECT = new LevelExamConfigurationDto(); 
    private static final String LEVEL_EXAM_CONFIG = "level_exam_config";
    private static final String LEVEL_EXAM_KEY = "level_to_exam_map";
    private static final String LEVEL_EXAM_EVALUATE_SWITCH = "level_to_exam_evaluate_switch";
    
    @Autowired
    private AppResourceBO appResourceBO;
    
    @Autowired
    ExamUserDispatchService  examUserDispatchService;
    
    @Autowired
    PartnerInstanceQueryService partnerInstanceQueryService;

    /**
     * 配置指定层级试卷id
     */
    @Override
    public boolean configure(LevelExamConfigurationDto configurationDto, String configurePerson) {
        if(configurationDto == null || configurationDto.getLevelExamMap() == null || configurePerson == null){
            logger.error("invalid configuration, dto:{}, configurePerson:{}", configurationDto, configurePerson);
            return false;
        }
        String value = JSON.toJSONString(configurationDto.getLevelExamMap());
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY, value, false, configurePerson);
    }

    /**
     * 查询层级试卷配置
     */
    @Override
    public LevelExamConfigurationDto queryConfigure() {
        AppResource appResource =  appResourceBO.queryAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY);
        if(appResource==null || StringUtils.isBlank(appResource.getValue())){
            return EMPTY_CONFIG_OBJECT; 
        }
        Map<String, Long> configuration =  JSON.parseObject(appResource.getValue(), new TypeReference<Map<String, Long>>(){});
        return new LevelExamConfigurationDto().setLevelExamMap(configuration);
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
     * 是否分发试卷
     * open:false表示不会分发试卷,这样晋升时不会考察是否通过考试
     */
    @Override
    public boolean configureSwitchForDispatchPaper(boolean open) {
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_KEY, null, !open, "system");
    }

    /**
     * 考试是否通过作为晋升必要条件的开关设置
     * open:true表示晋升时会查询该村小二是否通过所有相关考试
     */
    @Override
    public boolean configureSwitchForEvaluate(boolean open) {
        String switchStr = Boolean.toString(open);
        return appResourceBO.configAppResource(LEVEL_EXAM_CONFIG, LEVEL_EXAM_EVALUATE_SWITCH, switchStr, false, "system");
    }
    
    /**
     * 根据试卷配置信息给合伙人分发指定层级的试卷
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
        if(CollectionUtils.isEmpty(dispatchLevels)){
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
   
    private ExamDispatchDto newExamDispatchDto(Long userId, String nickName, Long paperId, String extendInfo, String dispatcher){
        ExamDispatchDto edd =  new ExamDispatchDto();
        edd.setUserId(userId);
        if(nickName==null){
            nickName = " ";
        }
        edd.setTaobaoNick(nickName);
        edd.setPaperId(paperId);
        edd.setDispatcher(dispatcher);
        edd.setDispatchSource(ExamDispatchSourceEnum.promotion);
        edd.setDispatchExtendInfo(extendInfo);
        return edd;
    }
    
}
