package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetDetailDto implements Serializable{

    private static final long serialVersionUID = 7100435184292730173L;

    private Long id;

    private String aliNo;

    private String name;

    private String useArea;

    private String user;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUseArea() {
        return useArea;
    }

    public void setUseArea(String useArea) {
        this.useArea = useArea;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
