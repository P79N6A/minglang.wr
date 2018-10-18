package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;

/**
 * 淘宝账号bo
 *
 * @author haihu.fhh
 */
public interface TaobaoAccountBo {

    /**
     * 是否是淘宝买卖家黑名单
     *
     * @param taobaoUserId
     * @return
     */
    boolean isTaobaoBuyerOrSellerBlack(Long taobaoUserId);

    /**
     * 是否是支付宝风险用户
     *
     * @param taobaoUserId
     * @return
     */
    boolean isAlipayRiskUser(AliPaymentAccountDto taobaoUserId);

}
