package com.taobao.cun.auge.payment.account.utils;

import com.taobao.cun.auge.common.utils.IdCardUtil;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.security.util.SensitiveDataUtil;

public final class PaymentAccountDtoUtil {

    private PaymentAccountDtoUtil() {

    }

    /**
     * 将支付宝账户信息脱敏
     *
     * @param paymentAccountDto 支付宝账号信息
     */
    public static void hidepaymentAccount(AliPaymentAccountDto paymentAccountDto) {
        paymentAccountDto.setAlipayId(SensitiveDataUtil.alipayLogonIdHide(paymentAccountDto.getAlipayId()));
        //和村站详情上，省份证脱敏规则保持一致
        paymentAccountDto.setIdCardNumber(IdCardUtil.idCardNoHide(paymentAccountDto.getIdCardNumber()));
        paymentAccountDto.setFullName(SensitiveDataUtil
            .customizeHide(paymentAccountDto.getFullName(), 0, paymentAccountDto.getFullName().length() - 1, 1));
    }
}
