package com.taobao.cun.auge.station.convert;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import com.taobao.cun.auge.dal.domain.LevelCourseExample.Criteria;
import com.taobao.cun.auge.station.condition.LevelCourseManageCondition;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.CourseLevelInfo;
import com.taobao.cun.auge.station.dto.LevelCourseEditDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningDto;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerOnlinePeixunStatusEnum;

public class LevelCourseConvertor {

    public static LevelCourse toLevelCourse(LevelCourseEditDto editDto) {
        if (editDto == null) {
            return null;
        }
        String courseName = editDto.getCourseName();
        String courseCode = editDto.getCourseCode();
        String grouthIndex = editDto.getGrowthIndex();
        return new LevelCourse(courseName, courseCode, grouthIndex);
    }
    
    public static List<LevelCourseEditDto> toCourseEditDto(List<LevelCourse> courseList, Map<String, CourseLevelInfo> courseLevelInfoMap){
        if(CollectionUtils.isEmpty(courseList)){
            return Lists.newArrayList();
        }
        List<LevelCourseEditDto> dtoList = Lists.newArrayList();
        for(LevelCourse course:courseList){
            LevelCourseEditDto editDto = new LevelCourseEditDto();
            editDto.setGmtCreate(course.getGmtCreate());
            editDto.setCourseCode(course.getCourseCode());
            editDto.setCourseName(course.getCourseName());
            editDto.setGrowthIndex(course.getTag());
            if(courseLevelInfoMap!=null && courseLevelInfoMap.get(course.getCourseCode())!=null){
                CourseLevelInfo info = courseLevelInfoMap.get(course.getCourseCode());
                editDto.setElectiveLevels(info.getElectiveLevels());
                editDto.setRequiredLevels(info.getRequiredLevels());
                
                Collections.sort(editDto.getRequiredLevels());
                Collections.sort(editDto.getElectiveLevels());
            }
            dtoList.add(editDto);
        }
        return dtoList;
    }
    
    public static LevelCourseExample toLevelCourseExample(LevelCourseManageCondition manageCondition){
        if(manageCondition == null){
            return new LevelCourseExample();
        }
        LevelCourseExample example = new LevelCourseExample();
        Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(manageCondition.getTag())){
            criteria.andTagEqualTo(manageCondition.getTag());
        }
        if(StringUtils.isNotBlank(manageCondition.getCourseCode())){
            criteria.andCourseCodeEqualTo(manageCondition.getCourseCode());
        }
        if(StringUtils.isNotBlank(manageCondition.getCourseName())){
            criteria.andCourseNameLike("%" + manageCondition.getCourseName() + "%");
        }
        return example;
    }
    
    public static Set<String> extractCourseCode(List<LevelCourse> courses){
        if(CollectionUtils.isEmpty(courses)){
            return Sets.newHashSet();
        }
        Set<String> codeSet = new HashSet<String>();
        for(LevelCourse lc:courses){
            if(StringUtils.isNotBlank(lc.getCourseCode())){
                codeSet.add(lc.getCourseCode());
            }
        }
        return codeSet;
    }
    
    /**
     * 转成courseCode到dto的Map
     */
    public static Map<String, PartnerPeixunDto> toCourseCodePeixunDtoMap(List<PartnerPeixunDto> peixunDtos){
        if(CollectionUtils.isEmpty(peixunDtos)){
            return Collections.emptyMap();
        }
        HashMap<String, PartnerPeixunDto> resultMap = Maps.newHashMap();
        for(PartnerPeixunDto dto:peixunDtos){
            if(dto!=null){
                resultMap.put(dto.getCourseCode(), dto);
            }
        }
        return resultMap;
    }
    
    /**
     * 将课程列表对象以及PartnerPeixunDto的课程状态和课程detail链接  一起转成对外输出的dto对象 
     */
    public static List<LevelCourseLearningDto> convertToCourseLearningDto(List<LevelCourse> courseList, 
                                                                          Map<String, PartnerOnlinePeixunDto> courseCodePeixunMap,
                                                                          Set<String> requiredCourseCodes, Set<String> electiveCourseCodes){
        if(CollectionUtils.isEmpty(courseList) ){
            return Collections.emptyList();
        }
        List<LevelCourseLearningDto> learningDtoList = Lists.newArrayList();
        for(LevelCourse course:courseList){
            LevelCourseLearningDto learningDto = new LevelCourseLearningDto();
            learningDto.setCourseName(course.getCourseName());
            learningDto.setGrowthIndex(course.getTag());
            if(requiredCourseCodes!=null && CollectionUtils.contains(requiredCourseCodes.iterator(), course.getCourseCode())) {
                learningDto.setCourseType(LevelCourseTypeEnum.REQUIRED.name());
            }else if(electiveCourseCodes!=null && CollectionUtils.contains(electiveCourseCodes.iterator(), course.getCourseCode())){
                learningDto.setCourseType(LevelCourseTypeEnum.ELECTIVE.name());
            }
            if(!CollectionUtils.isEmpty(courseCodePeixunMap) && courseCodePeixunMap.get(course.getCourseCode())!=null){
                PartnerOnlinePeixunDto partnerPeixunDto = courseCodePeixunMap.get(course.getCourseCode());
                if(partnerPeixunDto.getStatus()!=null){
                    learningDto.setStatus(partnerPeixunDto.getStatus().getCode());
                }
                learningDto.setCourseDetailUrl(partnerPeixunDto.getCourseUrl());
            }
            learningDtoList.add(learningDto);
        }
        return learningDtoList;
    }
    
    /**
     * 按照指标对list进行分组
     */
//    private static Map<String, List<LevelCourseLearningDto>> groupByGrowthIndex(List<LevelCourseLearningDto> dtoList){
//        Map<String, List<LevelCourseLearningDto>> resultMap = Maps.newHashMap();
//        if(CollectionUtils.isEmpty(dtoList)){
//            return resultMap;
//        }
//        for(LevelCourseLearningDto dto:dtoList){
//            List<LevelCourseLearningDto> tmpList = resultMap.get(dto.getGrowthIndex());
//            if(tmpList == null){
//                tmpList = Lists.newArrayList();
//                tmpList.add(dto);
//                resultMap.put(dto.getGrowthIndex(), tmpList);
//            }else{
//                tmpList.add(dto);
//            }
//        }
//        return resultMap;
//    }
    
    /**
     * 计算待学习课程数
     */
    public static int computeLearningCount(Set<String> requiredCourseList, Map<String, PartnerOnlinePeixunDto> courseStatusMap) {
        if(CollectionUtils.isEmpty(requiredCourseList) || CollectionUtils.isEmpty(courseStatusMap) ){
            return 0;
        }
        int toLearningCount = 0;
        for(String courseCode:requiredCourseList){
            PartnerOnlinePeixunDto partnerPeixunDto = courseStatusMap.get(courseCode);
            if(partnerPeixunDto!=null && partnerPeixunDto.getStatus()!=null 
                    && PartnerOnlinePeixunStatusEnum.WAIT_PEIXUN.getCode().equals(partnerPeixunDto.getStatus().getCode())) {
                toLearningCount++;
            }
        }
        return toLearningCount;
    }
    
    public static boolean isValidLevelCourse(LevelCourseEditDto course){
        return course!=null 
                && StringUtils.isNotBlank(course.getCourseCode())
                && StringUtils.isNotBlank(course.getCourseName());
    }
    
    static enum CourseStatus {
        DONE,NEW;
    }
}
