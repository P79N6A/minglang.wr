package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DwiCtStationTpaDDto implements Serializable{
	
	private static final long serialVersionUID = -6427660592944754268L;

	private Long id;

    private String statDate;

    private String dwInsTime;

    private String bizMonth;

    private String countyOrgId;

    private String countyOrgName;

    private Long stationId;

    private String stationName;

    private Long partnerStationId;

    private String partnerStationName;

    private String state;

    private Long srvcDayCntMtd;

    private Long payMordCntMtd023;

    private BigDecimal payOrdAmtMtd001;

    private Long payMordCntMtd015;

    private BigDecimal payOrdAmtMtd587;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStatDate() {
		return statDate;
	}

	public void setStatDate(String statDate) {
		this.statDate = statDate;
	}

	public String getDwInsTime() {
		return dwInsTime;
	}

	public void setDwInsTime(String dwInsTime) {
		this.dwInsTime = dwInsTime;
	}

	public String getBizMonth() {
		return bizMonth;
	}

	public void setBizMonth(String bizMonth) {
		this.bizMonth = bizMonth;
	}

	public String getCountyOrgId() {
		return countyOrgId;
	}

	public void setCountyOrgId(String countyOrgId) {
		this.countyOrgId = countyOrgId;
	}

	public String getCountyOrgName() {
		return countyOrgName;
	}

	public void setCountyOrgName(String countyOrgName) {
		this.countyOrgName = countyOrgName;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	public Long getPartnerStationId() {
		return partnerStationId;
	}

	public void setPartnerStationId(Long partnerStationId) {
		this.partnerStationId = partnerStationId;
	}

	public String getPartnerStationName() {
		return partnerStationName;
	}

	public void setPartnerStationName(String partnerStationName) {
		this.partnerStationName = partnerStationName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Long getSrvcDayCntMtd() {
		return srvcDayCntMtd;
	}

	public void setSrvcDayCntMtd(Long srvcDayCntMtd) {
		this.srvcDayCntMtd = srvcDayCntMtd;
	}

	public Long getPayMordCntMtd023() {
		return payMordCntMtd023;
	}

	public void setPayMordCntMtd023(Long payMordCntMtd023) {
		this.payMordCntMtd023 = payMordCntMtd023;
	}

	public BigDecimal getPayOrdAmtMtd001() {
		return payOrdAmtMtd001;
	}

	public void setPayOrdAmtMtd001(BigDecimal payOrdAmtMtd001) {
		this.payOrdAmtMtd001 = payOrdAmtMtd001;
	}

	public Long getPayMordCntMtd015() {
		return payMordCntMtd015;
	}

	public void setPayMordCntMtd015(Long payMordCntMtd015) {
		this.payMordCntMtd015 = payMordCntMtd015;
	}

	public BigDecimal getPayOrdAmtMtd587() {
		return payOrdAmtMtd587;
	}

	public void setPayOrdAmtMtd587(BigDecimal payOrdAmtMtd587) {
		this.payOrdAmtMtd587 = payOrdAmtMtd587;
	}
}
