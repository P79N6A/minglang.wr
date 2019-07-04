package com.taobao.cun.auge.cuncounty.tag;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.contactrecord.bo.CuntaoGovContactRecordQueryBo;
import com.taobao.cun.auge.contactrecord.dto.CuntaoGovContactRecordSummaryDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

import javax.annotation.Resource;
import java.util.List;

/**
 * 风险标签，基于政府拜访小记
 *
 * @author chengyu.zhoucy
 */
@Component("cuntaoCountyRiskTagBuilder")
public class CuntaoCountyRiskTagBuilder implements CuntaoCountyTagBuilder{
    private static final String HIGH = "高风险%d个";
    private static final String MIDDLE = "中风险%d个";
    private static final String LOW = "低风险%d个";
    @Resource
    private CuntaoGovContactRecordQueryBo cuntaoGovContactRecordQueryBo;

    @Override
    public Tuple2<CountyTag, CuntaoCountyListItem> build(Tuple2<CountyTag, CuntaoCountyListItem> tuple) {
        CountyTag countyTag = tuple.getT1();
        List<CuntaoGovContactRecordSummaryDto>  list = cuntaoGovContactRecordQueryBo.queryLatestRecords(Lists.newArrayList(countyTag.getCountyId()));
        if(CollectionUtils.isNotEmpty(list)){
            CuntaoGovContactRecordSummaryDto dto = list.get(0);
            if(dto.getLowRiskNum() != null && dto.getLowRiskNum().intValue() > 0){
                countyTag.getTags().add(String.format(HIGH, dto.getLowRiskNum()));
            }

            if(dto.getHighRiskNum() != null && dto.getHighRiskNum().intValue() > 0){
                countyTag.getTags().add(String.format(HIGH, dto.getHighRiskNum()));
            }

            if(dto.getMiddleRiskNum() != null && dto.getMiddleRiskNum().intValue() > 0){
                countyTag.getTags().add(String.format(MIDDLE, dto.getMiddleRiskNum()));
            }
        }
        return tuple;
    }
}
