package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;

public interface PartnerInstanceExtService {
	
	/**
	 * 根据合伙人的村点的id，查询子成员最大名额
	 * 
	 * @param partnerStationId
	 * @return
	 */
	public Integer findPartnerMaxChildNum(Long partnerStationId);

	/**
	 * 校验下一级是否超过名额限制，没有超过返回false，超过，返回true
	 * 
	 * @param parentStationId
	 *            父站点id
	 * 
	 * @return
	 */
	public Boolean validateChildNum(Long parentStationId);

	/**
	 * 查询合伙人扩展信息
	 * 
	 * @param instanceIds
	 *            合伙人实例id
	 * @return
	 */
	public List<PartnerInstanceExtDto> findPartnerExtInfos(List<Long> instanceIds);
	
	/**
	 * 保存合伙人扩展，不存在修改，存在，修改
	 * 
	 * @return
	 */
	public void savePartnerExtInfo(PartnerInstanceExtDto instanceExtDto);
	
}
