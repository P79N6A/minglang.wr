package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.enums.PartnerLifecycleGoodsReceiptEnum;

import com.taobao.cun.auge.station.enums.PartnerLifecycleReplenishMoneyEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecyclePositionConfirmEnum;

/**
 * 生命周期扩展表 服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerLifecycleBO {
	
	/**
	 * 新增生命周期扩展数据
	 * @param partnerLifecycle
	 * @
	 */
	public void addLifecycle(PartnerLifecycleDto partnerLifecycleDto);
	
	/**
	 * 更新生命周期扩展数据
	 * @param lifecycle
	 * @
	 */
	public void updateLifecycle(PartnerLifecycleDto partnerLifecycleDto);
	
	/**
	 * 获得当前生命周期扩展数据 主键id
	 * @param instanceId
	 * @param businessTypeEnum
	 * @param stepEnum
	 * @return
	 * @
	 */
	public Long getLifecycleItemsId(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) ;
	/**
	 * 获得当前生命周期扩展数据
	 * @param instanceId
	 * @param businessTypeEnum
	 * @param stepEnum
	 * @return
	 * @
	 */
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum, PartnerLifecycleCurrentStepEnum stepEnum) ;
	
	/**
	 *  获得当前生命周期扩展数据
	 * @param instanceId
	 * @param businessTypeEnum
	 * @return
	 * @
	 */
	public PartnerLifecycleItems getLifecycleItems(Long instanceId,PartnerLifecycleBusinessTypeEnum businessTypeEnum) ; 
	
	/**
	 * 根据实例id,删除生命周期数据
	 * @param instanceId
	 * @param operator
	 * @
	 */
	public void deleteLifecycleItems(Long instanceId,String operator) ; 
	
	
	/**
	 * 更新装修状态
	 * @param instanceId
	 * @param decorateStateEnum
	 * @param operator
	 * @
	 */
	public void updateDecorateState(Long instanceId, PartnerLifecycleDecorateStatusEnum decorateStateEnum,OperatorDto operatorDto) ; 
	
	/**
	 * 更新培训状态
	 * @param instanceId
	 * @param courseStatusEnum
	 * @param operator
	 * @
	 */
	public void updateCourseState(Long instanceId, PartnerLifecycleCourseStatusEnum courseStatusEnum,OperatorDto operatorDto) ;

	/**
	 * 根据ID获取PartnerLifecycleItems对象
	 * @param id
	 * @return
	 */
	public PartnerLifecycleItems getLifecycleItems(long id); 
	
	/**
	 * 确认经纬度位置
	 * @param instanceId
	 * @param partnerLifecyclePositionConfrimEnum
	 */
	public void updateConfirmPosition(Long instanceId, PartnerLifecyclePositionConfirmEnum partnerLifecyclePositionConfrimEnum);
	
	/**
	 * 更新补货金
	 * @param instanceId
	 * @param courseStatusEnum
	 * @param operator
	 * @
	 */
	public void updateReplenishMoney(Long instanceId, PartnerLifecycleReplenishMoneyEnum replenishMoneyEnum,OperatorDto operatorDto) ;
	
	/**
	 * 更新开业包收货状态
	 * @param instanceId
	 * @param courseStatusEnum
	 * @param operator
	 * @
	 */
	public void updateGoodsReceipt(Long instanceId, PartnerLifecycleGoodsReceiptEnum goodsReceiptEnum,OperatorDto operatorDto) ;
}
