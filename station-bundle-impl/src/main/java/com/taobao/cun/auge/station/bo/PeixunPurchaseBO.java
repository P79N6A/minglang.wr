package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.condition.PeixunPuchaseQueryCondition;
import com.taobao.cun.auge.station.dto.PeixunPurchaseDto;

public interface PeixunPurchaseBO {

	public Long createOrUpdatePeixunPurchase(PeixunPurchaseDto dto);
	
	public boolean audit(Long id,String operate,String auditDesc,Boolean isPass);
	
	public boolean rollback(Long id ,String operate);
	
	public boolean createOrder(Long id,String operate);
	
	public PageDto<PeixunPurchaseDto> queryPeixunPurchaseList(
			PeixunPuchaseQueryCondition condition);
}
