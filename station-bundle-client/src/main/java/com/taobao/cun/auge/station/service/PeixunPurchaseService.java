package com.taobao.cun.auge.station.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PartnerPeixunSupplierDto;
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

	public String getPurchaseJson();
	
	/**
	 * 根据省id获取对应供应商列表
	 */
	public List<PartnerPeixunSupplierDto> getSupplierListByProvince(Long provinceOrgId);
	
	/**
	 * 判断采购中心流程是否走完
	 */
	public String getPoNo(Long id);
	
}
