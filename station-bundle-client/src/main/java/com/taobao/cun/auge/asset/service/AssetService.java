package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;

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

}
