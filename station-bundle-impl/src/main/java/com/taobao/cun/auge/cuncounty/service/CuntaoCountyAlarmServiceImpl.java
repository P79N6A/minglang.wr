package com.taobao.cun.auge.cuncounty.service;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyAlarmBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyAlarmService.class)
public class CuntaoCountyAlarmServiceImpl implements CuntaoCountyAlarmService{
    @Resource
    private CuntaoCountyAlarmBo cuntaoCountyAlarmBo;
    @Override
    public void alarm() {
        cuntaoCountyAlarmBo.alarm();
    }
}
