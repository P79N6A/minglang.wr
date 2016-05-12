package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.condition.PartnerStationCondition;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;

/**
 * 合伙人实例服务
 * 
 * 合伙人每参与一次村淘，都会生成一个实例。
 * @author haihu.fhh
 *
 */
public interface TPInstanceService {
	
	/**
	 * 暂存人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long tempPartnerStation(PartnerStationCondition condition);
	
	/**
	 * 正式提交，人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long submitPartnerStation(PartnerStationCondition condition);
	
	/**
	 * 正式提交，暂存的人和村
	 * 
	 * @param condition
	 * @return
	 */
	public Long submitTempPartnerStation(PartnerStationCondition condition);
	
	
	/**
	 * 修改，人和村
	 * 
	 * @param condition
	 * @return
	 */
	public boolean updatePartnerStation(PartnerStationCondition condition);
	
	
	/**
	 * 删除合伙人实例
	 * 
	 * @param instanceId
	 * @return
	 */
	public boolean deletePartnerStation(Long instanceId);
	
	
	/**
	 * 签署入驻协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean signSettledProtocol(Long taobaoUserId);
	
	
	/**
	 * 签署管理协议
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	public boolean signManageProtocol(Long taobaoUserId);
	
	
	public PartnerInstanceDto find(PartnerStationCondition condition);
	
	
}
