package com.taobao.cun.auge.station.response;

import java.io.Serializable;
import java.util.Date;

public class TpaStationInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3620182765751172293L;

	private Long taobaoUserId; // 淘宝user_id
	
	private String loginId; // 淘宝登录账号
	
	private String applierName; // 申请人姓名
	
	private String mobile; // 手机号码
	
	private Date applyDate; // 申请日期
	
	private String name; // 服务点名
	
	private String email; // email
	
	private String idenNum; // 身份证号码
	
	private String alipayAccount; // 支付宝账号
	
	private String submittedPeopleName; // 提交人姓名
	
	private String operatorType;
	
	private Long partnerStationId;

	private String state; // 状态CODE
	
	private String stateMessage; // 状态信息

	private Long cainiaoStationId; // cainiaoStationId

	private String cainiaoLogisticsStatus; // 菜鸟物流状态

	private Long tpaStationId;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getApplierName() {
		return applierName;
	}

	public void setApplierName(String applierName) {
		this.applierName = applierName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getApplyDate() {
		return applyDate;
	}

	public void setApplyDate(Date applyDate) {
		this.applyDate = applyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdenNum() {
		return idenNum;
	}

	public void setIdenNum(String idenNum) {
		this.idenNum = idenNum;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getSubmittedPeopleName() {
		return submittedPeopleName;
	}

	public void setSubmittedPeopleName(String submittedPeopleName) {
		this.submittedPeopleName = submittedPeopleName;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
	}

	public Long getPartnerStationId() {
		return partnerStationId;
	}

	public void setPartnerStationId(Long partnerStationId) {
		this.partnerStationId = partnerStationId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStateMessage() {
		return stateMessage;
	}

	public void setStateMessage(String stateMessage) {
		this.stateMessage = stateMessage;
	}

	public Long getCainiaoStationId() {
		return cainiaoStationId;
	}

	public void setCainiaoStationId(Long cainiaoStationId) {
		this.cainiaoStationId = cainiaoStationId;
	}

	public String getCainiaoLogisticsStatus() {
		return cainiaoLogisticsStatus;
	}

	public void setCainiaoLogisticsStatus(String cainiaoLogisticsStatus) {
		this.cainiaoLogisticsStatus = cainiaoLogisticsStatus;
	}

	public Long getTpaStationId() {
		return tpaStationId;
	}

	public void setTpaStationId(Long tpaStationId) {
		this.tpaStationId = tpaStationId;
	}

}
