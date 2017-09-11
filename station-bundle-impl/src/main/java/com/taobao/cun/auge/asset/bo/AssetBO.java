package com.taobao.cun.auge.asset.bo;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetPurchaseDto;
import com.taobao.cun.auge.asset.dto.AssetQueryPageCondition;
import com.taobao.cun.auge.asset.dto.AssetScrapDto;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.AssetScrapListCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.Asset;

public interface AssetBO {

    public static final String NO_EXIT_ASSET = "，该资产不在系统中，请核对资产信息！如有疑问，请联系资产管理员。";
    public static final String NOT_OPERATOR = "，该资产不属于您，请核对资产信息！如有疑问，请联系资产管理员。";

    public void saveCuntaoAsset(CuntaoAssetDto cuntaoAssetDto, String operator);

    public CuntaoAssetDto getCuntaoAssetById(Long cuntaoAssetId);

    public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition);

    /**
     * 资产签收
     *
     * @return
     */
    public void signAsset(Long assetId, String operator);

    /**
     * 资产盘点
     *
     * @return
     */

    public void checkAsset(Long assetId, String operator, CuntaoAssetEnum checkRole);

    /**
     * 资产回收
     *
     * @param assetId
     */
    public void callbackAsset(Long assetId, String operator);

    public void updateAssetByMobile(CuntaoAssetDto cuntaoAssetDto);

    public void deleteAsset(Long assetId, String operator);

    public PageDto<CuntaoAssetDto> queryByPageMobile(AssetQueryCondition cuntaoAssetQueryCondition);

    public PageDto<String> getBoNoByOrgId(Long orgId, Integer pageNum, Integer pageSize);

    /**
     * 批量资产checking
     *
     * @param assetIds
     * @return
     */
    public void checkingAssetBatch(List<Long> assetIds, String operator);

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

    public void setAssetRecycleIsY(Long stationId, Long taobaoUserId);

    public void cancelAssetRecycleIsY(Long stationId, Long taobaoUserId);

    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator);

    public List<Asset> transferAssetSelfCounty(AssetTransferDto transferDto);

    public List<Asset> transferAssetOtherCounty(AssetTransferDto transferDto);

    public void agreeTransferAsset(AssetTransferDto transferDto);

    public void disagreeTransferAsset(AssetTransferDto transferDto);

    public AssetDetailDto judgeTransfer(AssetDto assetDto);

    public Asset getAssetById(Long assetId);

    public void cancelTransferAsset(List<Long> assetIds, String operator);

    public List<Asset> distributeAsset(AssetDistributeDto distributeDto);

    public AssetDetailDto buildAssetDetail(Asset asset);

    public PageDto<AssetDetailDto> getScrapAssetList(AssetScrapListCondition condition);

    public AssetDetailDto getScrapDetailById(Long id, AssetOperatorDto assetOperatorDto);

    public void scrapAsset(AssetScrapDto scrapDto);

    /**
     * 资产赔付成功
     *
     * @param scrapDto
     */
    public void scrapAssetByStation(AssetScrapDto scrapDto);

    /**
     * 设置资产已盘点
     *
     * @param checkDto
     * @return
     */
    public AssetDetailDto checkAsset(AssetCheckDto checkDto);

    /**
     * 设置资产待盘点
     *
     * @param assetId
     * @param operator
     * @return
     */
    public Boolean checkingAsset(Long assetId, String operator);

    /**
     * 获得已盘点资产
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Page<Asset> getCheckedAsset(Integer pageNum, Integer pageSize);

    /**
     * 检验退出逻辑
     *
     * @param stationId
     * @param taobaoUserId
     */
    public void validateAssetForQuiting(Long stationId, Long taobaoUserId);
    
    /**
     * 检验村点开业
     * 
     */
    public void validateAssetForOpenStation(Long instanceId);

    /**
     * 资产采购
     *
     * @return
     */
    public Long purchase(AssetPurchaseDto pList);

    /**
     * 新模型  列表查询
     *
     * @param query
     * @return
     */
    public PageDto<AssetDetailDto> queryByPage(AssetQueryPageCondition query);

    /**
     * 新模型 删除资产
     *
     * @param assetId
     * @param operator
     */
    public void delete(Long assetId, String operator);

    /**
     * 获得资产详情
     *
     * @param assetId
     * @return
     */
    public AssetDetailDto getDetail(Long assetId);

    /**
     * 获得分发中 村小二的资产列表
     *
     * @param stationId
     * @param taobaoUserId
     * @return
     */
    public List<AssetDetailDto> getDistributeAssetListByStation(Long stationId, Long taobaoUserId);

    /**
     * 获得村小二使用的资产
     *
     * @param stationId
     * @param taobaoUserId
     * @return
     */
    public List<AssetDetailDto> getUseAssetListByStation(Long stationId, Long taobaoUserId);


    public Map<String, String> getStationAssetState(Long stationId);

    public Map<String, String> buyAsset(CuntaoAssetDto assetDto);

    public Asset getAssetByAliNo(String aliNo);
    
    public AssetDetailDto judgeDistribute(AssetDto assetDto);

    public Boolean signAllAssetByCounty(AssetSignDto signDto);

    /**
     * 消费集团报废资产消息通知
     * @param assetDto
     */
    public void confirmScrapAsset(AssetDto assetDto);
    /**
     * 通知集团资产变更责任人
     * @param signDto
     */
    public void transferItAsset(AssetDto signDto);
}
