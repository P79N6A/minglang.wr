package com.taobao.cun.auge.level.taskflow;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.service.ProcessService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = com.taobao.cun.auge.Application.class)
///@WebAppConfigurationiguration
public class TestLevelTaskflow {

    @Autowired
    private ProcessService processService;
    
    @Test
    public void testStartLevelApprove() {
        String paramJson = "{\"applyTime\":1479723382996,\"businessCode\":\"partnerInstanceLevelAudit\",\"businessId\":3645112089,\"countyOrgId\":3,\"countyStationName\":\"桐庐县服务中心\",\"currentLevel\":{\"description\":\"高级合伙人\",\"level\":\"S6\"},\"employeeId\":\"106774\",\"employeeName\":\"周伟\",\"evaluateInfo\":\"{\\\"activeAppUserCnt\\\":98.00,\\\"activeAppUserCntScore\\\":25.00,\\\"countyOrgId\\\":3,\\\"currentLevel\\\":{\\\"description\\\":\\\"高级合伙人\\\",\\\"level\\\":\\\"S6\\\"},\\\"evaluateBy\\\":\\\"106774\\\",\\\"evaluateDate\\\":1477995376810,\\\"evaluateType\\\":{\\\"code\\\":\\\"SYSTEM\\\",\\\"desc\\\":\\\"系统评定\\\"},\\\"goods1GmvRatio\\\":0.34,\\\"goods1GmvRatioScore\\\":12.00,\\\"id\\\":1,\\\"lastEvaluateDate\\\":1467302400000,\\\"loyaltyVillagerCntScore\\\":12.00,\\\"monthlyIncome\\\":5000.00,\\\"monthlyIncomeLastSixMonth\\\":\\\"201610:2000|201609:9000|201608:8000|201607:7000|201606:6000|201605:5000|\\\",\\\"monthlyIncomeScore\\\":23.00,\\\"nextEvaluateDate\\\":1493633776810,\\\"operator\\\":\\\"106774\\\",\\\"operatorType\\\":{\\\"code\\\":\\\"BUC\\\",\\\"desc\\\":\\\"BUC用户\\\"},\\\"partnerInstanceId\\\":3645112089,\\\"preLevel\\\":{\\\"description\\\":\\\"中级合伙人\\\",\\\"level\\\":\\\"S5\\\"},\\\"repurchaseRate\\\":0.34,\\\"repurchaseRateScore\\\":18.00,\\\"score\\\":60.00,\\\"stationId\\\":11076,\\\"taobaoUserId\\\":3662275460,\\\"visitedProductCnt\\\":345.00,\\\"visitedProductCntScore\\\":8.00}\",\"monthlyIncome\":5000.00,\"partnerInstanceId\":3645112089,\"partnerName\":\"nm2IY9\",\"partnerTaobaoUserId\":3662275460,\"score\":60.00,\"stationId\":2901,\"stationName\":\"ZUOAI099测试村点\"}";
        PartnerInstanceLevelProcessDto levelProcessDto = JSON.parseObject(paramJson, PartnerInstanceLevelProcessDto.class);
//        System.out.println(JSON.toJSONString(levelProcessDto));
        processService.startLevelApproveProcess(levelProcessDto);
        
    }
}
