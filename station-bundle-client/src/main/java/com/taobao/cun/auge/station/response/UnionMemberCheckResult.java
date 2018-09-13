package com.taobao.cun.auge.station.response;

import java.io.Serializable;

import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;

/**
 * 校验优盟账号结果dto
 *
 * @author haihu.fhh
 */
public class UnionMemberCheckResult implements Serializable {

    private static final long serialVersionUID = -4476118451750576708L;

    private boolean success;

    private AliPaymentAccountDto aliPaymentAccountDto;

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

    public AliPaymentAccountDto getAliPaymentAccountDto() {
        return aliPaymentAccountDto;
    }

    public void setAliPaymentAccountDto(AliPaymentAccountDto aliPaymentAccountDto) {
        this.aliPaymentAccountDto = aliPaymentAccountDto;
    }
}
