package com.taobao.cun.auge.asset.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.convert.AssetIncomeConverter;
import com.taobao.cun.auge.asset.convert.AssetRolloutConverter;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetMobileConditionDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.dto.AssetRolloutQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.AssetIncome;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xiao on 17/5/17.
 */
@Component
@HSFProvider(serviceInterface = AssetMobileService.class)
public class AssetMobileServiceImpl implements AssetMobileService{

    @Autowired
    private AssetBO assetBO;
    
    @Autowired
    private AssetIncomeBO assetIncomeBO;
    @Autowired
    private AssetRolloutBO assetRolloutBO;

    @Autowired
    private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;

    @Autowired
    private DiamondConfiguredProperties configuredProperties;

    @Autowired
    private AssetFlowService assetFlowService;

    @Autowired
    private TairCache tairCache;

    @Override
    public Map<String, List<AssetMobileConditionDto>> getConditionMap(AssetOperatorDto operatorDto) {
        if (tairCache.get("assetCondtionMap") == null) {
            HashMap<String, List<AssetMobileConditionDto>> map = new HashMap<>();
            List<AssetMobileConditionDto> categoryList = configuredProperties.getCategoryMap().entrySet().stream().map(entry -> {
                AssetMobileConditionDto dto = new AssetMobileConditionDto();
                dto.setCode(entry.getKey());
                dto.setName(entry.getValue());
                return dto;
            }).collect(Collectors.toList());
            map.put("category", categoryList);
            List<AssetMobileConditionDto> statusList = new ArrayList<>();
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.DISTRIBUTE.getCode(), AssetStatusEnum.DISTRIBUTE.getDesc()));
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.USE.getCode(), AssetStatusEnum.USE.getDesc()));
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.TRANSFER.getCode(), AssetStatusEnum.TRANSFER.getDesc()));
            statusList.add(new AssetMobileConditionDto("UNCHECKED", "待盘点"));
            statusList.add(new AssetMobileConditionDto("Y", "待回收"));
            map.put("status", statusList);
            // 4 hour cache
            tairCache.put("assetCondtionMap", map, 4*60*60);
        }
        return tairCache.get("assetCondtionMap");
    }

    @Override
    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto) {
        return assetBO.getCategoryAssetList(operatorDto);
    }

    @Override
    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto) {
        return assetBO.getAreaAssetList(operatorDto);
    }

    @Override
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
        return assetBO.getCategoryAssetDetail(condition);
    }

    @Override
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
        return assetBO.getAreaAssetDetail(condition);
    }

    @Override
    @Transactional
    public AssetDetailDto signAssetByCounty(AssetDto signDto) {
        return assetBO.signAssetByCounty(signDto);
    }

    @Override
    @Transactional
    public AssetDetailDto recycleAsset(AssetDto recycleDto) {
        return assetBO.recycleAsset(recycleDto);
    }

    @Override
    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator) {
        return assetBO.getTransferAssetList(operator);
    }

    @Override
    public Boolean transferAssetSelfCounty(AssetTransferDto transferDto) {
        return assetBO.transferAssetSelfCounty(transferDto);
    }

    @Override
    public Boolean transferAssetOtherCounty(AssetTransferDto transferDto) {
        assetBO.transferAssetOtherCounty(transferDto);
        assetFlowService.createTransferFlow(1L, transferDto.getOperator());
        return Boolean.TRUE;
    }

    @Override
    public AssetDetailDto judgeTransfer(AssetDto assetDto) {
        return assetBO.judgeTransfer(assetDto);
    }

    @Override
	public PageDto<AssetIncomeDto> getIncomeList(
			AssetIncomeQueryCondition condition) {
        Page<AssetIncome> incomeList = assetIncomeBO.getIncomeList(condition);
        List<AssetIncomeDto> dtoList = new ArrayList<AssetIncomeDto>();
        for (AssetIncome ai : incomeList) {
            AssetIncomeDto aiDto = AssetIncomeConverter.toAssetIncomeDto(ai);
            aiDto.setCountList(assetRolloutIncomeDetailBO.queryCountByIncomeId(ai.getId(), null));
            aiDto.setWaitSignCountList(assetRolloutIncomeDetailBO.queryCountByIncomeId(ai.getId(),AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN));
            dtoList.add(aiDto);
        }
        return PageDtoUtil.success(incomeList, dtoList);
	}

	@Override
	public PageDto<AssetRolloutDto> getRolloutList(
			AssetRolloutQueryCondition condition) {
		 Page<AssetRollout> rolloutList = assetRolloutBO.getRolloutList(condition);
	        List<AssetRolloutDto> dtoList = new ArrayList<AssetRolloutDto>();
	        for (AssetRollout ai : rolloutList) {
	        	AssetRolloutDto aiDto = AssetRolloutConverter.toAssetRolloutDto(ai);
	            aiDto.setCountList(assetRolloutIncomeDetailBO.queryCountByRolloutId(ai.getId(), null));
	            aiDto.setWaitSignCountList(assetRolloutIncomeDetailBO.queryCountByRolloutId(ai.getId(),AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN));
	            dtoList.add(aiDto);
	        }
	        return PageDtoUtil.success(rolloutList, dtoList);
	}

}
