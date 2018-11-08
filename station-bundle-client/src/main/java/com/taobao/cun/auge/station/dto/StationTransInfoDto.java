package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

public class StationTransInfoDto  implements Serializable{

	private static final long serialVersionUID = -7981096622398483504L;

	/**
     * 主键ID
     */
    private Long id;

    /**
     * 对应村点，淘宝userid
     * 
     */
    private String taobaoUserId;

    /**
     * 对应村点ID
     */
    private Long stationId;

    /**
     * 类型
     */
    private String type;

    /**
     * 状态 com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum
     */
    private String status;

    /**
     * 来源数据，业务类型（）
     */
    private String fromBizType;

    /**
     * 目标数据，业务类型（）
     */
    private String toBizType;

    /**
     * 来源数据，业务时间（如：开业时间）
     */
    private Date oldOpenDate;

    /**
     * 目标数据，业务时间（如：开业时间）
     */
    private Date newOpenDate;
    
    private Date transDate;

    /**
     * 最近数据标示 （是：y,否：n）
     */
    private String isLatest;

    /**
     * 备注
     */
    private String remark;
    /**
     * 申请人
     */
	private String operator;
	/**
	 * 申请时间
	 */
	private Date operateTime;
	/**
	 * 申请人类型
	 * com.taobao.cun.auge.station.enums.OperatorTypeEnum;
	 */
	private String operatorType;
    
	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(String taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromBizType() {
		return fromBizType;
	}

	public void setFromBizType(String fromBizType) {
		this.fromBizType = fromBizType;
	}

	public String getToBizType() {
		return toBizType;
	}

	public void setToBizType(String toBizType) {
		this.toBizType = toBizType;
	}

	public Date getOldOpenDate() {
		return oldOpenDate;
	}

	public void setOldOpenDate(Date oldOpenDate) {
		this.oldOpenDate = oldOpenDate;
	}

	public Date getNewOpenDate() {
		return newOpenDate;
	}

	public void setNewOpenDate(Date newOpenDate) {
		this.newOpenDate = newOpenDate;
	}

	public Date getTransDate() {
		return transDate;
	}

	public void setTransDate(Date transDate) {
		this.transDate = transDate;
	}

	public String getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(String isLatest) {
		this.isLatest = isLatest;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
    
    
}
