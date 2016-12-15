package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApply;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;

public interface PartnerTypeChangeApplyBO {
	/**
	 * 是否是升级后的实例
	 *
	 * @param nextInstanceId 升级后的实例id
	 * @return
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public Boolean isUpgradePartnerInstance(Long nextInstanceId) throws AugeServiceException, AugeSystemException ;
	
	/**
	 * 根据升级后的实例id，查询升级申请单信息
	 *
	 * @param instanceId 升级后的实例id
	 * @return
	 * @throws AugeServiceException
	 * @throws AugeSystemException
	 */
	public PartnerTypeChangeApply getPartnerTypeChangeApply(Long upgradeInstanceId)	throws AugeServiceException, AugeSystemException;
}
