package com.taobao.cun.auge.station.service;

public interface WangWangTagService {
	/**
	 * 旺旺打标
	 * 
	 * @param taobaoNick
	 */
	public void addWangWangTagByNick(String taobaoNick);

	/**
	 * 旺旺去标
	 * 
	 * @param taobaoNick
	 */
	public void removeWangWangTagByNick(String taobaoNick);
}
