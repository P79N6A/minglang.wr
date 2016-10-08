package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ali.com.google.common.collect.Lists;
import com.ali.com.google.common.collect.Sets;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.LevelCourseBO;
import com.taobao.cun.auge.station.bo.impl.LevelCourseBOImpl;
import com.taobao.cun.auge.station.condition.LevelCourseCondition;
import com.taobao.cun.auge.station.condition.LevelCourseManageCondition;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.CourseLevelInfo;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.ResourceValueUpdateResult;
import com.taobao.cun.auge.station.convert.LevelCourseConvertor;
import com.taobao.cun.auge.station.dto.LevelCourseEditDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningStatisticsDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import com.taobao.cun.auge.station.service.LevelCourseManageService;
import com.taobao.cun.auge.station.service.LevelCourseQueryService;

public class LevelCourseServiceImpl implements LevelCourseManageService, LevelCourseQueryService {

    private static final Logger logger = LoggerFactory.getLogger(LevelCourseBOImpl.class);

    @Autowired
    private LevelCourseBO courseBo;
    
    @Autowired
    private AppResourceBO appResourceBo;
    
    @Override
    public boolean saveCourse(LevelCourseEditDto course) {
        if(!LevelCourseConvertor.isValidLevelCourse(course)){
            return false;
        }
        LevelCourseExample example = new LevelCourseExample();
        example.createCriteria().andCourseCodeEqualTo(course.getCourseCode());
        if(!CollectionUtils.isEmpty(courseBo.queryLevelCourse(example))){
            return false;
        }
        boolean isSaveSuccess = courseBo.saveLevelCourse(LevelCourseConvertor.toLevelCourse(course));
        return isSaveSuccess 
                && addLevelCourseCode(course.getRequiredLevels(), LevelCourseTypeEnum.REQUIRED, course.getCourseCode())
                && addLevelCourseCode(course.getElectiveLevels(), LevelCourseTypeEnum.ELECTIVE, course.getCourseCode());
    }

    @Override
    public boolean deleteCourse(String courseCode) {
        Assert.notNull(courseCode, "course code can not null!");
        boolean isDeleteSuccess = courseBo.deleteLevelCourse(courseCode);
         return isDeleteSuccess && removeLevelCourseCode(courseCode);
    }

    @Override
    public List<LevelCourseEditDto> queryManageLevelCourses(LevelCourseManageCondition condition) {
        if(!LevelCourseManageCondition.isValidManageCondition(condition)){
            return Collections.emptyList();
        }
        LevelCourseExample example = new LevelCourseExample();
        if(condition.isSearchByLevel()){
            Set<String> courseCodeSet = getCourseCodeList(condition.getUserLevel(), condition.getCourseType());
            if(CollectionUtils.isEmpty(courseCodeSet)){
                return Collections.emptyList();
            }
            example.createCriteria().andCourseCodeIn(new ArrayList<>(courseCodeSet));
        }else{
            example = LevelCourseConvertor.toLevelCourseExample(condition);
        }
        List<LevelCourse> levelCourseList =  courseBo.queryLevelCourse(example);
        Map<String, AppResource> resourceList = appResourceBo.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        Map<String, CourseLevelInfo> courseLevelInfoMap = LevelCourseConfigUtil.groupLevelByCourseCode(resourceList.values());
        return LevelCourseConvertor.toCourseEditDto(levelCourseList, courseLevelInfoMap);
    }

    @Override
    public LevelCourseLearningStatisticsDto getCourseLearningInfo(LevelCourseCondition condition) {
        if(!LevelCourseCondition.isValid(condition)){
            return LevelCourseLearningStatisticsDto.getNullDto();
        }
        List<LevelCourse> tagCourseList = courseBo.groupCoursesByTag(condition.getGroupCount());
        Set<String> totalCodeSet = LevelCourseConvertor.extractCourseCode(tagCourseList);
        
        String key = LevelCourseConfigUtil.getResourceKey(condition.getUserLevel(), LevelCourseTypeEnum.REQUIRED);
        AppResource resource = appResourceBo.queryAppResource(LevelCourseConfigUtil.getResourceType(), key);
        Set<String> courseCodeSet = Collections.emptySet();
        if(resource!=null){
            courseCodeSet = LevelCourseConfigUtil.parseCourseCodeSet(resource.getValue());
            totalCodeSet.addAll(courseCodeSet);
        }
        Map<String, PartnerPeixunDto> courseCodePeixunDtoMap = queryBatchOnlinePeixunProcess(condition.getUserId(), Lists.newArrayList(totalCodeSet));
        Map<String, List<LevelCourseLearningDto>> groupedLearningCourseList = LevelCourseConvertor.toGroupedLearningDtoMap(tagCourseList, courseCodePeixunDtoMap);
        int toLearningCount = LevelCourseConvertor.computeLearningCount(courseCodeSet, courseCodePeixunDtoMap);
        return new LevelCourseLearningStatisticsDto(groupedLearningCourseList, toLearningCount);
    }

    @Override
    public List<LevelCourseLearningDto> searchCourseLearningInfoByUserLevel(Long userId, String userLevel) {
        if(StringUtils.isEmpty(userLevel)){
            return Collections.emptyList();
        }
        Set<String> requiredCourseCodeSet = getCourseCodeList(userLevel, LevelCourseTypeEnum.REQUIRED);
        Set<String> electiveCourseCodeSet = getCourseCodeList(userLevel, LevelCourseTypeEnum.ELECTIVE);
        LevelCourseExample lce = new LevelCourseExample();
        List<String> totalCourseCodeList = new ArrayList<>(requiredCourseCodeSet);
        totalCourseCodeList.addAll(electiveCourseCodeSet);
        if(totalCourseCodeList.size()>0){
            lce.createCriteria().andCourseCodeIn(totalCourseCodeList);
            List<LevelCourse> levelCourseList = courseBo.queryLevelCourse(lce);
            Map<String, PartnerPeixunDto> peixunDtoMap = queryBatchOnlinePeixunProcess(userId, new ArrayList<>(totalCourseCodeList));
            return LevelCourseConvertor.convertToCourseLearningDto(levelCourseList, peixunDtoMap);
        }
        return Collections.emptyList();
    }
    
    @Override
    public List<LevelCourseLearningDto> searchCourseLearningInfoByTag(Long userId, String tag){
        if(StringUtils.isBlank(tag)){
            return Collections.emptyList();
        }

        LevelCourseExample lce = new LevelCourseExample();
        lce.createCriteria().andTagEqualTo(tag);
        List<LevelCourse> tagCourseList = courseBo.queryLevelCourse(lce);
        Set<String> courseCodeSet = LevelCourseConvertor.extractCourseCode(tagCourseList);
        Map<String, PartnerPeixunDto> peixunDtoMap = queryBatchOnlinePeixunProcess(userId, new ArrayList<>(courseCodeSet));
        return LevelCourseConvertor.convertToCourseLearningDto(tagCourseList, peixunDtoMap);
    }
    
    private boolean addLevelCourseCode(List<String> levels, LevelCourseTypeEnum type, String courseCode){
        if(CollectionUtils.isEmpty(levels)){
            return true;
        }
        Map<String, AppResource> resourceMap = appResourceBo.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        for (String level : levels) {
            String key = LevelCourseConfigUtil.getResourceKey(level, type);
            AppResource resource = resourceMap.get(key);
            if (resource == null) {
                appResourceBo.configAppResource(LevelCourseConfigUtil.getResourceType(), key, courseCode, false, "system");
            }else{
                ResourceValueUpdateResult result = LevelCourseConfigUtil.addCourseCodeToResourceValue(courseCode, resource.getValue());
                if(result.isModified()){
                    appResourceBo.configAppResource(LevelCourseConfigUtil.getResourceType(), key, result.getUpdatedResourceValue(), false, "system");
                }
            }
        }
        return true;
    }
    
    private boolean removeLevelCourseCode(String courseCode) {
        Map<String, AppResource> resourceMap = appResourceBo.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        for(AppResource resource:resourceMap.values()){
            ResourceValueUpdateResult result = LevelCourseConfigUtil.removeCourseCodeFrom(courseCode, resource.getValue());
            if(result.isModified()){
                appResourceBo.configAppResource(LevelCourseConfigUtil.getResourceType(), resource.getName(), result.getUpdatedResourceValue(), false, "system");
            }
        }
        return true;
    }
    
    private Set<String> getCourseCodeList(String level, LevelCourseTypeEnum type) {
        AppResource resource = appResourceBo.queryAppResource(LevelCourseConfigUtil.getResourceType(), LevelCourseConfigUtil.getResourceKey(level, type));
        if(resource==null || StringUtils.isBlank(resource.getValue())){
            return Sets.newHashSet();
        }
        return LevelCourseConfigUtil.parseCourseCodeSet(resource.getValue());
    }
    
    public Map<String, PartnerPeixunDto> queryBatchOnlinePeixunProcess(Long userId, List<String> courseCodes){
        return LevelCourseConvertor.toCourseCodePeixunDtoMap(new ArrayList<>());
    }

}
