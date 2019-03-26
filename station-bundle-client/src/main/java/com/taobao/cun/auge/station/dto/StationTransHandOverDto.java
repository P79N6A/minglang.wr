package com.taobao.cun.auge.station.dto;

import com.taobao.cun.auge.station.enums.PartnerInstanceRevenueStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.StationTransInfoTypeEnum;

import java.io.Serializable;

public class StationTransHandOverDto implements Serializable {

    private StationTransInfoTypeEnum stationTransInfoTypeEnum;

    private StationTransInfoDto stationTransInfoDto;

    private PartnerInstanceRevenueStatusEnum partnerInstanceRevenueStatusEnum;

    private StationRevenueTransInfoDto stationRevenueTransInfoDto;

    public StationTransInfoTypeEnum getStationTransInfoTypeEnum() {
        return stationTransInfoTypeEnum;
    }

    public void setStationTransInfoTypeEnum(StationTransInfoTypeEnum stationTransInfoTypeEnum) {
        this.stationTransInfoTypeEnum = stationTransInfoTypeEnum;
    }

    public StationTransInfoDto getStationTransInfoDto() {
        return stationTransInfoDto;
    }

    public void setStationTransInfoDto(StationTransInfoDto stationTransInfoDto) {
        this.stationTransInfoDto = stationTransInfoDto;
    }

    public PartnerInstanceRevenueStatusEnum getPartnerInstanceRevenueStatusEnum() {
        return partnerInstanceRevenueStatusEnum;
    }

    public void setPartnerInstanceRevenueStatusEnum(PartnerInstanceRevenueStatusEnum partnerInstanceRevenueStatusEnum) {
        this.partnerInstanceRevenueStatusEnum = partnerInstanceRevenueStatusEnum;
    }

    public StationRevenueTransInfoDto getStationRevenueTransInfoDto() {
        return stationRevenueTransInfoDto;
    }

    public void setStationRevenueTransInfoDto(StationRevenueTransInfoDto stationRevenueTransInfoDto) {
        this.stationRevenueTransInfoDto = stationRevenueTransInfoDto;
    }
}
