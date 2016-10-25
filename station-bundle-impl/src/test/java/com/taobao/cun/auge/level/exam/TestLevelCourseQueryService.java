package com.taobao.cun.auge.level.exam;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.taobao.cun.auge.station.condition.LevelCourseCondition;
import com.taobao.cun.auge.station.dto.LevelCourseLearningStatisticsDto;
import com.taobao.cun.auge.station.service.LevelCourseQueryService;
import com.taobao.cun.auge.station.service.impl.LevelCourseServiceImpl;

import mockit.Expectations;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.taobao.cun.auge.Application.class)
@WebAppConfiguration
public class TestLevelCourseQueryService {

    @Autowired
    private LevelCourseQueryService levelCourseQueryService;
    
    @Test
    public void testGetCourseLearningInfo(){
//        new Expectations(LevelCourseServiceImpl.class) {{
//            levelCourseQueryService.;  //任何 ClassToBeMocked 实例都受到影响，也包括上面的 anotherInstance
//            result = "mocked";
//          }};
        
        LevelCourseCondition condition = LevelCourseCondition.getUserLevelQueryCondition(1234L, "S6");
        LevelCourseLearningStatisticsDto statisticsDto = levelCourseQueryService.getCourseLearningInfo(condition);
        Assert.assertNotNull(statisticsDto);
    }
    
    @Test
    public void testSearchCourseLearningInfoByUserLevel(){
        
    }
    
    @Test
    public void testSearchCourseLearningInfoByTag(){
        
    }
}
