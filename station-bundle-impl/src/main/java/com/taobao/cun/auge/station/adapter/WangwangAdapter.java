package com.taobao.cun.auge.station.adapter;

public interface WangwangAdapter {

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
