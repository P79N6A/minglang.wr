package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;

public class PartnerInstanceLevelProcessDto implements Serializable {

	private static final long serialVersionUID = -3926533034603882942L;

	// 业务code
	private String businessCode;

	// 业务主键id
	private Long businessId;

	/**
	 * 县服务中心组织id
	 */
	private Long countyOrgId;
	/**
	 * 县服务中心
	 */
	private String countyStationName;
	/**
	 * 县小二工号
	 */
	private Long employeeId;
	
	/**
	 * 申请事件
	 */
	private Date applyTime;
	/**
	 * 合伙人淘宝id
	 */
	private Long partnerTaobaoUserId;
	/**
	 * 合伙人姓名
	 */
	private String partnerName;
	/**
	 * 村点id
	 */
	private Long stationId;
	/**
	 * 村点名
	 */
	private String stationName;
	/**
	 * 当前层级
	 */
	private PartnerInstanceLevelEnum currentLevel;

	/**
	 * 预授层级
	 */
	private PartnerInstanceLevelEnum expectedLevel;
	/**
	 * 分数
	 */
	private BigDecimal score;
	/**
	 * 月收入
	 */
	private BigDecimal monthlyIncome;

	public Long getCountyOrgId() {
		return countyOrgId;
	}

	public void setCountyOrgId(Long countyOrgId) {
		this.countyOrgId = countyOrgId;
	}

	public String getCountyStationName() {
		return countyStationName;
	}

	public void setCountyStationName(String countyStationName) {
		this.countyStationName = countyStationName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public Long getPartnerTaobaoUserId() {
		return partnerTaobaoUserId;
	}

	public void setPartnerTaobaoUserId(Long partnerTaobaoUserId) {
		this.partnerTaobaoUserId = partnerTaobaoUserId;
	}

	public String getPartnerName() {
		return partnerName;
	}

	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}

	public PartnerInstanceLevelEnum getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(PartnerInstanceLevelEnum currentLevel) {
		this.currentLevel = currentLevel;
	}

	public PartnerInstanceLevelEnum getExpectedLevel() {
		return expectedLevel;
	}

	public void setExpectedLevel(PartnerInstanceLevelEnum expectedLevel) {
		this.expectedLevel = expectedLevel;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public BigDecimal getMonthlyIncome() {
		return monthlyIncome;
	}

	public void setMonthlyIncome(BigDecimal monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
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
	

}
