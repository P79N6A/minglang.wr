package com.taobao.cun.auge.cuncounty.tag;

import com.google.common.base.Strings;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;
import com.taobao.cun.auge.cuncounty.vo.CountyTag;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 政府协议标签
 *
 * @author chengyu.zhoucy
 */
@Component("cuntaoCountyProtocolTagBuilder")
public class CuntaoCountyProtocolTagBuilder implements CuntaoCountyTagBuilder{
    @Override
    public Tuple2<CountyTag, CuntaoCountyListItem> build(Tuple2<CountyTag, CuntaoCountyListItem> tuple) {
        CountyTag countyTag = tuple.getT1();
        CuntaoCountyListItem item = tuple.getT2();

        if(!Strings.isNullOrEmpty(item.getSerialNum())){
            if(isWillExpire(item)) {
                countyTag.getTags().add(CuntaoCountyTagEnum.protocolWillExpire.getCode());
            }
        }else{
            if(item.getContractId() != null){
                countyTag.getTags().add(CuntaoCountyTagEnum.protocolMaybeNotExists.getCode());
            }else{
                countyTag.getTags().add(CuntaoCountyTagEnum.protocolNotExists.getCode());
            }
        }
        return tuple;
    }

    private boolean isWillExpire(CuntaoCountyListItem item){
        LocalDate date = LocalDate.now();
        date = date.plusDays(90);
        LocalDate protocolLocalDate = LocalDate.parse(item.getProtocolEndDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return date.isAfter(protocolLocalDate);
    }
}
