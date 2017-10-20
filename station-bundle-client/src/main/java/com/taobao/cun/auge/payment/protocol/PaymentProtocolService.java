package com.taobao.cun.auge.payment.protocol;

import com.taobao.cun.auge.common.result.Result;

/**
 * 支付协议接口
 * @author zhenhuan.zhangzh
 *
 */
public interface PaymentProtocolService {

	/**
	 * 创建支付协议签约URL
	 * @param taobaoUserId
	 * @return
	 */
	Result<String> createPaymentProcotolSignUrl(Long taobaoUserId);
	
	/**
	 * 返回用户是否签约接口
	 * @param taobaoUserId
	 * @return
	 */
	Result<Boolean> isSignedPaymentProcotol(Long taobaoUserId);
}
