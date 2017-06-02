package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AssetRolloutCancelDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetRollout;

public interface AssetRolloutBO {

	/**
	 * 查询出库单列表
	 * @param queryParam
	 * @return
	 */
	public Page<AssetRollout> getRolloutList(AssetRolloutQueryCondition queryParam);
	
	/**
	 * 增加出库单
	 * @param param
	 * @return
	 */
	public Long addRollout(AssetRolloutDto param);
	
	/**
	 * 撤销出库单
	 * 返回撤销的资产id
	 * @param rolloutId
	 */
	public List<Long> cancelRolleout(AssetRolloutCancelDto cancelDto);
	
	
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
	public Long  transferAssetSelfCounty(AssetTransferDto transferDto,List<Asset> assetList);
}
