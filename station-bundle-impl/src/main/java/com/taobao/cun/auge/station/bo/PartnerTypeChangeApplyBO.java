package com.taobao.cun.auge.station.bo;

import java.util.Map;

import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.station.dto.PartnerTypeChangeApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.exception.AugeSystemException;

public interface PartnerTypeChangeApplyBO {
	/**
	 * 是否是升级后的实例
	 *
	 * @param nextInstanceId 升级后的实例id
	 * @return
	 * @
	 * @throws AugeSystemException
	 */
	public Boolean isUpgradePartnerInstance(Long nextInstanceId,PartnerInstanceTypeChangeEnum typeChangeEnum)  ;
	
	/**
	 * 根据升级后的实例id，查询升级申请单信息
	 *
	 * @param instanceId 升级后的实例id
	 * @return
	 * @
	 * @throws AugeSystemException
	 */
	public PartnerTypeChangeApplyDto getPartnerTypeChangeApply(Long upgradeInstanceId) ;
	
	/**
	 * 新增类型变更申请单
	 * 
	 * @param applyDto
	 * @
	 * @throws AugeSystemException
	 */
	public Long addPartnerTypeChangeApply(PartnerTypeChangeApplyDto applyDto) ;
	
	/**
	 * 删除升级申请单
	 * 
	 * @param applyId
	 * @
	 * @throws AugeSystemException
	 */
	public void deletePartnerTypeChangeApply(Long applyId,String operator) ;
	
	/**
	 * 获取备份的村点信息
	 * 
	 * @param stationId
	 * @return
	 */
	public Map<String, String> backupStationInfo(Long stationId);
	
	/**
	 * 获取备份的村点信息
	 * 
	 * @param applyDto
	 * @return
	 */
	public StationDto fillStationDto(PartnerTypeChangeApplyDto applyDto) ;
}
