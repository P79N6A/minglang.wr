package com.taobao.cun.auge.asset.bo.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.stream.Collectors;

import com.taobao.cun.auge.asset.enums.AssetScrapReasonEnum;
import com.taobao.hsf.app.spring.util.annotation.HSFConsumer;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.it.asset.api.Attachment;
import com.alibaba.it.asset.api.CuntaoApiService;
import com.alibaba.it.asset.api.dto.AssetApiResultDO;
import com.alibaba.it.asset.api.dto.AssetLostQueryResult;
import com.alibaba.it.asset.api.dto.AssetLostRequestDto;
import com.alibaba.it.asset.api.dto.AssetTransDto;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetIncomeBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutBO;
import com.taobao.cun.auge.asset.bo.AssetRolloutIncomeDetailBO;
import com.taobao.cun.auge.asset.dto.AreaAssetDetailDto;
import com.taobao.cun.auge.asset.dto.AreaAssetListDto;
import com.taobao.cun.auge.asset.dto.AssetCategoryCountDto;
import com.taobao.cun.auge.asset.dto.AssetCheckDto;
import com.taobao.cun.auge.asset.dto.AssetDetailDto;
import com.taobao.cun.auge.asset.dto.AssetDetailQueryCondition;
import com.taobao.cun.auge.asset.dto.AssetDistributeDto;
import com.taobao.cun.auge.asset.dto.AssetDto;
import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.dto.AssetOperatorDto;
import com.taobao.cun.auge.asset.dto.AssetPurchaseDetailDto;
import com.taobao.cun.auge.asset.dto.AssetPurchaseDto;
import com.taobao.cun.auge.asset.dto.AssetQueryPageCondition;
import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.dto.AssetScrapDto;
import com.taobao.cun.auge.asset.dto.AssetSignDto;
import com.taobao.cun.auge.asset.dto.AssetSignEvent;
import com.taobao.cun.auge.asset.dto.AssetSignEvent.Content;
import com.taobao.cun.auge.asset.dto.AssetTransferDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetDetailDto;
import com.taobao.cun.auge.asset.dto.CategoryAssetListDto;
import com.taobao.cun.auge.asset.enums.AssetCheckStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeSignTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.RecycleStatusEnum;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.AssetScrapListCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetExample;
import com.taobao.cun.auge.dal.domain.AssetExtExample;
import com.taobao.cun.auge.dal.domain.AssetRollout;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;
import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExtExample;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.AssetExtMapper;
import com.taobao.cun.auge.dal.mapper.AssetMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetExtMapper;
import com.taobao.cun.auge.dal.mapper.CuntaoAssetMapper;
import com.taobao.cun.auge.event.AssetChangeEvent;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.flowRecord.condition.CuntaoFlowRecordCondition;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.crius.event.ExtEvent;
import com.taobao.cun.settle.bail.dto.CuntaoTransferBailDto;
import com.taobao.cun.settle.bail.enums.BailOperateTypeEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.ResultModel;

@Component
public class AssetBOImpl implements AssetBO {

    private static final Logger logger = LoggerFactory.getLogger(AssetBOImpl.class);

    private final Long inAccountUserId = 2631673100L;

    private static final String ASSET_SIGN = "assetSign";

    private static final String ASSET_CHECK = "assetCheck";

    private static final String ASEET_CATEGORY_YUNOS = "云OS";

    private static final String GROUP_CODE = "36821";

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private CuntaoAssetMapper cuntaoAssetMapper;

    @Autowired
    private CuntaoAssetExtMapper cuntaoAssetExtMapper;

    @Autowired
    private PartnerInstanceQueryService partnerInstanceQueryService;
    
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;
    
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Autowired
    private CuntaoNewBailService newBailService;

    @HSFConsumer(serviceGroup = "${it.service.group}", serviceVersion = "${it.service.version}")
    private CuntaoApiService cuntaoApiService;

    @Autowired
    private Emp360Adapter emp360Adapter;

    @Autowired
    private UicReadAdapter uicReadAdapter;

    @Autowired
    private AssetRolloutBO assetRolloutBO;

    @Autowired
    private AssetIncomeBO assetIncomeBO;

    @Autowired
    private AssetRolloutIncomeDetailBO assetRolloutIncomeDetailBO;

    @Autowired
    private AssetExtMapper assetExtMapper;

    @Autowired
    private CuntaoFlowRecordBO cuntaoFlowRecordBO;



    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void saveCuntaoAsset(CuntaoAssetDto cuntaoAssetDto, String operator) {
    	throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "服务不可用");
    }

    private AssetChangeEvent buildAssetChangeEvent(Long assetId, String type, String operator, OperatorTypeEnum opType,String desc) {
        AssetChangeEvent event = new AssetChangeEvent();
        event.setAssetId(assetId);
        event.setOperateTime(new Date());
        event.setDescription(desc);
        event.setType(type);
        event.setOperatorId(operator);
        if (OperatorTypeEnum.BUC.getCode().equals(opType.getCode())){//"cuntaobops".equals(RequestCtxUtil.getAppNameOfClient())) {
            String operatorName = emp360Adapter.getName(operator);
            event.setOperator(operatorName);
        } else if (OperatorTypeEnum.HAVANA.getCode().equals(opType.getCode())){//"cuntaoadmin".equals(RequestCtxUtil.getAppNameOfClient())) {
            String taobaoNick = uicReadAdapter.getTaobaoNickByTaobaoUserId(Long.parseLong(operator));
            event.setOperator(taobaoNick);
        } else {
            event.setOperator(operator);
        }
        return event;
    }

    @Override
    public CuntaoAssetDto getCuntaoAssetById(Long cuntaoAssetId) {
        Assert.notNull(cuntaoAssetId, "cuntaoAssetId can not be null");
        return convert2CuntaoAssetDto(cuntaoAssetMapper.selectByPrimaryKey(cuntaoAssetId));
    }

    @Override
    public PageDto<CuntaoAssetDto> queryByPage(AssetQueryCondition cuntaoAssetQueryCondition) {

        PageHelper.startPage(cuntaoAssetQueryCondition.getPageNum(), cuntaoAssetQueryCondition.getPageSize());
        CuntaoAssetExtExample example = new CuntaoAssetExtExample();
        Criteria cri = example.createCriteria();
        if (CollectionUtils.isNotEmpty(cuntaoAssetQueryCondition.getStates())) {
            cri.andStatusIn(cuntaoAssetQueryCondition.getStates());
        }
        if (CollectionUtils.isNotEmpty(cuntaoAssetQueryCondition.getNoStates())) {
            cri.andStatusNotIn(cuntaoAssetQueryCondition.getStates());
        }
        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getRole())) {
            cri.andOperatorRoleEqualTo(cuntaoAssetQueryCondition.getRole());
        }
        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getAliNo())) {
            cri.andAliNoEqualTo(cuntaoAssetQueryCondition.getAliNo());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getBoNo())) {
            cri.andBoNoEqualTo(cuntaoAssetQueryCondition.getBoNo());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getStatus())) {
            cri.andStatusEqualTo(cuntaoAssetQueryCondition.getStatus());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getProvince())) {
            cri.andProvinceEqualTo(cuntaoAssetQueryCondition.getProvince());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getCounty())) {
            cri.andCountyEqualTo(cuntaoAssetQueryCondition.getCounty());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getSerialNo())) {
            cri.andSerialNoEqualTo(cuntaoAssetQueryCondition.getSerialNo());
        }

        if (StringUtils.isNotEmpty(cuntaoAssetQueryCondition.getCheckStatus())) {
            cri.andCheckStatusEqualTo(cuntaoAssetQueryCondition.getCheckStatus());
        }

        if (Objects.nonNull(cuntaoAssetQueryCondition.getStationId())) {
            cri.andNewStationIdEqualTo(cuntaoAssetQueryCondition.getStationId());
        }

        if (Objects.nonNull(cuntaoAssetQueryCondition.getPartnerInstanceId())) {
            cri.andPartnerInstanceIdEqualTo(cuntaoAssetQueryCondition.getPartnerInstanceId());
        }

        if (cuntaoAssetQueryCondition.getOrgId() != null) {
            CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(cuntaoAssetQueryCondition.getOrgId());
            if (cuntaoOrgDto != null) {
                example.setFullIdPath(cuntaoOrgDto.getFullIdPath());
            }
        }
        example.setOrderByClause("a.id desc");
        Page<CuntaoAsset> page = (Page<CuntaoAsset>)cuntaoAssetExtMapper.selectByExample(example);
        List<CuntaoAssetDto> targetList = page.getResult().stream().map(source -> convert2CuntaoAssetDto(source))
            .collect(Collectors.toList());
        PageDto<CuntaoAssetDto> result = PageDtoUtil.success(page, targetList);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void signAsset(Long assetId, String operator) {
    	throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "服务不可用");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void checkAsset(Long assetId, String operator, CuntaoAssetEnum checkRole) {
    	throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "服务不可用");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void callbackAsset(Long assetId, String operator) {
    	throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "服务不可用");
    }

    @Override
    //TODO
    public void updateAssetByMobile(CuntaoAssetDto cuntaoAssetDto) {
        // TODO Auto-generated method stub
        //cuntaoAssetMapper.updateByPrimaryKey(asset);
    }

    @Override
    public void deleteAsset(Long assetId, String operator) {
        CuntaoAsset record = new CuntaoAsset();
        record.setId(assetId);
        record.setIsDeleted("y");
        record.setModifier(operator);
        record.setGmtModified(new Date());
        cuntaoAssetMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    @Deprecated
    public PageDto<CuntaoAssetDto> queryByPageMobile(AssetQueryCondition cuntaoAssetQueryCondition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PageDto<String> getBoNoByOrgId(Long orgId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        CuntaoAssetExtExample example = new CuntaoAssetExtExample();
        Criteria cri = example.createCriteria();
        cri.andStatusNotEqualTo("UNMATCH");
        if (orgId != null) {
            CuntaoOrgDto cuntaoOrgDto = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
            if (cuntaoOrgDto != null) {
                example.setFullIdPath(cuntaoOrgDto.getFullIdPath());
            }
        }
        cri.andIsDeletedEqualTo("n");
        Page<String> page = (Page<String>)cuntaoAssetExtMapper.selectBoNoByExample(example);
        return PageDtoUtil.success(page, page.getResult());
    }

    @Override
    public void checkingAssetBatch(List<Long> assetIds, String operator) {
        Assert.notNull(assetIds);
        Assert.notNull(operator);
        Asset record = new Asset();
        DomainUtils.beforeUpdate(record, operator);
        record.setCheckStatus(AssetCheckStatusEnum.CHECKING.getCode());

        AssetExample assetExample = new AssetExample();
        AssetExample.Criteria criteria = assetExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andIdIn(assetIds);
        assetMapper.updateByExampleSelective(record, assetExample);
    }

    @Override
    public CuntaoAssetDto queryAssetByUserAndCategory(Long userid) {
        Long stationId = partnerInstanceQueryService.getCurStationIdByTaobaoUserId(userid);
        CuntaoAssetExample example = new CuntaoAssetExample();
        example.createCriteria().andNewStationIdEqualTo(stationId).andCategoryEqualTo(ASEET_CATEGORY_YUNOS);
        List<CuntaoAsset> assets = cuntaoAssetMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(assets)) {
            return this.convert2CuntaoAssetDto(assets.iterator().next());
        }
        return null;
    }

    @Override
    public CuntaoAssetDto queryAssetBySerialNo(String serialNo) {
        CuntaoAssetExample example = new CuntaoAssetExample();
        example.createCriteria().andSerialNoEqualTo(serialNo).andCategoryEqualTo(ASEET_CATEGORY_YUNOS);
        List<CuntaoAsset> assets = cuntaoAssetMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(assets)) {
            return this.convert2CuntaoAssetDto(assets.iterator().next());
        }
        return null;
    }

    private CuntaoAsset convert2CuntaoAsset(CuntaoAssetDto cuntaoAssetDto) {
        CuntaoAsset cuntaoAsset = new CuntaoAsset();
        cuntaoAsset.setId(cuntaoAssetDto.getId());
        cuntaoAsset.setModifier("system");
        cuntaoAsset.setGmtModified(new Date());
        cuntaoAsset.setIsDeleted("n");
        cuntaoAsset.setCreator("system");
        cuntaoAsset.setGmtCreate(new Date());
        cuntaoAsset.setAliNo(cuntaoAssetDto.getAliNo());
        cuntaoAsset.setSerialNo(cuntaoAssetDto.getSerialNo());
        cuntaoAsset.setBrand(cuntaoAssetDto.getBrand());
        cuntaoAsset.setModel(cuntaoAssetDto.getModel());
        cuntaoAsset.setCategory(cuntaoAssetDto.getCategory());
        cuntaoAsset.setStatus(cuntaoAssetDto.getStatus());
        cuntaoAsset.setReceiver(cuntaoAssetDto.getReceiver());
        cuntaoAsset.setOperator(cuntaoAssetDto.getOperator());
        cuntaoAsset.setOperateTime(cuntaoAssetDto.getOperateTime());
        cuntaoAsset.setCounty(cuntaoAssetDto.getCounty());
        cuntaoAsset.setOrgId(cuntaoAssetDto.getOrgId());
        cuntaoAsset.setProvince(cuntaoAssetDto.getProvince());
        cuntaoAsset.setRemark(cuntaoAssetDto.getRemark());
        cuntaoAsset.setBoNo(cuntaoAssetDto.getBoNo());
        cuntaoAsset.setAssetOwner(cuntaoAssetDto.getAssetOwner());
        cuntaoAsset.setStationId(cuntaoAssetDto.getStationId());
        cuntaoAsset.setStationName(cuntaoAssetDto.getStationName());
        cuntaoAsset.setCheckStatus(cuntaoAssetDto.getCheckStatus());
        cuntaoAsset.setCheckTime(cuntaoAssetDto.getCheckTime());
        cuntaoAsset.setOperatorRole(cuntaoAssetDto.getOperatorRole());
        cuntaoAsset.setCheckOperator(cuntaoAssetDto.getCheckOperator());
        cuntaoAsset.setCheckRole(cuntaoAssetDto.getCheckRole());
        cuntaoAsset.setNewStationId(cuntaoAssetDto.getNewStationId());
        cuntaoAsset.setPartnerInstanceId(cuntaoAssetDto.getPartnerInstanceId());
        return cuntaoAsset;
    }

    private CuntaoAssetDto convert2CuntaoAssetDto(CuntaoAsset cuntaoAsset) {
        CuntaoAssetDto cuntaoAssetDto = new CuntaoAssetDto();
        if (cuntaoAsset != null) {
            cuntaoAssetDto.setId(cuntaoAsset.getId());
            cuntaoAssetDto.setAliNo(cuntaoAsset.getAliNo());
            cuntaoAssetDto.setSerialNo(cuntaoAsset.getSerialNo());
            cuntaoAssetDto.setBrand(cuntaoAsset.getBrand());
            cuntaoAssetDto.setModel(cuntaoAsset.getModel());
            cuntaoAssetDto.setCategory(cuntaoAsset.getCategory());
            cuntaoAssetDto.setStatus(cuntaoAsset.getStatus());
            cuntaoAssetDto.setReceiver(cuntaoAsset.getReceiver());
            cuntaoAssetDto.setOperator(cuntaoAsset.getOperator());
            cuntaoAssetDto.setOperateTime(cuntaoAsset.getOperateTime());
            cuntaoAssetDto.setCounty(cuntaoAsset.getCounty());
            cuntaoAssetDto.setOrgId(cuntaoAsset.getOrgId());
            cuntaoAssetDto.setProvince(cuntaoAsset.getProvince());
            cuntaoAssetDto.setRemark(cuntaoAsset.getRemark());
            cuntaoAssetDto.setBoNo(cuntaoAsset.getBoNo());
            cuntaoAssetDto.setAssetOwner(cuntaoAsset.getAssetOwner());
            cuntaoAssetDto.setStationId(cuntaoAsset.getStationId());
            cuntaoAssetDto.setStationName(cuntaoAsset.getStationName());
            cuntaoAssetDto.setCheckStatus(cuntaoAsset.getCheckStatus());
            cuntaoAssetDto.setCheckTime(cuntaoAsset.getCheckTime());
            cuntaoAssetDto.setOperatorRole(cuntaoAsset.getOperatorRole());
            cuntaoAssetDto.setCheckOperator(cuntaoAsset.getCheckOperator());
            cuntaoAssetDto.setCheckRole(cuntaoAsset.getCheckRole());
        }
        return cuntaoAssetDto;
    }

    @Override
    public CuntaoAssetDto queryAssetByAliNoOrSerialNo(String serialNoOrAliNo) {
        CuntaoAssetExample example = new CuntaoAssetExample();
        List<CuntaoAsset> assets = cuntaoAssetMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(assets)) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "can not find biz by serialNoOrAliNo[" + serialNoOrAliNo + "]");
        }
        return convert2CuntaoAssetDto(assets.iterator().next());
    }

    @Override
    public List<CategoryAssetListDto> getCategoryAssetList(AssetOperatorDto operatorDto) {
        Objects.requireNonNull(operatorDto.getOperator(), "工号不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operatorDto.getOperator())
            .andStatusIn(AssetStatusEnum.getValidStatusList());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        Map<String, List<Asset>> listMap = new HashMap<>();
        for (Asset asset : assetList) {
            listMap.computeIfAbsent(asset.getCategory(), k -> new ArrayList<>()).add(asset);
        }
        List<CategoryAssetListDto> categoryAssetList = new ArrayList<>();
        for (Entry<String, List<Asset>> entry : listMap.entrySet()) {
            CategoryAssetListDto listDto = new CategoryAssetListDto();
            List<Asset> list = entry.getValue();
            Asset asset = list.get(0);
            listDto.setCategory(entry.getKey());
            listDto.setCategoryName(diamondConfiguredProperties.getCategoryMap().get(entry.getKey()));
            listDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
            listDto.setOwner(asset.getOwnerName());
            listDto.setTotal(String.valueOf(list.size()));
            listDto.setPutAway(String.valueOf(
                list.stream().filter(
                    i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER
                        .getCode().equals(i.getStatus())).count()));
            categoryAssetList.add(listDto);
        }
        categoryAssetList.sort(Comparator.comparing(CategoryAssetListDto::getCategory));
        return categoryAssetList;
    }

    @Override
    public List<AreaAssetListDto> getAreaAssetList(AssetOperatorDto operatorDto) {
        Objects.requireNonNull(operatorDto.getOperator(), "工号不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operatorDto.getOperator())
            .andStatusIn(AssetStatusEnum.getValidStatusList());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        Map<Long, List<Asset>> listMap = new HashMap<>();
        for (Asset asset : assetList) {
            listMap.computeIfAbsent(asset.getUseAreaId(), k -> new ArrayList<>()).add(asset);
        }
        List<AreaAssetListDto> dtoList = new ArrayList<>();
        for (Entry<Long, List<Asset>> entry : listMap.entrySet()) {
            AreaAssetListDto dto = new AreaAssetListDto();
            List<Asset> list = entry.getValue();
            Asset asset = list.get(0);
            if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getUseAreaId()).getName());
            } else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
                dto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
            }
            dto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
            dto.setOwner(asset.getOwnerName());
            dto.setUseAreaType(asset.getUseAreaType());
            dto.setUseAreaId(asset.getUseAreaId());
            dto.setPutAway(String.valueOf(
                list.stream().filter(
                    i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER
                        .getCode().equals(i.getStatus())).count()));
            dto.setCountList(buildAssetCountDtoList(list));
            dtoList.add(dto);
        }
        return dtoList;
    }

    @Override
    public CategoryAssetDetailDto getCategoryAssetDetail(AssetDetailQueryCondition condition) {
        Objects.requireNonNull(condition.getOperator(), "工号不能为空");
        Objects.requireNonNull(condition.getCategory(), "资产种类不能为空");
        Objects.requireNonNull(condition.getUseAreaType(), "区域类型不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getOperator()).
            andCategoryEqualTo(condition.getCategory()).andStatusIn(AssetStatusEnum.getValidStatusList());
        //组织头部
        List<Asset> preAssets = assetMapper.selectByExample(assetExample);
        if (CollectionUtils.isEmpty(preAssets)) {
            return null;
        }
        CategoryAssetDetailDto assetDetailDto = new CategoryAssetDetailDto();
        assetDetailDto.setCategory(condition.getCategory());
        assetDetailDto.setCategoryName(diamondConfiguredProperties.getCategoryMap().get(condition.getCategory()));
        assetDetailDto.setTotal(String.valueOf(preAssets.size()));
        assetDetailDto.setPutAway(String.valueOf(
            preAssets.stream().filter(
                i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER.getCode()
                    .equals(i.getStatus())).count()));
        assetDetailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(preAssets.get(0).getOwnerOrgId()).getName());
        assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
        AssetExample conditionExample = new AssetExample();
        AssetExample.Criteria criteria = conditionExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getOperator()).
            andCategoryEqualTo(condition.getCategory());
        //组织尾巴
        if (StringUtils.isNotEmpty(condition.getStatus())) {
            if ("Y".equals(condition.getStatus())) {
                criteria.andRecycleEqualTo(condition.getStatus());
                criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
            } else if ("CHECKING".equals(condition.getStatus())) {
                criteria.andCheckStatusEqualTo(condition.getStatus());
                criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
            } else {
                criteria.andStatusEqualTo(condition.getStatus());
            }
        } else {
            criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
        }
        if (StringUtils.isNotEmpty(condition.getAliNo())) {
            criteria.andAliNoEqualTo(condition.getAliNo());
        }
        criteria.andUseAreaTypeEqualTo(condition.getUseAreaType());
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<Asset> assets = assetMapper.selectByExample(conditionExample);
        Page<Asset> assetPage = (Page<Asset>)assets;
        assetDetailDto.setDetailList(PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assets)));
        return assetDetailDto;
    }

    @Override
    public AreaAssetDetailDto getAreaAssetDetail(AssetDetailQueryCondition condition) {
        Objects.requireNonNull(condition.getOperator(), "工号不能为空");
        Objects.requireNonNull(condition.getUseAreaId(), "区域不能为空");
        Objects.requireNonNull(condition.getUseAreaType(), "区域类型不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andUseAreaTypeEqualTo(condition.getUseAreaType())
            .andOwnerWorknoEqualTo(condition.getOperator()).andUseAreaIdEqualTo(condition.getUseAreaId()).andStatusIn(
            AssetStatusEnum.getValidStatusList());
        //组织头部
        List<Asset> preAssets = assetMapper.selectByExample(assetExample);
        if (CollectionUtils.isEmpty(preAssets)) {
            return null;
        }
        AreaAssetDetailDto assetDetailDto = new AreaAssetDetailDto();
        assetDetailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(preAssets.get(0).getOwnerOrgId()).getName());
        assetDetailDto.setOwner(preAssets.get(0).getOwnerName());
        assetDetailDto.setCategoryCountDtoList(buildAssetCountDtoList(preAssets));
        AssetExample conditionExample = new AssetExample();
        AssetExample.Criteria criteria = conditionExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andUseAreaTypeEqualTo(condition.getUseAreaType()).andOwnerWorknoEqualTo(
            condition.getOperator()).andUseAreaIdEqualTo(condition.getUseAreaId());
        //组织尾巴
        if (StringUtils.isNotEmpty(condition.getStatus())) {
            if ("Y".equals(condition.getStatus())) {
                criteria.andRecycleEqualTo(condition.getStatus());
            } else if ("CHECKING".equals(condition.getStatus())) {
                criteria.andCheckStatusEqualTo(condition.getStatus());
            } else {
                criteria.andStatusEqualTo(condition.getStatus());
            }
        } else {
            criteria.andStatusIn(AssetStatusEnum.getValidStatusList());
        }
        if (StringUtils.isNotEmpty(condition.getAliNo())) {
            criteria.andAliNoEqualTo(condition.getAliNo());
        }
        if (StringUtils.isNotEmpty(condition.getCategory())) {
            criteria.andCategoryEqualTo(condition.getCategory());
        }
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<Asset> assets = assetMapper.selectByExample(conditionExample);
        Page<Asset> assetPage = (Page<Asset>)assets;
        assetDetailDto.setDetailList(PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assets)));
        return assetDetailDto;
    }

    @Override
    public AssetDetailDto signAssetByCounty(AssetDto signDto) {
        Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
        Objects.requireNonNull(signDto.getOperator(), "用户不能为空");
        Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andStatusIn(
            AssetStatusEnum.getCanCountySignStatusList());
        Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
        if (asset == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "入库失败" + AssetBO.NO_EXIT_ASSET);
        }
        Asset updateAsset = buildUpdateAsset(signDto, true);
        updateAsset.setId(asset.getId());
        boolean res = assetMapper.updateByPrimaryKeySelective(updateAsset) > 0;
        if (AssetStatusEnum.TRANSFER.getCode().equals(asset.getStatus()) && res) {
            //调集团接口转移责任人
            transferItAsset(signDto);
            sendAppMessage(asset.getOwnerWorkno(), updateAsset, AssetStatusEnum.TRANSFER.getCode());
        } else {
            //调集团接口出库
            obtainItAsset(signDto);
        }
        return buildAssetDetail(assetMapper.selectByPrimaryKey(updateAsset.getId()));
    }

    private void obtainItAsset(AssetDto signDto) {
        AssetTransDto transDto = new AssetTransDto();
        transDto.setAssetCode(signDto.getAliNo());
        transDto.setOwner(signDto.getOperator());
        transDto.setUser(signDto.getOperator());
        transDto.setVoucherId("obtainAsset" + signDto.getAliNo());
        transDto.setWorkId(signDto.getOperator());
        transDto.setGroupCode(GROUP_CODE);
        AssetApiResultDO<Boolean> result = cuntaoApiService.assetObtain(
            Collections.singletonList(transDto));
        if (!result.isSuccess() || !result.getResult()) {
            logger.error("{bizType}, obtainItAsset error,{errorMsg} ", "assetError", result.getErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "集团资产出库失败,请联系管理员！");
        }
    }
    
    @Override
    public void transferItAsset(AssetDto signDto) {
        AssetTransDto transDto = new AssetTransDto();
        transDto.setAssetCode(signDto.getAliNo());
        transDto.setVoucherId("transferAsset" + signDto.getAliNo());
        transDto.setOwner(signDto.getOperator());
        transDto.setGroupCode(GROUP_CODE);
        transDto.setWorkId(signDto.getOperator());
        transDto.setUser(signDto.getOperator());
        AssetApiResultDO<Boolean> result = cuntaoApiService.transferOwner(
            Collections.singletonList(transDto));
        if (!result.isSuccess()) {
            logger.error("{bizType}, transferItAsset error,{errorMsg} ", "assetError", result.getErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "集团资产转移失败,请联系管理员！");
        }

    }

    @Override
    public void sendAppMessage(String owner, Asset asset, String type) {
        AssetSignEvent signEvent = new AssetSignEvent();
        signEvent.setAppId("cuntaoCRM");
        signEvent.setReceivers(Collections.singletonList(Long.valueOf(owner)));
        signEvent.setReceiverType("EMPIDS");
        signEvent.setMsgType("ASSET");
        //signEvent.setMsgTypeDetail("SIGN");
        signEvent.setMsgTypeDetail(type);
        signEvent.setAction("all");
        Content content = signEvent.new Content();
        content.setBizId(asset.getId());
        content.setPublishTime(new Date());
        if (AssetStatusEnum.SIGN.getCode().equals(type)) {
            content.setTitle("您转移的资产已被对方签收，请关注！");
            content.setContent(
                "您转移至" + cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName() + " " + emp360Adapter
                    .getName(asset.getOwnerWorkno()) + "的资产已被对方签收，查看详情");
        } else if (AssetStatusEnum.SCRAP.getCode().equals(type)) {
            content.setTitle("您申报的资产遗失、损毁已完成赔付，请关注！");
            content.setContent(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName() + " " + emp360Adapter
                    .getName(asset.getOwnerWorkno()) + "的资产遗失、损毁已完成赔付，查看详情");

        }
        signEvent.setContent(content);
        EventDispatcherUtil.dispatch("CRM_ASSET_SIGN", new ExtEvent(JSON.toJSONString(signEvent)));
    }

    @Override
    public Boolean signAssetByStation(AssetDto signDto) {
        Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
        Objects.requireNonNull(signDto.getOperator(), "用户不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo()).andStatusEqualTo(
            AssetStatusEnum.DISTRIBUTE.getCode());
        Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
        if (asset == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "签收失败" + AssetBO.NO_EXIT_ASSET);
        }
        assetIncomeBO.signAssetByStation(asset.getId(), signDto.getOperator());

        Asset updateAsset = new Asset();
        DomainUtils.beforeUpdate(updateAsset, signDto.getOperator());
        updateAsset.setStatus(AssetStatusEnum.USE.getCode());
        updateAsset.setId(asset.getId());
        updateAsset.setUseAreaType(AssetUseAreaTypeEnum.STATION.getCode());
        updateAsset.setUserId(signDto.getOperator());
        updateAsset.setUseAreaId(
            partnerInstanceQueryService.getCurStationIdByTaobaoUserId(Long.parseLong(signDto.getOperator())));
        updateAsset.setUserName(uicReadAdapter.getFullName(Long.valueOf(signDto.getOperator())));
        return assetMapper.updateByPrimaryKeySelective(updateAsset) > 0;
    }

    @Override
    public AssetDetailDto recycleAsset(AssetDto signDto) {
        Objects.requireNonNull(signDto.getAliNo(), "编号不能为空");
        Objects.requireNonNull(signDto.getOperator(), "操作人不能为空");
        Objects.requireNonNull(signDto.getOperatorOrgId(), "组织不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(signDto.getAliNo());
        Asset asset = ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
        if (asset == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "回收失败" + AssetBO.NO_EXIT_ASSET);
        }
        if (!asset.getOwnerWorkno().equals(signDto.getOperator()) || !asset.getOwnerOrgId().equals(
            signDto.getOperatorOrgId())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "回收失败" + AssetBO.NOT_OPERATOR + getPromptInfo(asset));
        }
        
        if (!AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
                throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                    "回收失败, 当前资产不是村小二使用，不需要回收" + getPromptInfo(asset));
            }
        
        //创建入库单
        createRecycleIncome(asset,asset.getUseAreaId(),Long.parseLong(asset.getUserId()));
        
        DomainUtils.beforeUpdate(asset, signDto.getOperator());
        asset.setStatus(AssetStatusEnum.USE.getCode());
        asset.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
        asset.setUserId(signDto.getOperator());
        asset.setUseAreaId(signDto.getOperatorOrgId());
        asset.setUserName(emp360Adapter.getName(signDto.getOperator()));
        asset.setRecycle(RecycleStatusEnum.N.getCode());
        assetMapper.updateByPrimaryKeySelective(asset);
        return buildAssetDetail(asset);
    }
    
    private void createRecycleIncome(Asset a, Long stationId,Long taobaoUserId) {
    	Station s = stationBO.getStationById(stationId);
		if (s == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"分发失败，服务站信息异常");
		}
		String sName = s.getName();
		
		Partner p = partnerInstanceBO.getPartnerByStationId(stationId);
		if (p == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"分发失败，合伙人信息异常");
		}
		
        //创建入库单
        AssetIncomeDto icDto = new AssetIncomeDto();
        icDto.setApplierAreaId(stationId);
        icDto.setApplierAreaName(s.getName());
        icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.STATION);
        icDto.setApplierId(String.valueOf(taobaoUserId));
        icDto.setApplierName(p.getName());
        icDto.setReceiverName(a.getOwnerName());
        icDto.setReceiverOrgId(a.getOwnerOrgId());
        icDto.setReceiverOrgName(getOrgName(a.getOwnerOrgId()));
        icDto.setReceiverWorkno(a.getOwnerWorkno());
        icDto.setRemark("从  "+sName +" 回收");
        icDto.setStatus(AssetIncomeStatusEnum.DONE);
        icDto.setType(AssetIncomeTypeEnum.RECOVER);
        icDto.setSignType(AssetIncomeSignTypeEnum.SCAN);
        icDto.copyOperatorDto(OperatorDto.defaultOperator());
        Long incomeId = assetIncomeBO.addIncome(icDto);

        AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
        detail.setAssetId(a.getId());
        detail.setCategory(a.getCategory());
        detail.setIncomeId(incomeId);
        detail.setStatus(AssetRolloutIncomeDetailStatusEnum.HAS_SIGN);
        detail.setType(AssetRolloutIncomeDetailTypeEnum.RECOVER);
        detail.setOperatorTime(new Date());
        detail.copyOperatorDto(OperatorDto.defaultOperator());
        assetRolloutIncomeDetailBO.addDetail(detail);
    }

    @Override
    public PageDto<AssetDetailDto> getTransferAssetList(AssetOperatorDto operator) {
        Objects.requireNonNull(operator.getOperator(), "工号不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(operator.getOperator())
            .andStatusIn(AssetStatusEnum.getValidStatusList());
        PageHelper.startPage(operator.getPageNum(), operator.getPageSize());
        PageHelper.orderBy("status asc");
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        Page<Asset> assetPage = (Page<Asset>)assetList;
        return PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assetList));
    }

    @Override
    public List<Asset> transferAssetSelfCounty(AssetTransferDto transferDto) {
        Objects.requireNonNull(transferDto.getOperator(), "操作人工号不能为空");
        Objects.requireNonNull(transferDto.getOperatorOrgId(), "操作人组织不能为空");
        Objects.requireNonNull(transferDto.getReason(), "转移原因不能为空");
        Objects.requireNonNull(transferDto.getReceiverAreaId(), "接受区域不能为空");
        Objects.requireNonNull(transferDto.getReceiverWorkNo(), "接受人工号不能为空");
        if (!transferDto.getReceiverAreaId().equals(transferDto.getOperatorOrgId())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您转移的目标非本县");
        }
        AssetExample assetExample = new AssetExample();
        AssetExample.Criteria criteria = assetExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(transferDto.getOperator()).andStatusIn(AssetStatusEnum.getValidStatusList());
        if (CollectionUtils.isNotEmpty(transferDto.getUnTransferAssetIdList())) {
            criteria.andIdNotIn(transferDto.getUnTransferAssetIdList());
        }
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您转移的资产中包含待对方入库的资产");
        }
        Asset asset = new Asset();
        DomainUtils.beforeUpdate(asset, transferDto.getOperator());
        asset.setStatus(AssetStatusEnum.TRANSFER.getCode());
        assetMapper.updateByExampleSelective(asset, assetExample);
        return assetList;
    }

    @Override
    public List<Asset> transferAssetOtherCounty(AssetTransferDto transferDto) {
        Objects.requireNonNull(transferDto.getOperator(), "工号不能为空");
        Objects.requireNonNull(transferDto.getOperatorOrgId(), "操作人组织不能为空");
        Objects.requireNonNull(transferDto.getReason(), "转移原因不能为空");
        Objects.requireNonNull(transferDto.getReceiverAreaId(), "接受区域不能为空");
        Objects.requireNonNull(transferDto.getReceiverWorkNo(), "接受人工号不能为空");
        Objects.requireNonNull(transferDto.getPayment(), "物流费用不能为空");
        Objects.requireNonNull(transferDto.getDistance(), "运输距离不能为空");
        Objects.requireNonNull(transferDto.getTransferAssetIdList(), "转移资产不能为空");
        AssetExample assetExample = new AssetExample();
        AssetExample.Criteria criteria = assetExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(transferDto.getOperator()).andIdIn(
            transferDto.getTransferAssetIdList());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您转移的资产中包含待对方入库的资产");
        }
        if (!assetList.stream().allMatch(
            asset -> AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您转移的资产中包含已下发至村点的资产");
        }
        Asset asset = new Asset();
        DomainUtils.beforeUpdate(asset, transferDto.getOperator());
        asset.setStatus(AssetStatusEnum.PEND.getCode());
        assetMapper.updateByExampleSelective(asset, assetExample);
        return assetList;
    }

    @Override
    public void agreeTransferAsset(AssetTransferDto transferDto) {
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(transferDto.getTransferAssetIdList())
            .andStatusEqualTo(AssetStatusEnum.PEND.getCode());
        Asset asset = new Asset();
        asset.setStatus(AssetStatusEnum.TRANSFER.getCode());
        DomainUtils.beforeUpdate(asset, transferDto.getOperator());
        assetMapper.updateByExampleSelective(asset, assetExample);
    }

    @Override
    public void disagreeTransferAsset(AssetTransferDto transferDto) {
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(transferDto.getTransferAssetIdList())
            .andStatusEqualTo(AssetStatusEnum.PEND.getCode());
        Asset asset = new Asset();
        asset.setStatus(AssetStatusEnum.USE.getCode());
        DomainUtils.beforeUpdate(asset, transferDto.getOperator());
        assetMapper.updateByExampleSelective(asset, assetExample);
    }

    @Override
    public AssetDetailDto judgeTransfer(AssetDto assetDto) {
        Objects.requireNonNull(assetDto.getAliNo(), "编号不能为空");
        Objects.requireNonNull(assetDto.getOperator(), "操作人不能为空");
        Asset asset = getAssetByAliNo(assetDto.getAliNo());
        if (asset == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "录入失败" + AssetBO.NO_EXIT_ASSET);
        }
        if (!asset.getOwnerWorkno().equals(assetDto.getOperator())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "录入失败" + AssetBO.NOT_OPERATOR + getPromptInfo(asset));
        }
        if (!AssetStatusEnum.USE.getCode().equals(asset.getStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "录入失败，该资产正处于分发、转移中！" + getPromptInfo(asset));
        }
        return buildAssetDetail(asset);
    }

    @Override
    public Asset getAssetByAliNo(String aliNo) {
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(aliNo);
        return ResultUtils.selectOne(assetMapper.selectByExample(assetExample));
    }

    /**
     * 计算出不同类型的资产数量 电脑->20 显示器->10
     *
     * @param list
     * @return
     */
    private List<AssetCategoryCountDto> buildAssetCountDtoList(List<Asset> list) {
        Map<String, List<Asset>> countListMap = new HashMap<>();
        for (Asset countAsset : list) {
            countListMap.computeIfAbsent(countAsset.getCategory(), k -> new ArrayList<>()).add(countAsset);
        }
        List<AssetCategoryCountDto> countList = new ArrayList<>();
        for (Entry<String, List<Asset>> countEntry : countListMap.entrySet()) {
            AssetCategoryCountDto assetCountDto = new AssetCategoryCountDto();
            assetCountDto.setCategory(countEntry.getKey());
            assetCountDto.setCategoryName(diamondConfiguredProperties.getCategoryMap().get(countEntry.getKey()));
            assetCountDto.setTotal(String.valueOf(countEntry.getValue().size()));
            assetCountDto.setPutAway(String.valueOf(
                countEntry.getValue().stream().filter(
                    i -> AssetStatusEnum.DISTRIBUTE.getCode().equals(i.getStatus()) || AssetStatusEnum.TRANSFER
                        .getCode().equals(i.getStatus())).count()));
            countList.add(assetCountDto);
        }
        return countList;
    }

    private List<AssetDetailDto> buildAssetDetailDtoList(List<Asset> assetList) {
        return assetList.stream().map(this::buildAssetDetail).collect(Collectors.toList());
    }

    @Override
    public AssetDetailDto buildAssetDetail(Asset asset) {
        AssetDetailDto detailDto = new AssetDetailDto();
        BeanUtils.copyProperties(asset, detailDto);
        if (AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType())) {
            detailDto.setUseArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getUseAreaId()).getName());
        } else if (AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType())) {
            detailDto.setUseArea(stationBO.getStationById(asset.getUseAreaId()).getName());
        }
        detailDto.setStatus(AssetStatusEnum.valueOf(asset.getStatus()));
        detailDto.setCategoryName(diamondConfiguredProperties.getCategoryMap().get(asset.getCategory()));
        detailDto.setOwner(asset.getOwnerName());
        detailDto.setOwnerArea(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
        detailDto.setId(asset.getId());
        detailDto.setCheckStatus(AssetCheckStatusEnum.valueof(asset.getCheckStatus()));
        detailDto.setAreaType(AssetUseAreaTypeEnum.valueOf(asset.getUseAreaType()));

        return detailDto;
    }

    @Override
    public PageDto<AssetDetailDto> getScrapAssetList(AssetScrapListCondition condition) {
        Objects.requireNonNull(condition.getOperator(), "工号不能为空");
        Objects.requireNonNull(condition.getUseAreaType(), "赔付区域类型不能为空");
        Objects.requireNonNull(condition.getUseAreaId(), "赔付地点不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(condition.getOperator())
            .andUseAreaTypeEqualTo(condition.getUseAreaType()).andUseAreaIdEqualTo(condition.getUseAreaId())
            .andStatusIn(AssetStatusEnum.getCanScrapStatusList());
        PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        Page<Asset> assetPage = (Page<Asset>)assetList;
        return PageDtoUtil.success(assetPage, buildAssetDetailDtoList(assetList));
    }

    @Override
    public AssetDetailDto getScrapDetailById(Long id, AssetOperatorDto assetOperatorDto) {
        Objects.requireNonNull(assetOperatorDto.getOperator(), "工号不能为空");
        Objects.requireNonNull(id, "赔付资产不能为空");
        Asset asset = assetMapper.selectByPrimaryKey(id);
        if (!("n".equals(asset.getIsDeleted()) && assetOperatorDto.getOperator().equals(asset.getOwnerWorkno()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您赔付的资产不属于您名下");
        }
        AssetDetailDto detailDto = buildAssetDetail(asset);
        AssetApiResultDO<AssetLostQueryResult> queryResult = cuntaoApiService.assetLostQuery(
            detailDto.getAliNo(), GROUP_CODE);
        if (!queryResult.isSuccess()) {
            logger.error("{bizType}, getScrapDetailById error,{errorMsg} ", "assetError", queryResult.getErrorMsg());
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "查询资产净值失败,请联系管理员！");
        }
        detailDto.setPayment(queryResult.getResult().getNetValue());
        return detailDto;
    }

    @Override
    public void scrapAsset(AssetScrapDto scrapDto) {
        Objects.requireNonNull(scrapDto.getOperator(), "工号不能为空");
        Objects.requireNonNull(scrapDto.getOperatorOrgId(), "操作人组织不能为空");
        Objects.requireNonNull(scrapDto.getScrapAssetId(), "赔付资产不能为空");
        Objects.requireNonNull(scrapDto.getScrapAreaId(), "赔付地点不能为空");
        Objects.requireNonNull(scrapDto.getScrapAreaType(), "赔付区域不能为空");
        Objects.requireNonNull(scrapDto.getFree(), "请申请是否免赔");
        Objects.requireNonNull(scrapDto.getReason(), "赔付原因不能为空");
        Objects.requireNonNull(scrapDto.getPayment(), "赔付金额不能为空");
        Asset asset = assetMapper.selectByPrimaryKey(scrapDto.getScrapAssetId());
        validateScrapAsset(asset, scrapDto.getOperator());
        //发起赔付流程
        scrapingItAsset(asset, scrapDto);
        asset.setStatus(AssetStatusEnum.SCRAPING.getCode());
        DomainUtils.beforeUpdate(asset, scrapDto.getOperator());
        assetMapper.updateByPrimaryKeySelective(asset);
    }

    private void scrapingItAsset(Asset asset, AssetScrapDto scrapDto){
        AssetLostRequestDto requestDto = new AssetLostRequestDto();
        requestDto.setAssetCode(asset.getAliNo());
        requestDto.setWorkId(asset.getUserId());
        requestDto.setCurrentCost(Double.parseDouble(scrapDto.getPayment()));
        requestDto.setVoucherId("scrapingAsset" + asset.getAliNo());
        requestDto.setDeductible(scrapDto.getFree());
        requestDto.setApplicantWorkId(scrapDto.getOperator());
        requestDto.setReason(AssetScrapReasonEnum.valueOf(scrapDto.getReason()).getDesc());
        if (CollectionUtils.isNotEmpty(scrapDto.getAttachmentList())) {
            requestDto.setAttachments(scrapDto.getAttachmentList().stream().map(attachment -> {
                Attachment itAttachment = new Attachment();
                BeanUtils.copyProperties(attachment, itAttachment);
                return itAttachment;
            }).collect(Collectors.toList()));
        }
        requestDto.setGroupCode(GROUP_CODE);
        AssetApiResultDO<Boolean> result = cuntaoApiService.assetLostScrappingWorkflow(requestDto);
        if (!result.isSuccess()) {
            logger.error("{bizType},{parameter} scraping it asset fail " + result.getErrorMsg(), "assetError", JSON.toJSONString(requestDto));
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "集团资产发起赔付失败,请联系管理员！");
        }
    }

    @Override
    public void scrapAssetByStation(AssetScrapDto scrapDto) {
        Objects.requireNonNull(scrapDto.getOperator(), "工号不能为空");
        Objects.requireNonNull(scrapDto.getPayment(), "赔偿金额不能为空");
        Objects.requireNonNull(scrapDto.getReason(), "赔偿金额不能为空");
        Asset asset = assetMapper.selectByPrimaryKey(scrapDto.getScrapAssetId());
        //资产状态校验
        validateScrapAsset(asset, scrapDto.getOperator());
        //保证金转移
        transferBail(scrapDto, asset.getUserId());
        //通知集团资产废弃
        scrapItAsset(asset, scrapDto);
        //村淘库资产状态变更
        asset.setStatus(AssetStatusEnum.SCRAP.getCode());
        DomainUtils.beforeUpdate(asset, scrapDto.getOperator());
        assetMapper.updateByPrimaryKeySelective(asset);
        sendAppMessage(asset.getOwnerWorkno(), asset, AssetStatusEnum.SCRAP.getCode());
    }

    private void scrapItAsset(Asset asset, AssetScrapDto scrapDto) {
        AssetLostRequestDto requestDto = new AssetLostRequestDto();
        requestDto.setAssetCode(asset.getAliNo());
        requestDto.setWorkId(asset.getUserId());
        requestDto.setCurrentCost(Double.parseDouble(scrapDto.getPayment()));
        requestDto.setVoucherId("scrapAsset" + asset.getAliNo());
        requestDto.setDeductible("n");
        requestDto.setApplicantWorkId(scrapDto.getOperator());
        requestDto.setGroupCode(GROUP_CODE);
        requestDto.setReason(AssetScrapReasonEnum.valueOf(scrapDto.getReason()).getDesc());
        AssetApiResultDO<Boolean> result = cuntaoApiService.assetLostScrapping(requestDto);
        if (!result.isSuccess()) {
            logger.error("{bizType},{parameter} scrap it asset fail " + result.getErrorMsg(), "assetError", JSON.toJSONString(requestDto));
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "集团资产赔付失败,请联系管理员！");
        }
    }

    private void validateScrapAsset(Asset asset, String operator) {
        if (!"n".equals(asset.getIsDeleted())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您赔付的资产仓库中不存在");
        }
        if (!AssetStatusEnum.getCanScrapStatusList().contains(asset.getStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您赔付的资产不在正常使用状态下");
        }
        if (!operator.equals(asset.getOwnerWorkno())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您赔付的资产不属于您名下");
        }
    }

    private void transferBail(AssetScrapDto scrapDto, String userId) {
        CuntaoTransferBailDto bailDto = buildBailDtoByScrapDto(scrapDto, userId);
        ResultModel<Boolean> resultModel = newBailService.transferUserBail(bailDto);
        if (!resultModel.isSuccess()) {
            logger.warn("{bizType},{parameter} transfer bail fail " + resultModel.getMessage(), "assetWarn",
                JSON.toJSONString(bailDto));
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "资产赔付保证金转移失败,请联系管理员！");
        }
    }

    private CuntaoTransferBailDto buildBailDtoByScrapDto(AssetScrapDto scrapDto, String userId) {
        CuntaoTransferBailDto bailDto = new CuntaoTransferBailDto();
        bailDto.setInAccountUserId(inAccountUserId);
        bailDto.setOutAccountUserId(Long.valueOf(userId));
        bailDto.setUserTypeEnum(UserTypeEnum.PARTNER);
        bailDto.setAmount(Long.valueOf(scrapDto.getPayment()) * 100);
        bailDto.setSource("org");
        bailDto.setReason("scrapAsset");
        bailDto.setBailOperateTypeEnum(BailOperateTypeEnum.ACTIVE_TRANSFER);
        bailDto.setOutOrderId("scrap_asset" + scrapDto.getScrapAssetId());
        return bailDto;
    }

    @Override
    public Asset getAssetById(Long assetId) {
        Objects.requireNonNull(assetId, "资产id不能为空");
        return assetMapper.selectByPrimaryKey(assetId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void cancelTransferAsset(List<Long> assetIds, String operator) {
        Objects.requireNonNull(assetIds, "资产列表不能为空");
        Objects.requireNonNull(operator, "操作人不能为空");
        Asset asset = new Asset();
        asset.setStatus(AssetStatusEnum.USE.getCode());
        DomainUtils.beforeUpdate(asset, operator);
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(assetIds);
        assetMapper.updateByExampleSelective(asset, assetExample);

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public List<Asset> distributeAsset(AssetDistributeDto distributeDto) {
        ValidateUtils.validateParam(distributeDto);
        Objects.requireNonNull(distributeDto.getStationId(), "服务站id不能为空");
        
        if (CollectionUtils.isEmpty(distributeDto.getAssetIdList())) {
        	 throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "待分发资产不能为空!");
        }
        Station s = stationBO.getStationById(distributeDto.getStationId()); 
        if (s ==null || !(canDistributeList().contains(s.getStatus()))) {
        	 throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "当前服务站状态不能分发资产!");
        }
        
        AssetExample assetExample = new AssetExample();
        AssetExample.Criteria criteria = assetExample.createCriteria();
        criteria.andIsDeletedEqualTo("n").andOwnerWorknoEqualTo(distributeDto.getOperator()).andIdIn(
            distributeDto.getAssetIdList());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        if (!assetList.stream().allMatch(asset -> AssetStatusEnum.USE.getCode().equals(asset.getStatus()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您分发的资产中包含待对方入库的资产!");
        }
        if (!assetList.stream().allMatch(
            asset -> AssetUseAreaTypeEnum.COUNTY.getCode().equals(asset.getUseAreaType()))) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "您分发的资产中包含已下发至村点的资产!");
        }
        Asset asset = new Asset();
        DomainUtils.beforeUpdate(asset, distributeDto.getOperator());
        asset.setStatus(AssetStatusEnum.DISTRIBUTE.getCode());
        assetMapper.updateByExampleSelective(asset, assetExample);
        return assetList;
    }
    
    private  List<String> canDistributeList() {
    	List<String> sStatus = new ArrayList<String>();
    	sStatus.add(StationStatusEnum.SERVICING.getCode());
    	sStatus.add(StationStatusEnum.DECORATING.getCode());
    	return sStatus;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public AssetDetailDto checkAsset(AssetCheckDto checkDto) {
        ValidateUtils.validateParam(checkDto);
        Objects.requireNonNull(checkDto.getAliNo(), "盘点资产不能为空");
        Objects.requireNonNull(checkDto.getUserId(), "盘点人不能为空");
        Objects.requireNonNull(checkDto.getUseAreaType(), "盘点人区域类型不能为空");
        String aliNo = checkDto.getAliNo();
        String userId = checkDto.getUserId();
        String useAreaType = checkDto.getUseAreaType().getCode();

        Asset asset = validateUserIdForAssetCheck(userId, useAreaType, aliNo);

        Asset record = new Asset();
        DomainUtils.beforeUpdate(record, checkDto.getOperator());
        record.setCheckStatus(AssetCheckStatusEnum.CHECKED.getCode());
        record.setCheckTime(new Date());
        record.setId(asset.getId());
        assetMapper.updateByPrimaryKeySelective(record);

        AssetChangeEvent event = buildAssetChangeEvent(asset.getId(),
            CuntaoFlowRecordTargetTypeEnum.NEW_ASSET_CHECK.getCode(), checkDto.getOperator(),checkDto.getOperatorType(),
            AssetCheckStatusEnum.CHECKED.getDesc());
        EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
        return buildAssetDetail(assetMapper.selectByPrimaryKey(asset.getId()));
    }

    private Asset validateUserIdForAssetCheck(String userId, String useAreaType, String aliNo) {
        Asset asset = getAssetByAliNo(aliNo);
        if (asset == null || !StringUtils.equals(AssetCheckStatusEnum.CHECKING.getCode(), asset.getCheckStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "盘点失败" + AssetBO.NO_EXIT_ASSET);
        }
        if (!asset.getUseAreaType().equals(useAreaType) && !asset.getUserId().equals(userId)) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "盘点失败" + AssetBO.NOT_OPERATOR + getPromptInfo(asset));
        }
        return asset;
    }

    private String getPromptInfo(Asset asset) {
        StringBuilder sb = new StringBuilder();
        sb.append("资产编号：");
        sb.append(asset.getAliNo());
        sb.append(",资产名称：[");
        sb.append(diamondConfiguredProperties.getCategoryMap().get(asset.getCategory()));
        sb.append("]");
        sb.append(asset.getBrand());
        sb.append(" ");
        sb.append(asset.getModel());
        sb.append(",责任地点：");
        sb.append(cuntaoOrgServiceClient.getCuntaoOrg(asset.getOwnerOrgId()).getName());
        sb.append(",责任人：");
        sb.append(asset.getOwnerName());
        return sb.toString();

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false, rollbackFor = Exception.class)
    public Boolean checkingAsset(Long assetId, String operator) {
        Asset record = new Asset();
        DomainUtils.beforeUpdate(record, operator);
        record.setCheckStatus(AssetCheckStatusEnum.CHECKING.getCode());
        record.setCheckTime(new Date());
        record.setId(assetId);
        assetMapper.updateByPrimaryKeySelective(record);
        return Boolean.TRUE;
    }

    @Override
    public Page<Asset> getCheckedAsset(Integer pageNum, Integer pageSize) {
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andCheckStatusEqualTo(
            AssetCheckStatusEnum.CHECKED.getCode()).andStatusEqualTo(AssetStatusEnum.SCRAP.getCode());
        PageHelper.startPage(pageNum, pageSize);
        return (Page<Asset>)assetMapper.selectByExample(assetExample);
    }

    @Override
    public void setAssetRecycleIsY(Long stationId, Long taobaoUserId) {
        Objects.requireNonNull(stationId, "村点id不能为空");
        Objects.requireNonNull(taobaoUserId, "taobaoUserId不能为空");
        AssetExample assetExample = bulidStationAssetParam(stationId, taobaoUserId);
        
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        if (CollectionUtils.isEmpty(assetList)) {
        	return;
        }
        
        Asset asset = new Asset();
        DomainUtils.beforeUpdate(asset, OperatorDto.DEFAULT_OPERATOR);
        asset.setRecycle(RecycleStatusEnum.Y.getCode());
        assetMapper.updateByExampleSelective(asset, assetExample);
    }
    
    private String getOrgName(Long orgId){
		String name = "";
		try {
			CuntaoOrgDto orgDto = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
			if (orgDto !=null) {
				name= orgDto.getName();
			}
			return name;
		} catch (Exception e) {
			logger.error("assetRolloutBO.getOrgName error param:"+orgId,e);
			throw e;
		}
	}

    @Override
    public void cancelAssetRecycleIsY(Long stationId, Long taobaoUserId) {
        Objects.requireNonNull(stationId, "村点id不能为空");
        Objects.requireNonNull(taobaoUserId, "taobaoUserId不能为空");
        AssetExample assetExample = bulidStationAssetParam(stationId, taobaoUserId);
        
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        if (CollectionUtils.isEmpty(assetList)) {
        	return;
        }
        
        Asset asset = new Asset();
        DomainUtils.beforeUpdate(asset, OperatorDto.DEFAULT_OPERATOR);
        asset.setRecycle(RecycleStatusEnum.N.getCode());
        assetMapper.updateByExampleSelective(asset, assetExample);
    }

    private AssetExample bulidStationAssetParam(Long stationId, Long taobaoUserId) {
        AssetExample assetExample = new AssetExample();
        List<String> sList = new ArrayList<String>();
        sList.add(AssetStatusEnum.SCRAPING.getCode());
        sList.add(AssetStatusEnum.SCRAP.getCode());
        assetExample.createCriteria().andIsDeletedEqualTo("n").andUseAreaIdEqualTo(stationId).andUseAreaTypeEqualTo(
            AssetUseAreaTypeEnum.STATION.getCode())
            .andUserIdEqualTo(String.valueOf(taobaoUserId))
            .andStatusNotIn(sList);
        return assetExample;
    }

    @Override
    public void validateAssetForQuiting(Long stationId, Long taobaoUserId) {
        Objects.requireNonNull(stationId, "村点id不能为空");
        Objects.requireNonNull(taobaoUserId, "taobaoUserId不能为空");
        AssetExample assetExample = bulidStationAssetParam(stationId, taobaoUserId);
        int count = assetMapper.countByExample(assetExample);
        if (count > 0) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "退出失败，有资产未回收，请用小二APP回收资产。");
        }
        List<AssetRollout> arList = assetRolloutBO.getDistributeAsset(stationId, taobaoUserId);
        if (CollectionUtils.isNotEmpty(arList)) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "退出失败，有资产待村小二签收，请先回收资产。");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Long purchase(AssetPurchaseDto purDto) {
        Objects.requireNonNull(purDto, "采购数据不能为空");
        Objects.requireNonNull(purDto.getDetailDto(), "采购详情数据不能为空");

        //创建入库单
        AssetIncomeDto icDto = new AssetIncomeDto();
        icDto.setApplierAreaId(0L);
        icDto.setApplierAreaName("采购系统");
        icDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.CAIGOU);
        icDto.setApplierId(purDto.getOperator());
        icDto.setApplierName(emp360Adapter.getName(purDto.getOperator()));
        icDto.setReceiverName(purDto.getOwnerName());
        icDto.setReceiverOrgId(purDto.getOwnerOrgId());
        icDto.setReceiverOrgName(purDto.getOwnerOrgName());
        icDto.setReceiverWorkno(purDto.getOwnerWorkno());
        icDto.setRemark("采购");
        icDto.setStatus(AssetIncomeStatusEnum.TODO);
        icDto.setType(AssetIncomeTypeEnum.PURCHASE);
        icDto.setSignType(AssetIncomeSignTypeEnum.SCAN);
        icDto.setPoNo(purDto.getPoNo());
        icDto.copyOperatorDto(purDto);
        Long incomeId = assetIncomeBO.addIncome(icDto);

        for (AssetPurchaseDetailDto pDto : purDto.getDetailDto()) {
        	//校验资产重复alino是否唯一
        	if(!checkUniqueAliNo(pDto.getAliNo())){
        		 throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "采购失败" + ",当前资产["+pDto.getAliNo()+"]已存在");
        	}
            Asset a = bulidPurAsset(purDto, pDto);
            DomainUtils.beforeInsert(a, purDto.getOperator());
            assetMapper.insert(a);
            Long assetId = a.getId();

            AssetRolloutIncomeDetailDto detail = new AssetRolloutIncomeDetailDto();
            detail.setAssetId(assetId);
            detail.setCategory(a.getCategory());
            detail.setIncomeId(incomeId);
            detail.setStatus(AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN);
            detail.setType(AssetRolloutIncomeDetailTypeEnum.PURCHASE);
            detail.setOperatorTime(new Date());
            detail.copyOperatorDto(purDto);
            assetRolloutIncomeDetailBO.addDetail(detail);
        }

        return incomeId;
    }

    private Boolean checkUniqueAliNo(String aliNo) {
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andAliNoEqualTo(aliNo);
        //组织头部
        List<Asset> preAssets = assetMapper.selectByExample(assetExample);
        if (CollectionUtils.isEmpty(preAssets)) {
            return true;
        }
        return false;
	}

	private Asset bulidPurAsset(AssetPurchaseDto purDto, AssetPurchaseDetailDto pdDto) {
        Asset a = new Asset();
        a.setAliNo(pdDto.getAliNo());
        a.setBrand(pdDto.getBrand());
        a.setCategory(pdDto.getCategory());
        a.setModel(pdDto.getModel());
        a.setOwnerName(purDto.getOwnerName());
        a.setOwnerOrgId(purDto.getOwnerOrgId());
        a.setOwnerWorkno(purDto.getOwnerWorkno());
        a.setPoNo(purDto.getPoNo());
        a.setSerialNo(pdDto.getSerialNo());
        a.setStatus(AssetStatusEnum.SIGN.getCode());
        a.setUseAreaId(purDto.getOwnerOrgId());
        a.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
        a.setUserId(purDto.getOwnerWorkno());
        a.setUserName(purDto.getOwnerName());
        a.setCheckStatus(AssetCheckStatusEnum.UNCHECKED.getCode());
        a.setRecycle(RecycleStatusEnum.N.getCode());
        return a;
    }

    @Override
    public PageDto<AssetDetailDto> queryByPage(AssetQueryPageCondition query) {
        AssetExtExample example = new AssetExtExample();
        AssetExtExample.Criteria cri = example.createCriteria();
        cri.andIsDeletedEqualTo("n");
        if (CollectionUtils.isNotEmpty(query.getStatusList())) {
            cri.andStatusIn(query.getStatusList());
        }
        if (StringUtils.isNotEmpty(query.getAliNo())) {
            cri.andAliNoEqualTo(query.getAliNo());
        }

        if (StringUtils.isNotEmpty(query.getPoNo())) {
            cri.andPoNoEqualTo(query.getPoNo());
        }

        if (StringUtils.isNotEmpty(query.getCheckStatus())) {
            cri.andCheckStatusEqualTo(query.getCheckStatus());
        }

        if (query.getStationId() != null) {
            cri.andUseAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode());
            cri.andUseAreaIdEqualTo(query.getStationId());
        }

        if (StringUtils.isNotEmpty(query.getFullIdPath())) {
            example.setFullIdPath(query.getFullIdPath());
        }

        if (StringUtils.isNotEmpty(query.getUserId())) {
            cri.andUserIdEqualTo(query.getUserId());
        }

        example.setOrderByClause("a.gmt_modified desc");
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        Page<Asset> page = assetExtMapper.selectByExample(example);
        List<AssetDetailDto> targetList = buildAssetDetailDtoList(page);
        return PageDtoUtil.success(page, targetList);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delete(Long assetId, String operator) {
        Asset a = getAssetById(assetId);
        if (a == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "删除失败" + AssetBO.NO_EXIT_ASSET);
        }
        if (!StringUtils.equals(AssetStatusEnum.SIGN.getCode(), a.getStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "删除失败,只能删除待签收的资产");
        }

        Asset record = new Asset();
        DomainUtils.beforeDelete(record, operator);
        record.setId(a.getId());
        assetMapper.updateByPrimaryKeySelective(record);

        //如果有入库单   检查 是否删除入库单
        AssetRolloutIncomeDetail detail = assetRolloutIncomeDetailBO.queryWaitSignByAssetId(assetId);
        if (detail != null) {
        	assetRolloutIncomeDetailBO.deleteWaitSignDetail(assetId, operator);
            Long incomeId = detail.getIncomeId();
            if (incomeId == null) {
                throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                    "删除失败，待签收资产没有对应的入库单，请核对资产信息！如有疑问，请联系资产管理员。");
            }
            //更新出入库单状态
            if (CollectionUtils.isEmpty(assetRolloutIncomeDetailBO.queryListByIncomeId(incomeId))) {
                assetIncomeBO.deleteAssetIncome(incomeId, operator);
            }
        }
    }

    @Override
    public AssetDetailDto getDetail(Long assetId) {
        Asset a = getAssetById(assetId);
        if (a == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "查询失败" + AssetBO.NO_EXIT_ASSET);
        }
        return buildAssetDetail(a);
    }

    @Override
    public List<AssetDetailDto> getDistributeAssetListByStation(
        Long stationId, Long taobaoUserId) {
        Objects.requireNonNull(stationId, "服务站id不能为空");
        Objects.requireNonNull(taobaoUserId, "taobaouserId不能为空");
        List<AssetRollout> arList = assetRolloutBO.getDistributeAsset(stationId, taobaoUserId);
        if (CollectionUtils.isEmpty(arList)) {
            return new ArrayList<AssetDetailDto>();
        }
        List<Long> assetIdList = new ArrayList<Long>();
        for (AssetRollout ar : arList) {
            List<AssetRolloutIncomeDetail> deList = assetRolloutIncomeDetailBO.queryListByRolloutId(ar.getId());
            List<AssetRolloutIncomeDetail> waitSignList = deList.stream().filter(
                i -> AssetRolloutIncomeDetailStatusEnum.WAIT_SIGN.getCode().equals(i.getStatus())).collect(
                Collectors.toList());
            assetIdList.addAll(
                waitSignList.stream().map(AssetRolloutIncomeDetail::getAssetId).collect(Collectors.toList()));
        }
        if (CollectionUtils.isEmpty(assetIdList)) {
        	return null;
        }
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andIdIn(assetIdList);
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        return buildAssetDetailDtoList(assetList);
    }

    @Override
    public List<AssetDetailDto> getUseAssetListByStation(Long stationId,
                                                         Long taobaoUserId) {
        Objects.requireNonNull(stationId, "服务站id不能为空");
        Objects.requireNonNull(taobaoUserId, "taobaouserId不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andUseAreaIdEqualTo(stationId).andUserIdEqualTo(
            String.valueOf(taobaoUserId)).andUseAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode()).andStatusEqualTo(AssetStatusEnum.USE.getCode());
        List<Asset> assetList = assetMapper.selectByExample(assetExample);
        return buildAssetDetailDtoList(assetList);
    }


    @Override
    public Map<String, String> getStationAssetState(Long stationId) {
        Map<String, String> result = new HashMap<>();
        result.put("canBuy", String.valueOf(diamondConfiguredProperties.getCanBuyStationList().contains(stationId)));
        result.put("hasBuy", String.valueOf(getBuyAssetRecord(stationId) != null));
        return result;
    }

    private CuntaoFlowRecord getBuyAssetRecord(Long stationId) {
        CuntaoFlowRecordCondition condition = new CuntaoFlowRecordCondition();
        condition.setTargetId(stationId);
        condition.setTargetType(CuntaoFlowRecordTargetTypeEnum.ASSET_BUY.getCode());
        return cuntaoFlowRecordBO.getRecordByTargetIdAndType(condition);
    }

    @Override
    public Map<String, String> buyAsset(CuntaoAssetDto assetDto) {
        Map<String, String> result = new HashMap<>();
        result.put("success", "true");
        List<Asset> assetList = checkAssetState(assetDto, result);
        if ("true".equals(result.get("success"))) {
            CuntaoTransferBailDto bailDto = buildBailDto(assetDto);
            ResultModel<Boolean> resultModel = newBailService.transferUserBail(bailDto);
            if (resultModel.isSuccess() && resultModel.getResult()) {
                scrapBuyAsset(assetList.stream().map(Asset::getId).collect(Collectors.toList()), assetDto.getOperator());
                saveBuyRecord(assetDto, assetList);
                result.put("success", "true");
            } else {
                result.put("message", getErrorMsg(resultModel.getMessage()));
                result.put("success", "false");
                logger.warn("{bizType},{parameter} buy asset fail " + resultModel.getMessage(), "assetWarn",
                    JSON.toJSONString(bailDto));
            }
        }
        return result;
    }

    private String getErrorMsg(String msg) {
        for (String key : diamondConfiguredProperties.getAssetTransferErrorMap().keySet()) {
            if (msg.contains(key)) {
                return diamondConfiguredProperties.getAssetTransferErrorMap().get(key);
            }
        }
        return msg;
    }

    private void scrapBuyAsset(List<Long> idList, String operator) {
        AssetExample example = new AssetExample();
        example.createCriteria().andIsDeletedEqualTo("n").andIdIn(idList);
        Asset record = new Asset();
        record.setStatus(AssetStatusEnum.SCRAP.getCode());
        DomainUtils.beforeUpdate(record, operator);
        assetMapper.updateByExampleSelective(record, example);
    }

    private List<Asset> checkAssetState(CuntaoAssetDto assetDto, Map<String, String> result) {
        Long stationId = assetDto.getNewStationId();
        if (stationId == null || assetDto.getOperator() == null) {
            result.put("message", "操作信息不能为空!");
            result.put("success", "false");
            return null;
        }
        if (!diamondConfiguredProperties.getCanBuyStationList().contains(stationId)) {
            result.put("message", "该村点不允许提交资产采购意向,请联系资产管理员!");
            result.put("success", "false");
            return null;
        }
        if (getBuyAssetRecord(stationId) != null) {
            result.put("message", "该村点已经提交过资产采购意向!");
            result.put("success", "false");
            return null;
        }
        List<Asset> assetList = getUseAssetListByStationId(stationId);
        if (!validateStationAssetNum(assetList)) {
            result.put("message", "对不起该村点不符合自购资格，您名下的资产需为1主机1显示器1电视时方可进行自购，请将3个设备背面的编码提供给您对应的县运营小二处理");
            result.put("success", "false");
        }
        return assetList;
    }

    public List<Asset> getUseAssetListByStationId(Long stationId) {
        Objects.requireNonNull(stationId, "服务站id不能为空");
        AssetExample assetExample = new AssetExample();
        assetExample.createCriteria().andIsDeletedEqualTo("n").andUseAreaIdEqualTo(stationId)
            .andUseAreaTypeEqualTo(AssetUseAreaTypeEnum.STATION.getCode()).andStatusEqualTo(AssetStatusEnum.USE.getCode());
        return assetMapper.selectByExample(assetExample);
    }

    private CuntaoTransferBailDto buildBailDto(CuntaoAssetDto assetDto) {
        CuntaoTransferBailDto bailDto = new CuntaoTransferBailDto();
        bailDto.setInAccountUserId(inAccountUserId);
        bailDto.setOutAccountUserId(Long.valueOf(assetDto.getOperator()));
        bailDto.setUserTypeEnum(UserTypeEnum.PARTNER);
        Long assetValue = diamondConfiguredProperties.getStationValueMap().get(assetDto.getNewStationId());
        if (assetValue != null && assetValue != 0L) {
            bailDto.setAmount(assetValue);
        } else {
            bailDto.setAmount(1500*100L);
        }
        bailDto.setSource("org");
        bailDto.setReason("buyAsset");
        bailDto.setBailOperateTypeEnum(BailOperateTypeEnum.ACTIVE_TRANSFER);
        bailDto.setOutOrderId("buy_asset" + assetDto.getNewStationId());
        return bailDto;
    }

    private boolean validateStationAssetNum(List<Asset> assetList) {
        if (assetList == null || assetList.size() != 3) {
            return false;
        }
        List<String> categoryList = assetList.stream().map(Asset::getCategory).collect(
            Collectors.toList());
        return categoryList.contains("MAIN") && categoryList.contains("TV") && categoryList.contains("DISPLAY");
    }

    private void saveBuyRecord(CuntaoAssetDto assetDto, List<Asset> assetList) {
        CuntaoFlowRecord record = new CuntaoFlowRecord();
        record.setTargetId(assetDto.getNewStationId());
        record.setTargetType(CuntaoFlowRecordTargetTypeEnum.ASSET_BUY.getCode());
        record.setNodeTitle(CuntaoFlowRecordTargetTypeEnum.ASSET_BUY.getDesc());
        record.setOperateTime(new Date());
        record.setOperatorWorkid(assetDto.getOperator());
        record.setOperatorName(assetDto.getOperator());
        record.setRemarks(assetList.stream().map(Asset::getId).map(String::valueOf)
            .collect(Collectors.joining(",")));
        cuntaoFlowRecordBO.addRecord(record);
    }

	@Override
	public AssetDetailDto judgeDistribute(AssetDto assetDto) {
		Objects.requireNonNull(assetDto.getAliNo(), "编号不能为空");
        Objects.requireNonNull(assetDto.getOperator(), "操作人不能为空");
        Asset asset = getAssetByAliNo(assetDto.getAliNo());
        if (asset == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "录入失败" + AssetBO.NO_EXIT_ASSET);
        }
        if (!asset.getOwnerWorkno().equals(assetDto.getOperator())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "录入失败" + AssetBO.NOT_OPERATOR + getPromptInfo(asset));
        }
        if (!AssetStatusEnum.USE.getCode().equals(asset.getStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                "录入失败,该资产正处于分发、转移中！" + getPromptInfo(asset));
        }
        return buildAssetDetail(asset);
	}

    @Override
    public Boolean signAllAssetByCounty(AssetSignDto signDto) {
        Objects.requireNonNull(signDto.getIncomeId(), "入库id不能为空");
        Objects.requireNonNull(signDto.getOperator(), "操作人不能为空");
        Objects.requireNonNull(signDto.getOperatorOrgId(), "操作人组织不能为空");
        List<Long> idList = assetRolloutIncomeDetailBO.queryListByIncomeId(signDto.getIncomeId()).stream().map(
            AssetRolloutIncomeDetail::getAssetId).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(idList)) {

            Asset updateAsset = buildUpdateAsset(signDto, false);
            AssetExample example = new AssetExample();
            example.createCriteria().andIsDeletedEqualTo("n").andIdIn(idList);
            List<Asset> assetList = assetMapper.selectByExample(example);
            if (!assetList.stream().allMatch(asset -> AssetUseAreaTypeEnum.STATION.getCode().equals(asset.getUseAreaType()))) {
                throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,
                    "签收失败,该资产不处于村点使用下!");
            }
            assetMapper.updateByExampleSelective(updateAsset, example);
            //批量调集团接口转移资产责任人
            assetList.stream().map(
                asset -> {
                    AssetDto assetDto = new AssetDto();
                    assetDto.setAliNo(asset.getAliNo());
                    assetDto.setOperator(signDto.getOperator());
                    return assetDto;
                }
            ).forEach(this::transferItAsset);
        }
        return true;
    }

    @Override
    public void confirmScrapAsset(AssetDto assetDto) {
        Asset asset = getAssetByAliNo(assetDto.getAliNo());
        if (asset == null || AssetStatusEnum.SCRAP.getCode().equals(asset.getStatus())) {
            logger.warn("{bizType}, asset had scrap " + assetDto.getAliNo(), "assetWarn");
            return;
        }
        Asset record = new Asset();
        DomainUtils.beforeUpdate(record, assetDto.getOperator());
        record.setId(asset.getId());
        record.setStatus(AssetStatusEnum.SCRAP.getCode());
        assetMapper.updateByPrimaryKeySelective(record);
        sendAppMessage(asset.getOwnerWorkno(), asset, AssetStatusEnum.SCRAP.getCode());
    }

    private Asset buildUpdateAsset(OperatorDto signDto, boolean changUser) {
        Asset updateAsset = new Asset();
        DomainUtils.beforeUpdate(updateAsset, signDto.getOperator());
        updateAsset.setStatus(AssetStatusEnum.USE.getCode());
        updateAsset.setOwnerName(emp360Adapter.getName(signDto.getOperator()));
        updateAsset.setOwnerOrgId(signDto.getOperatorOrgId());
        updateAsset.setOwnerWorkno(signDto.getOperator());
        if (changUser) {
            updateAsset.setUseAreaType(AssetUseAreaTypeEnum.COUNTY.getCode());
            updateAsset.setUserId(signDto.getOperator());
            updateAsset.setUseAreaId(signDto.getOperatorOrgId());
            updateAsset.setUserName(emp360Adapter.getName(signDto.getOperator()));
        }
        return updateAsset;
    }

	@Override
	public void validateAssetForOpenStation(Long instanceId) {
		
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "检验开业失败，当前村点不存在" );
        }
		Long stationId = rel.getStationId();
		Long taobaoUserId = rel.getTaobaoUserId();
		List<AssetDetailDto> adList = getUseAssetListByStation(stationId,taobaoUserId);
		if (CollectionUtils.isEmpty(adList)) {
			  throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "检验开业失败，当前村点没有签收资产" );
		}
		List<String> categoryList = adList.stream().map(AssetDetailDto::getCategory).collect(Collectors.toList());
		if(!(categoryList.contains("TV") && categoryList.contains("MAIN") && categoryList.contains("DISPLAY"))){
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE, "检验开业失败，资产须为1台电视,1台显示器,1台主机" );
		}
	}
}
