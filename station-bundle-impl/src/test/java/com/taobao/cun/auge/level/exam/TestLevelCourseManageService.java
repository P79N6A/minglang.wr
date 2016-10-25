package com.taobao.cun.auge.level.exam;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.auge.station.condition.LevelCourseManageCondition;
import com.taobao.cun.auge.station.dto.LevelCourseEditDto;
import com.taobao.cun.auge.station.service.LevelCourseManageService;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.taobao.cun.auge.Application.class)
@WebAppConfiguration
public class TestLevelCourseManageService {

    @Autowired
    private LevelCourseManageService levelCourseManageService;
    
    @Test
    public void testSaveCourse(){
        LevelCourseEditDto course = new LevelCourseEditDto("kaoshi", "CVBNB", "monthlyIncoming");
        course.setElectiveLevels(Lists.newArrayList("S5"));
        course.setRequiredLevels(Lists.newArrayList("S6","S7"));
        boolean saveSuccess = levelCourseManageService.saveCourse(course );
        Assert.assertTrue(saveSuccess);
    }
    
    @Test
    public void testDeleteCourse(){
        String courseCode = insertCourse(Lists.newArrayList("S7", "S8"), Lists.newArrayList("S5", "S6"));
        boolean deleteSuccess = levelCourseManageService.deleteCourse(courseCode);
        Assert.assertTrue(deleteSuccess);
    }

    @Test
    public void testQueryManageLevelCourses(){
        String courseCode = insertCourse(Lists.newArrayList("S7", "S8"), Lists.newArrayList("S5", "S6"));
        LevelCourseManageCondition condition = new LevelCourseManageCondition();
        condition.setCourseCode(courseCode);
        List<LevelCourseEditDto> levelCourses = levelCourseManageService.queryManageLevelCourses(condition );
        Assert.assertEquals(levelCourses.size(), 1);
        
        LevelCourseEditDto course = levelCourses.get(0);
        Assert.assertArrayEquals(Lists.newArrayList("S7", "S8").toArray(), course.getRequiredLevels().toArray());
        Assert.assertArrayEquals(Lists.newArrayList("S5", "S6").toArray(), course.getElectiveLevels().toArray());
    }
    
    private String insertCourse(List<String> requiredCourses, List<String> electiveCourses) {
        String courseCode = "CDBNG";
        levelCourseManageService.deleteCourse(courseCode);
        LevelCourseEditDto course = new LevelCourseEditDto("kaoshi", courseCode, "monthlyIncoming");
        course.setElectiveLevels(electiveCourses);
        course.setRequiredLevels(requiredCourses);
        levelCourseManageService.saveCourse(course );
        return courseCode;
    }
}
