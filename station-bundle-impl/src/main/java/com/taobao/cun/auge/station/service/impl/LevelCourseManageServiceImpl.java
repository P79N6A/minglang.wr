package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ali.com.google.common.collect.Sets;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.dal.domain.LevelCourse;
import com.taobao.cun.auge.dal.domain.LevelCourseExample;
import com.taobao.cun.auge.station.bo.LevelCourseBO;
import com.taobao.cun.auge.station.condition.LevelCourseManageCondition;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.CourseLevelInfo;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.ResourceValueUpdateResult;
import com.taobao.cun.auge.station.convert.LevelCourseConvertor;
import com.taobao.cun.auge.station.dto.LevelCourseEditDto;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.LevelCourseManageService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("levelCourseManageService")
@HSFProvider(serviceInterface = LevelCourseManageService.class)
public class LevelCourseManageServiceImpl implements LevelCourseManageService {

    private static final String COURSE_HAD_EXIST = "COURSE_HAD_EXIST";
    
    @Autowired
    private LevelCourseBO courseBo;
    
    @Autowired
    private AppResourceService appResourceService;
    
    /**
     * 新增一个晋升培训课程,目前不支持更新课程所以如果已存在则直接返回false;
     * 保存课程信息,在按照层级更新层级课程配置信息;
     */
    @Override
    public boolean saveCourse(LevelCourseEditDto course) {
        if(!LevelCourseConvertor.isValidLevelCourse(course)){
            return false;
        }
        LevelCourseExample example = new LevelCourseExample();
        example.createCriteria().andCourseCodeEqualTo(course.getCourseCode());
        if(!CollectionUtils.isEmpty(courseBo.queryLevelCourse(example))){
            throw new AugeBusinessException(COURSE_HAD_EXIST, "duplilcate course code,course code had exsit:" + course.getCourseCode());
        }
        LevelCourse levelCourse = LevelCourseConvertor.toLevelCourse(course);
        levelCourse.setGmtCreate(new Date());
        levelCourse.setGmtModified(new Date());
        boolean isSaveSuccess = courseBo.saveLevelCourse(levelCourse);
        return isSaveSuccess 
                && addLevelCourseCode(course.getRequiredLevels(), LevelCourseTypeEnum.REQUIRED, course.getCourseCode())
                && addLevelCourseCode(course.getElectiveLevels(), LevelCourseTypeEnum.ELECTIVE, course.getCourseCode());
    }

    /**
     * 删除一个课程
     * 1.删除level_course表中基本课程信息
     * 2.从层级课程配置中删除此课程
     */
    @Override
    public boolean deleteCourse(String courseCode) {
        Assert.notNull(courseCode, "course code can not null!");
        boolean isDeleteSuccess = courseBo.deleteLevelCourse(courseCode);
         return isDeleteSuccess && removeLevelCourseCode(courseCode);
    }

    /**
     * 搜索课程:按照用户层级搜索或者按照课程本身信息搜索,结果由下面两部组装而成
     * 1.基本课程数据
     * 2.层级课程呢配置
     */
    @Override
    public List<LevelCourseEditDto> queryManageLevelCourses(LevelCourseManageCondition condition) {
//        if(!LevelCourseManageCondition.isValidManageCondition(condition)){
//            return Collections.emptyList();
//        }
        LevelCourseExample example = new LevelCourseExample();
        if(condition.isSearchByLevel()){
            LevelCourseTypeEnum courseType = condition.getCourseType() != null ? condition.getCourseType() : LevelCourseTypeEnum.REQUIRED;
            Set<String> courseCodeSet = getCourseCodeList(condition.getUserLevel(), courseType);
            if(CollectionUtils.isEmpty(courseCodeSet)){
                return Collections.emptyList();
            }
            example.createCriteria().andCourseCodeIn(new ArrayList<>(courseCodeSet));
        }else{
            example = LevelCourseConvertor.toLevelCourseExample(condition);
        }
        example.setOrderByClause("gmt_create desc");
        example.setLimitStart(0);
        example.setLimitEnd(200);
        
        List<LevelCourse> levelCourseList =  courseBo.queryLevelCourse(example);
        Map<String, AppResourceDto> resourceList = appResourceService.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        Map<String, CourseLevelInfo> courseLevelInfoMap = LevelCourseConfigUtil.groupLevelByCourseCode(resourceList.values());
        return LevelCourseConvertor.toCourseEditDto(levelCourseList, courseLevelInfoMap);
    }

    private boolean addLevelCourseCode(List<String> levels, LevelCourseTypeEnum type, String courseCode){
        if(CollectionUtils.isEmpty(levels)){
            return true;
        }
        Map<String, AppResourceDto> resourceMap = appResourceService.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        for (String level : levels) {
            String key = LevelCourseConfigUtil.getResourceKey(level, type);
            AppResourceDto resource = resourceMap.get(key);
            if (resource == null) {
            	appResourceService.configAppResource(LevelCourseConfigUtil.getResourceType(), key, courseCode, false, "system");
            }else{
                ResourceValueUpdateResult result = LevelCourseConfigUtil.addCourseCodeToResourceValue(courseCode, resource.getValue());
                if(result.isModified()){
                	appResourceService.configAppResource(LevelCourseConfigUtil.getResourceType(), key, result.getUpdatedResourceValue(), false, "system");
                }
            }
        }
        return true;
    }
    
    private Set<String> getCourseCodeList(String level, LevelCourseTypeEnum type) {
    	AppResourceDto resource = appResourceService.queryAppResource(LevelCourseConfigUtil.getResourceType(), LevelCourseConfigUtil.getResourceKey(level, type));
        if(resource==null || StringUtils.isBlank(resource.getValue())){
            return Sets.newHashSet();
        }
        return LevelCourseConfigUtil.parseCourseCodeSet(resource.getValue());
    }
    
    private boolean removeLevelCourseCode(String courseCode) {
        Map<String, AppResourceDto> resourceMap = appResourceService.queryAppResourceMap(LevelCourseConfigUtil.getResourceType());
        for(AppResourceDto resource:resourceMap.values()){
            ResourceValueUpdateResult result = LevelCourseConfigUtil.removeCourseCodeFrom(courseCode, resource.getValue());
            if(result.isModified()){
            	appResourceService.configAppResource(LevelCourseConfigUtil.getResourceType(), resource.getName(), result.getUpdatedResourceValue(), false, "system");
            }
        }
        return true;
    }
}
