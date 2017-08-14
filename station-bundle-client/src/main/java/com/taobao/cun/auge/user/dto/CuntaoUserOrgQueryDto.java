package com.taobao.cun.auge.user.dto;

import java.io.Serializable;
import java.util.List;

public class CuntaoUserOrgQueryDto implements Serializable{
    private static final long serialVersionUID = -2127169798913233170L;
    private int pageSize;
	private int page;
    private String userName;//用户名,可为空
    private String loginId;//登陆账号,可为空
    private List<Long> orgIds;//所属组织id,不能为空
    private List<CuntaoUserStausEnum> userStatuses;//用户状态
    /**
     * 用户类型
     */
    private CuntaoUserTypeEnum userType; 
    
    private Long providerId;
    
    public static long getSerialVersionUID() {
        return serialVersionUID;
    }


    public int getPageSize() {
		return pageSize;
	}


	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}


	public int getPage() {
		return page;
	}


	public void setPage(int page) {
		this.page = page;
	}


	public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Long> getOrgIds() {
        return orgIds;
    }

    public void setOrgIds(List<Long> orgIds) {
        this.orgIds = orgIds;
    }

    public List<CuntaoUserStausEnum> getUserStatuses() {
        return userStatuses;
    }

    public void setUserStatuses(List<CuntaoUserStausEnum> userStatuses) {
        this.userStatuses = userStatuses;
    }

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public CuntaoUserTypeEnum getUserType() {
		return userType;
	}

	public void setUserType(CuntaoUserTypeEnum userType) {
		this.userType = userType;
	}

	public Long getProviderId() {
		return providerId;
	}

	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}
	
}
