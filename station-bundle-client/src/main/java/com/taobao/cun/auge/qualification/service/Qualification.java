package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;
import java.util.Date;

public class Qualification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6410238660518611089L;

		//公司名称
		private String companyName;
		
		//工商营业执照注册号/统一社会信用代码
		private String qualiNo;
		
		//证件类型
		private String certificationType;
		
		//法定代表人
		private String legalPerson;

		//企业（机构）类型描述
		private String enterpriceTypeDesc;
		//企业（机构）类型 0：个人 1：企业
		private Integer enterpriceType;
		
		//工商营业执照有效期起始时间
		private String qualiStartTime;

		//工商营业执照有效期截止时间
		private String qualiEndTime;

		//注册地址
		private String regsiterAddress;

		//经营范围
		private String bizScope;

		//经营状态
		private String operationStatus;

		//工商营业执照发证机关
		private String agencies;
		
		//营业执照照片
		private String qualiPic;
		
		//状态 1：有效 2：无效
		private Integer status;
		
		//失效时间
		private Date  invalidTime;
		
		//资质主体ID
		private Long qualiInfoId;
		
		//partnerInstanceId
		private Long partnerInstanceId;
		
		//taobaoUserId
		private Long taobaoUserId;
		//资质ID
		private Long qualiId;
		//认证类型
		private Integer eidType;
		//认证用户
		private String eid;
		//资质提交时间
		private Date submitTime;
		//资质审核通过时间
		private Date auditTime;
		
		/**
		 * 合伙人实例状态
		 */
		private String partnerInstanceState;
		
		/**
		 * 转大B时间
		 */
		private Date bigBusinessTime;
		
		private String errorMessage;
		
		private String errorCode;
		
		private String qualiOss;
		
		/**
		 * 认证图片链接
		 */
		private String qualiImageUrl;
		
		private String updateFlag;
		
		private String updateDesc;
		
		private String updateReason;
		
		private String updateDate;
		
		/**
		 * 插入数据来源，
		 */
		private String source;
		
		public String getSource() {
			return source;
		}

		public void setSource(String source) {
			this.source = source;
		}

		public String getUpdateDate() {
			return updateDate;
		}

		public void setUpdateDate(String updateDate) {
			this.updateDate = updateDate;
		}
		
		public String getUpdateFlag() {
			return updateFlag;
		}

		public void setUpdateFlag(String updateFlag) {
			this.updateFlag = updateFlag;
		}

		public String getUpdateDesc() {
			return updateDesc;
		}

		public void setUpdateDesc(String updateDesc) {
			this.updateDesc = updateDesc;
		}

		public String getUpdateReason() {
			return updateReason;
		}

		public void setUpdateReason(String updateReason) {
			this.updateReason = updateReason;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public String getQualiNo() {
			return qualiNo;
		}

		public void setQualiNo(String qualiNo) {
			this.qualiNo = qualiNo;
		}

		public String getCertificationType() {
			return certificationType;
		}

		public void setCertificationType(String certificationType) {
			this.certificationType = certificationType;
		}

		public String getLegalPerson() {
			return legalPerson;
		}

		public void setLegalPerson(String legalPerson) {
			this.legalPerson = legalPerson;
		}

		public String getQualiStartTime() {
			return qualiStartTime;
		}

		public void setQualiStartTime(String qualiStartTime) {
			this.qualiStartTime = qualiStartTime;
		}

		public String getQualiEndTime() {
			return qualiEndTime;
		}

		public void setQualiEndTime(String qualiEndTime) {
			this.qualiEndTime = qualiEndTime;
		}

		public String getRegsiterAddress() {
			return regsiterAddress;
		}

		public void setRegsiterAddress(String regsiterAddress) {
			this.regsiterAddress = regsiterAddress;
		}

		public String getBizScope() {
			return bizScope;
		}

		public void setBizScope(String bizScope) {
			this.bizScope = bizScope;
		}

		public String getOperationStatus() {
			return operationStatus;
		}

		public void setOperationStatus(String operationStatus) {
			this.operationStatus = operationStatus;
		}

		public String getAgencies() {
			return agencies;
		}

		public void setAgencies(String agencies) {
			this.agencies = agencies;
		}

		public String getQualiPic() {
			return qualiPic;
		}

		public void setQualiPic(String qualiPic) {
			this.qualiPic = qualiPic;
		}

		public static long getSerialversionuid() {
			return serialVersionUID;
		}

		public Integer getStatus() {
			return status;
		}

		public void setStatus(Integer status) {
			this.status = status;
		}

		public Date getInvalidTime() {
			return invalidTime;
		}

		public void setInvalidTime(Date invalidTime) {
			this.invalidTime = invalidTime;
		}

		public Long getQualiInfoId() {
			return qualiInfoId;
		}

		public void setQualiInfoId(Long qualiInfoId) {
			this.qualiInfoId = qualiInfoId;
		}

		public Long getPartnerInstanceId() {
			return partnerInstanceId;
		}

		public void setPartnerInstanceId(Long partnerInstanceId) {
			this.partnerInstanceId = partnerInstanceId;
		}

		public Long getTaobaoUserId() {
			return taobaoUserId;
		}

		public void setTaobaoUserId(Long taobaoUserId) {
			this.taobaoUserId = taobaoUserId;
		}

		public Long getQualiId() {
			return qualiId;
		}

		public void setQualiId(Long qualiId) {
			this.qualiId = qualiId;
		}

		public Integer getEidType() {
			return eidType;
		}

		public void setEidType(Integer eidType) {
			this.eidType = eidType;
		}

		public String getEid() {
			return eid;
		}

		public void setEid(String eid) {
			this.eid = eid;
		}

		public String getEnterpriceTypeDesc() {
			return enterpriceTypeDesc;
		}

		public void setEnterpriceTypeDesc(String enterpriceTypeDesc) {
			this.enterpriceTypeDesc = enterpriceTypeDesc;
		}

		public Integer getEnterpriceType() {
			return enterpriceType;
		}

		public void setEnterpriceType(Integer enterpriceType) {
			this.enterpriceType = enterpriceType;
		}

		public Date getSubmitTime() {
			return submitTime;
		}

		public void setSubmitTime(Date submitTime) {
			this.submitTime = submitTime;
		}

		public Date getAuditTime() {
			return auditTime;
		}

		public void setAuditTime(Date auditTime) {
			this.auditTime = auditTime;
		}

		public String getPartnerInstanceState() {
			return partnerInstanceState;
		}

		public void setPartnerInstanceState(String partnerInstanceState) {
			this.partnerInstanceState = partnerInstanceState;
		}

		public Date getBigBusinessTime() {
			return bigBusinessTime;
		}

		public void setBigBusinessTime(Date bigBusinessTime) {
			this.bigBusinessTime = bigBusinessTime;
		}

		public String getErrorMessage() {
			return errorMessage;
		}

		public void setErrorMessage(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getQualiOss() {
			return qualiOss;
		}

		public void setQualiOss(String qualiOss) {
			this.qualiOss = qualiOss;
		}

		public String getErrorCode() {
			return errorCode;
		}

		public void setErrorCode(String errorCode) {
			this.errorCode = errorCode;
		}

		public String getQualiImageUrl() {
			return qualiImageUrl;
		}

		public void setQualiImageUrl(String qualiImageUrl) {
			this.qualiImageUrl = qualiImageUrl;
		}

}
