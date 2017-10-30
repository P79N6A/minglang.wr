package com.taobao.cun.auge.asset.bo;

import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.AssetIncome;

public interface AssetIncomeBO {

	/**
	 * 查询入库单列表
	 * @param queryParam
	 * @return
	 */
	public PageDto<AssetIncomeDto> getIncomeList(AssetIncomeQueryCondition condition);
	
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
	 * 县点资产签收
	 * @param aliNo
	 * @param operator
	 */
	public void signAssetByCounty(String aliNo,String operator);
	
	/**
	 * 村点签收资产
	 * @param assetId
	 * @param typeEnum
	 * @param operator
	 */
	public void signAssetByStation(Long assetId,String operator);
	
	/**
	 * 县一键签收资产
	 * @param signDto
	 */
	public void signAllAssetByCounty(AssetSignDto signDto);
	
	/**
	 * 撤销入库单
	 * @param incomeId
	 * @param operator
	 */
	public void cancelAssetIncome(Long incomeId,String operator);
	/**
	 * 删除入库单
	 * @param incomeId
	 * @param operator
	 */
	public void deleteAssetIncome(Long incomeId,String operator);
	
}
