package com.taobao.cun.auge.fengkong.data.service;

public interface FengKongDataService {

	/**
	 * 虚假校验黑名单接口新增，接口人：杨钊
	 * @param taobaoUserId
	 * @return
	 */
	public Boolean checkXuJiaBlackList(Long taobaoUserId);
}
