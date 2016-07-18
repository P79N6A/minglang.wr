package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;

public interface PartnerInstanceExtBO {

	/**
	 * 查询合伙人子成员的最大名额，db中有，则返回，没有则返回默认最大配额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long instanceId);
	
	
	/**
	 * 查询合伙人数据库中，当前子成员最大配额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerCurMaxChildNum(Long instanceId);
	
	/**
	 * 批量查询合伙人实例扩展信息
	 * 
	 * @param instanceIds
	 * @return
	 */
	public List<PartnerInstanceExt> findPartnerInstanceExts(List<Long> instanceIds);
	
	/**
	 * 修改合伙人最大子成员配额
	 * 
	 * @param instanceId
	 * @param maxNum
	 */
	public void updatePartnerInstanceExt(Long instanceId, Integer maxNum,String operator);
	
	
	/**
	 * 新增合伙人扩展
	 * 
	 * @param instanceExt
	 */
	public void addPartnerInstanceExt(PartnerInstanceExt instanceExt);
}
