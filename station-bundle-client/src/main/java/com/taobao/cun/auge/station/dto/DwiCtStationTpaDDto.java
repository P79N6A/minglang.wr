package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DwiCtStationTpaDDto implements Serializable{
	
	private static final long serialVersionUID = -6427660592944754268L;

	private Long id;

    /**
     * 统计日期
     */
    private String statDate;

    /**
     * 数据插入时间
     */
    private String dwInsTime;

    /**
     * 业务月份
     */
    private String bizMonth;

    /**
     * 县服务中心orgId
     */
    private String countyOrgId;

    /**
     * 县服务中心名称
     */
    private String countyOrgName;

    /**
     * 村小二服务站id
     */
    private Long stationId;
    
    /**
     * 村小二服务站名称
     */
    private String stationName;

    /**
     * 淘帮手服务站id
     */
    private Long partnerStationId;

    /**
     * 淘帮手服务站名称
     */
    private String partnerStationName;

    /**
     * 淘帮手服务站状态
     */
    private String state;

    /**
     * 自然月初截至月底，服务中天数
     */
    private Long srvcDayCntMtd;

    /**
     * 自然月初截至月底，含佣父订单数
     */
    private Long payMordCntMtd023;

    /**
     * 自然月初截至月底，含佣成交金额
     */
    private BigDecimal payOrdAmtMtd001;

    /**
     * 自然月初截至月底，实物父订单数
     */
    private Long payMordCntMtd015;

    /**
     * 自然月初截至月底，实物子订单成交金额
     */
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
