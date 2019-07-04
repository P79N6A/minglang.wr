package com.taobao.cun.auge.cuncounty.tag;

import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import org.springframework.stereotype.Component;
import reactor.core.publisher.SynchronousSink;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;

/**
 * Flux发布
 */
@Component
public class Publisher{
    @Resource
    private CuntaoCountyQueryBo cuntaoCountyQueryBo;

    public void publish(SynchronousSink<Tuple2<CountyTag, CuntaoCountyListItem>> sink) {
        CuntaoCountyCondition condition = new CuntaoCountyCondition();
        condition.setOrgId(1L);
        condition.setPage(1);
        condition.setPageSize(Integer.MAX_VALUE);
        condition.setState(CuntaoCountyStateEnum.OPENING.getCode());
        PageOutput<CuntaoCountyListItem> page = cuntaoCountyQueryBo.query(condition);
        page.getResult().forEach(c-> {
            CountyTag countyTag = new CountyTag();
            countyTag.setCountyId(c.getId());
            sink.next(Tuples.of(countyTag, c));
        });
        sink.complete();
    }
}
