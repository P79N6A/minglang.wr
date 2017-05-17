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

    public List<CategoryAssetListDto> getCategoryAssetListByWorkNo(String workNo);

    public List<AreaAssetListDto> getAreaAssetListByWorkNo(String workNo);

    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition);

    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition);

}
