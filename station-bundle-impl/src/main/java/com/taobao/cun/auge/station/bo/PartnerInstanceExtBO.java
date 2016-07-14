package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;

public interface PartnerInstanceExtBO {

	/**
	 * 查询合伙人子成员的最大名额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long instanceId);
	
	/**
	 * 批量查询合伙人实例扩展信息
	 * 
	 * @param instanceIds
	 * @return
	 */
	public List<PartnerInstanceExt> findPartnerInstanceExts(List<Long> instanceIds);

}
