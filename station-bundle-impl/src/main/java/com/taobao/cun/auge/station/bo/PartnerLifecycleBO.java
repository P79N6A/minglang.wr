package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
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
	public void addLifecycle(PartnerLifecycleDto partnerLifecycleDto)throws AugeServiceException;
	
	/**
	 * 更新生命周期扩展数据
	 * @param lifecycle
	 * @throws AugeServiceException
	 */
	public void updateLifecycle(PartnerLifecycleDto partnerLifecycleDto)throws AugeServiceException;
	
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
	
	/**
	 *  获得当前生命周期扩展数据
	 * @param instanceId
	 * @param businessTypeEnum
	 * @return
	 * @throws AugeServiceException
	 */
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum) throws AugeServiceException; 
	
	/**
	 * 根据实例id,删除生命周期数据
	 * @param instanceId
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void deleteLifecycleItems(Long instanceId,String operator) throws AugeServiceException; 
	
	
	/**
	 * 更新装修状态
	 * @param instanceId
	 * @param decorateStateEnum
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void updateDecorateState(Long instanceId, PartnerLifecycleDecorateStatusEnum decorateStateEnum,OperatorDto operatorDto) throws AugeServiceException; 
	
	/**
	 * 更新培训状态
	 * @param instanceId
	 * @param courseStatusEnum
	 * @param operator
	 * @throws AugeServiceException
	 */
	public void updateCourseState(Long instanceId, PartnerLifecycleCourseStatusEnum courseStatusEnum,OperatorDto operatorDto) throws AugeServiceException;

	/**
	 * 根据ID获取PartnerLifecycleItems对象
	 * @param id
	 * @return
	 */
	public PartnerLifecycleItems getLifecycleItems(long id); 
}
