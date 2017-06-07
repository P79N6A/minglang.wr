package com.taobao.cun.auge.asset.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetMobileConditionDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutCancelDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDetailDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailExtDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * Created by xiao on 17/5/17.
 */
public interface AssetMobileService {

    /**
     * 获得资产筛选条件
     * @param operatorDto
     * @return
     */
    public Map<String, List<AssetMobileConditionDto>> getConditionMap(AssetOperatorDto operatorDto);

    /**
     * 我的资产列表(按类目聚合)查询
     * @param operatorDto
     * @return
     */
    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto);

    /**
     * 我的资产列表(按使用区域聚合)查询
     * @param operatorDto
     * @return
     */
    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto);

    /**
     * 通过我的资产类目聚合列表点击进入的详情页
     * @param condition
     * @return
     */
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition);

    /**
     * 通过我的资产区域聚合列表点击进入的详情页
     * @param condition
     * @return
     */
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition);

    /**
     * 入库单列表查询
     * @param condition
     * @return
     */
    public PageDto<AssetIncomeDto> getIncomeList(AssetIncomeQueryCondition condition); 
    
    /**
     * 出库单列表查询
     * @param condition
     * @return
     */
    public PageDto<AssetRolloutDto> getRolloutList(AssetRolloutQueryCondition condition); 
    /**
     * 资产签收(操作人县小二)
     * @param signDto
     * @return
     */
    public AssetDetailDto signAssetByCounty(AssetDto signDto);
    /**
     * 资产回收
     * @param recycleDto
     * @return
     */
    public AssetDetailDto recycleAsset(AssetDto recycleDto);

    /**
     * 资产转移时获得资产列表
     * @param operator
     * @return
     */
    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator);

    /**
     * 转移资产给本县
     * @return
     */
    public Boolean transferAssetSelfCounty(AssetTransferDto transferDto);

    /**
     * 转移资产给他县
     * @return
     */
    public Boolean transferAssetOtherCounty(AssetTransferDto transferDto);

    /**
     * 判断资产是否能转移,不能的话会抛出异常
     * @param assetDto
     */
    public AssetDetailDto judgeTransfer(AssetDto assetDto);
    
    /**
     * 资产出库撤销（针对转移，分发的 单个资产）
     * @param cancelDto
     * @return
     */
    public Boolean cancelAssetRollout(AssetRolloutCancelDto cancelDto);
    
    /**
     * 资产分发
     * @param distributeDto
     * @return
     */
    public Boolean distributeAsset(AssetDistributeDto distributeDto);
    
    /**
     * 获得入库单详情列表    查询  待入库，已入库，已撤销的资产时，使用
     * @param incomeId
     * @return
     */
    public PageDto<AssetRolloutIncomeDetailExtDto> queryPageForIncomeDetail(AssetIncomeDetailQueryCondition con);
    
    /**
     * 查询入库单详情 基础信息
     * @param incomeId
     * @return
     */
    public AssetIncomeDetailDto getIncomeDetailDto(Long incomeId);
    
    
    /**
     * 获得出库单详情列表    查询  待对方入库，对方部分入库，对方全部入库，已撤回的资产时，使用
     * @param incomeId
     * @return
     */
    public PageDto<AssetRolloutIncomeDetailExtDto> queryPageForRolloutDetail(AssetRolloutDetailQueryCondition con);
    
    /**
     * 查询出库单详情 基础信息
     * @param incomeId
     * @return
     */
    public AssetRolloutDetailDto getRolloutDetailDto(Long rolloutId);
    
    /**
     * 资产盘点
     * @param checkDto
     * @return
     */
    public Boolean checkAsset(AssetCheckDto checkDto);

}
