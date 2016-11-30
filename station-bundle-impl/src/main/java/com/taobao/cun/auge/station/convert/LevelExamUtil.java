package com.taobao.cun.auge.station.convert;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum.PartnerInstanceLevel;
import com.taobao.cun.crius.exam.dto.ExamDispatchDto;

public class LevelExamUtil {
    
    public static final String LEVEL_EXAM_CONFIG = "level_exam_config";
    public static final String LEVEL_EXAM_KEY = "level_to_exam_map";
    public static final String LEVEL_EXAM_EVALUATE_SWITCH = "level_to_exam_evaluate_switch";
    public static final String LEVEL_EXAM_KEY_SWITCH = "level_to_exam_dispatch_switch";

    /**
     * 层级晋升试卷分发规则:
     * S4/S5/S6分发S5 S6的试卷
     * 其他层级则分发当前层级以及以下的其他层级试卷
     */
    public static List<PartnerInstanceLevel> computeShouldTakeExamList(PartnerInstanceLevel evaluateLevel){
        PartnerInstanceLevel maxExamLevel = evaluateLevel;
        if(evaluateLevel.ordinal() <= PartnerInstanceLevel.S6.ordinal()){
            maxExamLevel = PartnerInstanceLevel.S6;
        }
        List<PartnerInstanceLevel> shouldExamLevel = Lists.newArrayList();
        for(PartnerInstanceLevel level:PartnerInstanceLevel.values()){
            if(level.ordinal() <= maxExamLevel.ordinal()){
                shouldExamLevel.add(level);
            }
        }
        return shouldExamLevel;
    } 
    
    /**
     * 解析已经分发的晋升试卷,转成晋升层级与paperId的Map
     */
    public static Map<PartnerInstanceLevel, Long> parseDispatchedExamLevels(List<ExamDispatchDto> dispatchedList) {
        if(CollectionUtils.isEmpty(dispatchedList)){
            return Collections.emptyMap();
        }
        Map<PartnerInstanceLevel, Long> dispatchedLevels = Maps.newHashMap();
        for(ExamDispatchDto dispatchDto:dispatchedList){
            String extendInfo = dispatchDto.getDispatchExtendInfo();
            if(StringUtils.isNotBlank(extendInfo)){
                ExamLevelExtendInfo examLevelInfo = JSON.parseObject(extendInfo, ExamLevelExtendInfo.class);
                PartnerInstanceLevel examLevel = PartnerInstanceLevel.valueOf(examLevelInfo.getExamLevel());
                dispatchedLevels.put(examLevel, dispatchDto.getPaperId());
            }
        }
        return dispatchedLevels;
    }

    public static class ExamLevelExtendInfo implements Serializable {
        private static final long serialVersionUID = 1359381885987160892L;
        private String examLevel;
        public ExamLevelExtendInfo(){}
        public ExamLevelExtendInfo(String examLevel) {
            super();
            this.examLevel = examLevel;
        }

        public String getExamLevel() {
            return examLevel;
        }

        public void setExamLevel(String examLevel) {
            this.examLevel = examLevel;
        }
    }
    
    public static void main(String[] args){
        System.out.print(JSON.toJSONString(new ExamLevelExtendInfo("S8")));
    }
}
