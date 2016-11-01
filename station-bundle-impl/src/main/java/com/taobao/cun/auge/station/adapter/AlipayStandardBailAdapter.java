package com.taobao.cun.auge.station.adapter;


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
	public boolean dealStandardBail(com.taobao.cun.auge.station.dto.AlipayStandardBailDto alipayStandardBailDto);
}
