package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Map;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;

public class StationModifyApplyDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -3538823131925690116L;

	/**
     * 主键id
     */
    private Long id;

    /**
     *  业务类型
     * @mbggenerated
     */
    private StationModifyApplyBusitypeEnum busiType;

    /**
     * 村点id
     */
    private Long stationId;

    /**
     * 申请的信息 
     */
    private Map<String,String> infoMap;

    /**
     *  状态
     */
    private StationModifyApplyStatusEnum status;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StationModifyApplyBusitypeEnum getBusiType() {
		return busiType;
	}

	public void setBusiType(StationModifyApplyBusitypeEnum busiType) {
		this.busiType = busiType;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Map<String, String> getInfoMap() {
		return infoMap;
	}

	public void setInfoMap(Map<String, String> infoMap) {
		this.infoMap = infoMap;
	}

	public StationModifyApplyStatusEnum getStatus() {
		return status;
	}

	public void setStatus(StationModifyApplyStatusEnum status) {
		this.status = status;
	}
}
