package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.Date;

public class CuntaoUserOrgVO implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long id;
	private Date gmtCreate;
	private Date gmtModified;
	private String creator;
	private String modifier;
	private String loginId;
	private Long orgId;
	private String status;
	private String userType;
	private Date startTime;
	private Date endTime;
	private String userName;
	private String feature;
	private Integer featureCc;
	private Long providerId;
	private Long userId;
	private UserRoleEnum userRoleEnum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getGmtCreate() {
		return gmtCreate;
	}

	public void setGmtCreate(Date gmtCreate) {
		this.gmtCreate = gmtCreate;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	public Integer getFeatureCc() {
		return featureCc;
	}

	public void setFeatureCc(Integer featureCc) {
		this.featureCc = featureCc;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserRoleEnum getUserRoleEnum() {
		return userRoleEnum;
	}

	public void setUserRoleEnum(UserRoleEnum userRoleEnum) {
		this.userRoleEnum = userRoleEnum;
	}
}