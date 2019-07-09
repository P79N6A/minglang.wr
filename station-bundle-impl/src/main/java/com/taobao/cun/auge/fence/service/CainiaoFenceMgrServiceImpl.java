package com.taobao.cun.auge.fence.service;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import java.util.List;

@HSFProvider(serviceInterface = CainiaoFenceMgrService.class)
public class CainiaoFenceMgrServiceImpl implements CainiaoFenceMgrService{
    @Resource
    private FenceEntityMapper fenceEntityMapper;
    @Resource
    private RailServiceAdapter railServiceAdapter;
    @Resource
    private AppBizLogBo appBizLogBo;
    @Override
    public String getCainiaoFences(Long cainiaoFenceId) {
        return railServiceAdapter.getCainiaoFenceJSON(cainiaoFenceId);
    }

    @Override
    public void check() {
        FenceEntityExample example = new FenceEntityExample();
        example.createCriteria().andIsDeletedEqualTo("n");
        List<FenceEntity> fenceEntities = fenceEntityMapper.selectByExample(example);

        final List<Long> errors = Lists.newArrayList();

        Flux.fromIterable(fenceEntities)
                .parallel(3)
                .subscribe(f->{
                    if(!railServiceAdapter.isExistsCainiaoFence(f.getCainiaoFenceId())){
                        SimpleAppBizLog simpleAppBizLog = new SimpleAppBizLog();
                        simpleAppBizLog.setState("error");
                        simpleAppBizLog.setMessage("not exists");
                        simpleAppBizLog.setCreator("sys");
                        simpleAppBizLog.setBizType("fence_check");
                        simpleAppBizLog.setBizKey(f.getCainiaoFenceId());
                        appBizLogBo.addLog(simpleAppBizLog);
                    }
                });
    }
}
