package com.taobao.cun.auge.qualification.service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import com.ali.com.google.common.collect.Maps;
import com.alibaba.pm.sc.api.quali.constants.UserQualiRecordStatus;
import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.qualification.service.Qualification;

@RefreshScope
@Component
public class QualificationBuilder {
	
	public static final int PERSONAL_BUSINESS = 0;
	
	public static final int SMALL_BUSINESS = 1;
	
	public static final int BIG_BUSINESS = 2;
	
	//公司名称
	@Value("${quali.companyName}")
	private Long companyName;
	
	//工商营业执照注册号/统一社会信用代码
	@Value("${quali.qualiNo}")
	private Long qualiNo;
	
	//证件类型
	@Value("${quali.certificationType}")
	private Long certificationType;
	
	//法定代表人
	@Value("${quali.legalPerson}")
	private Long legalPerson;

	//企业（机构）类型
	@Value("${quali.enterpriceTypeDesc}")
	private Long enterpriceTypeDesc;

	//工商营业执照有效期起始时间
	@Value("${quali.qualiStartTime}")
	private Long qualiStartTime;

	//工商营业执照有效期截止时间
	@Value("${quali.qualiEndTime}")
	private Long qualiEndTime;

	//注册地址
	@Value("${quali.regsiterAddress}")
	private Long regsiterAddress;

	//经营范围
	@Value("${quali.bizScope}")
	private Long bizScope;

	//经营状态
	@Value("${quali.operationStatus}")
	private Long operationStatus;

	//工商营业执照发证机关
	@Value("${quali.agencies}")
	private Long agencies;
	
	//营业执照照片
	@Value("${quali.qualiPic}")
	private Long qualiPic;
	
	private static Map<String,Integer> businessTypeMapping = Maps.newHashMap();
	static{
		businessTypeMapping.put("个体户", SMALL_BUSINESS);
		businessTypeMapping.put("个体", SMALL_BUSINESS);
		businessTypeMapping.put("个体工商户", SMALL_BUSINESS);
		businessTypeMapping.put("自然人", PERSONAL_BUSINESS);
	}
	
	public Qualification build(Long  taobaoUserId,Optional<EntityQuali> entityQuail,Optional<UserQualiRecord> userQualiRecord){
		Qualification qualification = new Qualification();
		if(entityQuail.isPresent()){
			EntityQuali eq = entityQuail.get();
			Map<Long,Object> content = eq.getQuali().getContent();
			qualification.setTaobaoUserId(taobaoUserId);
			qualification.setEid(eq.getEid());
			qualification.setEidType(eq.getEidType());
			qualification.setQualiId(eq.getQuali().getId());
			qualification.setInvalidTime(eq.getQuali().getInvalidTime());
			qualification.setQualiInfoId(eq.getQuali().getQualiInfoId());
			qualification.setStatus(eq.getQuali().getStatus());
			qualification.setAgencies(getContent(content,companyName));
			qualification.setBizScope(getContent(content,bizScope));
			qualification.setCertificationType(getContent(content,certificationType));
			qualification.setCompanyName(getContent(content,companyName));
			qualification.setEnterpriceTypeDesc(getContent(content,enterpriceTypeDesc));
			qualification.setEnterpriceType(getEnterpriceType(getContent(content,enterpriceTypeDesc)));
			qualification.setLegalPerson(getContent(content,legalPerson));
			qualification.setOperationStatus(getContent(content,operationStatus));
			qualification.setQualiEndTime(getContent(content,qualiEndTime));
			qualification.setQualiNo(getContent(content,qualiNo));
			qualification.setQualiPic(getContent(content,qualiPic));
			qualification.setQualiStartTime(getContent(content,qualiStartTime));
			qualification.setRegsiterAddress(getContent(content,regsiterAddress));
			
		}
		if(userQualiRecord.isPresent()){
			int auditStatus = userQualiRecord.get().getStatus();
			if(UserQualiRecordStatus.AUDIT_FAIL ==  auditStatus){
				qualification.setStatus(QualificationStatus.AUDIT_FAIL);
				qualification.setErrorMessage(userQualiRecord.get().getReason());
			}
			if(UserQualiRecordStatus.TO_BE_AUDITED ==  auditStatus){
				qualification.setStatus(QualificationStatus.SUBMIT_SUCESS);
				qualification.setErrorCode("");
				qualification.setErrorMessage("");
			}
				qualification.setSubmitTime(userQualiRecord.get().getSubmitTime());
				qualification.setAuditTime(userQualiRecord.get().getAuditTime());
		}
		
		return qualification;	
	}
	
	private Integer getEnterpriceType(String enterpriceType){
		Integer enterpriceTypeValue = businessTypeMapping.get(enterpriceType);
		if(null != enterpriceTypeValue){
			return enterpriceTypeValue;
		}
		return BIG_BUSINESS;
	}
	
	public String getContent(Map<Long,Object> content,Long key){
		Optional<Object> optional =  Optional.ofNullable(content.get(key));
		if(optional.isPresent()){
			return optional.get().toString();
		}
		return null;
	}
	
	public Long getCompanyName() {
		return companyName;
	}

	public void setCompanyName(Long companyName) {
		this.companyName = companyName;
	}

	public Long getQualiNo() {
		return qualiNo;
	}

	public void setQualiNo(Long qualiNo) {
		this.qualiNo = qualiNo;
	}

	public Long getCertificationType() {
		return certificationType;
	}

	public void setCertificationType(Long certificationType) {
		this.certificationType = certificationType;
	}

	public Long getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(Long legalPerson) {
		this.legalPerson = legalPerson;
	}

	public Long getQualiStartTime() {
		return qualiStartTime;
	}

	public void setQualiStartTime(Long qualiStartTime) {
		this.qualiStartTime = qualiStartTime;
	}

	public Long getQualiEndTime() {
		return qualiEndTime;
	}

	public void setQualiEndTime(Long qualiEndTime) {
		this.qualiEndTime = qualiEndTime;
	}

	public Long getRegsiterAddress() {
		return regsiterAddress;
	}

	public void setRegsiterAddress(Long regsiterAddress) {
		this.regsiterAddress = regsiterAddress;
	}

	public Long getBizScope() {
		return bizScope;
	}

	public void setBizScope(Long bizScope) {
		this.bizScope = bizScope;
	}

	public Long getOperationStatus() {
		return operationStatus;
	}

	public void setOperationStatus(Long operationStatus) {
		this.operationStatus = operationStatus;
	}

	public Long getAgencies() {
		return agencies;
	}

	public void setAgencies(Long agencies) {
		this.agencies = agencies;
	}

	public Long getQualiPic() {
		return qualiPic;
	}

	public void setQualiPic(Long qualiPic) {
		this.qualiPic = qualiPic;
	}

	public Long getEnterpriceTypeDesc() {
		return enterpriceTypeDesc;
	}

	public void setEnterpriceTypeDesc(Long enterpriceTypeDesc) {
		this.enterpriceTypeDesc = enterpriceTypeDesc;
	}
	
}
