package com.taobao.cun.auge.station.adapter;

public interface UicReadAdapter {
	
	public String getFullName(Long taobaoUserId);
	
	public Long getTaobaoUserIdByTaobaoNick(String taobaoNick);
	
}
