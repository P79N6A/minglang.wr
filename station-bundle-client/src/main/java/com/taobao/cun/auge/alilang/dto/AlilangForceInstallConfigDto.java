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
     * 强制安装的组织id列表
     */
    private List<Long> forceInstallOrgIdList;

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
}
