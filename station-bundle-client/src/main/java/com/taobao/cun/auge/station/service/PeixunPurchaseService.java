package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunSupplierDto;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;

import java.util.List;

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

	/**
	 * 查询课程培训供应商
	 * @return
	 */
	public List<PartnerPeixunSupplierDto> getSupplierList();

}
