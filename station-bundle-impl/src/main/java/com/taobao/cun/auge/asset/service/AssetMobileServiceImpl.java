package com.taobao.cun.auge.asset.service;

import java.util.List;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xiao on 17/5/17.
 */
@Component
@HSFProvider(serviceInterface = AssetMobileService.class)
public class AssetMobileServiceImpl implements AssetMobileService{

    private static final Logger logger = LoggerFactory.getLogger(AssetMobileServiceImpl.class);

    @Autowired
    private AssetBO assetBO;

    @Override
    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto) {
        try {
            return assetBO.getCategoryAssetList(operatorDto);
        } catch (Exception e) {
            logger.error("AssetMobileService getCategoryAssetList error " + JSON.toJSONString(operatorDto));
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto) {
        try {
            return assetBO.getAreaAssetList(operatorDto);
        } catch (Exception e) {
            logger.error("AssetMobileService getAreaAssetList error " + JSON.toJSONString(operatorDto));
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
        try {
            return assetBO.getCategoryAssetDetail(condition);
        } catch (Exception e) {
            logger.error("AssetMobileService getCategoryAssetDetail error " + JSON.toJSONString(condition));
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
        try {
            return assetBO.getAreaAssetDetail(condition);
        } catch (Exception e) {
            logger.error("AssetMobileService getAreaAssetDetail error " + JSON.toJSONString(condition));
            throw new AugeBusinessException("系统异常");
        }
    }
}
