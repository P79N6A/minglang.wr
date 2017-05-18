package com.taobao.cun.auge.asset.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.dal.domain.AssetIncome;

public interface AssetIncomeBO {

	/**
	 * 查询入库单列表
	 * @param queryParam
	 * @return
	 */
	public Page<AssetIncome> getIncomeList(AssetIncomeQueryCondition queryParam);
}
