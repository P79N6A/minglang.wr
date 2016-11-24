package com.taobao.cun.auge.station.service;

public interface PartnerTpgService {

	/**
	 * 升级成供赢通会员
	 * @param taobaoUserId
	 * @return
	 */
	public boolean upgradeTpg(Long partnerInstanceId);
	
	/**
	 * 降级供赢通会员
	 * @param parnterInstanceId
	 * @return
	 */
	public boolean degradeTpg(Long partnerInstanceId);
}
