package com.taobao.cun.auge.station.condition;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationApplyStateEnum;

public class OldPartnerInstancePageCondition extends PageQuery{

	private static final long serialVersionUID = 2555611748293416838L;

	// 村服务站所属村淘组织
	private String orgIdPath;

	// 村服务站编号
	private String stationNum;

	// 村服务站名称
	private String stationName;

	// 村服务站管理员
	private String managerId;

	// 村服务站地址
	private Address address;

	// 合伙人taobao昵称
	private String taobaoNick;

	// 合伙人姓名
	private String partnerName;

	// 合伙人状态
	private StationApplyStateEnum stationApplyState;

	// 合伙人类型
	@NotNull(message = "partnerType is null")
	private PartnerInstanceTypeEnum partnerType;

	// 所属TP商id
	private Long providerId;

	public String getOrgIdPath() {
		return orgIdPath;
	}

	public void setOrgIdPath(String orgIdPath) {
		this.orgIdPath = orgIdPath;
	}

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public StationApplyStateEnum getStationApplyState() {
		return stationApplyState;
	}

	public void setStationApplyState(StationApplyStateEnum stationApplyState) {
		this.stationApplyState = stationApplyState;
	}

	public PartnerInstanceTypeEnum getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
		this.partnerType = partnerType;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
}