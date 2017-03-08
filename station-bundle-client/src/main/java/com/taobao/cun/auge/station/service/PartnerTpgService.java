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
	
	/**
	 * 是否是供赢通会员
	 * @param partnerInstanceId
	 * @return
	 */
	public boolean isPartnerTpg(Long partnerInstanceId);

	public boolean hasTpgTag(Long taobaoUserId);
}
