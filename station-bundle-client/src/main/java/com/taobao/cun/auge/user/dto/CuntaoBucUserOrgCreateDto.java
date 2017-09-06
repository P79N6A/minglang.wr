package com.taobao.cun.auge.user.dto;

import java.io.Serializable;


public class CuntaoBucUserOrgCreateDto implements Serializable{
    private static final long serialVersionUID = 1868465957240777679L;
    private String workNo;//员工工号
    private String userName;//员工姓名
    private Long orgId;//组织id
    private CuntaoUserTypeEnum userType;
    private String mobile;
    private String divisionId;
    
    private Long providerId;
    private String operator;
    
    
    
    public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

	public CuntaoUserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(CuntaoUserTypeEnum userType) {
		this.userType = userType;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(String divisionId) {
		this.divisionId = divisionId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}


}
