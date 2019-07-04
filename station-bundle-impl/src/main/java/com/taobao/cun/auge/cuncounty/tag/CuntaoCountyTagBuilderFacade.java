package com.taobao.cun.auge.cuncounty.tag;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWriteBo;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;
import javax.inject.Qualifier;

/**
 * 县服务中心标签构建器
 *
 * @author chengyu.zhoucy
 */
@Component
public class CuntaoCountyTagBuilderFacade {
    @Resource
    private Publisher publisher;
    @Resource(name = "cuntaoCountyProtocolTagBuilder")
    private CuntaoCountyTagBuilder cuntaoCountyProtocolTagBuilder;
    @Resource(name = "cuntaoCountyRiskTagBuilder")
    private CuntaoCountyTagBuilder cuntaoCountyRiskTagBuilder;
    @Resource
    private CuntaoCountyWriteBo cuntaoCountyWriteBo;

    public void build(){
        Flux.generate(publisher::publish)
                .parallel(3)
                .map(cuntaoCountyProtocolTagBuilder::build)
                .map(cuntaoCountyRiskTagBuilder::build)
                .map(t->t.getT1())
                .subscribe(cuntaoCountyWriteBo::updateTags);
    }
}
