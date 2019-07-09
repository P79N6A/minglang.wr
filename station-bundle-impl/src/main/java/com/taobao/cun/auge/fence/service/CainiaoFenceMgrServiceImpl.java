package com.taobao.cun.auge.fence.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

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
        Flux.create(this::publish, FluxSink.OverflowStrategy.BUFFER)
                .onErrorContinue((e, f)->{
                    SimpleAppBizLog simpleAppBizLog = new SimpleAppBizLog();
                    simpleAppBizLog.setState("error");
                    simpleAppBizLog.setMessage(e.getMessage());
                    simpleAppBizLog.setCreator("sys");
                    simpleAppBizLog.setBizType("fence_check");
                    simpleAppBizLog.setBizKey(((FenceEntity)f).getCainiaoFenceId());
                    appBizLogBo.addLog(simpleAppBizLog);
                })
                .parallel(3)
                .subscribe(f->{
                    if(!railServiceAdapter.isExistsCainiaoFence(f.getCainiaoFenceId())){
                        SimpleAppBizLog simpleAppBizLog = new SimpleAppBizLog();
                        simpleAppBizLog.setState("success");
                        simpleAppBizLog.setMessage("not exists");
                        simpleAppBizLog.setCreator("sys");
                        simpleAppBizLog.setBizType("fence_check");
                        simpleAppBizLog.setBizKey(f.getCainiaoFenceId());
                        appBizLogBo.addLog(simpleAppBizLog);
                    }
                });
    }

    private void publish(FluxSink<FenceEntity> sink){
        int page = 1;
        int pages = 0;
        while(true) {
            FenceEntityExample example = new FenceEntityExample();
            example.createCriteria().andIsDeletedEqualTo("n");
            PageHelper.startPage(page, 1000);
            PageDto<FenceEntity> pageDto = (PageDto<FenceEntity>) fenceEntityMapper.selectByExample(example);
            if(pageDto.getItems() != null){
                for (FenceEntity item : pageDto.getItems()) {
                    sink.next(item);
                }
            }
            if(pages == 0) {
                pages = pageDto.getPages();
            }
            if(page >= pages){
                break;
            }
            page++;
        }
        sink.complete();
    }
}
