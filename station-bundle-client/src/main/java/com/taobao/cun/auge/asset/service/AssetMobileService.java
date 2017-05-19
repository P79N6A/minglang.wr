package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.common.PageDto;

/**
 * Created by xiao on 17/5/17.
 */
public interface AssetMobileService {

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
     * 资产签收(操作人村小二)
     * @param signDto
     * @return
     */
    public Boolean signAssetByStation(AssetDto signDto);

    /**
     * 资产回收
     * @param recycleDto
     * @return
     */
    public Boolean recycleAsset(AssetDto recycleDto);

}
