package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum.PartnerInstanceType;

public class PartnerInstancePageCondition extends PageQuery {

	private static final long serialVersionUID = 8658912725579809791L;

	private String stationNum;

	private String stationName;

	private String taobaoNick;

	private String managerId;

	private PartnerInstanceStateEnum partnerInstanceState;

	private Address address;

	private PartnerInstanceType partnerType;

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

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public PartnerInstanceStateEnum getPartnerInstanceState() {
		return partnerInstanceState;
	}

	public void setPartnerInstanceState(PartnerInstanceStateEnum partnerInstanceState) {
		this.partnerInstanceState = partnerInstanceState;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public PartnerInstanceType getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(PartnerInstanceType partnerType) {
		this.partnerType = partnerType;
	}
}
