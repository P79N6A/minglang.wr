package com.taobao.cun.auge.asset.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import com.github.pagehelper.Page;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
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
import com.taobao.cun.auge.asset.dto.AssetScrapDto;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.ScrapFreeStatusEnum;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by xiao on 17/5/17.
 */
@Component
@HSFProvider(serviceInterface = AssetMobileService.class)
public class AssetMobileServiceImpl implements AssetMobileService {

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
            List<AssetMobileConditionDto> categoryList = configuredProperties.getCategoryMap().entrySet().stream().map(
                entry -> {
                    AssetMobileConditionDto dto = new AssetMobileConditionDto();
                    dto.setCode(entry.getKey());
                    dto.setName(entry.getValue());
                    return dto;
                }).collect(Collectors.toList());
            map.put("category", categoryList);
            List<AssetMobileConditionDto> statusList = new ArrayList<>();
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.DISTRIBUTE.getCode(),
                AssetStatusEnum.DISTRIBUTE.getDesc()));
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.USE.getCode(), AssetStatusEnum.USE.getDesc()));
            statusList.add(
                new AssetMobileConditionDto(AssetStatusEnum.TRANSFER.getCode(), AssetStatusEnum.TRANSFER.getDesc()));
            statusList.add(new AssetMobileConditionDto(AssetStatusEnum.PEND.getCode(), AssetStatusEnum.PEND.getDesc()));
            statusList.add(new AssetMobileConditionDto("CHECKING", "待盘点"));
            statusList.add(new AssetMobileConditionDto("Y", "待回收"));
            map.put("status", statusList);
            // 4 hour cache
            tairCache.put("assetCondtionMap", map, 4 * 60 * 60);
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
        //签收的时候需要进行签收的资产人比对且在签收成功时需要更新出库单状态
        assetIncomeBO.signAssetByCounty(signDto.getAliNo(), signDto.getOperator(),signDto.getOperatorOrgId());
        return assetBO.signAssetByCounty(signDto);
    }

    @Override
    @Transactional
    public Boolean signAllAssetByCounty(AssetSignDto signDto) {
       assetBO.signAllAssetByCounty(signDto);
        assetIncomeBO.signAllAssetByCounty(signDto);
        return true;
    }

    @Override
    public AssetDetailDto recycleAsset(AssetDto recycleDto) {
        return assetBO.recycleAsset(recycleDto);
    }

    @Override
    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator) {
        return assetBO.getTransferAssetList(operator);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public List<Long> transferAssetSelfCounty(AssetTransferDto transferDto) {
        List<Long> result = new ArrayList<>();
        //1.资产状态变更
        List<Asset> assetList = assetBO.transferAssetSelfCounty(transferDto);
        //2  生成出入库单
        List<Asset> countyUseList = assetList.stream().filter(
            i -> AssetUseAreaTypeEnum.COUNTY.getCode().equals(i.getUseAreaType())).collect(Collectors.toList());
        List<Asset> stationUseList = assetList.stream().filter(
            i -> AssetUseAreaTypeEnum.STATION.getCode().equals(i.getUseAreaType())).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(countyUseList)) {
            result.add(assetRolloutBO.transferAssetSelfCounty(transferDto, countyUseList, AssetIncomeSignTypeEnum.SCAN));
        }
        if (CollectionUtils.isNotEmpty(stationUseList)) {
            result.add(assetRolloutBO.transferAssetSelfCounty(transferDto, stationUseList, AssetIncomeSignTypeEnum.CONFIRM));
        }
        return result;

    }

    @Override
    @Transactional
    public List<Long> transferAssetOtherCounty(AssetTransferDto transferDto) {
        //1 资产状态变更
        List<Asset> assetList = assetBO.transferAssetOtherCounty(transferDto);
        //2. 生成出库单,根据出库单的主键来创建工作流
        Long rolloutId = assetRolloutBO.transferAssetOtherCounty(transferDto, assetList);
        //3 生成工作流 审批
        assetFlowService.createTransferFlow(rolloutId, transferDto.getOperator());
        return Collections.singletonList(rolloutId);
    }

    @Override
    public AssetDetailDto judgeTransfer(AssetDto assetDto) {
        return assetBO.judgeTransfer(assetDto);
    }

    @Override
    public PageDto<AssetIncomeDto> getIncomeList(AssetIncomeQueryCondition condition) {
        return assetIncomeBO.getIncomeList(condition);
    }

    @Override
    public PageDto<AssetRolloutDto> getRolloutList(AssetRolloutQueryCondition condition) {
        return assetRolloutBO.getRolloutList(condition);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Boolean cancelAssetRollout(AssetRolloutCancelDto cancelDto) {
        //1.撤销出库资产
        assetRolloutBO.cancelRolleoutAsset(cancelDto);
        //2.更新资产状态为使用中
        List<Long> assetIds = new ArrayList<Long>();
        assetIds.add(cancelDto.getAssetId());
        assetBO.cancelTransferAsset(assetIds, cancelDto.getOperator());
        return Boolean.TRUE;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Long distributeAsset(AssetDistributeDto distributeDto) {
    	throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "分发功能不可用!");
       /* //1.检验资产为县使用，操作人 和资产责任人一致 更新资产状态为分发中
        List<Asset> assetList = assetBO.distributeAsset(distributeDto);
        //2.创建出库单
        Long rolloutId = assetRolloutBO.distributeAsset(distributeDto, assetList);
        return rolloutId;*/
    }

    @Override
    public PageDto<AssetRolloutIncomeDetailExtDto> queryPageForIncomeDetail(AssetIncomeDetailQueryCondition con) {
        Objects.requireNonNull(con, "参数不能为空");
        Objects.requireNonNull(con.getIncomeId(), "入库单id不能为空");
        Long incomeId = con.getIncomeId();
        Page<AssetRolloutIncomeDetailExtDto> assetList = assetRolloutIncomeDetailBO.queryPageByIncomeId(incomeId,
            con.getStatusEnum(), con.getPageNum(), con.getPageSize());
        PageDto<AssetRolloutIncomeDetailExtDto> res = PageDtoUtil.success(assetList,
            bulidAssetDetailDtoList(assetList));
        return res;
    }

    private List<AssetRolloutIncomeDetailExtDto> bulidAssetDetailDtoList(
        List<AssetRolloutIncomeDetailExtDto> assetList) {
        List<AssetRolloutIncomeDetailExtDto> res = new ArrayList<AssetRolloutIncomeDetailExtDto>();
        if (CollectionUtils.isNotEmpty(assetList)) {
            for (AssetRolloutIncomeDetailExtDto a : assetList) {
                a.setCategoryName(configuredProperties.getCategoryMap().get(a.getCategory()));
                res.add(a);
                
            }
        }
        return res;
    }

    @Override
    public AssetIncomeDetailDto getIncomeDetailDto(Long incomeId) {
        Objects.requireNonNull(incomeId, "入库单id不能为空");
        AssetIncomeDetailDto deDto = new AssetIncomeDetailDto();
        AssetIncomeDto iDto = assetIncomeBO.getIncomeDtoById(incomeId);
        deDto.setAssetIncomeDto(iDto);
        if (assetRolloutIncomeDetailBO.hasCancelAssetByIncomeId(incomeId)) {
            deDto.setHasCanceldata(Boolean.TRUE);
        }
        return deDto;
    }

    @Override
    public PageDto<AssetRolloutIncomeDetailExtDto> queryPageForRolloutDetail(
        AssetRolloutDetailQueryCondition con) {
        Objects.requireNonNull(con, "参数不能为空");
        Objects.requireNonNull(con.getRolloutId(), "出库单id不能为空");
        Long rolloutId = con.getRolloutId();
        Page<AssetRolloutIncomeDetailExtDto> assetList = assetRolloutIncomeDetailBO.queryPageByRolloutId(rolloutId,
            con.getStatusEnum(), con.getPageNum(), con.getPageSize());
        PageDto<AssetRolloutIncomeDetailExtDto> res = PageDtoUtil.success(assetList,
            bulidAssetDetailDtoList(assetList));
        return res;
    }

    @Override
    public AssetRolloutDetailDto getRolloutDetailDto(Long rolloutId) {
        Objects.requireNonNull(rolloutId, "出库单id不能为空");
        AssetRolloutDetailDto deDto = new AssetRolloutDetailDto();
        AssetRolloutDto iDto = assetRolloutBO.getRolloutDtoById(rolloutId);
        deDto.setAssetRolloutDto(iDto);
        if (assetRolloutIncomeDetailBO.hasCancelAssetByRolloutId(rolloutId)) {
            deDto.setHasCanceldata(Boolean.TRUE);
        }
        return deDto;
    }

    @Override
    public PageDto<AssetDetailDto> getScrapAssetList(AssetScrapListCondition condition) {
        return assetBO.getScrapAssetList(condition);
    }

    @Override
    public AssetDetailDto getScrapDetailById(Long id, AssetOperatorDto assetOperatorDto) {
        return assetBO.getScrapDetailById(id, assetOperatorDto);
    }

    @Override
    @Transactional
    public Boolean scrapAsset(AssetScrapDto scrapDto) {
        if (ScrapFreeStatusEnum.N.getCode().equals(scrapDto.getFree()) && AssetUseAreaTypeEnum.STATION.getCode().equals(
            scrapDto.getScrapAreaType())) {
            //村点的直接扣除保证金
            assetBO.scrapAssetByStation(scrapDto);
        } else {
            assetBO.scrapAsset(scrapDto);
        }
        assetRolloutBO.scrapAsset(scrapDto);
        return true;
    }

    @Override
    public AssetDetailDto checkAsset(AssetCheckDto checkDto) {
        return assetBO.checkAsset(checkDto);
    }

	@Override
	public AssetDetailDto judgeDistribute(AssetDto assetDto) {
		throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "分发功能不可用!");
		// return assetBO.judgeDistribute(assetDto);
	}
}
