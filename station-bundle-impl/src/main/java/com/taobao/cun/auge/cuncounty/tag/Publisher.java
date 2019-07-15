package com.taobao.cun.auge.cuncounty.tag;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyQueryBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateEnum;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import org.springframework.stereotype.Component;
import reactor.core.publisher.FluxSink;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.annotation.Resource;
import java.util.List;

/**
 * Flux发布
 */
@Component
public class Publisher{
    @Resource
    private CuntaoCountyQueryBo cuntaoCountyQueryBo;

    public void tagPublish(FluxSink<Tuple2<CountyTag, CuntaoCountyListItem>> sink) {
        doPublish(sink, Lists.newArrayList(
                CuntaoCountyStateEnum.OPENING.getCode(),
                CuntaoCountyStateEnum.WAIT_OPEN.getCode()));
    }

    public void alarmPublish(FluxSink<Tuple2<CountyTag, CuntaoCountyListItem>> sink) {
        doPublish(sink, Lists.newArrayList(CuntaoCountyStateEnum.OPENING.getCode()));
    }

    private void doPublish(FluxSink<Tuple2<CountyTag, CuntaoCountyListItem>> sink, List<String> states){
        CuntaoCountyCondition condition = new CuntaoCountyCondition();
        condition.setOrgId(1L);
        condition.setPage(1);
        condition.setPageSize(Integer.MAX_VALUE);
        condition.setStates(states);
        PageOutput<CuntaoCountyListItem> page = cuntaoCountyQueryBo.query(condition);
        page.getResult().forEach(c-> {
            CountyTag countyTag = new CountyTag();
            countyTag.setCountyId(c.getId());
            sink.next(Tuples.of(countyTag, c));
        });
        sink.complete();
    }
}
