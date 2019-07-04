package com.taobao.cun.auge.cuncounty.service;

import com.taobao.cun.auge.common.concurrent.Executors;
import com.taobao.cun.auge.cuncounty.tag.CuntaoCountyTagBuilderFacade;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;

@HSFProvider(serviceInterface = CuntaoCountyTagBuildJobService.class)
public class CuntaoCountyTagBuildJobServiceImpl implements CuntaoCountyTagBuildJobService{
    @Resource
    private CuntaoCountyTagBuilderFacade cuntaoCountyTagBuilderFacade;

    private ExecutorService executorService = null;

    public CuntaoCountyTagBuildJobServiceImpl() {
        executorService = Executors.newFixedThreadPool(1, "county-tag-job-");
    }

    @Override
    public void build() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                cuntaoCountyTagBuilderFacade.build();
            }
        });
    }
}
