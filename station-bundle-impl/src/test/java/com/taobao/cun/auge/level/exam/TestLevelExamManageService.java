package com.taobao.cun.auge.level.exam;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.taobao.cun.auge.station.dto.LevelExamConfigurationDto;
import com.taobao.cun.auge.station.service.LevelExamManageService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.taobao.cun.auge.Application.class)
@WebAppConfiguration
public class TestLevelExamManageService {

    @Autowired
    private LevelExamManageService levelExamManageService;
    
    @Test
    public void testConfigure(){
        LevelExamConfigurationDto configurationDto = new LevelExamConfigurationDto()
                .configureS4(123L)
                .configureS5(213L)
                .configureS6(124L)
                .configureS7(222L)
                .configureS8(233L);
        String confiurePerson = "test";
        boolean isSuccess = levelExamManageService.configure(configurationDto, confiurePerson );
        Assert.assertTrue(isSuccess);
        configurationDto.configureS4(122L);
        isSuccess = levelExamManageService.configure(configurationDto, confiurePerson );
        Assert.assertTrue(isSuccess);
    }
    
    @Test
    public void testQueryConfigure(){
        LevelExamConfigurationDto configurationDto = levelExamManageService.queryConfigure();
        Assert.assertNotNull(configurationDto);
    }
}
