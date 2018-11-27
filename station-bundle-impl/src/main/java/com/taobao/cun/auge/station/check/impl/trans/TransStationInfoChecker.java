package com.taobao.cun.auge.station.check.impl.trans;

import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 18/11/5.
 */
@Component
public class TransStationInfoChecker implements StationTransChecker {

    @Autowired
    private StationService stationService;

    @Override
    public void check(PartnerInstanceTransDto transDto) {
         stationService.getStationInfoValidateRule(transDto.getInstanceId(), transDto.getStationDto());
    }

}
