package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;

public class PartnerInstancePageCondition extends PageQuery {

	private static final long serialVersionUID = 8658912725579809791L;
	
	private Long[] orgIds;

	private String stationNum;

	private String stationName;

	private String taobaoNick;

	private String managerId;


	private Address address;

	
	private String partnerName;
	
	private PartnerInstanceStateEnum partnerInstanceState;
//	private PartnerInstanceTypeEnum partnerType;
	
	private String partnerType;
	
	private Long providerId;

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

	public String getPartnerType() {
		return partnerType;
	}

	public void setPartnerType(String partnerType) {
		this.partnerType = partnerType;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long[] getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(Long[] orgIds) {
		this.orgIds = orgIds;
	}

//	public PartnerInstanceTypeEnum getPartnerType() {
//		return partnerType;
//	}
//
//	public void setPartnerType(PartnerInstanceTypeEnum partnerType) {
//		this.partnerType = partnerType;
//	}

}
