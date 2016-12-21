package com.taobao.cun.auge.alilang.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by linjianke on 16/12/8.
 */
public class AlilangForceInstallConfigDto implements Serializable {
    private static final long serialVersionUID = 3679886746930298317L;
    /**
     * 淘宝id白名单， 白名单用户不强制安装村蜜
     */
    private List<Long> whiteList;
    /**
     * 是否全部合伙人强制安装
     * 当true时，forceInstallOrgIdList将不起作用
     */
    private boolean forceAllPartnerInstall;
    /**
     * 强制安装的组织id列表
     */
    private List<Long> forceInstallOrgIdList;
    /**
     * 需要强制安装的合伙人类型
     */
    private List<String> forceInstallPartnerTypeList;

    public List<Long> getWhiteList() {
        return whiteList;
    }

    public void setWhiteList(List<Long> whiteList) {
        this.whiteList = whiteList;
    }

    public List<Long> getForceInstallOrgIdList() {
        return forceInstallOrgIdList;
    }

    public void setForceInstallOrgIdList(List<Long> forceInstallOrgIdList) {
        this.forceInstallOrgIdList = forceInstallOrgIdList;
    }

    public List<String> getForceInstallPartnerTypeList() {
        return forceInstallPartnerTypeList;
    }

    public void setForceInstallPartnerTypeList(List<String> forceInstallPartnerTypeList) {
        this.forceInstallPartnerTypeList = forceInstallPartnerTypeList;
    }

    public boolean isForceAllPartnerInstall() {
        return forceAllPartnerInstall;
    }

    public void setForceAllPartnerInstall(boolean forceAllPartnerInstall) {
        this.forceAllPartnerInstall = forceAllPartnerInstall;
    }
}
