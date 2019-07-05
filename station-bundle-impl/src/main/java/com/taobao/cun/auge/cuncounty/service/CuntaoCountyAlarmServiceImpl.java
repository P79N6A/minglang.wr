package com.taobao.cun.auge.cuncounty.service;

import com.taobao.cun.auge.common.concurrent.Executors;
import com.taobao.cun.auge.cuncounty.alarm.AlarmTest;
import com.taobao.cun.auge.cuncounty.alarm.CuntaoCountyAlarm;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyAlarmService.class)
public class CuntaoCountyAlarmServiceImpl implements CuntaoCountyAlarmService{
    @Resource
    private CuntaoCountyAlarm cuntaoCountyProtocolAlarm;
    @Resource
    private CuntaoCountyAlarm cuntaoCountyVisitAlarm;
    @Resource
    private AlarmTest alarmTest;

    private ExecutorService executorService = null;

    public CuntaoCountyAlarmServiceImpl() {
        executorService = Executors.newFixedThreadPool(1, "county-tag-job-");
    }
    @Override
    public void test(Long bizId, String empId, String textContent, String msgType){
        alarmTest.alarm(bizId, empId, textContent, msgType);
    }

    @Override
    public void protocolAlarm() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cuntaoCountyProtocolAlarm.alarm();
            }
        });
    }

    @Override
    public void visitAlarm() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cuntaoCountyVisitAlarm.alarm();
            }
        });
    }
}
