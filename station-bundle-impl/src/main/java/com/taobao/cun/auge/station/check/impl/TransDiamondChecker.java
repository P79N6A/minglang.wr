package com.taobao.cun.auge.station.check.impl;

import com.taobao.cun.auge.configuration.DiamondFactory;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.StationTransInfoBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class TransDiamondChecker implements StationTransChecker {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Override
    public void check(PartnerInstanceTransDto transDto) {
         if (StationTransInfoBizTypeEnum.YOUPIN.equals(transDto.getType().getFromBizType())) {
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(transDto.getInstanceId());
            if (!DiamondFactory.getTransDiamondConfig().contains(String.valueOf(rel.getStationId()))) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
                    "不在业务清单列表里的服务站点不可转型");
            }
         }
    }

}
