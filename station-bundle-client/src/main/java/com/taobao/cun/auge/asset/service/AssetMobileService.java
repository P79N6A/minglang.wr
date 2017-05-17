package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;

/**
 * Created by xiao on 17/5/17.
 */
public interface AssetMobileService {

    /**
     * 我的资产按类目聚合在一起
     * @param workNo
     * @return
     */
    public List<CategoryAssetListDto> getCategoryAssetListByWorkNo(String workNo);

    /**
     * 我的资产按区域聚合在一起
     * @param workNo
     * @return
     */
    public List<AreaAssetListDto> getAreaAssetListByWorkNo(String workNo);

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

}
