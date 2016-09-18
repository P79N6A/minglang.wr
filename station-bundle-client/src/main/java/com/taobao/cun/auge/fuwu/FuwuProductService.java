package com.taobao.cun.auge.fuwu;

import com.taobao.cun.auge.fuwu.dto.FuwuProductDto;
import com.taobao.cun.auge.fuwu.dto.FuwuProductPackageDto;

/**
 * 产品服务
 * @author yi.shaoy
 *
 */
public interface FuwuProductService {

	/**
	 * 根据套餐id获取产品列表
	 * @return
	 */
	public FuwuProductPackageDto queryProductListByPolicyId(Integer policyId);
	
	/**
	 * 根据产品code获取产品详情
	 */
	public FuwuProductDto queryProductByCode(String productCode);
	
}
