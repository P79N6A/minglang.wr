package com.taobao.cun.auge.station.service.impl;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.IdCardUtil;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.FrozenMoneyAmountConfig;
import com.taobao.cun.auge.dal.domain.*;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;
import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.*;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;
import com.taobao.cun.auge.station.convert.*;
import com.taobao.cun.auge.station.dto.*;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.cun.auge.station.service.NewRevenueCommunicationService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.interfaces.PartnerInstanceLevelDataQueryService;
import com.taobao.cun.auge.station.util.PartnerInstanceStateEnumUtil;
import com.taobao.cun.auge.station.util.PartnerInstanceTypeEnumUtil;
import com.taobao.cun.auge.station.validate.StationValidator;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.testuser.TestUserService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.hsf.util.RequestCtxUtil;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.util.RandomUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Service("partnerInstanceQueryService")
@HSFProvider(serviceInterface = PartnerInstanceQueryService.class, clientTimeout = 7000)
public class PartnerInstanceQueryServiceImpl implements PartnerInstanceQueryService {


    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceQueryService.class);
    private static final String LEVEL_CACHE_PRE = "CUN_TP_LEVEL_";

    @Autowired
    PartnerStationRelExtMapper partnerStationRelExtMapper;

    @Autowired
    StationBO stationBO;

    @Autowired
    PartnerBO partnerBO;

    @Autowired
    ProtocolBO protocolBO;

    @Autowired
    AttachmentService criusAttachmentService;

    @Autowired
    AccountMoneyBO accountMoneyBO;

    @Autowired
    PartnerProtocolRelBO partnerProtocolRelBO;

    @Autowired
    PartnerInstanceBO partnerInstanceBO;

    @Autowired
    PartnerLifecycleBO partnerLifecycleBO;

    @Autowired
    CloseStationApplyBO closeStationApplyBO;

    @Autowired
    QuitStationApplyBO quitStationApplyBO;

    @Autowired
    PartnerInstanceLevelBO partnerInstanceLevelBO;

    @Autowired
    PartnerInstanceLevelDataQueryService partnerInstanceLevelDataQueryService;

    @Autowired
    CountyStationBO countyStationBO;

    @Autowired
    TairCache tairCache;

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private StoreReadBO storeReadBO;

    private List<ProcessedStationStatusExecutor> processedStationStatusExecutorList;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Autowired
    private FrozenMoneyAmountConfig frozenMoneyConfig;

    @Autowired
    private StationTransInfoBO stationTransInfoBO;

    @Autowired
    private NewRevenueCommunicationService newRevenueCommunicationService;

    private boolean isC2BTestUser(Long taobaoUserId) {
        return testUserService.isTestUser(taobaoUserId, "c2b", true);
    }

    @Override
    public PartnerInstanceDto queryInfo(Long stationId, OperatorDto operator) {
        ValidateUtils.notNull(stationId);
        Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);

        PartnerInstanceCondition condition = new PartnerInstanceCondition();
        condition.setInstanceId(instanceId);
        condition.setNeedPartnerInfo(Boolean.TRUE);
        condition.setNeedStationInfo(Boolean.TRUE);
        condition.setNeedDesensitization(Boolean.TRUE);
        condition.setNeedPartnerLevelInfo(Boolean.TRUE);
        condition.copyOperatorDto(operator);

        return queryInfo(condition);
    }

    @Override
    public PartnerInstanceDto queryLastClosePartnerInstance(Long stationId) {
        ValidateUtils.notNull(stationId);
        PartnerStationRel psRel = partnerInstanceBO.findLastClosePartnerInstance(stationId);

        return PartnerInstanceConverter.convert(psRel);
    }

    @Override
    public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) {
        // 参数校验
        BeanValidator.validateWithThrowable(condition);
        PartnerStationRel psRel = partnerInstanceBO.findPartnerInstanceById(condition.getInstanceId());
        Assert.notNull(psRel, "partner instace not exists");

        // 获得生命周期数据
        PartnerLifecycleDto lifecycleDto = PartnerLifecycleConverter
                .toPartnerLifecycleDto(getLifecycleItem(psRel.getId(), psRel.getState()));
        PartnerInstanceDto insDto = PartnerInstanceConverter.convert(psRel);
        insDto.setPartnerLifecycleDto(lifecycleDto);
        if (!InstanceTypeEnum.LX.getCode().equals(psRel.getType()) && !InstanceTypeEnum.UM.getCode().equals(psRel.getType())) {
            insDto.setStationApplyState(
                    PartnerLifecycleRuleParser.parseStationApplyState(psRel.getType(), psRel.getState(), lifecycleDto));

        }

        if (null != condition.getNeedPartnerInfo() && condition.getNeedPartnerInfo()) {
            Partner partner = partnerBO.getPartnerById(insDto.getPartnerId());
            PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
            if (condition.getNeedDesensitization()) {
                setSafedInfo(partnerDto);
            }
            partnerDto.setAttachments(
                    criusAttachmentService.getAttachmentList(partner.getId(), AttachmentBizTypeEnum.PARTNER));
            insDto.setPartnerDto(partnerDto);
        }

        if (null != condition.getNeedStationInfo() && condition.getNeedStationInfo()) {
            Station station = stationBO.getStationById(insDto.getStationId());
            StationDto stationDto = StationConverter.toStationDto(station);
            stationDto.setAttachments(
                    criusAttachmentService.getAttachmentList(stationDto.getId(), AttachmentBizTypeEnum.CRIUS_STATION));
            insDto.setStationDto(stationDto);

            CountyStation countyStation = countyStationBO.getCountyStationByOrgId(stationDto.getApplyOrg());
            stationDto.setCountyStationName(countyStation.getName());
        }

        if (null != condition.getNeedPartnerLevelInfo() && condition.getNeedPartnerLevelInfo()) {
            PartnerInstanceLevel level = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(
                    insDto.getId());
            PartnerInstanceLevelDto partnerInstanceLevelDto = PartnerInstanceLevelConverter.toPartnerInstanceLevelDto(
                    level);
            insDto.setPartnerInstanceLevel(partnerInstanceLevelDto);
        }
        buildStoreInfo(insDto);
        //buildInspectionInfo(insDto);
        return insDto;
    }

    @Override
    public List<PartnerInstanceDto> queryPartnerInstances(Long stationId) {
        ValidateUtils.notNull(stationId);
        List<PartnerStationRel> psRels = partnerInstanceBO.findPartnerInstances(stationId);

        return PartnerInstanceConverter.convertRel2Dto(psRels);
    }

    @Override
    public boolean isAllPartnerQuit(Long stationId) {
        ValidateUtils.notNull(stationId);
        return partnerInstanceBO.isAllPartnerQuit(stationId);
    }

    @Override
    public boolean isOtherPartnerQuit(Long instanceId) {
        ValidateUtils.notNull(instanceId);
        return partnerInstanceBO.isOtherPartnerQuit(instanceId);
    }

    private String getErrorMessage(String methodName, String param, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("PartnerInstanceQueryService-Error|").append(methodName).append("(.param=").append(param).append(").")
                .append("errorMessage:").append(error);
        return sb.toString();
    }

    private void setSafedInfo(PartnerDto partnerDto) {
        if (partnerDto != null) {
            if (StringUtils.isNotBlank(partnerDto.getAlipayAccount())) {
                partnerDto.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(partnerDto.getAlipayAccount()));
            }
            if (StringUtils.isNotBlank(partnerDto.getName())) {
                partnerDto.setName(
                        SensitiveDataUtil.customizeHide(partnerDto.getName(), 0, partnerDto.getName().length() - 1, 1));
            }
            if (StringUtil.isNotBlank(partnerDto.getIdenNum())) {
                partnerDto.setIdenNum(IdCardUtil.idCardNoHide(partnerDto.getIdenNum()));
            }
            if (StringUtil.isNotBlank(partnerDto.getTaobaoNick())) {
                partnerDto.setTaobaoNick(SensitiveDataUtil.taobaoNickHide(partnerDto.getTaobaoNick()));
            }
        }
    }

    @Override
    public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition) {
        // 参数校验
        BeanValidator.validateWithThrowable(pageCondition);

        StationApplyStateEnum stationApplyState = pageCondition.getStationApplyState();

        // 先从partner_station_rel，partner，station,cuntao_org查询基本信息
        PartnerInstanceExample example = PartnerInstanceConverter.convert(pageCondition);
        PageHelper.startPage(pageCondition.getPageNum(), pageCondition.getPageSize());
        Page<PartnerInstance> page = partnerStationRelExtMapper.selectPartnerInstancesByExample(example);
        // ALL，组装生命周期中数据
        if (null == stationApplyState) {
            buildLifecycleItems(page);
        }
        PageDto<PartnerInstanceDto> success = PageDtoUtil.success(page, PartnerInstanceConverter.convert(page));
        buildStoreInfo(success.getItems());
        buildNameRuleFlag(success.getItems());
        return success;
    }

    @Override
    public PageDto<PartnerInstanceDto> queryByPage(StationCondition stationCondition) {
        ValidateUtils.validateParam(stationCondition);

        StationExtExample stationExtExample = StationExtExampleConverter.convert(stationCondition);

        PageHelper.startPage(stationCondition.getPageStart(), stationCondition.getPageSize());
        Page<PartnerInstance> page = partnerStationRelExtMapper.selectPartnerInstancesByStationExample(
                stationExtExample);

        PageDto<PartnerInstanceDto> success = PageDtoUtil.success(page, PartnerInstanceConverter.convert(page));
        buildStoreInfo(success.getItems());
        return success;
    }

    private void buildStoreInfo(List<PartnerInstanceDto> partnerInstances) {
        if (CollectionUtils.isNotEmpty(partnerInstances)) {
            for (PartnerInstanceDto instance : partnerInstances) {
                if (instance.getStationId() != null) {
                    StoreDto storeDto = storeReadBO.getStoreDtoByStationId(instance.getStationId());
                    if (storeDto != null) {
                        instance.getStationDto().setStoreDto(storeDto);
                    }
                }
            }
        }
    }

    private void buildStoreInfo(PartnerInstanceDto partnerInstance) {
        if (partnerInstance != null && partnerInstance.getStationId() != null) {
            StoreDto storeDto = storeReadBO.getStoreDtoByStationId(partnerInstance.getStationId());
            if (storeDto != null) {
                partnerInstance.getStationDto().setStoreDto(storeDto);
            }
        }
    }

    private void buildNameRuleFlag(List<PartnerInstanceDto> partnerInstances) {
        if (CollectionUtils.isNotEmpty(partnerInstances)) {
            List<String> stationNameSuffix = diamondConfiguredProperties.getStationNameSuffix();
            for (PartnerInstanceDto instance : partnerInstances) {
                boolean isRule = false;
                try {
                    if (!PartnerInstanceStateEnum.getStateForCanUpdateStationName().contains(
                            instance.getState().getCode())) {
                        instance.getStationDto().setInvalidNameMsg("");
                        continue;
                    }
                    //如果名称已经正确了。后缀带有标准的字样，就不带后缀校验
                    String checkName = instance.getStationDto().getName();
                    String headName = "";
                    for (String rs : stationNameSuffix) {
                        if (checkName.lastIndexOf(rs) >= 0) {
                            isRule = true;
                            headName = checkName.substring(0, checkName.lastIndexOf(rs));
                            break;
                        }
                    }
                    if (isRule && StationValidator.nameFormatCheck(headName)) {
                        if ((StationModeEnum.V4.getCode().equals(instance.getMode()) && checkName.lastIndexOf("天猫优品服务站")
                                >= 0) ||
                                (!StationModeEnum.V4.getCode().equals(instance.getMode()) && checkName.lastIndexOf(
                                        "农村淘宝服务站") >= 0)) {
                            instance.getStationDto().setInvalidNameMsg("");
                        }
                    } else {
                        //符合规格的没有异常标识，否则信息塞入DTO供前台使用
                        instance.getStationDto().setInvalidNameMsg("not rule name");
                    }
                } catch (AugeBusinessException e) {
                    instance.getStationDto().setInvalidNameMsg(e.getMessage());
                    ;
                }
            }
        }
    }

    private void buildLifecycleItems(Page<PartnerInstance> page) {
        for (PartnerInstance instance : page) {
            PartnerLifecycleItems lifecycle = getLifecycleItem(instance.getId(), instance.getState());
            if (null != lifecycle) {
                instance.setBusinessType(lifecycle.getBusinessType());
                instance.setSettledProtocol(lifecycle.getSettledProtocol());
                instance.setBond(lifecycle.getBond());
                instance.setQuitProtocol(lifecycle.getQuitProtocol());
                instance.setLogisticsApprove(lifecycle.getLogisticsApprove());
                instance.setCurrentStep(lifecycle.getCurrentStep());
                instance.setRoleApprove(lifecycle.getRoleApprove());
                instance.setConfirm(lifecycle.getConfirm());
                instance.setSystem(lifecycle.getSystem());
            }
        }
    }

    private PartnerLifecycleItems getLifecycleItem(Long id, String state) {
        if (PartnerInstanceStateEnum.SETTLING.getCode().equals(state)) {
            return partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.SETTLING);
        } else if (PartnerInstanceStateEnum.CLOSING.getCode().equals(state)) {
            return partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.CLOSING);
        } else if (PartnerInstanceStateEnum.QUITING.getCode().equals(state)) {
            return partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.QUITING);
        } else if (PartnerInstanceStateEnum.DECORATING.getCode().equals(state)) {
            return partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.DECORATING);
        }
        return null;
    }

    @Override
    public Long getPartnerInstanceIdByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        return partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);
    }

    @Override
    public PartnerInstanceDto getActivePartnerInstance(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            return null;
        }
        PartnerInstanceDto instance = PartnerInstanceConverter.convert(rel);

        // 获得生命周期数据
        PartnerLifecycleDto lifecycleDto = PartnerLifecycleConverter
                .toPartnerLifecycleDto(getLifecycleItem(rel.getId(), rel.getState()));
        instance.setPartnerLifecycleDto(lifecycleDto);

        Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
        PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
        instance.setPartnerDto(partnerDto);

        Station station = stationBO.getStationById(instance.getStationId());
        StationDto stationDto = StationConverter.toStationDto(station);
        if (stationDto.getStationType() != null) {
            StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationDto.getId());
            if (storeDto != null) {
                stationDto.setStoreDto(storeDto);
            }
        }
        instance.setStationDto(stationDto);

        return instance;
    }

    @Override
    public List<PartnerInstanceDto> getBatchActivePartnerInstance(List<Long> taobaoUserId,
                                                                  List<PartnerInstanceTypeEnum> instanceTypes,
                                                                  List<PartnerInstanceStateEnum> states) {
        List<PartnerStationRel> rels = partnerInstanceBO.getBatchActivePartnerInstance(taobaoUserId,
                PartnerInstanceTypeEnumUtil.extractCode(instanceTypes), PartnerInstanceStateEnumUtil.extractCode(states));
        List<PartnerInstanceDto> instances = PartnerInstanceConverter.convertRel2Dto(rels);
        return instances;
    }

    @Override
    public AccountMoneyDto getAccountMoney(Long taobaoUserId, AccountMoneyTypeEnum type) {
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            return null;
        }
        return accountMoneyBO.getAccountMoney(type, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, rel.getId());
    }

    @Override
    public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) {
        return closeStationApplyBO.getCloseStationApply(partnerInstanceId);
    }

    @Override
    public CloseStationApplyDto getCloseStationApplyById(Long applyId) {
        return closeStationApplyBO.getCloseStationApplyById(applyId);
    }

    @Override
    public ProtocolSigningInfoDto getProtocolSigningInfo(Long taobaoUserId, ProtocolTypeEnum type) {
        ProtocolSigningInfoDto info = new ProtocolSigningInfoDto();
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            logger.info("no active partner instance for user : {}", taobaoUserId);
            return null;
        }
        PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
        condition.setInstanceId(rel.getId());
        condition.setOperator(String.valueOf(taobaoUserId));
        condition.setOperatorType(OperatorTypeEnum.HAVANA);
        PartnerInstanceDto instance = queryInfo(condition);
        ProtocolDto protocol = null;
        if (this.isC2BTestUser(taobaoUserId) && ProtocolTypeEnum.SETTLE_PRO.equals(type)) {
            protocol = protocolBO.getValidProtocol(ProtocolTypeEnum.C2B_SETTLE_PRO);
        } else {
            protocol = protocolBO.getValidProtocol(type);
        }
        info.setPartnerInstance(instance);
        info.setProtocol(protocol);

        if (null == instance || null == protocol) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "instance or protocol is null");
        }
        // 走入驻生命周期表
        if (ProtocolTypeEnum.SETTLE_PRO.equals(type)) {
            PartnerLifecycleItems lifecycleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
                    PartnerLifecycleBusinessTypeEnum.SETTLING);

            // 合伙人当前不状态不为入驻中，或不存在入驻生命周期record
            if (!PartnerInstanceStateEnum.SETTLING.equals(instance.getState()) || null == lifecycleItems) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
                        "当前合伙人的状态不允许开展该业务");
            }
            PartnerLifecycleSettledProtocolEnum itemState = PartnerLifecycleSettledProtocolEnum
                    .valueof(lifecycleItems.getSettledProtocol());
            if (null == itemState) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,
                        "invalid settle protocol in lifecycle_items");
            }
            info.setHasSigned(PartnerLifecycleSettledProtocolEnum.SIGNED.equals(itemState) ? true : false);
        } else if (ProtocolTypeEnum.MANAGE_PRO.equals(type)) {
            // 管理协议不走生命周期，随时可以签
            if (!PartnerInstanceStateEnum.unReSettlableStatusCodeList().contains(instance.getState().getCode())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
                        "当前合伙人的状态不允许开展该业务");
            }
            PartnerProtocolRelDto dto = partnerProtocolRelBO.getPartnerProtocolRelDto(type, instance.getId(),
                    PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
            info.setHasSigned(null == dto ? false : true);
        }
        return info;
    }

    @Override
    public BondFreezingInfoDto getBondFreezingInfoDto(Long taobaoUserId) {
        BondFreezingInfoDto info = new BondFreezingInfoDto();
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            logger.info("no active partner instance for user : {}", taobaoUserId);
            return null;
        }
        if (PartnerInstanceStateEnum.SETTLING.getCode().equals(rel.getState())) {//冻结入驻保证金
            PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
            condition.setInstanceId(rel.getId());
            condition.setOperator(String.valueOf(taobaoUserId));
            condition.setOperatorType(OperatorTypeEnum.HAVANA);
            PartnerInstanceDto instance = queryInfo(condition);
            AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                    AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
            PartnerProtocolRelDto settleProtocol = null;
            settleProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.C2B_SETTLE_PRO,
                    instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
            if (settleProtocol == null) {
                settleProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.SETTLE_PRO,
                        instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
            }
            if (null == instance || null == bondMoney || null == settleProtocol || null == settleProtocol
                    .getConfirmTime()) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,
                        "bond money or settle protocol not exist");
            }
            info.setPartnerInstance(instance);
            info.setAcountMoney(bondMoney);
            info.setProtocolConfirmTime(settleProtocol.getConfirmTime());
            if (AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
                info.setHasFrozen(false);
            } else if (AccountMoneyStateEnum.HAS_FROZEN.equals(bondMoney.getState())) {
                info.setHasFrozen(true);
            } else {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,
                        "invalid account_money state");
            }
        }
        //else if ((PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) ) {//补货金冻结金额
        // getReplenishMoney(taobaoUserId, info, rel);
        //  }

        return info;
    }

    private void getReplenishMoney(Long taobaoUserId, BondFreezingInfoDto info,
                                   PartnerStationRel rel) {
        PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
        condition.setInstanceId(rel.getId());
        condition.setOperator(String.valueOf(taobaoUserId));
        condition.setOperatorType(OperatorTypeEnum.HAVANA);
        PartnerInstanceDto instance = queryInfo(condition);
        AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
        if (null == instance || null == bondMoney) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "replenish_money not exist");
        }
        info.setPartnerInstance(instance);
        info.setAcountMoney(bondMoney);
        if (AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
            info.setHasFrozen(false);
        } else if (AccountMoneyStateEnum.HAS_FROZEN.equals(bondMoney.getState())) {
            info.setHasFrozen(true);
        } else {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "invalid account_money state");
        }
    }

    @Override
    public PartnerProtocolRelDto getProtocolRel(Long objectId, PartnerProtocolRelTargetTypeEnum targetType,
                                                ProtocolTypeEnum type) {
        return partnerProtocolRelBO.getPartnerProtocolRelDto(type, objectId, targetType);
    }

    @Override
    public QuitStationApplyDto getQuitStationApply(Long instanceId) {
        ValidateUtils.notNull(instanceId);
        return QuitStationApplyConverter.tQuitStationApplyDto(quitStationApplyBO.findQuitStationApply(instanceId));
    }

    @Override
    public QuitStationApplyDto getQuitStationApplyById(Long applyId) {
        ValidateUtils.notNull(applyId);
        return QuitStationApplyConverter.tQuitStationApplyDto(quitStationApplyBO.getQuitStationApplyById(applyId));
    }

    @Override
    public PartnerInstanceLevelDto getPartnerInstanceLevel(Long taobaoUserId) {
        Assert.notNull(taobaoUserId);
        String cacheKey = LEVEL_CACHE_PRE + taobaoUserId;
        PartnerInstanceLevelDto dto = (PartnerInstanceLevelDto) tairCache.get(cacheKey);
        if (null != dto) {
            // 防止缓存击穿
            if (null == dto.getTaobaoUserId() || null == dto.getCurrentLevel()) {
                return null;
            }
            return dto;
        }
        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == instance || !PartnerInstanceTypeEnum.TP.getCode().equals(instance.getType())) {
            putLevelToCache(cacheKey, new PartnerInstanceLevelDto(), 300);
            return null;
        }
        PartnerInstanceLevel level = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(
                instance.getId());
        if (null == level) {
            if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())) {
                logger.error("PartnerInstaceLevel not exists: " + taobaoUserId);
            }
            putLevelToCache(cacheKey, new PartnerInstanceLevelDto(), 300);
            return null;
        }
        dto = PartnerInstanceLevelConverter.toPartnerInstanceLevelDtoWithoutId(level);
        //防止缓存雪崩
        int expireTime = 60 * 60 * 1 + RandomUtil.getInt(1, 100);
        putLevelToCache(cacheKey, dto, expireTime);
        return dto;
    }

    private void putLevelToCache(String cacheKey, PartnerInstanceLevelDto partnerInstanceLevelDto, int i) {
        tairCache.put(cacheKey, partnerInstanceLevelDto, 300);
    }

    @Override
    public PartnerInstanceLevelGrowthDto getPartnerInstanceLevelGrowthData(Long taobaoUserId) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthData(taobaoUserId);
    }

    @Override
    public List<PartnerInstanceLevelGrowthTrendDto> getPartnerInstanceLevelGrowthTrendData(Long taobaoUserId,
                                                                                           String statDate) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthTrendData(taobaoUserId, statDate);
    }

    @Override
    public StationStatisticDto getStationStatistics(StationStatisticsCondition condition) {
        if (condition.getBizTypeEnum() != null) {
            if (condition.getBizTypeEnum().equals(StationBizTypeEnum.STATION)) {
                condition.setModeIsNull("y");
                condition.setCategoryIsNull("y");
            }
            if (condition.getBizTypeEnum().equals(StationBizTypeEnum.YOUPIN)) {
                condition.setMode(StationModeEnum.V4.getCode());
                condition.setCategoryIsNull("y");
            }
            if (condition.getBizTypeEnum().equals(StationBizTypeEnum.YOUPIN_ELEC)) {
                condition.setMode(StationModeEnum.V4.getCode());
                condition.setCategory(TPCategoryEnum.ELEC.getCode());
            }

        }

        if (null == processedStationStatusExecutorList) {
            initProcessedStationStatusExecutorList();
        }
        List<ProcessedStationStatus> whole = new CopyOnWriteArrayList<ProcessedStationStatus>();
        processedStationStatusExecutorList.parallelStream().forEach(executor -> {
            try {
                whole.addAll(executor.execute(condition));
            } catch (Exception e) {
                logger.error("{parameter}", condition, e);
            }
        });
        //
        //		List<ProcessedStationStatus> processingList = partnerStationRelExtMapper.countProcessingStatus
        // (condition);
        //		List<ProcessedStationStatus> processedList = partnerStationRelExtMapper.countProcessedStatus
        // (condition);
        //		List<ProcessedStationStatus> courseList = partnerStationRelExtMapper.countCourseStatus(condition);
        //		List<ProcessedStationStatus> decorateList = partnerStationRelExtMapper.countDecorateStatus(condition);
        //		List<ProcessedStationStatus> whole = new ArrayList<ProcessedStationStatus>();
        //		whole.addAll(processingList);
        //		whole.addAll(processedList);
        //		whole.addAll(courseList);
        //		whole.addAll(decorateList);
        StationStatisticDto statusDtoList = ProcessedStationStatusConverter.toProcessedStationStatusDtos(whole);
        return statusDtoList;
    }

    private synchronized void initProcessedStationStatusExecutorList() {
        if (null != processedStationStatusExecutorList) {
            return;
        }
        processedStationStatusExecutorList = Lists.newArrayList();
        processedStationStatusExecutorList.add(c -> partnerStationRelExtMapper.countProcessingStatus(c));
        processedStationStatusExecutorList.add(c -> partnerStationRelExtMapper.countProcessedStatus(c));
        processedStationStatusExecutorList.add(c -> partnerStationRelExtMapper.countCourseStatus(c));
        processedStationStatusExecutorList.add(c -> partnerStationRelExtMapper.countDecorateStatus(c));
    }

    @Override
    public PartnerInstanceDto getCurrentPartnerInstanceByPartnerId(Long partnerId) {
        ValidateUtils.notNull(partnerId);
        return partnerInstanceBO.getCurrentPartnerInstanceByPartnerId(partnerId);
    }

    @Override
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByPartnerId(Long partnerId) {
        ValidateUtils.notNull(partnerId);
        return partnerInstanceBO.getHistoryPartnerInstanceByPartnerId(partnerId);
    }

    /**
     * 查询服务站，当前实例
     *
     * @param stationId
     * @return
     */
    @Override
    public PartnerInstanceDto getCurrentPartnerInstanceByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);

        PartnerInstanceCondition condition = new PartnerInstanceCondition();
        condition.setInstanceId(instanceId);
        condition.setNeedPartnerInfo(Boolean.TRUE);
        condition.setNeedStationInfo(Boolean.TRUE);
        condition.setNeedDesensitization(Boolean.TRUE);
        condition.setNeedPartnerLevelInfo(Boolean.TRUE);
        condition.copyOperatorDto(OperatorDto.defaultOperator());

        return queryInfo(condition);
    }

    @Override
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        return partnerInstanceBO.getHistoryPartnerInstanceByStationId(stationId);
    }

    @Override
    public PartnerInstanceLevelGrowthDtoV2 getPartnerInstanceLevelGrowthDataV2(Long taobaoUserId) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthDataV2(taobaoUserId);
    }

    @Override
    public List<PartnerInstanceLevelGrowthTrendDtoV2> getPartnerInstanceLevelGrowthTrendDataV2(Long taobaoUserId,
                                                                                               String statDate) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthTrendDataV2(taobaoUserId, statDate);
    }

    @Override
    public List<PartnerInstanceDto> queryByPartnerInstanceIds(List<Long> partnerInstanceIds) {
        if (CollectionUtils.isEmpty(partnerInstanceIds)) {
            return Collections.<PartnerInstanceDto>emptyList();
        }
        List<PartnerInstance> instances = partnerStationRelExtMapper.selectPartnerInstancesByIds(partnerInstanceIds);

        List<PartnerInstanceDto> success = PartnerInstanceConverter.convert(instances);
        return success;
    }

    @Override
    public Long getCurStationIdByTaobaoUserId(Long taobaoUserId) {
        PartnerStationRel instance = partnerInstanceBO.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
        return null != instance ? instance.getStationId() : null;
    }

    @Override
    public List<PartnerInstanceDto> queryByTaobaoUserIds(
            List<Long> taobaoUserIds) {
        if (CollectionUtils.isEmpty(taobaoUserIds)) {
            return Collections.<PartnerInstanceDto>emptyList();
        }
        List<PartnerInstance> instances = partnerStationRelExtMapper.selectPartnerInstancesByTaobaoUserIds(
                taobaoUserIds);

        List<PartnerInstanceDto> success = PartnerInstanceConverter.convert(instances);
        return success;
    }

    @Override
    public List<PartnerInstanceDto> queryTpaPartnerInstances(Long parentStationId) {
        List<PartnerInstanceDto> instances = Lists.newArrayList();
        List<PartnerStationRel> psRels = this.partnerInstanceBO.queryTpaPartnerInstances(parentStationId);
        for (PartnerStationRel instance : psRels) {
            Station station = stationBO.getStationById(instance.getStationId());
            Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(instance, station, partner);
            instances.add(partnerInstanceDto);
        }
        return instances;
    }

    @Override
    public List<PartnerInstanceDto> queryTpaPartnerInstances(Long parentStationId, PartnerInstanceStateEnum state) {
        List<PartnerInstanceDto> instances = Lists.newArrayList();
        List<PartnerStationRel> psRels = this.partnerInstanceBO.queryTpaPartnerInstances(parentStationId, state);
        for (PartnerStationRel instance : psRels) {
            Station station = stationBO.getStationById(instance.getStationId());
            Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(instance, station, partner);
            instances.add(partnerInstanceDto);
        }
        return instances;
    }

    @Override
    public ReplenishDto getReplenishDtoByTaobaoUserId(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (rel == null) {
            String error = getErrorMessage("getInfoByTaobaoUserId", String.valueOf(taobaoUserId), "rel is null");
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, error);
        }
        if (!StationModeEnum.V4.getCode().equals(rel.getMode())) {
            return null;
        }
        PartnerLifecycleItems decoItems = partnerLifecycleBO.getLifecycleItems(rel.getId(),
                PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
        if (null == decoItems) {
            decoItems = partnerLifecycleBO.getLifecycleItems(rel.getId(),
                    PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.END);
        }
        if (null == decoItems) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "当前合伙人的状态不允许开展该业务");
        }
        ReplenishDto rDto = new ReplenishDto();
        rDto.setPartnerInstanceId(rel.getId());
        rDto.setStationId(rel.getStationId());
        rDto.setTaobaoUserId(taobaoUserId);

        if (decoItems.getReplenishMoney() == null || PartnerLifecycleReplenishMoneyEnum.WAIT_FROZEN.getCode().equals(
                decoItems.getReplenishMoney())) {
            rDto.setStatus(ReplenishStatusEnum.WAIT_FROZEN);
            rDto.setOrderUrl(diamondConfiguredProperties.getReplenishFrozenUrl());
        }
        if (PartnerLifecycleReplenishMoneyEnum.HAS_FROZEN.getCode().equals(decoItems.getReplenishMoney())) {
            rDto.setStatus(ReplenishStatusEnum.HAS_FROZEN);
            rDto.setOrderUrl(diamondConfiguredProperties.getReplenishOrderUrl());
        }
        if (PartnerLifecycleGoodsReceiptEnum.Y.getCode().equals(decoItems.getGoodsReceipt())) {
            rDto.setStatus(ReplenishStatusEnum.GOODS_RECEIVE);
        }
        return rDto;
    }

    //获取升级4.0村站待补交基础保证金金额
    @Override
    public BondFreezingInfoDto getBondFreezingInfoForTrans(Long taobaoUserId) {
        BondFreezingInfoDto info = new BondFreezingInfoDto();
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            logger.info("no active partner instance for user : {}", taobaoUserId);
            return null;
        }
        PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
        condition.setInstanceId(rel.getId());
        condition.setOperator(String.valueOf(taobaoUserId));
        condition.setOperatorType(OperatorTypeEnum.HAVANA);
        PartnerInstanceDto instance = queryInfo(condition);
        AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
        if (null == instance || null == bondMoney) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "PARTNER_BOND not exist");
        }
        PartnerProtocolRelDto settleProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(
                ProtocolTypeEnum.C2B_SETTLE_PRO,
                instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
        if (settleProtocol != null) {
            info.setProtocolConfirmTime(settleProtocol.getConfirmTime());
        }
        info.setPartnerInstance(instance);
        //基础保证金额度-已经缴纳=剩余4.0需要代缴。目前基础额度是10000
        BigDecimal backBailMoney = new BigDecimal(Double.toString(0.0));
        BigDecimal baseMoney = new BigDecimal(Double.toString(frozenMoneyConfig.getTPFrozenMoneyAmount()));
        backBailMoney = baseMoney.subtract(bondMoney.getMoney());
        bondMoney.setMoney(backBailMoney);
        info.setAcountMoney(bondMoney);
        if (AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
            info.setHasFrozen(false);
        } else if (AccountMoneyStateEnum.HAS_FROZEN.equals(bondMoney.getState())) {
            info.setHasFrozen(true);
        } else {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "invalid account_money state");
        }
        return info;
    }

    private void logRefactorMethod() {
        try {
            logger.error("应用名为：" + RequestCtxUtil.getAppNameOfClient() + " 调用了重构过渡接口");
        } catch (Exception e) {
            logger.error("重构过渡接口被调用");
        }
    }

    @Override
    public StationBizTypeEnum getBizTypeByInstanceId(Long instanceId) {
        ValidateUtils.notNull(instanceId);
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (null == rel) {
            return null;
        }
        if (StationBizTypeEnum.TPA.getCode().equals(rel.getType())) {
            return StationBizTypeEnum.TPA;
        } else if (StationBizTypeEnum.UM.getCode().equals(rel.getType())) {
            return StationBizTypeEnum.UM;
        }

        Station station = stationBO.getStationById(rel.getStationId());
        if (null == station) {
            return null;
        }
        if ("TP".equals(rel.getType())) {
            if (StationModeEnum.V4.getCode().equals(rel.getMode())) {
                return "ELEC".equals(station.getCategory()) ? StationBizTypeEnum.YOUPIN_ELEC
                        : StationBizTypeEnum.YOUPIN;
            } else {
                return StationBizTypeEnum.STATION;
            }
        } else if ("TPS".equals(rel.getType())) {
            return StationBizTypeEnum.TPS_ELEC;
        }
        return null;
    }

    @Override
    public StationBizTypeEnum getBizTypeByTaobaoUserId(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            return null;
        }
        return getBizTypeByInstanceId(rel.getId());
    }

    @Override
    public StationTransInfoTypeEnum getWaitConfirmTransInfoTypeByTaobaoUserId(Long taobaoUserId) {
        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (instance != null && PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState()) &&
                PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode().equals(instance.getTransStatus())) {
            StationTransInfo lastTransInfo = stationTransInfoBO.getLastTransInfoByStationId(instance.getStationId());
            return StationTransInfoTypeEnum.getTransInfoTypeEnumByCode(lastTransInfo.getType());
        }
        return null;
    }

    @Override
    public PartnerInstanceRevenueStatusEnum getWaitConfirmRevenueTransInfoTypeByTaobaoUserId(Long taobaoUserId) {
        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (instance != null) {
            NewRevenueCommunicationDto newRevenueCommunicationDto = newRevenueCommunicationService.getProcessApprovePassNewRevenueCommunication(NewRevenueCommunicationBusinessTypeEnum.REVENUE_INVITE.getCode(), instance.getId().toString());
            if (newRevenueCommunicationDto != null && StringUtils.isBlank(instance.getIncomeMode())) {
                return PartnerInstanceRevenueStatusEnum.WAIT_REVENUE_TRANS;
            }
        }
        return null;
    }

    @Override
    public StationTransHandOverDto getStationTransHandOverInfoByTaobaoUserId(Long taobaoUserId) {

        StationTransHandOverDto stationTransHandOverDto = new StationTransHandOverDto();
        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        //获取转型类型及信息
        if (instance != null && PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState()) &&
                PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode().equals(instance.getTransStatus())) {
            StationTransInfo lastTransInfo = stationTransInfoBO.getLastTransInfoByStationId(instance.getStationId());
            stationTransHandOverDto.setStationTransInfoTypeEnum(StationTransInfoTypeEnum.getTransInfoTypeEnumByCode(lastTransInfo.getType()));
            if (lastTransInfo != null) {
                stationTransHandOverDto.setStationTransInfoDto(toStationTransInfoDto(lastTransInfo));
            }
        }
        //获取收入切换类型及信息
        if (instance != null) {
            NewRevenueCommunicationDto newRevenueCommunicationDto = newRevenueCommunicationService.getProcessApprovePassNewRevenueCommunication(NewRevenueCommunicationBusinessTypeEnum.REVENUE_INVITE.getCode(), instance.getId().toString());
            if (newRevenueCommunicationDto != null) {
                stationTransHandOverDto.setPartnerInstanceRevenueStatusEnum(PartnerInstanceRevenueStatusEnum.WAIT_REVENUE_TRANS);
                stationTransHandOverDto.setStationRevenueTransInfoDto(toStationRevenueTransInfoDto(instance, newRevenueCommunicationDto));
            }
        }
        return stationTransHandOverDto;
    }

    @Override
    public String getStationTransHandOverTypeInfoByTaobaoUserId(Long taobaoUserId) {

        StationTransHandOverTypeInfoDto stationTransHandOverTypeInfoDto = new StationTransHandOverTypeInfoDto();
        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (instance != null) {
            NewRevenueCommunicationDto newRevenueCommunicationDto = newRevenueCommunicationService.getProcessApprovePassNewRevenueCommunication(NewRevenueCommunicationBusinessTypeEnum.REVENUE_INVITE.getCode(), instance.getId().toString());
            if (newRevenueCommunicationDto != null) {
                stationTransHandOverTypeInfoDto.setStationTransHandOverTypeEnum(StationTransHandOverTypeEnum.REVENUE_HAND_OVER);
                stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.WAIT_TRANS);
            } else {
                NewRevenueCommunicationDto finishNewRevenueCommunicationDto = newRevenueCommunicationService.getFinishApprovePassNewRevenueCommunication(NewRevenueCommunicationBusinessTypeEnum.REVENUE_INVITE.getCode(), instance.getId().toString());
                if (finishNewRevenueCommunicationDto != null) {
                    stationTransHandOverTypeInfoDto.setStationTransHandOverTypeEnum(StationTransHandOverTypeEnum.REVENUE_HAND_OVER);
                    stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.FINISH);
                }
                if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState()) &&
                        PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode().equals(instance.getTransStatus())) {
                    StationTransInfo lastTransInfo = stationTransInfoBO.getLastTransInfoByStationId(instance.getStationId());
                    if (lastTransInfo != null) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverTypeEnum(StationTransHandOverTypeEnum.valueof(lastTransInfo.getType()));
                        stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.WAIT_TRANS);
                    }

                    BondFreezingInfoDto freezingInfo = this.getBondFreezingInfoForTrans(taobaoUserId);
                    if (null == instance || null == freezingInfo) {
                        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "PARTNER_BOND not exist");
                    }
                    PartnerProtocolRelDto settleProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(
                            ProtocolTypeEnum.C2B_SETTLE_PRO,
                            instance.getId(), PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
                    if (settleProtocol != null && freezingInfo.getAcountMoney().getMoney().doubleValue()  > 0) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.WAIT_FREZON);
                    }

                } else {
                    StationTransInfo lastTransInfo = stationTransInfoBO.getLastTransInfoByStationId(instance.getStationId());
                    if (lastTransInfo != null) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverTypeEnum(StationTransHandOverTypeEnum.valueof(lastTransInfo.getType()));
                    }
                    PartnerLifecycleItems lifecycleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
                            PartnerLifecycleBusinessTypeEnum.DECORATING);
                    if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState()) && lifecycleItems != null && "N".equals(lifecycleItems.getDecorateStatus())) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.WAIT_DECOTATION);
                    }

                    if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState()) && lifecycleItems != null && "Y".equals(lifecycleItems.getDecorateStatus())) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.WAIT_OPEN);
                    }

                    if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())) {
                        stationTransHandOverTypeInfoDto.setStationTransHandOverNodeEnum(StationTransHandOverNodeEnum.FINISH);
                    }
                }

            }

        }

        Map<String, String> result = new HashMap<String, String>();
        if (stationTransHandOverTypeInfoDto.getStationTransHandOverTypeEnum() != null) {
            result.put("stationTransHandOverType", stationTransHandOverTypeInfoDto.getStationTransHandOverTypeEnum().getCode());
        }
        if (stationTransHandOverTypeInfoDto.getStationTransHandOverNodeEnum() != null) {
            result.put("stationTransHandOverNode", stationTransHandOverTypeInfoDto.getStationTransHandOverNodeEnum().getCode());
        }

        String stringJson = JSON.toJSONString(result);

        return stringJson;
    }

    @Override
    public InstanceDto getActiveInstance(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        if (null == rel) {
            return null;
        }
        InstanceDto instance = PartnerInstanceConverter.convertToInstanceDto(rel);

        // 获得生命周期数据
        PartnerLifecycleDto lifecycleDto = PartnerLifecycleConverter
                .toPartnerLifecycleDto(getLifecycleItem(rel.getId(), rel.getState()));
        instance.setPartnerLifecycleDto(lifecycleDto);

        Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
        PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
        instance.setPartnerDto(partnerDto);

        Station station = stationBO.getStationById(instance.getStationId());
        StationDto stationDto = StationConverter.toStationDto(station);
        if (stationDto.getStationType() != null) {
            StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationDto.getId());
            if (storeDto != null) {
                stationDto.setStoreDto(storeDto);
            }
        }
        instance.setStationDto(stationDto);

        return instance;
    }

    private StationTransInfoDto toStationTransInfoDto(StationTransInfo stationTransInfo) {
        StationTransInfoDto stationTransInfoDto = new StationTransInfoDto();
        stationTransInfoDto.setId(stationTransInfo.getId());
        stationTransInfoDto.setFromBizType(stationTransInfo.getFromBizType());
        stationTransInfoDto.setToBizType(stationTransInfo.getToBizType());
        stationTransInfoDto.setStationId(stationTransInfo.getStationId());
        stationTransInfoDto.setStatus(stationTransInfo.getStatus());
        stationTransInfoDto.setTaobaoUserId(stationTransInfo.getTaobaoUserId());
        stationTransInfoDto.setTransDate(stationTransInfo.getTransDate());
        stationTransInfoDto.setType(stationTransInfo.getType());
        stationTransInfoDto.setIsLatest(stationTransInfo.getIsLatest());
        stationTransInfoDto.setIsModifyLnglat(stationTransInfo.getIsModifyLnglat());
        stationTransInfoDto.setNewOpenDate(stationTransInfo.getNewOpenDate());
        stationTransInfoDto.setOldOpenDate(stationTransInfo.getOldOpenDate());
        stationTransInfoDto.setOperateTime(stationTransInfo.getOperateTime());
        stationTransInfoDto.setOperator(stationTransInfo.getOperator());
        stationTransInfoDto.setOperateTime(stationTransInfo.getOperateTime());
        stationTransInfoDto.setOperatorType(stationTransInfo.getOperatorType());
        stationTransInfoDto.setRemark(stationTransInfo.getRemark());
        return stationTransInfoDto;
    }

    private StationRevenueTransInfoDto toStationRevenueTransInfoDto(PartnerStationRel instance, NewRevenueCommunicationDto newRevenueCommunicationDto) {
        StationRevenueTransInfoDto stationRevenueTransInfoDto = new StationRevenueTransInfoDto();
        stationRevenueTransInfoDto.setTaobaoUserId(instance.getTaobaoUserId().toString());
        stationRevenueTransInfoDto.setIncomeMode(instance.getIncomeMode());
        stationRevenueTransInfoDto.setIncomeModeBeginTime(instance.getIncomeModeBeginTime());
        stationRevenueTransInfoDto.setStationId(instance.getStationId());
        stationRevenueTransInfoDto.setStatus(PartnerInstanceRevenueStatusEnum.WAIT_REVENUE_TRANS.getCode());
        stationRevenueTransInfoDto.setType(instance.getType());
        stationRevenueTransInfoDto.setOperateTime(newRevenueCommunicationDto.getCommuTime());
        stationRevenueTransInfoDto.setOperator(newRevenueCommunicationDto.getOperator());
        stationRevenueTransInfoDto.setRemark(newRevenueCommunicationDto.getCommuContent());
        return stationRevenueTransInfoDto;
    }
}
