package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class DwiCtStationTpaIncomeMDto implements Serializable {

	private static final long serialVersionUID = 5624759613233290535L;

	private Long id;

	private String statDate;

	private String bizMonth;

	private String countyOrgId;

	private String countyOrgName;

	private Long stationId;

	private String stationName;

	private Long tpaSrvcCnt1d;

	private BigDecimal ctSrvcTpaIncomeEstFeeAmtMtd;

	private BigDecimal ctSrvcTpaIncomeEstFeeAmtAvgMtd;

	private Long stationSrvcCnt;

	private Long ctSrvcTpaIncomeEstFeeAmtAvgRankMtd;

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

	public Long getTpaSrvcCnt1d() {
		return tpaSrvcCnt1d;
	}

	public void setTpaSrvcCnt1d(Long tpaSrvcCnt1d) {
		this.tpaSrvcCnt1d = tpaSrvcCnt1d;
	}

	public BigDecimal getCtSrvcTpaIncomeEstFeeAmtMtd() {
		return ctSrvcTpaIncomeEstFeeAmtMtd;
	}

	public void setCtSrvcTpaIncomeEstFeeAmtMtd(BigDecimal ctSrvcTpaIncomeEstFeeAmtMtd) {
		this.ctSrvcTpaIncomeEstFeeAmtMtd = ctSrvcTpaIncomeEstFeeAmtMtd;
	}

	public BigDecimal getCtSrvcTpaIncomeEstFeeAmtAvgMtd() {
		return ctSrvcTpaIncomeEstFeeAmtAvgMtd;
	}

	public void setCtSrvcTpaIncomeEstFeeAmtAvgMtd(BigDecimal ctSrvcTpaIncomeEstFeeAmtAvgMtd) {
		this.ctSrvcTpaIncomeEstFeeAmtAvgMtd = ctSrvcTpaIncomeEstFeeAmtAvgMtd;
	}

	public Long getStationSrvcCnt() {
		return stationSrvcCnt;
	}

	public void setStationSrvcCnt(Long stationSrvcCnt) {
		this.stationSrvcCnt = stationSrvcCnt;
	}

	public Long getCtSrvcTpaIncomeEstFeeAmtAvgRankMtd() {
		return ctSrvcTpaIncomeEstFeeAmtAvgRankMtd;
	}

	public void setCtSrvcTpaIncomeEstFeeAmtAvgRankMtd(Long ctSrvcTpaIncomeEstFeeAmtAvgRankMtd) {
		this.ctSrvcTpaIncomeEstFeeAmtAvgRankMtd = ctSrvcTpaIncomeEstFeeAmtAvgRankMtd;
	}

}
