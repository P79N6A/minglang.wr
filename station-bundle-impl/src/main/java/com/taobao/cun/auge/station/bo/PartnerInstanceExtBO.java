package com.taobao.cun.auge.station.bo;

public interface PartnerInstanceExtBO {

	/**
	 * 查询合伙人子成员的最大名额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long instanceId);

}
