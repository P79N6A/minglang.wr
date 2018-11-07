package com.taobao.cun.auge.station.check.impl.trans;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class TransSameTypeChecker implements StationTransChecker {

    @Override
    public void check(PartnerInstanceTransDto transDto) {
        if (transDto.getType().getFromBizType().equals(transDto.getType().getToBizType())) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "不可转型为同等类型服务站");
        }
    }

}
