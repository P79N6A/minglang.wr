package com.taobao.cun.auge.cuncounty.tag;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWriteBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private Logger logger = LoggerFactory.getLogger(CuntaoCountyTagBuilderFacade.class);
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
                .onErrorContinue((e,o)->logger.error(e.getMessage(), e))
                .parallel(3)
                .map(cuntaoCountyProtocolTagBuilder::build)
                .map(cuntaoCountyRiskTagBuilder::build)
                .map(t->t.getT1())
                .subscribe(cuntaoCountyWriteBo::updateTags);
    }
}
