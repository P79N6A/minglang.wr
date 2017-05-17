package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetMobileServiceImpl implements AssetMobileService{

    @Autowired
    private AssetBO assetBO;

    @Override
    public List<CategoryAssetListDto> getCategoryAssetListByWorkNo(String workNo) {
        return assetBO.getCategoryAssetListByWorkNo(workNo);
    }

    @Override
    public List<AreaAssetListDto> getAreaAssetListByWorkNo(String workNo) {
        return null;
    }

    @Override
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
        return null;
    }

    @Override
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
        return null;
    }
}
