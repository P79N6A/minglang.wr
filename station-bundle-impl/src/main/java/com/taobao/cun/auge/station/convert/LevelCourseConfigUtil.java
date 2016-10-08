package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import com.ali.com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;

public class LevelCourseConfigUtil {
   private static final String LEVEL_COURSETYPE_SPLITTER = "_";
   private static final String SPLITTER = ",";
   public static final String LEVEL_COURSE_RELATION = "LevelToCourse";
    
   public static String getResourceType(){
       return LEVEL_COURSE_RELATION;
   }
   
    public static String getResourceKey(String level, LevelCourseTypeEnum type){
       Assert.notNull(type);
       Assert.notNull(level);
       return new StringBuilder(level).append(LEVEL_COURSETYPE_SPLITTER).append(type.toString()).toString();
   }
   
    public static ResourceValueUpdateResult removeCourseCodeFrom(String courseCode, String resourceValue){
        if(StringUtils.isBlank(resourceValue) || StringUtils.isEmpty(courseCode) || !StringUtils.contains(resourceValue, courseCode)){
            return new ResourceValueUpdateResult(false, resourceValue);
        }
        Set<String> codeSet = parseCourseCodeSet(resourceValue);
        codeSet.remove(courseCode);
        return new ResourceValueUpdateResult(true, toResourceValue(codeSet));
    }
    
    /**
     * 将此courseCode添加到resourceValue中(如果没有包含的话)
     */
    public static ResourceValueUpdateResult addCourseCodeToResourceValue(String courseCode, String resourceValue){
        Set<String> codeSet = parseCourseCodeSet(resourceValue);
        if(codeSet.contains(courseCode)){
            return new ResourceValueUpdateResult(false, resourceValue);
        }
        codeSet.add(courseCode);
        return new ResourceValueUpdateResult(true, toResourceValue(codeSet));
    }
    
    public static Set<String> parseCourseCodeSet(String resourceValue) {
        Set<String> codeSet = Sets.newLinkedHashSet();
        if(StringUtils.isBlank(resourceValue)){
            return codeSet;
        }
        Collections.addAll(codeSet, resourceValue.split(SPLITTER));
        return codeSet;
    }
    
    public static String toResourceValue(Set<String> courseCodes){
        if(CollectionUtils.isEmpty(courseCodes)){
            return "";
        }
        StringBuilder s = new StringBuilder("");
        for(String code:courseCodes){
            s.append(code).append(SPLITTER);
        }
        return s.substring(0, s.length() - 1);
    }
    
    public static Map<String, CourseLevelInfo>  groupLevelByCourseCode(Collection<AppResource> resourceList){
        if(CollectionUtils.isEmpty(resourceList)){
            return Maps.newHashMap();
        }
        
        Map<String, CourseLevelInfo> codeToLevelInfoMap = Maps.newHashMap();
        for(AppResource resource:resourceList){
            if(StringUtils.isNotBlank(resource.getValue()) && StringUtils.isNotBlank(resource.getName())){
                Set<String> courseCodeSet = parseCourseCodeSet(resource.getValue());
                for(String code:courseCodeSet){
                    CourseLevelInfo levelInfo = codeToLevelInfoMap.get(code);
                    if(levelInfo == null){
                        levelInfo = new CourseLevelInfo();
                        codeToLevelInfoMap.put(code, levelInfo);
                    }
                    levelInfo.addLevelCourseCode(code, resource.getName());
                }
            }
        }
        return codeToLevelInfoMap;
    }
    
    public static class ResourceValueUpdateResult {
        boolean isModified;
        public String updatedResourceValue;
       public ResourceValueUpdateResult(boolean isModified, String updatedResourceValue) {
           super();
           this.isModified = isModified;
           this.updatedResourceValue = updatedResourceValue;
       }
       
       public boolean isModified() {
           return isModified;
       }
       
       public void setModified(boolean isModified) {
           this.isModified = isModified;
       }
       
       public String getUpdatedResourceValue() {
           return updatedResourceValue;
       }
       
       public void setUpdatedResourceValue(String updatedResourceValue) {
           this.updatedResourceValue = updatedResourceValue;
       }
    }
    
    public static class CourseLevelInfo {
        private List<String> requiredLevels = new ArrayList<>();
        private List<String>electiveLevels = new ArrayList<>();
        
        public List<String> getRequiredLevels() {
            return requiredLevels;
        }
        
        public boolean addLevelCourseCode(String code, String name) {
            String[] levelAndCourseType = parseLevelCourseTypeInfo(name);
            if(levelAndCourseType==null || levelAndCourseType.length!=2){
                return false;
            }
            if(LevelCourseTypeEnum.isRequiredCourse(levelAndCourseType[1])){
                return this.addRequiredLevel(levelAndCourseType[0]);
            }else if(LevelCourseTypeEnum.isElectiveCourse(levelAndCourseType[1])){
                return this.addElectiveLevel(levelAndCourseType[0]);
            }
            return false;
        }

        public List<String> getElectiveLevels() {
            return electiveLevels;
        }
        
        public boolean addRequiredLevel(String level){
            if(!requiredLevels.contains(level)){
                requiredLevels.add(level);
                return true;
            }
            return false;
        }
        
        public boolean addElectiveLevel(String level){
            if(!electiveLevels.contains(level)){
                electiveLevels.add(level);
                return true;
            }
            return false;
        }
        
        private static String[] parseLevelCourseTypeInfo(String resourceName){
            if(StringUtils.isBlank(resourceName)){
                return null;
            }
            return resourceName.split(LEVEL_COURSETYPE_SPLITTER);
        }
    }
    
}
