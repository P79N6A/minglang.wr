package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;

public interface AssetBO {


    public void saveCuntaoAsset(CuntaoAssetDto cuntaoAssetDto,String operator);
   
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
    
    public void updateAssetByMobile(CuntaoAssetDto cuntaoAssetDto);
    
    public void deleteAsset(Long assetId,String operator);
    
    public PageDto<CuntaoAssetDto> queryByPageMobile(AssetQueryCondition cuntaoAssetQueryCondition);
    
    public PageDto<String> getBoNoByOrgId(Long orgId,Integer pageNum,Integer pageSize);

    /**
     * 批量资产checking
     * @param assetIds
     * @return
     */
    public void checkingAssetBatch(List<Long> assetIds,String operator);
    
    public CuntaoAssetDto queryAssetByUserAndCategory(Long userid);
    
    public CuntaoAssetDto queryAssetBySerialNo(String serialNo);
    
    public CuntaoAssetDto queryAssetByAliNoOrSerialNo(String serialNoOrAliNo);
}
