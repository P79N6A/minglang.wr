package com.taobao.cun.auge.station.check.impl;

import java.util.List;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;

/**
 * Created by xiao on 18/11/5.
 */

public class StationTransCheckerFactory {

    public static List<StationTransChecker> getStationTransCheckerList() {
        return CheckerFactory.getCheckerList(StationTransChecker.class);
    }

    public static void check(PartnerInstanceTransDto transDto) {
        getStationTransCheckerList().forEach(i -> i.check(transDto));
    }

}
