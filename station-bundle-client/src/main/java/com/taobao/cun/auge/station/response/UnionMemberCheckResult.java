package com.taobao.cun.auge.station.response;

import java.io.Serializable;

/**
 * 校验优盟账号结果dto
 *
 * @author haihu.fhh
 */
public class UnionMemberCheckResult implements Serializable {

    private static final long serialVersionUID = -4476118451750576708L;

    private boolean success;

    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
