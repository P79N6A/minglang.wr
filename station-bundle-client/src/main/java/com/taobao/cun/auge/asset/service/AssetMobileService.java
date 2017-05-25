package com.taobao.cun.auge.asset.service;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * Created by xiao on 17/5/17.
 */
public interface AssetMobileService {


    public Map<String, String> getCategoryMap(AssetOperatorDto operatorDto);

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
    public PageDto<AssetIncomeDto> getIncomeLsit(AssetIncomeQueryCondition condition); 
    /**
     * 资产签收(操作人县小二)
     * @param signDto
     * @return
     */
    public Boolean signAssetByCounty(AssetDto signDto);
    /**
     * 资产回收
     * @param recycleDto
     * @return
     */
    public Boolean recycleAsset(AssetDto recycleDto);

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
     * 判断资产是否能转移,不能的话会抛出异常
     * @param assetDto
     */
    public Boolean judgeTransfer(AssetDto assetDto);

}
