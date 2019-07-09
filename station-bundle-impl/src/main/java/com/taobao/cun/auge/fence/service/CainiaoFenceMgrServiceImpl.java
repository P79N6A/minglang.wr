package com.taobao.cun.auge.fence.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.FenceEntityExample;
import com.taobao.cun.auge.dal.mapper.FenceEntityMapper;
import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.cun.auge.log.SimpleAppBizLog;
import com.taobao.cun.auge.log.bo.AppBizLogBo;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.Resource;

@HSFProvider(serviceInterface = CainiaoFenceMgrService.class)
public class CainiaoFenceMgrServiceImpl implements CainiaoFenceMgrService{
    private Logger logger = LoggerFactory.getLogger(getClass());
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
        Runnable runnable = () ->
        Flux.create(this::publish, FluxSink.OverflowStrategy.BUFFER)
                .onErrorContinue((e, f)->{
                    SimpleAppBizLog simpleAppBizLog = new SimpleAppBizLog();
                    simpleAppBizLog.setState("error");
                    simpleAppBizLog.setMessage(e.getMessage().substring(0, 125));
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

        new Thread(runnable).run();
    }

    private void publish(FluxSink<FenceEntity> sink){
        int page = 1;
        while(true) {
            logger.info("page {}", page);
            FenceEntityExample example = new FenceEntityExample();
            example.createCriteria().andIsDeletedEqualTo("n");
            PageHelper.startPage(page, 1000);
            Page<FenceEntity> pageDto = (Page<FenceEntity>) fenceEntityMapper.selectByExample(example);

            pageDto.stream().forEach(item->sink.next(item));

            if(page >= pageDto.getPages()){
                break;
            }
            page++;
        }
        sink.complete();
    }
}
