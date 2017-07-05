package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetPurchaseDto;
import com.taobao.cun.auge.asset.dto.AssetQueryPageCondition;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;

/**
 * 资产服务，从cuntaocenter中迁移到auge中
 * @author zhenhuan.zhangzh
 *
 */
public interface AssetService {

	   public void saveAsset(CuntaoAssetDto cuntaoAssetDto,String operator);

	   public CuntaoAssetDto getCuntaoAssetById(Long cuntaoAssetId);
	   
	   public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition);

	   /**
	     * 资产签收
	     * @return
	     */
	    public void signAsset(Long assetId,String operator);
	    
	    /**
	     * 资产盘点
	     * @return
	     */
	    
	    public void checkAsset(Long assetId,String operator,CuntaoAssetEnum checkRole);
	    
	    /**
	     * 资产回收
	     * @param assetId
	     */
	    public void callbackAsset(Long assetId,String operator);
	    
	    /**
	     * 删除资产
	     * @param assetId
	     * @param operator
	     */
	    public void deleteAsset(Long assetId,String operator);
	    

	    public PageDto<String> getBoNoByOrgId(Long orgId,Integer pageNum,Integer pageSize);
	    
	    public void checkingAssetBatch(List<Long> assetIds,String operator);
	    
	    public CuntaoAssetDto queryAssetByUserAndCategory(Long userid);
	    
	    public CuntaoAssetDto queryAssetBySerialNo(String serialNo);

	    public AssetRolloutDto getRolloutById(Long id);

	    public void processAuditAssetTransfer(Long rolloutId, ProcessApproveResultEnum resultEnum);
	    /**
	     * 启动盘点 ，定时任务使用，分页查询已盘点资产
	     * @param pageNum
	     * @param pageSize
	     */
	    public PageDto<Long> getCheckedAssetId(Integer pageNum,Integer pageSize);
	    /**
	     * 启动盘点，定时任务使用
	     * @param assetId
	     * @param operator
	     */
	    public Boolean checkingAsset(Long assetId,String operator);
	    /**
	     * 采购资产
	     * @param assetPurchaseDto
	     * @return
	     */
	    public Long purchase(AssetPurchaseDto assetPurchaseDto);
	    
	    /**
	     * 新模型  列表查询
	     * @param query
	     * @return
	     */
	    public PageDto<AssetDetailDto> queryByPage(AssetQueryPageCondition query);
	    
	    /**
	     * 新模型  删除资产
	     * @param assetId
	     * @param operator
	     */
	    public void delete(Long assetId,String operator);
	    
	    /**
	     * 获得资产详情
	     * @param assetid
	     * @return
	     */
	    public AssetDetailDto getDetail(Long assetId);
	    
	    /**
	     *  获得出库单详情列表
	     * @param assetId
	     * @param pageNum
	     * @param pageSize
	     * @return
	     */
	    public PageDto<AssetRolloutIncomeDetailDto> queryAssetRiDetailByPage(Long assetId,int pageNum,int pageSize);
	    
	    /**
	     * 获得 分发中的资产  待村小二签收的资产，admin使用
	     * @param stationId
	     * @param taobaoUserId
	     * @return
	     */
	    public List<AssetDetailDto> getDistributeAssetListByStation (Long stationId, Long taobaoUserId);
	    /**
	     * 获得村小二使用的资产 admin使用
	     * @param stationId
	     * @param taobaoUserId
	     * @return
	     */
	    public List<AssetDetailDto> getUseAssetListByStation (Long stationId, Long taobaoUserId);
	    
	    /**
	     * 设置资产已盘点  admin使用
	     * @param checkDto
	     * @return
	     */
	    public Boolean checkAsset(AssetCheckDto checkDto);
	    
	    /**
	     * 村小二 签收资产    admin使用
	     * @param signDto
	     * @return
	     */
	    public AssetDetailDto signAssetByStation(AssetDto signDto);
}
