package com.taobao.cun.auge.alilang.dto;

import java.io.Serializable;

/**
 * Created by linjianke on 16/12/8.
 */
public class AlilangProfileDto implements Serializable{
    private static final long serialVersionUID = 8746516120131781137L;
    /**
     * 淘宝用户ID
     */
    private Long taobaoUserId;
    /**
     * 村蜜阿里郎用户ID
     */
    private String aliLangUserId;
    /**
     * 是否强制安装村蜜阿里郎
     */
    private boolean forceInstallAlilang;

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getAliLangUserId() {
        return aliLangUserId;
    }

    public void setAliLangUserId(String aliLangUserId) {
        this.aliLangUserId = aliLangUserId;
    }

    public boolean isForceInstallAlilang() {
        return forceInstallAlilang;
    }

    public void setForceInstallAlilang(boolean forceInstallAlilang) {
        this.forceInstallAlilang = forceInstallAlilang;
    }
}
