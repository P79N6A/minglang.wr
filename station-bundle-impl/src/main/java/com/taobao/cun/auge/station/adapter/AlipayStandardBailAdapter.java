package com.taobao.cun.auge.station.adapter;

import com.taobao.cun.dto.alipay.AlipayStandardBailDto;

/**
 * 支付宝保证金操作适配器
 *
 */
public interface AlipayStandardBailAdapter {
	/**
	 * 支付宝保证金处理接口
	 * 
	 * @param alipayStandardBailDto
	 * @return
	 */
	public boolean dealStandardBail(AlipayStandardBailDto alipayStandardBailDto);
}
