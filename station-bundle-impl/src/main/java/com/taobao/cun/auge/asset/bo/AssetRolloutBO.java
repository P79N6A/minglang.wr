package com.taobao.cun.auge.asset.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.dal.domain.AssetRollout;

public interface AssetRolloutBO {

	/**
	 * 查询出库单列表
	 * @param queryParam
	 * @return
	 */
	public Page<AssetRollout> getRolloutList(AssetRolloutQueryCondition queryParam);
}
