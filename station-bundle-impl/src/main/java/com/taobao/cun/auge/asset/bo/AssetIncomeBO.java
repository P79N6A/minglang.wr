package com.taobao.cun.auge.asset.bo;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.dal.domain.AssetIncome;

public interface AssetIncomeBO {

	/**
	 * 查询入库单列表
	 * @param queryParam
	 * @return
	 */
	public Page<AssetIncome> getIncomeList(AssetIncomeQueryCondition queryParam);
	
	/**
	 * 增加入库单
	 * @param param
	 * @return
	 */
	public Long addIncome(AssetIncomeDto param);
	
	/**
	 * 更新入库单状态
	 * @param incomeId
	 * @param statusEnum
	 */
	public void updateStatus(Long incomeId,AssetIncomeStatusEnum statusEnum,String operator);
	
	/**
	 *  获得入库单
	 * @param incomeId
	 * @return
	 */
	public AssetIncome getIncomeById(Long incomeId);
	
	/**
	 *  获得入库单Dto
	 * @param incomeId
	 * @return
	 */
	public AssetIncomeDto getIncomeDtoById(Long incomeId);
	
	/**
	 * 签收资产
	 * @param assetId
	 * @param typeEnum
	 * @param operator
	 */
	public void signAsset(Long assetId,String operator);
}
