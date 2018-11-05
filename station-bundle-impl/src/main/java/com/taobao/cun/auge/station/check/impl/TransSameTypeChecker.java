package com.taobao.cun.auge.station.check.impl;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.StationClassEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class TransSameTypeChecker implements StationTransChecker {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Override
    public void check(PartnerInstanceTransDto transDto) {
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(transDto.getInstanceId());
        if (StationModeEnum.V4.getCode().equals(rel.getMode()) && StationClassEnum.STATION_YP.getCode().equals(transDto.getToBizType())) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"不可转型为同等类型服务站");
        }
    }

}
