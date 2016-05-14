package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 生命周期扩展表 服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerLifecycleBO {
	
	/**
	 * 新增生命周期扩展数据
	 * @param partnerLifecycle
	 * @throws AugeServiceException
	 */
	public void addLifecycle(PartnerLifecycleCondition partnerLifecycle)throws AugeServiceException;
	
	/**
	 * 更新生命周期扩展数据
	 * @param lifecycle
	 * @throws AugeServiceException
	 */
	public void updateLifecycle(PartnerLifecycleCondition lifecycle)throws AugeServiceException;
	
	/**
	 * 获得当前生命周期扩展数据 主键id
	 * @param instanceId
	 * @param businessTypeEnum
	 * @param stepEnum
	 * @return
	 * @throws AugeServiceException
	 */
	public Long getLifecycleItemsId(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException;
	/**
	 * 获得当前生命周期扩展数据
	 * @param instanceId
	 * @param businessTypeEnum
	 * @param stepEnum
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) throws AugeServiceException;
}
