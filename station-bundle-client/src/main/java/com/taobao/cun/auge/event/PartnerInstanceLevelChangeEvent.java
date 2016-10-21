package com.taobao.cun.auge.event;

import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEvaluateTypeEnum;

public class PartnerInstanceLevelChangeEvent extends OperatorDto {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6742134437243083700L;

	/**
	 * 实例id,内部使用，外部系统不要使用
	 */
	private Long partnerInstanceId;

	/**
	 * 服务站id
	 */
	private Long stationId;

	/**
	 * 当前合伙人淘宝userId
	 */
	private Long taobaoUserId;

	/**
	 * 当前层级
	 */
	private PartnerInstanceLevelEnum currentLevel;
	/**
	 * 评定人
	 */
	private String evaluateBy;
	/**
	 * 评定类型: 系统评定，人工评定
	 */
	private PartnerInstanceLevelEvaluateTypeEnum evaluateType;
	/**
	 * 上次评定层级
	 */
	private PartnerInstanceLevelEnum preLevel;
	/**
	 * 预授层级
	 */
	private PartnerInstanceLevelEnum expectedLevel;
	/**
	 * 评定日期
	 */
	private Date evaluateDate;

	/**
	 * 备注
	 */
	private String remark;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public PartnerInstanceLevelEnum getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(PartnerInstanceLevelEnum currentLevel) {
		this.currentLevel = currentLevel;
	}

	public String getEvaluateBy() {
		return evaluateBy;
	}

	public void setEvaluateBy(String evaluateBy) {
		this.evaluateBy = evaluateBy;
	}

	public PartnerInstanceLevelEnum getPreLevel() {
		return preLevel;
	}

	public void setPreLevel(PartnerInstanceLevelEnum preLevel) {
		this.preLevel = preLevel;
	}

	public PartnerInstanceLevelEnum getExpectedLevel() {
		return expectedLevel;
	}

	public void setExpectedLevel(PartnerInstanceLevelEnum expectedLevel) {
		this.expectedLevel = expectedLevel;
	}

	public Date getEvaluateDate() {
		return evaluateDate;
	}

	public void setEvaluateDate(Date evaluateDate) {
		this.evaluateDate = evaluateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public PartnerInstanceLevelEvaluateTypeEnum getEvaluateType() {
		return evaluateType;
	}

	public void setEvaluateType(PartnerInstanceLevelEvaluateTypeEnum evaluateType) {
		this.evaluateType = evaluateType;
	}

}
