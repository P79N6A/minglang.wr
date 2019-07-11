package com.taobao.cun.auge.cuncounty.tag;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyGovContractBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyProtocolRiskEnum;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

import javax.annotation.Resource;

/**
 * 政府协议标签
 *
 * @author chengyu.zhoucy
 */
@Component("cuntaoCountyProtocolTagBuilder")
public class CuntaoCountyProtocolTagBuilder implements CuntaoCountyTagBuilder{
    @Resource
    private CuntaoCountyGovContractBo cuntaoCountyGovContractBo;

    @Override
    public Tuple2<CountyTag, CuntaoCountyListItem> build(Tuple2<CountyTag, CuntaoCountyListItem> tuple) {
        CountyTag countyTag = tuple.getT1();
        CuntaoCountyListItem item = tuple.getT2();

        CuntaoCountyProtocolRiskEnum cuntaoCountyProtocolRiskEnum = cuntaoCountyGovContractBo.checkContractRisk(item.getId());

        if(cuntaoCountyProtocolRiskEnum != null){
            countyTag.getTags().add(cuntaoCountyProtocolRiskEnum.getCode());
        }

        return tuple;
    }
}
