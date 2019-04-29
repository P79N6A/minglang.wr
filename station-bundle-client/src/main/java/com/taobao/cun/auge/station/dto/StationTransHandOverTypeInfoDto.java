package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.station.enums.StationTransHandOverNodeEnum;
import com.taobao.cun.auge.station.enums.StationTransHandOverTypeEnum;

import java.io.Serializable;

public class StationTransHandOverTypeInfoDto implements Serializable {

    private StationTransHandOverTypeEnum stationTransHandOverTypeEnum;

    private StationTransHandOverNodeEnum stationTransHandOverNodeEnum;

    public StationTransHandOverTypeEnum getStationTransHandOverTypeEnum() {
        return stationTransHandOverTypeEnum;
    }

    public void setStationTransHandOverTypeEnum(StationTransHandOverTypeEnum stationTransHandOverTypeEnum) {
        this.stationTransHandOverTypeEnum = stationTransHandOverTypeEnum;
    }

    public StationTransHandOverNodeEnum getStationTransHandOverNodeEnum() {
        return stationTransHandOverNodeEnum;
    }

    public void setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum stationTransHandOverNodeEnum) {
        this.stationTransHandOverNodeEnum = stationTransHandOverNodeEnum;
    }
}
