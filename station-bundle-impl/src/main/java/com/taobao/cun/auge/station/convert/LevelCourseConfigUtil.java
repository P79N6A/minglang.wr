package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ali.com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * 类LevelCourseConfigUtil.java的实现描述：层级课程配置相关工具方法
 * 层级课程关系配置在appResource表,其中app_resource表的type为@LevelCourseConfigUtil.LEVEL_COURSE_RELATION;
 * name为level_LevelCourseTypeEnum如:S4_REQUIRED,S6_ELECTIVE,value为该层级选修或必修courseCode,code之间通过SPLITTER分割;
 * @author xujianhui 2016年10月24日 上午10:43:47
 */
public class LevelCourseConfigUtil {
    /**
     * 层级跟课程类型连接符(构造appResource的name)
     */
   private static final String LEVEL_COURSETYPE_SPLITTER = "_";
   /**
    * courseCode分隔符
    */
   private static final String COURSECODE_SPLITTER = ",";
   /**
    * appResource中的type值
    */
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
        boolean removed = codeSet.remove(courseCode);
        if(removed){
            resourceValue = toResourceValue(codeSet);
        }
        return new ResourceValueUpdateResult(removed, resourceValue);
    }
    
    /**
     * 将此courseCode添加到resourceValue中(如果没有包含的话)
     * 此方法对相同的courseCode和resourceValue保证幂等
     */
    public static ResourceValueUpdateResult addCourseCodeToResourceValue(String courseCode, String resourceValue){
        Set<String> codeSet = parseCourseCodeSet(resourceValue);
        if(codeSet.contains(courseCode)){
            return new ResourceValueUpdateResult(false, resourceValue);
        }
        codeSet.add(courseCode);
        return new ResourceValueUpdateResult(true, toResourceValue(codeSet));
    }
    
    /**
     * 将courseCode解析成集合返回
     * @param resourceValue
     * @return
     */
    public static Set<String> parseCourseCodeSet(String resourceValue) {
        Set<String> codeSet = Sets.newLinkedHashSet();
        if(StringUtils.isBlank(resourceValue)){
            return codeSet;
        }
        Collections.addAll(codeSet, resourceValue.split(COURSECODE_SPLITTER));
        return codeSet;
    }
    
    public static String toResourceValue(Set<String> courseCodes){
        if(CollectionUtils.isEmpty(courseCodes)){
            return "";
        }
        StringBuilder s = new StringBuilder("");
        for(String code:courseCodes){
            s.append(code).append(COURSECODE_SPLITTER);
        }
        return s.substring(0, s.length() - 1);
    }
    
    public static Map<String, CourseLevelInfo>  groupLevelByCourseCode(Collection<AppResourceDto> resourceList){
        if(CollectionUtils.isEmpty(resourceList)){
            return Maps.newHashMap();
        }
        
        Map<String, CourseLevelInfo> codeToLevelInfoMap = Maps.newHashMap();
        for(AppResourceDto resource:resourceList){
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

        @Override
        public String toString() {
            return "CourseLevelInfo [requiredLevels=" + requiredLevels + ", electiveLevels=" + electiveLevels + "]";
        }
        
    }
    
}
