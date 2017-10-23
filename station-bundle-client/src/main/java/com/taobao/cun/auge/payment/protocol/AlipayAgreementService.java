package com.taobao.cun.auge.payment.protocol;

import com.taobao.cun.auge.common.result.Result;

/**
 * 支付协议接口
 * @author zhenhuan.zhangzh
 *
 */
public interface AlipayAgreementService {

	/**
	 * 创建支付协议签约URL
	 * @param taobaoUserId
	 * @return
	 */
	Result<String> createAlipayAgreementSignUrl(Long taobaoUserId);
	
	/**
	 * 返回用户是否签约接口
	 * @param taobaoUserId
	 * @return
	 */
	Result<Boolean> isAlipayAgreement(Long taobaoUserId);
	
	/**
	 * 签约支付回调
	 * @param taobaoUserId
	 * @return
	 */
	Result<Void> alipayAgreementCallBack(Long taobaoUserId);
}
