package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;

/**
 * 培训集采表
 * @author yi.shaoy
 *
 */
public interface PeixunPurchaseService {

	/**
	 * 生成或更新采购信息
	 */
	public Long createOrUpdatePeixunPurchase(PeixunPurchaseDto dto);
	
	/**
	 * 审核集采申请
	 */
	public boolean audit(Long id,String operate,String auditDesc,Boolean isPass);
	
	/**
	 * 撤回集采申请
	 */
	public boolean rollback(Long id ,String operate);
	
	/**
	 * 下单
	 */
	public boolean createOrder(Long id,String operate);
	
	/**
	 * 
	 * @param condition
	 * @return
	 */
	public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(PeixunPuchaseQueryCondition condition);
	
	public PeixunPurchaseDto queryById(Long id);

}
