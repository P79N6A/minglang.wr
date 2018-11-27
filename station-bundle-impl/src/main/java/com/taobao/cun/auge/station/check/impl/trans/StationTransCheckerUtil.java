package com.taobao.cun.auge.station.check.impl.trans;

import java.util.List;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.check.impl.CheckerFactory;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;

/**
 * Created by xiao on 18/11/5.
 */

public class StationTransCheckerUtil {

    private static List<StationTransChecker> getStationTransCheckerList() {
        return CheckerFactory.getCheckerList(StationTransChecker.class);
    }

    public static void check(PartnerInstanceTransDto transDto) {
        getStationTransCheckerList().forEach(i -> i.check(transDto));
    }

}
