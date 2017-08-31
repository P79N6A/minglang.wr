package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutCancelDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetScrapDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRollout;

public interface AssetRolloutBO {

	/**
	 * 查询出库单列表
	 * @param queryParam
	 * @return
	 */
	public PageDto<AssetRolloutDto> getRolloutList(AssetRolloutQueryCondition condition);
	
	/**
	 * 增加出库单
	 * @param param
	 * @return
	 */
	public Long addRollout(AssetRolloutDto param);
	
	/**
	 * 撤销出库 资产
	 * 返回撤销的资产id
	 * @param rolloutId
	 */
	public void cancelRolleoutAsset(AssetRolloutCancelDto cancelDto);
	
	
	/**
	 *  获得出库单
	 * @param rolloutId
	 * @return
	 */
	public AssetRollout getRolloutById(Long rolloutId);
	
	/**
	 *  获得出库单Dto
	 * @param rolloutId
	 * @return
	 */
	public AssetRolloutDto getRolloutDtoById(Long rolloutId);
	
	
	/**
	 * 更新出库单状态
	 * @param incomeId
	 * @param statusEnum
	 */
	public void updateStatus(Long rolloutId,AssetRolloutStatusEnum statusEnum,String operator);
	
	/**
	 * 转移至他县
	 * @param transferDto
	 * @return
	 */
	public Long  transferAssetOtherCounty(AssetTransferDto transferDto,List<Asset> assetList);
	
	/**
	 * 转移至本县
	 * @param transferDto
	 * @return
	 */
	public Long  transferAssetSelfCounty(AssetTransferDto transferDto,List<Asset> assetList,AssetIncomeSignTypeEnum signType);
	
	/**
	 * 分发资产到村点
	 * @param distributeDto
	 * @param assetList
	 * @return
	 */
	public Long  distributeAsset(AssetDistributeDto distributeDto,List<Asset> assetList);

	public Long scrapAsset(AssetScrapDto scrapDto);
	
	public List<AssetRollout> getDistributeAsset(Long stationId,Long taobaoUserId);
	/**
	 * 转移他县 审批通过，创建入库单
	 * @param rolloutDto
	 */
	public void transferAssetOtherCounty(AssetRolloutDto rolloutDto);
}
