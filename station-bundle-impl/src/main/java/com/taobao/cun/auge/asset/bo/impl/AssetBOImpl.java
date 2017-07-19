package com.taobao.cun.auge.asset.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.settle.bail.dto.CuntaoTransferBailDto;
import com.taobao.cun.settle.bail.enums.BailOperateTypeEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.ResultModel;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.service.AssetQueryCondition;
import com.taobao.cun.auge.asset.service.CuntaoAssetDto;
import com.taobao.cun.auge.asset.service.CuntaoAssetEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExample.Criteria;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExtExample;
import com.taobao.cun.auge.dal.domain.CuntaoFlowRecord;
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
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.util.RequestCtxUtil;

@Component
public class AssetBOImpl implements AssetBO {

    @Autowired
    private CuntaoAssetMapper cuntaoAssetMapper;

    @Autowired
    private CuntaoAssetExtMapper cuntaoAssetExtMapper;

    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Resource
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    private static final String ASSET_SIGN = "assetSign";

    private static final String ASSET_CHECK = "assetCheck";

    private static final String ASEET_CATEGORY_YUNOS = "云OS";

    @Autowired
    private Emp360Adapter emp360Adapter;

    @Autowired
    private UicReadAdapter uicReadAdapter;

    @Autowired
    private CuntaoFlowRecordBO cuntaoFlowRecordBO;

    @Autowired
    private CuntaoNewBailService newBailService;

    private final Long inAccountUserId = 2631673100L;

    private static final Logger logger = LoggerFactory.getLogger(AssetBOImpl.class);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void saveCuntaoAsset(CuntaoAssetDto cuntaoAssetDto, String operator) {

        Assert.notNull(cuntaoAssetDto, "cuntaoAssetDto can not be null");
        if (cuntaoAssetDto.getId() == null) {
            if (cuntaoAssetDto.getStationId() != null) {
                Long stationId = partnerInstanceQueryService.findStationIdByStationApplyId(
                    Long.parseLong(cuntaoAssetDto.getStationId()));
                Long partnerInstanceId = partnerInstanceQueryService.getPartnerInstanceId(
                    Long.valueOf(cuntaoAssetDto.getStationId()));
                cuntaoAssetDto.setNewStationId(stationId);
                cuntaoAssetDto.setPartnerInstanceId(partnerInstanceId);
            }
            CuntaoAsset asset = convert2CuntaoAsset(cuntaoAssetDto);
            cuntaoAssetMapper.insertSelective(asset);
        } else {
            CuntaoAsset cuntaoAsset = cuntaoAssetMapper.selectByPrimaryKey(cuntaoAssetDto.getId());
            cuntaoAssetMapper.updateByPrimaryKeySelective(convert2CuntaoAsset(cuntaoAssetDto));
            if (!CuntaoAssetEnum.STATION_SIGN.getCode().equals(cuntaoAsset.getStatus()) && CuntaoAssetEnum.STATION_SIGN
                .getCode().equals(cuntaoAssetDto.getStatus())) {
                AssetChangeEvent event = buildAssetChangeEvent(cuntaoAssetDto.getId(), ASSET_SIGN, operator,
                    cuntaoAssetDto.getStatus());
                EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
            }
            if (!CuntaoAssetEnum.CHECKED.getCode().equals(cuntaoAsset.getCheckStatus()) && CuntaoAssetEnum.CHECKED
                .getCode().equals(cuntaoAssetDto.getCheckStatus())) {
                AssetChangeEvent event = buildAssetChangeEvent(cuntaoAssetDto.getId(), ASSET_CHECK, operator,
                    cuntaoAssetDto.getCheckStatus());
                EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
            }
        }
    }

    private AssetChangeEvent buildAssetChangeEvent(Long assetId, String type, String operator, String desc) {
        AssetChangeEvent event = new AssetChangeEvent();
        event.setAssetId(assetId);
        event.setOperateTime(new Date());
        event.setDescription(desc);
        event.setType(type);
        event.setOperatorId(operator);
        if ("cuntaobops".equals(RequestCtxUtil.getAppNameOfClient())) {
            String operatorName = emp360Adapter.getName(operator);
            event.setOperator(operatorName);
        } else if ("cuntaoadmin".equals(RequestCtxUtil.getAppNameOfClient())) {
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
        Assert.notNull(assetId, "assetId can not be null");
        Assert.notNull(operator, "operator can not be null");
        CuntaoAsset asset = new CuntaoAsset();
        asset.setId(assetId);
        asset.setModifier(operator);
        asset.setGmtModified(new Date());
        asset.setStatus(CuntaoAssetEnum.STATION_SIGN.getCode());
        asset.setReceiver(operator);
        asset.setOperator(operator);
        asset.setOperatorRole(CuntaoAssetEnum.PARTNER.getCode());
        asset.setOperateTime(new Date());
        cuntaoAssetMapper.updateByPrimaryKeySelective(asset);
        AssetChangeEvent event = buildAssetChangeEvent(assetId, ASSET_CHECK, operator, asset.getCheckStatus());
        EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void checkAsset(Long assetId, String operator, CuntaoAssetEnum checkRole) {
        Assert.notNull(assetId, "assetId can not be null");
        Assert.notNull(operator, "operator can not be null");
        CuntaoAsset asset = new CuntaoAsset();
        asset.setModifier(operator);
        asset.setGmtModified(new Date());
        asset.setId(assetId);
        asset.setCheckStatus(CuntaoAssetEnum.CHECKED.getCode());
        asset.setCheckTime(new Date());
        asset.setCheckOperator(operator);
        asset.setCheckRole(checkRole.getCode());
        cuntaoAssetMapper.updateByPrimaryKeySelective(asset);
        AssetChangeEvent event = buildAssetChangeEvent(assetId, ASSET_SIGN, operator, asset.getStatus());
        EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void callbackAsset(Long assetId, String operator) {
        Assert.notNull(assetId, "assetId can not be null");
        Assert.notNull(operator, "operator can not be null");
        CuntaoAsset asset = cuntaoAssetMapper.selectByPrimaryKey(assetId);
        if (getBuyAssetRecord(asset.getNewStationId()) != null) {
            throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"对不起，该资产已申请自购，无法进行回收！");
        }
        asset.setStatus(CuntaoAssetEnum.COUNTY_SIGN.getCode());
        asset.setModifier(operator);
        asset.setGmtModified(new Date());
        asset.setStationId(null);
        asset.setNewStationId(null);
        asset.setPartnerInstanceId(null);
        cuntaoAssetMapper.updateByPrimaryKey(asset);
        AssetChangeEvent event = buildAssetChangeEvent(assetId, ASSET_SIGN, operator, asset.getStatus());
        EventDispatcherUtil.dispatch(EventConstant.ASSET_CHANGE_EVENT, event);
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
        PageDto<String> result = PageDtoUtil.success(page, page.getResult());
        return result;
    }

    @Override
    public void checkingAssetBatch(List<Long> assetIds, String operator) {
        Assert.notNull(assetIds);
        Assert.notNull(operator);
        CuntaoAsset record = new CuntaoAsset();
        record.setCheckStatus(CuntaoAssetEnum.CHECKING.getCode());
        record.setOperator(operator);
        CuntaoAssetExample example = new CuntaoAssetExample();
        example.createCriteria().andIdIn(assetIds);
        this.cuntaoAssetMapper.updateByExampleSelective(record, example);
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
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"can not find biz by serialNoOrAliNo[" + serialNoOrAliNo + "]");
        }
        return convert2CuntaoAssetDto(assets.iterator().next());
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
        Long stationId = assetDto.getNewStationId();
        if (stationId == null || assetDto.getOperator() == null) {
            result.put("message", "操作信息不能为空!");
            result.put("success", "false");
        }
        if (!diamondConfiguredProperties.getCanBuyStationList().contains(stationId)) {
            result.put("message", "该村点不允许提交资产采购意向,请联系资产管理员!");
            result.put("success", "false");
        }
        if (getBuyAssetRecord(stationId) != null) {
            result.put("message", "该村点已经提交过资产采购意向!");
            result.put("success", "false");
        }
        if (!validateStationAssetNum(stationId)) {
            result.put("message", "对不起,该村点不符合采购资格,名下资产须为1台电视,1台显示器,1台主机时方可提交采购!");
            result.put("success", "false");
        }
        if ("true".equals(result.get("success"))) {
            CuntaoTransferBailDto bailDto = buildBailDto(assetDto);
            ResultModel<Boolean> resultModel = newBailService.transferUserBail(bailDto);
            if (resultModel.isSuccess() && resultModel.getResult()) {
                saveBuyRecord(assetDto);
                result.put("success", "true");
            } else {
                result.put("message", resultModel.getMessage());
                logger.warn("{bizType},{parameter} buy asset fail " + resultModel.getMessage(), "assetWarn",
                    JSON.toJSONString(bailDto));
            }

        }
        return result;
    }

    private CuntaoTransferBailDto buildBailDto(CuntaoAssetDto assetDto) {
        CuntaoTransferBailDto bailDto = new CuntaoTransferBailDto();
        bailDto.setInAccountUserId(inAccountUserId);
        bailDto.setOutAccountUserId(Long.valueOf(assetDto.getOperator()));
        bailDto.setUserTypeEnum(UserTypeEnum.PARTNER);
        //bailDto.setAmount(1500*100L);
        bailDto.setAmount(15L);
        bailDto.setSource("org");
        bailDto.setReason("buyAsset");
        bailDto.setBailOperateTypeEnum(BailOperateTypeEnum.ACTIVE_TRANSFER);
        bailDto.setOutOrderId("buy_asset"+assetDto.getNewStationId());
        return bailDto;
    }

    private boolean validateStationAssetNum(Long stationId) {
        AssetQueryCondition condition = new AssetQueryCondition();
        condition.setStationId(stationId);
        condition.setPageNum(1);
        condition.setPageSize(10);
        PageDto<CuntaoAssetDto> pageDto = queryByPage(condition);
        if (pageDto.getTotal() != 3L) {
            return false;
        }
        List<String> categoryList = pageDto.getItems().stream().map(CuntaoAssetDto::getCategory).collect(
            Collectors.toList());
        return categoryList.contains("主机") && categoryList.contains("电视机") && categoryList.contains("显示器");
    }

    private void saveBuyRecord(CuntaoAssetDto assetDto) {
        CuntaoFlowRecord record = new CuntaoFlowRecord();
        record.setTargetId(assetDto.getNewStationId());
        record.setTargetType(CuntaoFlowRecordTargetTypeEnum.ASSET_BUY.getCode());
        record.setNodeTitle(CuntaoFlowRecordTargetTypeEnum.ASSET_BUY.getDesc());
        record.setOperateTime(new Date());
        record.setOperatorWorkid(assetDto.getOperator());
        record.setOperatorName(assetDto.getOperator());
        cuntaoFlowRecordBO.addRecord(record);
    }
}
