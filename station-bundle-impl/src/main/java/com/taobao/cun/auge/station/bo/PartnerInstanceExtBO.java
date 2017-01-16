package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;

public interface PartnerInstanceExtBO {

	/**
	 * 查询合伙人数据库中，当前子成员最大配额
	 * 
	 * @param instanceId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long instanceId);
	
	/**
	 * 查询合伙人当前实际子成员数量
	 * 
	 * @param partnerInstanceId
	 * @return
	 */
	public Integer findPartnerChildrenNum(Long instanceId);
	
	/**
	 * 批量查询合伙人实例扩展信息
	 * 
	 * @param instanceIds
	 * @return
	 */
	public List<PartnerInstanceExt> findPartnerInstanceExts(List<Long> instanceIds);
	
	/**
	 * 查询合伙人实例扩展
	 * 
	 * @param instanceId
	 * @return
	 */
	public PartnerInstanceExt findPartnerInstanceExt(Long instanceId);
	
	/**
	 * 修改合伙人扩展
	 * 
	 * @param instanceExtDto
	 */
	public void updatePartnerInstanceExt(PartnerInstanceExtDto instanceExtDto);
	
	
	/**
	 * 新增合伙人扩展
	 * 
	 * @param instanceExtDto
	 */
	public Long addPartnerInstanceExt(PartnerInstanceExtDto instanceExtDto);
}
