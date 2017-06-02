package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;

public interface AssetRolloutIncomeDetailBO {

	/**
	 * 入库单id  和状态查资产总数
	 * @param incomeId
	 * @param status
	 * @return
	 */
	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, AssetRolloutIncomeDetailStatusEnum status);
	
	/**
	 * 出库单id  和状态查资产总数
	 * @param rolloutId
	 * @param status
	 * @return
	 */
	public List<AssetCategoryCountDto> queryCountByRolloutId(Long rolloutId, AssetRolloutIncomeDetailStatusEnum status);
	
	/**
	 * 签收确认资产
	 * @param id
	 * @param typeEnum
	 */
	public void signAsset(Long id, String operator);
	
	/**
	 * 根据出库单id 查询 对应资产是否都被签收
	 * @param rolloutId
	 * @return
	 */
	public Boolean isAllSignByRolloutId(Long rolloutId);
	
	/**
	 * 根据入库单id 查询 对应资产是否都被签收
	 * @param incomeId
	 * @return
	 */
	public Boolean isAllSignByIncomeId(Long incomeId);
	
	/**
	 * 新增出入库单详情
	 * @param param
	 * @return
	 */
	public Long addDetail(AssetRolloutIncomeDetailDto param);
	
	/**
	 * 根据出库单id 查询
	 * @param rolloutId
	 * @return
	 */
	public List<AssetRolloutIncomeDetail> queryListByRolloutId(Long rolloutId);
	
	/**
	 * 查询待签收资产
	 * @param assetId
	 * @return
	 */
	public AssetRolloutIncomeDetail queryWaitSignByAssetId(Long assetId);
	
	/**
	 * 撤销
	 * @param rolloutId
	 */
	public void  cancel(Long rolloutId, String operator);
}
