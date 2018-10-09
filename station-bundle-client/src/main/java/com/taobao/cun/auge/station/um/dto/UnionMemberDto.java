package com.taobao.cun.auge.station.um.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;

/**
 * 优盟实体dto
 *
 * @author haihu.fhh
 */
public class UnionMemberDto implements Serializable {

    private static final long serialVersionUID = 6581735268960126634L;

    /**
     * 优盟入驻实例id
     */
    private Long instanceId;

    /**
     * 优盟归属村站id
     */
    private Long parentStationId;

    /**
     * 状态
     */
    private UnionMemberStateEnum state;

    /**
     * 优盟站点信息dto
     */
    private StationDto stationDto;

    /**
     * 优盟人的信息dto
     */
    private PartnerDto partnerDto;

    public Long getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(Long instanceId) {
        this.instanceId = instanceId;
    }

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    public UnionMemberStateEnum getState() {
        return state;
    }

    public void setState(UnionMemberStateEnum state) {
        this.state = state;
    }

    public PartnerDto getPartnerDto() {
        return partnerDto;
    }

    public void setPartnerDto(PartnerDto partnerDto) {
        this.partnerDto = partnerDto;
    }

    public StationDto getStationDto() {
        return stationDto;
    }

    public void setStationDto(StationDto stationDto) {
        this.stationDto = stationDto;
    }
}
