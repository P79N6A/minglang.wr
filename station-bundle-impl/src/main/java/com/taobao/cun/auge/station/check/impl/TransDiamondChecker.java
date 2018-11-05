package com.taobao.cun.auge.station.check.impl;

import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.StationClassEnum;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class TransDiamondChecker implements StationTransChecker {

    @Override
    public void check(PartnerInstanceTransDto transDto) {
         if (StationClassEnum.STATION_ELEC.getCode().equals(transDto.getToBizType())) {

         }
    }

}
