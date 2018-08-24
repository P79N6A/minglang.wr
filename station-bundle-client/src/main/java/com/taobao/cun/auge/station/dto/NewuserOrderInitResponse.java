package com.taobao.cun.auge.station.dto;

public class NewuserOrderInitResponse {
    private Boolean success;
    /**
     * 是否还有下一页
     */
    private Boolean hasNext;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getHasNext() {
        return hasNext;
    }

    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }
}
