package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.Asset;

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

    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto);

    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto);

    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition);

    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition);

    public AssetDetailDto signAssetByCounty(AssetDto signDto);

    public Boolean signAssetByStation(AssetDto signDto);

    public AssetDetailDto recycleAsset(AssetDto signDto);

    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator);

    public List<Asset> transferAssetSelfCounty(AssetTransferDto transferDto);

    public List<Asset> transferAssetOtherCounty(AssetTransferDto transferDto);

    public AssetDetailDto judgeTransfer(AssetDto assetDto);

    public  Asset getAssetById(Long assetId);
    
    public  void cancelAsset(List<Long> assetIds,String operator);
    
    public List<Asset> distributeAsset(AssetDistributeDto distributeDto);
    
    public AssetDetailDto buildAssetDetail(Asset asset);
}
