package com.taobao.cun.auge.insurance.dto;

import java.io.Serializable;

/**
 * Created by xiao on 18/10/22.
 */
public class PersonInfoDto implements Serializable {

    private static final long serialVersionUID = -3540587239300511424L;

    /**
     * 账户持有人支付宝帐号
     */
    private String alipayAccount;

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }
}
