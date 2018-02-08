package com.taobao.cun.auge.level.exam;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.CourseLevelInfo;
import com.taobao.cun.auge.station.convert.LevelCourseConfigUtil.ResourceValueUpdateResult;
import com.taobao.cun.auge.station.enums.LevelCourseTypeEnum;
import org.junit.Assert;
import org.junit.Test;

public class TestExamConfigUtil {

    @Test
    public void testGetResourceKey() {
        String level = "S6";
        LevelCourseTypeEnum type = LevelCourseTypeEnum.ELECTIVE;
        String key = LevelCourseConfigUtil.getResourceKey(level , type );
        Assert.assertEquals("S6_ELECTIVE", key);
    }
    
    @Test
    public void testRemoveCourseCodeFrom(){
        String resourceValue = "12345,adbce,werty,hellow";
        String courseCode = "werty";
        ResourceValueUpdateResult updateResult = LevelCourseConfigUtil.removeCourseCodeFrom(courseCode, resourceValue);
        Assert.assertEquals("12345,adbce,hellow", updateResult.getUpdatedResourceValue());
        Assert.assertTrue(updateResult.isModified());
        
        courseCode = "wert";
        updateResult = LevelCourseConfigUtil.removeCourseCodeFrom(courseCode, resourceValue);
        Assert.assertEquals(resourceValue, updateResult.getUpdatedResourceValue());
        Assert.assertFalse(updateResult.isModified());
    }
    
    @Test
    public void testAddCourseCodeToResourceValue(){
        String resourceValue =null;
        String courseCode = "12345";
        ResourceValueUpdateResult updateResult = LevelCourseConfigUtil.addCourseCodeToResourceValue(courseCode, resourceValue);
        Assert.assertEquals(courseCode, updateResult.getUpdatedResourceValue());
        Assert.assertTrue(updateResult.isModified());
        
        resourceValue = "hello,world,okkkk";
        courseCode = "begine";
        updateResult = LevelCourseConfigUtil.addCourseCodeToResourceValue(courseCode, resourceValue);
        Assert.assertEquals("hello,world,okkkk,begine", updateResult.getUpdatedResourceValue());
        Assert.assertTrue(updateResult.isModified());
        
        resourceValue = "hello,world,okkkk";
        courseCode = "world";
        updateResult = LevelCourseConfigUtil.addCourseCodeToResourceValue(courseCode, resourceValue);
        Assert.assertEquals(resourceValue, updateResult.getUpdatedResourceValue());
        Assert.assertFalse(updateResult.isModified());
    }
    
    @Test
    public void testParseCourseCodeSet(){
        Set<String> codeSets = LevelCourseConfigUtil.parseCourseCodeSet("hello,world,ok,world");
        Assert.assertArrayEquals(new String[]{"hello","world","ok"}, codeSets.toArray());
        
        codeSets = LevelCourseConfigUtil.parseCourseCodeSet(null);
        Assert.assertArrayEquals(new String[]{}, codeSets.toArray());
    }
    
    @Test
    public void testGroupLevelByCourseCode(){
        Collection<AppResourceDto> resourceList = generateAppResources();
        Map<String, CourseLevelInfo> codeToLevelInfo =LevelCourseConfigUtil.groupLevelByCourseCode(resourceList);
        CourseLevelInfo code1Levels = codeToLevelInfo.get("1");
        Assert.assertNotNull(code1Levels);
        Assert.assertEquals(code1Levels.getElectiveLevels(), Lists.newArrayList());
        Assert.assertEquals(code1Levels.getRequiredLevels(), Lists.newArrayList("S5"));
        
        CourseLevelInfo code4Levels = codeToLevelInfo.get("4");
        Assert.assertNotNull(code4Levels);
        Assert.assertEquals(code4Levels.getElectiveLevels(), Lists.newArrayList("S5","S6"));
        Assert.assertEquals(code4Levels.getRequiredLevels(), Lists.newArrayList("S5"));
    }
    
    @Test
    public void testToResourceValue(){
        Set<String> strs = new LinkedHashSet<>();
        strs.add("hello");
        strs.add("world");
        strs.add("test");
        String codeStr = LevelCourseConfigUtil.toResourceValue(strs);
        Assert.assertEquals("hello,world,test", codeStr);
        
        codeStr = LevelCourseConfigUtil.toResourceValue(null);
        Assert.assertEquals("", codeStr);
    }
    
    Collection<AppResourceDto> generateAppResources(){
        Collection<AppResourceDto> resources = new ArrayList<AppResourceDto>(4);
        AppResourceDto appResource = new AppResourceDto();
        resources.add(appResource);
        appResource.setType(LevelCourseConfigUtil.getResourceType());
        appResource.setName(LevelCourseConfigUtil.getResourceKey("S5", LevelCourseTypeEnum.REQUIRED));
        appResource.setValue("1,2,3,4");
        
        appResource = new AppResourceDto();
        resources.add(appResource);
        appResource.setType(LevelCourseConfigUtil.getResourceType());
        appResource.setName(LevelCourseConfigUtil.getResourceKey("S5", LevelCourseTypeEnum.ELECTIVE));
        appResource.setValue("4,5");
        
        appResource = new AppResourceDto();
        resources.add(appResource);
        appResource.setType(LevelCourseConfigUtil.getResourceType());
        appResource.setName(LevelCourseConfigUtil.getResourceKey("S6", LevelCourseTypeEnum.REQUIRED));
        appResource.setValue("5,6,7");
        
        appResource = new AppResourceDto();
        resources.add(appResource);
        appResource.setType(LevelCourseConfigUtil.getResourceType());
        appResource.setName(LevelCourseConfigUtil.getResourceKey("S6", LevelCourseTypeEnum.ELECTIVE));
        appResource.setValue("4,8,9");
        return resources;
    }
}
