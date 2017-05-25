package com.taobao.cun.auge.asset.service;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetIncomeConverter;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xiao on 17/5/17.
 */
@Component
@HSFProvider(serviceInterface = AssetMobileService.class)
public class AssetMobileServiceImpl implements AssetMobileService{

    private static final Logger logger = LoggerFactory.getLogger(AssetMobileServiceImpl.class);

    @Autowired
    private AssetBO assetBO;
    
    @Autowired
    private AssetIncomeBO assetIncomeBO;

    @Autowired
    private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;

    @Autowired
    private DiamondConfiguredProperties configuredProperties;

    @Override
    public Map<String, String> getCategoryMap(AssetOperatorDto operatorDto) {
        return configuredProperties.getCategoryMap();
    }

    @Override
    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto) {
        try {
            return assetBO.getCategoryAssetList(operatorDto);
        } catch (Exception e) {
            logger.error("AssetMobileService getCategoryAssetList error " + JSON.toJSONString(operatorDto), e);
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto) {
        try {
            return assetBO.getAreaAssetList(operatorDto);
        } catch (Exception e) {
            logger.error("AssetMobileService getAreaAssetList error " + JSON.toJSONString(operatorDto), e);
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
        try {
            return assetBO.getCategoryAssetDetail(condition);
        } catch (Exception e) {
            logger.error("AssetMobileService getCategoryAssetDetail error " + JSON.toJSONString(condition), e);
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
        try {
            return assetBO.getAreaAssetDetail(condition);
        } catch (Exception e) {
            logger.error("AssetMobileService getAreaAssetDetail error " + JSON.toJSONString(condition), e);
            throw new AugeBusinessException("系统异常");
        }
    }

    @Override
    @Transactional
    public Boolean signAssetByCounty(AssetDto signDto) {
        try {
            return assetBO.signAssetByCounty(signDto);
        } catch (NullPointerException | AugeBusinessException e) {
            throw new AugeBusinessException(e.getMessage());
        } catch (Exception e) {
            logger.error("AssetMobileService signAsset error " + JSON.toJSONString(signDto), e);
            throw new AugeBusinessException("系统异常，签收失败");
        }
    }

    @Override
    @Transactional
    public Boolean recycleAsset(AssetDto recycleDto) {
        try {
            return assetBO.recycleAsset(recycleDto);
        } catch (NullPointerException | AugeBusinessException e) {
            throw new AugeBusinessException(e.getMessage());
        } catch (Exception e) {
            logger.error("AssetMobileService signAsset error " + JSON.toJSONString(recycleDto), e);
            throw new AugeBusinessException("系统异常，签收失败");
        }
    }

    @Override
    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator) {
        try {
            return assetBO.getTransferAssetList(operator);
        } catch (Exception e) {
            logger.error("AssetMobileService getTransferAssetList error " + JSON.toJSONString(operator), e);
            throw new AugeBusinessException("系统异常，获取列表失败");
        }
    }

    @Override
    public Boolean transferAssetSelfCounty(AssetTransferDto transferDto) {
        try {
            return assetBO.transferAssetSelfCounty(transferDto);
        } catch (NullPointerException | AugeBusinessException e) {
            throw new AugeBusinessException(e.getMessage());
        } catch (Exception e) {
            logger.error("AssetMobileService transferAssetSelfCounty error " + JSON.toJSONString(transferDto), e);
            throw new AugeBusinessException("系统异常，签收失败");
        }
    }

    @Override
    public Boolean judgeTransfer(AssetDto assetDto) {
        try {
            return assetBO.judgeTransfer(assetDto);
        } catch (NullPointerException | AugeBusinessException e) {
            throw new AugeBusinessException(e.getMessage());
        } catch (Exception e) {
            logger.error("AssetMobileService judgementTransfer error " + JSON.toJSONString(assetDto), e);
            throw new AugeBusinessException("系统异常，验证失败");
        }
    }

    @Override
	public PageDto<AssetIncomeDto> getIncomeLsit(
			AssetIncomeQueryCondition condition) {
		try {
			Page<AssetIncome> incomeList = assetIncomeBO.getIncomeList(condition);
			List<AssetIncomeDto> dtoList = new ArrayList<AssetIncomeDto>();
			for (AssetIncome ai : incomeList) {
				AssetIncomeDto aiDto = AssetIncomeConverter.toAssetIncomeDto(ai);
				aiDto.setCountList(assetRolloutIncomeDetailBO.queryCountByIncomeId(ai.getId(), null));
				aiDto.setWaitSignCountList(assetRolloutIncomeDetailBO.queryCountByIncomeId(ai.getId(),AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN));
				dtoList.add(aiDto);
			}
			return PageDtoUtil.success(incomeList, dtoList);
		} catch (Exception e) {
			logger.error("AssetMobileService getIncomeLsit error " + JSON.toJSONString(condition),e);
            throw new AugeBusinessException("系统异常，查询入库单失败");
		}
	}


}
