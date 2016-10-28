package com.taobao.cun.auge.level.exam;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.taobao.cun.auge.station.condition.LevelCourseCondition;
import com.taobao.cun.auge.station.dto.LevelCourseLearningDto;
import com.taobao.cun.auge.station.dto.LevelCourseLearningStatisticsDto;
import com.taobao.cun.auge.station.service.impl.LevelCourseServiceImpl;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.taobao.cun.auge.Application.class)
@WebAppConfiguration
public class TestLevelCourseQueryService {

    @Autowired
    private LevelCourseServiceImpl levelCourseQueryService;
    
    @Test
    public void testGetCourseLearningInfo(){
        LevelCourseCondition condition = LevelCourseCondition.getUserLevelQueryCondition(1234L, "S6");
        LevelCourseLearningStatisticsDto statisticsDto = levelCourseQueryService.getCourseLearningInfo(condition);
        Assert.assertNotNull(statisticsDto);
    }
    
    @Test
    public void testSearchCourseLearningInfoByUserLevel(){
        List<LevelCourseLearningDto> learningDtoList = levelCourseQueryService.searchCourseLearningInfoByUserLevel(1000L, "S6");
        Assert.assertNotNull(learningDtoList);
    }
    
    @Test
    public void testSearchCourseLearningInfoByTag(){
        List<LevelCourseLearningDto> learningDtoList = levelCourseQueryService.searchCourseLearningInfoByTag(1000L, "monthlyIncoming");
        Assert.assertNotNull(learningDtoList);
    }
}
