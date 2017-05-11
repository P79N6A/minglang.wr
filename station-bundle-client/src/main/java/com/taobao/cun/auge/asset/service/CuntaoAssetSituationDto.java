package com.taobao.cun.auge.asset.service;

import java.io.Serializable;

/**
 * @author quanzhu.wangqz
 *
 */
public class CuntaoAssetSituationDto implements Serializable  {

	private static final long serialVersionUID = -3469189812481637610L;
	
	//县库存资产
	public static final String  COUNTY_HAS_ASSET="countyHasAsset";
	
	//已申请资产
	public static final String  COUNTY_APPLY__ASSET="countyApplyAsset";
	
	/**
	 * 类目名称
	 */
	private String category;
	
	/**
	 * 总数
	 */
	private Long count;

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
