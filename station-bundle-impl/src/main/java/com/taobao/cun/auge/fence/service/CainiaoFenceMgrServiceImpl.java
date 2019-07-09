package com.taobao.cun.auge.fence.service;

import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import javax.annotation.Resource;

@HSFProvider(serviceInterface = CainiaoFenceMgrService.class)
public class CainiaoFenceMgrServiceImpl implements CainiaoFenceMgrService{
    @Resource
    private RailServiceAdapter railServiceAdapter;
    @Override
    public String getCainiaoFences(Long cainiaoFenceId) {
        return railServiceAdapter.getCainiaoFenceJSON(cainiaoFenceId);
    }
}
