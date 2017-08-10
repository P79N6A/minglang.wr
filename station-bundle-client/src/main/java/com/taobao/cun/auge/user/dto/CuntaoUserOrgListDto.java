package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.Date;

public class CuntaoUserOrgListDto implements Serializable{
    private static final long serialVersionUID = -1105740371067514549L;
    private String loginId;//登陆账号
    private String userName;//用户姓名
    private CuntaoUserStausEnum userStatus;//用户状态枚举
    private Date startTime;//生效时间
    private Date endTime;//失效时间
    private Long orgId;//组织id
    private String mobile;//手机号
    private Long divisionId;
    private String workNo;
    private Long providerId;
    private Long id;
    
    
    public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public CuntaoUserStausEnum getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(CuntaoUserStausEnum userStatus) {
        this.userStatus = userStatus;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getDivisionId() {
		return divisionId;
	}

	public void setDivisionId(Long divisionId) {
		this.divisionId = divisionId;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
}

