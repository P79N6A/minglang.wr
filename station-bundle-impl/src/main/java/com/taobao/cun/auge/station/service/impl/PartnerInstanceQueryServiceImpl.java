package com.taobao.cun.auge.station.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.alibaba.common.lang.StringUtil;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;

import com.ali.com.google.common.collect.Lists;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.cache.TairCache;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.IdCardUtil;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.ProcessedStationStatus;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.example.PartnerInstanceExample;
import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.condition.StationStatisticsCondition;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelConverter;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.convert.ProcessedStationStatusConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationExtExampleConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.BondFreezingInfoDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthDtoV2;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelGrowthTrendDtoV2;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.dto.ProtocolSigningInfoDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.ReplenishDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationStatisticDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleGoodsReceiptEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleReplenishMoneyEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.ReplenishStatusEnum;
import com.taobao.cun.auge.station.enums.StationApplyStateEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.service.interfaces.PartnerInstanceLevelDataQueryService;
import com.taobao.cun.auge.station.util.PartnerInstanceStateEnumUtil;
import com.taobao.cun.auge.station.util.PartnerInstanceTypeEnumUtil;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.testuser.TestUserService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.util.RandomUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

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
    PartnerInstanceHandler partnerInstanceHandler;

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
        insDto.setStationApplyState(PartnerLifecycleRuleParser.parseStationApplyState(psRel.getType(), psRel.getState(), lifecycleDto));

        if (null != condition.getNeedPartnerInfo() && condition.getNeedPartnerInfo()) {
            Partner partner = partnerBO.getPartnerById(insDto.getPartnerId());
            PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
            if (condition.getNeedDesensitization()) {
                setSafedInfo(partnerDto);
            }
            partnerDto.setAttachments(criusAttachmentService.getAttachmentList(partner.getId(), AttachmentBizTypeEnum.PARTNER));
            insDto.setPartnerDto(partnerDto);
        }

        if (null != condition.getNeedStationInfo() && condition.getNeedStationInfo()) {
            Station station = stationBO.getStationById(insDto.getStationId());
            StationDto stationDto = StationConverter.toStationDto(station);
            stationDto.setAttachments(criusAttachmentService.getAttachmentList(stationDto.getId(), AttachmentBizTypeEnum.CRIUS_STATION));
            insDto.setStationDto(stationDto);

            CountyStation countyStation = countyStationBO.getCountyStationByOrgId(stationDto.getApplyOrg());
            stationDto.setCountyStationName(countyStation.getName());
        }

        if (null != condition.getNeedPartnerLevelInfo() && condition.getNeedPartnerLevelInfo()) {
            PartnerInstanceLevel level = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(insDto.getId());
            PartnerInstanceLevelDto partnerInstanceLevelDto = PartnerInstanceLevelConverter.toPartnerInstanceLevelDto(level);
            insDto.setPartnerInstanceLevel(partnerInstanceLevelDto);
        }
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
                partnerDto.setName(SensitiveDataUtil.customizeHide(partnerDto.getName(), 0, partnerDto.getName().length() - 1, 1));
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
        return success;
    }

    @Override
    public PageDto<PartnerInstanceDto> queryByPage(StationCondition stationCondition) {
        ValidateUtils.validateParam(stationCondition);

        StationExtExample stationExtExample = StationExtExampleConverter.convert(stationCondition);

        PageHelper.startPage(stationCondition.getPageStart(), stationCondition.getPageSize());
        Page<PartnerInstance> page = partnerStationRelExtMapper.selectPartnerInstancesByStationExample(stationExtExample);

        PageDto<PartnerInstanceDto> success = PageDtoUtil.success(page, PartnerInstanceConverter.convert(page));
        return success;
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
    public Long getPartnerInstanceId(Long stationApplyId) {
        ValidateUtils.notNull(stationApplyId);
        return partnerInstanceBO.getInstanceIdByStationApplyId(stationApplyId);
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
        if(stationDto.getStationType()!=null){
        	if(stationDto.isStore()){
        		StoreDto storeDto = storeReadBO.getStoreDtoByStationId(stationDto.getId());
        		stationDto.setStoreDto(storeDto);
        	}
        }
        instance.setStationDto(stationDto);

        return instance;
    }

    @Override
    public List<PartnerInstanceDto> getBatchActivePartnerInstance(List<Long> taobaoUserId, List<PartnerInstanceTypeEnum> instanceTypes, List<PartnerInstanceStateEnum> states) {
        List<PartnerStationRel> rels = partnerInstanceBO.getBatchActivePartnerInstance(taobaoUserId, PartnerInstanceTypeEnumUtil.extractCode(instanceTypes), PartnerInstanceStateEnumUtil.extractCode(states));
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
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前合伙人的状态不允许开展该业务");
            }
            PartnerLifecycleSettledProtocolEnum itemState = PartnerLifecycleSettledProtocolEnum
                    .valueof(lifecycleItems.getSettledProtocol());
            if (null == itemState) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "invalid settle protocol in lifecycle_items");
            }
            info.setHasSigned(PartnerLifecycleSettledProtocolEnum.SIGNED.equals(itemState) ? true : false);
        } else if (ProtocolTypeEnum.MANAGE_PRO.equals(type)) {
            // 管理协议不走生命周期，随时可以签
            if (!PartnerInstanceStateEnum.unReSettlableStatusCodeList().contains(instance.getState().getCode())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "当前合伙人的状态不允许开展该业务");
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
        if (null == rel ) {
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
             if (null == instance || null == bondMoney || null == settleProtocol || null == settleProtocol.getConfirmTime()) {
                 throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "bond money or settle protocol not exist");
             }
             info.setPartnerInstance(instance);
             info.setAcountMoney(bondMoney);
             info.setProtocolConfirmTime(settleProtocol.getConfirmTime());
             if (AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
                 info.setHasFrozen(false);
             } else if (AccountMoneyStateEnum.HAS_FROZEN.equals(bondMoney.getState())) {
                 info.setHasFrozen(true);
             } else {
                 throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "invalid account_money state");
             }
        }else if ((PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) ) {//补货金冻结金额
	       	 getReplenishMoney(taobaoUserId, info, rel);
        }
       
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
    public Long getStationApplyId(Long instanceId) {
        ValidateUtils.notNull(instanceId);
        return partnerInstanceBO.findStationApplyId(instanceId);
    }

    @Override
    public PartnerProtocolRelDto getProtocolRel(Long objectId, PartnerProtocolRelTargetTypeEnum targetType, ProtocolTypeEnum type) {
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
        PartnerInstanceLevel level = partnerInstanceLevelBO.getPartnerInstanceLevelByPartnerInstanceId(instance.getId());
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
    public List<PartnerInstanceLevelGrowthTrendDto> getPartnerInstanceLevelGrowthTrendData(Long taobaoUserId, String statDate) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthTrendData(taobaoUserId, statDate);
    }

    @Override
    public StationStatisticDto getStationStatistics(StationStatisticsCondition condition) {
        if (null == processedStationStatusExecutorList) {
            initProcessedStationStatusExecutorList();
        }
        List<ProcessedStationStatus> whole = new CopyOnWriteArrayList<ProcessedStationStatus>();
        processedStationStatusExecutorList.parallelStream().forEach(executor -> {
            try {
                whole.addAll(executor.execute(condition));
            }catch (Exception e) {
                logger.error("{parameter}", condition, e);
            }
        });
//
//		List<ProcessedStationStatus> processingList = partnerStationRelExtMapper.countProcessingStatus(condition);
//		List<ProcessedStationStatus> processedList = partnerStationRelExtMapper.countProcessedStatus(condition);
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
    public List<PartnerInstanceLevelGrowthTrendDtoV2> getPartnerInstanceLevelGrowthTrendDataV2(Long taobaoUserId, String statDate) {
        return partnerInstanceLevelDataQueryService.getPartnerInstanceLevelGrowthTrendDataV2(taobaoUserId, statDate);
    }

    @Override
    public Long findStationApplyIdByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        return partnerInstanceBO.findStationApplyIdByStationId(stationId);
    }

    @Override
    public Long findStationIdByStationApplyId(Long stationApplyId) {
        ValidateUtils.notNull(stationApplyId);
        return partnerInstanceBO.findStationIdByStationApplyId(stationApplyId);
    }

    @Override
    public List<PartnerInstanceDto> queryByStationApplyIds(List<Long> stationApplyIds) {
        if (CollectionUtils.isEmpty(stationApplyIds)) {
            return Collections.<PartnerInstanceDto>emptyList();
        }
        List<PartnerInstance> instances = partnerStationRelExtMapper.selectPartnerInstancesByStationApplyIds(stationApplyIds);

        List<PartnerInstanceDto> success = PartnerInstanceConverter.convert(instances);
        return success;
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
        List<PartnerInstance> instances = partnerStationRelExtMapper.selectPartnerInstancesByTaobaoUserIds(taobaoUserIds);

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
    public List<PartnerInstanceDto> queryTpaPartnerInstances(Long parentStationId,PartnerInstanceStateEnum state) {
        List<PartnerInstanceDto> instances = Lists.newArrayList();
        List<PartnerStationRel> psRels = this.partnerInstanceBO.queryTpaPartnerInstances(parentStationId,state);
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
		PartnerStationRel  rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (rel == null) {
			String error = getErrorMessage("getInfoByTaobaoUserId",String.valueOf(taobaoUserId), "rel is null");
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,error);
		}
		if (!StationModeEnum.V4.getCode().equals(rel.getMode())) {
			return null;
		}
		PartnerLifecycleItems decoItems = partnerLifecycleBO.getLifecycleItems(rel.getId(),
		        PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (null == decoItems ) {
			decoItems = partnerLifecycleBO.getLifecycleItems(rel.getId(),
			        PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.END);
        }
	  if (null == decoItems ) {
          throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"当前合伙人的状态不允许开展该业务");
      }
	  ReplenishDto rDto =new ReplenishDto();
	  rDto.setPartnerInstanceId(rel.getId());
	  rDto.setStationId(rel.getStationId());
	  rDto.setTaobaoUserId(taobaoUserId);
	  
	  if (PartnerLifecycleReplenishMoneyEnum.WAIT_FROZEN.getCode().equals(decoItems.getReplenishMoney())) {
		  rDto.setStatus(ReplenishStatusEnum.WAIT_FROZEN);
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
}
