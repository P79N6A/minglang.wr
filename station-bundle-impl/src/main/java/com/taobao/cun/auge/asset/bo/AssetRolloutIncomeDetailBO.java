package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailExtDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;

public interface AssetRolloutIncomeDetailBO {

	/**
	 * 入库单id  和状态查资产总数
	 * @param incomeId
	 * @param status
	 * @return
	 */
	public List<AssetCategoryCountDto> queryCountByIncomeId(Long incomeId, List<String> statusList);
	
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
	 * 根据入库单id 查询
	 * @param incomeId
	 * @return
	 */
	public List<AssetRolloutIncomeDetail> queryListByIncomeId(Long incomeId);
	/**
	 * 查询待签收资产
	 * @param assetId
	 * @return
	 */
	public AssetRolloutIncomeDetail queryWaitSignByAssetId(Long assetId);
	
	/**
	 * 撤销  
	 * @return 
	 */
	public AssetRolloutIncomeDetail  cancel(Long assetId, String operator);
	/**
	 * 查询入库单资产详情列表
	 * @param incomeId
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<AssetRolloutIncomeDetailExtDto> queryPageByIncomeId(Long incomeId,AssetRolloutIncomeDetailStatusEnum status,int pageNum,int pageSize);
	
	/**
	 * 查询出库单资产详情列表
	 * @param incomeId
	 * @param status
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public Page<AssetRolloutIncomeDetailExtDto> queryPageByRolloutId(Long rolloutId,AssetRolloutIncomeDetailStatusEnum status,int pageNum,int pageSize);
	
	/**
	 * 是否有撤销的资产
	 * @param incomeId
	 * @return
	 */
	public Boolean hasCancelAssetByIncomeId(Long incomeId);
	
	/**
	 * 是否有撤销的资产
	 * @param incomeId
	 * @return
	 */
	public Boolean hasCancelAssetByRolloutId(Long rolloutId);
	
	/**
	 * 查询资产出入库单详情
	 * @param assetId
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public PageDto<AssetRolloutIncomeDetailDto> queryAssetRiDetailByPage(Long assetId, int pageNum, int pageSize);
	
	/**
	 * 删除待入库详情
	 * @param assetId
	 * @param operator
	 */
	public void deleteWaitSignDetail(Long assetId,String operator);
	
}
