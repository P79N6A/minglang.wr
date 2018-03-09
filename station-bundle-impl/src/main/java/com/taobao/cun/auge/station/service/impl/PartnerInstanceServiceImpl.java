package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;

import com.ali.com.google.common.collect.Maps;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.FrozenMoneyAmountConfig;
import com.taobao.cun.auge.configuration.MailConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.ChangeTPEvent;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceLevelChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.PartnerInstanceTypeChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.flowRecord.dto.CuntaoFlowRecordDto;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.flowRecord.service.CuntaoFlowRecordQueryService;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.org.dto.CuntaoUser;
import com.taobao.cun.auge.org.dto.CuntaoUserRole;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.bo.CuntaoFlowRecordBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.PartnerTypeChangeApplyBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.check.PartnerInstanceChecker;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelEventConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.BatchMailDto;
import com.taobao.cun.auge.station.dto.CancelUpgradePartnerInstance;
import com.taobao.cun.auge.station.dto.ChangeTPDto;
import com.taobao.cun.auge.station.dto.ConfirmCloseDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.FreezeBondDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDegradeDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceThrawSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceUpgradeDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.PartnerTypeChangeApplyDto;
import com.taobao.cun.auge.station.dto.PartnerUpdateServicingDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.dto.StationUpdateServicingDto;
import com.taobao.cun.auge.station.dto.SyncModifyBelongTPForTpaDto;
import com.taobao.cun.auge.station.dto.SyncModifyLngLatDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecyclePositionConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleReplenishMoneyEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerInstanceExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.station.notify.listener.ProcessProcessor;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.cun.auge.station.service.CaiNiaoService;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;
import com.taobao.cun.auge.station.util.PartnerInstanceEventUtil;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.station.validate.StationValidator;
import com.taobao.cun.auge.testuser.TestUserService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.auge.user.service.UserRole;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.settle.bail.dto.CuntaoBailDetailDto;
import com.taobao.cun.settle.bail.dto.CuntaoBailDetailQueryDto;
import com.taobao.cun.settle.bail.dto.CuntaoBailDetailReturnDto;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.PagedResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 合伙人实例服务接口
 *
 * @author quanzhu.wangqz
 */
@Service("partnerInstanceService")
@HSFProvider(serviceInterface = PartnerInstanceService.class,clientTimeout=8000)
public class PartnerInstanceServiceImpl implements PartnerInstanceService {

    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceService.class);

    private static final int DEFAULT_EVALUATE_INTERVAL = 6;

    @Autowired
    PartnerProtocolRelBO partnerProtocolRelBO;
    @Autowired
    PartnerInstanceBO partnerInstanceBO;
    @Autowired
    PartnerInstanceHandler partnerInstanceHandler;
    @Autowired
    PartnerLifecycleBO partnerLifecycleBO;
    @Autowired
    StationBO stationBO;
    @Autowired
    QuitStationApplyBO quitStationApplyBO;
    @Autowired
    Emp360Adapter emp360Adapter;
    @Autowired
    UicReadAdapter uicReadAdapter;
    @Autowired
    PartnerBO partnerBO;
    @Autowired
    TradeAdapter tradeAdapter;
    @Autowired
    AttachmentService criusAttachmentService;
    @Autowired
    PaymentAccountQueryAdapter paymentAccountQueryAdapter;
    @Autowired
    AccountMoneyBO accountMoneyBO;
    @Autowired
    CloseStationApplyBO closeStationApplyBO;
    @Autowired
    GeneralTaskSubmitService generalTaskSubmitService;
    @Autowired
    AppResourceService appResourceService;
    @Autowired
    StationApplySyncBO syncStationApplyBO;

    @Autowired
    PartnerInstanceExtService partnerInstanceExtService;

    @Autowired
    PartnerInstanceExtBO partnerInstanceExtBO;
    @Autowired
    PartnerInstanceLevelBO partnerInstanceLevelBO;

    @Autowired
    CuntaoFlowRecordBO cuntaoFlowRecordBO;

    @Autowired
    CaiNiaoService caiNiaoService;

    @Autowired
    PartnerPeixunBO partnerPeixunBO;

    @Autowired
    StationDecorateBO stationDecorateBO;

    @Autowired
    CountyStationBO countyStationBO;
    @Autowired
    CuntaoUserService cuntaoUserService;
    @Autowired
    CuntaoUserRoleService cuntaoUserRoleService;

    @Autowired
    PartnerInstanceChecker partnerInstanceChecker;

    @Autowired
    PartnerTypeChangeApplyBO partnerTypeChangeApplyBO;

    @Autowired
    private TestUserService testUserService;

    @Autowired
    private CuntaoFlowRecordQueryService cuntaoFlowRecordQueryService;

    @Autowired
    private FrozenMoneyAmountConfig frozenMoneyConfig;

    @Autowired
    private MailConfiguredProperties mailConfiguredProperties;
    
    @Autowired
    private EnhancedUserQueryService enhancedUserQueryService;

    @Autowired
	private StateMachineService stateMachineService;
    
    @Autowired
	private StationNumConfigBO stationNumConfigBO;
    
    @Autowired
    private ProcessProcessor processProcessor;
    
    @Autowired
    private CuntaoNewBailService newBailService;
    
    @Autowired
    LifeCycleValidator lifeCycleValidator;
    
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long addTemp(PartnerInstanceDto partnerInstanceDto){
        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"deprecated method, call applySettle() instead");
        /*
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		try {
			setTempCommonInfo(partnerInstanceDto);
			Long instanceId = addCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.ADD, instanceId);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("addTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		*/
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long updateTemp(PartnerInstanceDto partnerInstanceDto){
        throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"deprecated method, call updateSettle() instead");
		/*
		ValidateUtils.validateParam(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getStationDto());
		ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
		ValidateUtils.notNull(partnerInstanceDto.getId());
		try {
			setTempCommonInfo(partnerInstanceDto);
			Long instanceId = partnerInstanceDto.getId();
			updateCommon(partnerInstanceDto);
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
			return instanceId;
		} catch (AugeServiceException augeException) {
			String error = getAugeExceptionErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto),
					augeException.toString());
			logger.error(error, augeException);
			throw augeException;
		} catch (Exception e) {
			String error = getErrorMessage("updateTemp", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
			logger.error(error, e);
			throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
		}
		*/
    }

    /**
     * 更新固点协议
     *
     * @param stationDto
     * @param stationId
     */
    private void saveStationFixProtocol(StationDto stationDto, Long stationId) {
        if (stationDto.getAreaType() != null) {
            if (StringUtils.equals(StationAreaTypeEnum.FIX_NEW.getCode(), stationDto.getAreaType().getCode())) {
                PartnerProtocolRelDeleteDto deleteDto = new PartnerProtocolRelDeleteDto();
                deleteDto.setObjectId(stationId);
                deleteDto.setTargetType(PartnerProtocolRelTargetTypeEnum.CRIUS_STATION);
                List<ProtocolTypeEnum> fixProList = new ArrayList<ProtocolTypeEnum>();
                fixProList.add(ProtocolTypeEnum.GOV_FIXED);
                fixProList.add(ProtocolTypeEnum.TRIPARTITE_FIXED);
                deleteDto.copyOperatorDto(stationDto);
                deleteDto.setProtocolTypeList(fixProList);

                partnerProtocolRelBO.deletePartnerProtocolRel(deleteDto);
                PartnerProtocolRelDto fixPro = stationDto.getFixedProtocols();

                if (fixPro != null) {
                    fixPro.copyOperatorDto(stationDto);
                    fixPro.setObjectId(stationId);
                    partnerProtocolRelBO.addPartnerProtocolRel(fixPro);
                }
            }
        }
    }

    private void checkStationNumDuplicate(Long stationId, String newStationNum) {
        // 判断服务站编号是否使用中
        String oldStationNum = null;
        if (stationId != null) {
            Station oldStation = stationBO.getStationById(stationId);
            oldStationNum = oldStation.getStationNum();
        }
        if (!StringUtils.equals(oldStationNum, newStationNum)) {
            int count = stationBO.getStationCountByStationNum(newStationNum);
            if (count > 0) {
                throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE,"服务站编号重复");
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateByPartner(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto){
        ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
        PartnerValidator.validateParnterUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getPartnerDto());
        StationValidator.validateStationUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getStationDto());
        Long stationId = partnerInstanceUpdateServicingDto.getStationDto().getStationId();
        ValidateUtils.notNull(stationId);

            Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);
            partnerInstanceUpdateServicingDto.setId(instanceId);
            updateInternal(partnerInstanceUpdateServicingDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void update(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto){
        ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
        ValidateUtils.notNull(partnerInstanceUpdateServicingDto.getId());
        PartnerValidator.validateParnterCanUpdateInfo(partnerInstanceUpdateServicingDto.getPartnerDto());
        StationValidator.validateStationCanUpdateInfo(partnerInstanceUpdateServicingDto.getStationDto());
            updateInternal(partnerInstanceUpdateServicingDto);

            // 修改子成员配额
            Integer childNum = partnerInstanceUpdateServicingDto.getChildNum();
            if (null != childNum) {
                PartnerChildMaxNumUpdateDto updateDto = new PartnerChildMaxNumUpdateDto();
                updateDto.setInstanceId(partnerInstanceUpdateServicingDto.getId());
                updateDto.setMaxChildNum(childNum);
                updateDto.setReason(PartnerMaxChildNumChangeReasonEnum.EDIT);
                updateDto.copyOperatorDto(partnerInstanceUpdateServicingDto);

                partnerInstanceExtService.updatePartnerMaxChildNum(updateDto);
            }
    }

    private void updateInternal(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) {
        Long partnerInstanceId = partnerInstanceUpdateServicingDto.getId();
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
        Long stationId = rel.getStationId();
        Long partnerId = rel.getPartnerId();

        if (partnerInstanceUpdateServicingDto.getPartnerDto() != null) {
            updatePartnerForServicing(partnerInstanceUpdateServicingDto, partnerId);
        }
        if (partnerInstanceUpdateServicingDto.getStationDto() != null) {
            updateStationForServicing(partnerInstanceUpdateServicingDto, stationId);
        }
        // 同步station_apply
        syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstanceId);

        if (isNeedToUpdateCainiaoStation(rel.getState())) {
            generalTaskSubmitService.submitUpdateCainiaoStation(partnerInstanceId, partnerInstanceUpdateServicingDto.getOperator());
        }
    }

    private boolean isNeedToUpdateCainiaoStation(String state) {
        return PartnerInstanceStateEnum.DECORATING.getCode().equals(state) || PartnerInstanceStateEnum.SERVICING.getCode().equals(state)
                || PartnerInstanceStateEnum.CLOSING.getCode().equals(state);
    }

    private void updateStationForServicing(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto, Long stationId) {
        StationUpdateServicingDto sDto = partnerInstanceUpdateServicingDto.getStationDto();

        StationDto stationDto = new StationDto();
        // 判断服务站编号是否使用中
        if (StringUtil.isNotBlank(sDto.getStationNum())) {
            checkStationNumDuplicate(stationId, sDto.getStationNum());
            stationDto.setStationNum(sDto.getStationNum());
        }

        stationDto.setAddress(sDto.getAddress());
        stationDto.setAreaType(sDto.getAreaType());
        stationDto.setCovered(sDto.getCovered());
        stationDto.setDescription(sDto.getDescription());
        stationDto.setFeature(sDto.getFeature());
        stationDto.setFixedProtocols(sDto.getFixedProtocols());
        stationDto.setFixedType(sDto.getFixedType());
        stationDto.setFormat(sDto.getFormat());
        stationDto.setId(stationId);
        stationDto.setLogisticsState(sDto.getLogisticsState());
        stationDto.setManagerId(sDto.getManagerId());
        stationDto.setName(sDto.getName());
        stationDto.setProducts(sDto.getProducts());

        stationDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
        stationDto.setPartnerInstanceIsOnTown(sDto.getPartnerInstanceIsOnTown());
        stationBO.updateStation(stationDto);

        // 更新固点协议
        saveStationFixProtocol(stationDto, stationId);
        criusAttachmentService.modifyAttachementBatch(sDto.getAttachments(), stationId, AttachmentBizTypeEnum.CRIUS_STATION,
                OperatorConverter.convert(partnerInstanceUpdateServicingDto));
    }

    private void updatePartnerForServicing(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto, Long partnerId) {
        PartnerUpdateServicingDto pDto = partnerInstanceUpdateServicingDto.getPartnerDto();
        //验证手机号唯一性
        if (!partnerInstanceBO.judgeMobileUseble(null, partnerId, pDto.getMobile())) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE,"该手机号已被使用");
        }
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
        partnerDto.setId(partnerId);
        partnerDto.setMobile(pDto.getMobile());
        partnerDto.setEmail(pDto.getEmail());
        partnerDto.setBusinessType(pDto.getBusinessType());
        partnerBO.updatePartner(partnerDto);
        if(pDto.getAttachments()!=null && !pDto.getAttachments().isEmpty()){
        	  criusAttachmentService.modifyAttachementBatch(pDto.getAttachments(), partnerId, AttachmentBizTypeEnum.PARTNER,
                      OperatorConverter.convert(partnerInstanceUpdateServicingDto));
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto){
        ValidateUtils.validateParam(partnerInstanceDeleteDto);
        Long instanceId = partnerInstanceDeleteDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            if (rel == null || StringUtils.isEmpty(rel.getType())) {
                throw new NullPointerException("partner instance not exists");
            }
            partnerInstanceHandler.handleDelete(partnerInstanceDeleteDto, rel);
            // 同步删除
            syncStationApplyBO.deleteStationApply(rel.getId());
    }

    private void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version, ProtocolTypeEnum protocolTypeEnum, boolean needFrozenMoney) {
        ValidateUtils.notNull(taobaoUserId);
        ValidateUtils.notNull(waitFrozenMoney);
            PartnerStationRel psRel = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId, PartnerInstanceStateEnum.SETTLING);
            Assert.notNull(psRel, "partner instance not exists");
            Long instanceId = psRel.getId();
            PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING);
            Assert.notNull(items, "PartnerLifecycleItems not exists");

            PartnerLifecycleItemCheckResultEnum checkSettled = PartnerLifecycleRuleParser.parseExecutable(
                    PartnerInstanceTypeEnum.valueof(psRel.getType()), PartnerLifecycleItemCheckEnum.settledProtocol, items);
            if (PartnerLifecycleItemCheckResultEnum.EXECUTED == checkSettled) {
                return;
            } else if (PartnerLifecycleItemCheckResultEnum.NONEXCUTABLE == checkSettled) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前操作不满足执行条件");
            }


            partnerProtocolRelBO.signProtocol(taobaoUserId, protocolTypeEnum, instanceId,
                    PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

            if (needFrozenMoney) {
                addWaitFrozenMoney(instanceId, taobaoUserId, waitFrozenMoney);
            }

            PartnerLifecycleDto param = new PartnerLifecycleDto();
            param.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNED);
            param.setLifecycleId(items.getId());
            partnerLifecycleBO.updateLifecycle(param);

            // 乐观锁
            PartnerInstanceDto instance = new PartnerInstanceDto();
            instance.setId(instanceId);
            instance.setVersion(version);
            instance.setOperator(String.valueOf(taobaoUserId));
            instance.setOperatorType(OperatorTypeEnum.HAVANA);
            partnerInstanceBO.updatePartnerStationRel(instance);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void signC2BSettledProtocol(Long taobaoUserId, boolean signedC2BProtocol, boolean isFrozenMoney){
        PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        boolean isTestUser = isC2BTestUser(rel);
        Double frozenMoney = this.frozenMoneyConfig.getFrozenMoneyByType(rel.getType());
        Long version = rel.getVersion();
        if (!isTestUser) {
            //非测试用户走老代码
            this.signSettledProtocol(taobaoUserId, frozenMoney, version);
        } else {
            if (!signedC2BProtocol && !isSignSettleProtocol(rel.getId())) {
                signSettledProtocol(taobaoUserId, frozenMoney, version, ProtocolTypeEnum.C2B_SETTLE_PRO, !isFrozenMoney);
            } else if (!signedC2BProtocol && isSignSettleProtocol(rel.getId())) {
                partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.C2B_SETTLE_PRO, rel.getId(),
                        PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
            }
        }
    }

    private boolean isSignSettleProtocol(Long partnerInstanceId) {
        return Optional.ofNullable(partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.SETTLE_PRO, partnerInstanceId, PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE)).isPresent();
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version){
        signSettledProtocol(taobaoUserId, waitFrozenMoney, version, ProtocolTypeEnum.SETTLE_PRO, true);
    }

    private boolean isC2BTestUser(PartnerStationRel parnterInstance) {
        return testUserService.isTestUser(parnterInstance.getTaobaoUserId(), "c2b", true);
    }


    private void addWaitFrozenMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney) {
        ValidateUtils.notNull(instanceId);
        AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
        accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
        accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
        accountMoneyDto.setOperatorType(OperatorTypeEnum.HAVANA);
        accountMoneyDto.setObjectId(instanceId);
        accountMoneyDto.setState(AccountMoneyStateEnum.WAIT_FROZEN);
        accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
        accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
        accountMoneyDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);

        AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
        if (dupRecord != null) {
            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
        } else {
            accountMoneyBO.addAccountMoney(accountMoneyDto);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void signManageProtocol(Long taobaoUserId, Long version){
        ValidateUtils.notNull(taobaoUserId);
            PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            Assert.notNull(partnerStationRel, "partner instance not exists");

            partnerProtocolRelBO.signProtocol(taobaoUserId, ProtocolTypeEnum.MANAGE_PRO, partnerStationRel.getId(),
                    PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);

            // 乐观锁
            PartnerInstanceDto instance = new PartnerInstanceDto();
            instance.setId(partnerStationRel.getId());
            instance.setVersion(version);
            instance.setOperator(String.valueOf(taobaoUserId));
            instance.setOperatorType(OperatorTypeEnum.HAVANA);
            partnerInstanceBO.updatePartnerStationRel(instance);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerStationRel.getId());
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean freezeBond(Long taobaoUserId, Double frozenMoney){
        FreezeBondDto freezeBondDto = new FreezeBondDto();
        freezeBondDto.setOperatorType(OperatorTypeEnum.HAVANA);
        freezeBondDto.setOperator(String.valueOf(taobaoUserId));

        freezeBondDto.setTaobaoUserId(taobaoUserId);
        return freezeBond(freezeBondDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean freezeBond(FreezeBondDto freezeBondDto){
        ValidateUtils.validateParam(freezeBondDto);
        Long taobaoUserId = freezeBondDto.getTaobaoUserId();
        String accountNo = freezeBondDto.getAccountNo();
        String alipayAccount = freezeBondDto.getAlipayAccount();
        Double money = freezeBondDto.getMoney();

        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        
        if(PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState())) {
        	PartnerLifecycleItems settleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
                    PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.PROCESSING);
            AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                    AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
            if (!PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState()) || null == settleItems || null == bondMoney
                    || !AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前合伙人的状态不允许开展该业务");
            }
            String operator = String.valueOf(taobaoUserId);

            // 修改生命周期表
            PartnerLifecycleDto lifecycleUpdateDto = new PartnerLifecycleDto();
            lifecycleUpdateDto.setLifecycleId(settleItems.getId());
            lifecycleUpdateDto.setBond(PartnerLifecycleBondEnum.HAS_FROZEN);
            lifecycleUpdateDto.setOperator(operator);
            lifecycleUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
            partnerLifecycleBO.updateLifecycle(lifecycleUpdateDto);

            // 修改保证金冻结状态
            AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
            accountMoneyUpdateDto.setObjectId(bondMoney.getObjectId());
            accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
            accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
            accountMoneyUpdateDto.setFrozenTime(new Date());
            accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
            accountMoneyUpdateDto.setAlipayAccount(alipayAccount);
            accountMoneyUpdateDto.setAccountNo(accountNo);
            accountMoneyUpdateDto.setOperator(operator);
            accountMoneyUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instance.getId());

            Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
            generalTaskSubmitService.submitSettlingSysProcessTasks(PartnerInstanceConverter.convert(instance, null, partner), operator);
        }else if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState())&& StationModeEnum.V4.getCode().equals(instance.getMode())) {
        		return frozenReplenishMoneyForDecorate(taobaoUserId, accountNo, alipayAccount,
    					instance,money);
        }else if ( PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())&& StationModeEnum.V4.getCode().equals(instance.getMode())) {
        	   return frozenReplenishMoneyForService(taobaoUserId, accountNo, alipayAccount,
					instance,money);
        }
        // 流转日志, 合伙人入驻
        // bulidRecordEventForPartnerEnter(stationApplyDetailDto);
        return true;
    }
    
    private void addHasFrozenReplienishMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney,String alipayAccount,String accountNo) {
        ValidateUtils.notNull(instanceId);
        AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
        accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
        accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
        accountMoneyDto.setOperatorType(OperatorTypeEnum.HAVANA);
        accountMoneyDto.setObjectId(instanceId);
        accountMoneyDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
        accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
        accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
        accountMoneyDto.setType(AccountMoneyTypeEnum.REPLENISH_MONEY);
        accountMoneyDto.setFrozenTime(new Date());
        accountMoneyDto.setAlipayAccount(alipayAccount);
        accountMoneyDto.setAccountNo(accountNo);
        
        AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
        if (dupRecord != null) {
            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
        } else {
            accountMoneyBO.addAccountMoney(accountMoneyDto);
        }
    }
    
    private boolean frozenReplenishMoneyForService(Long taobaoUserId, String accountNo,
			String alipayAccount, PartnerStationRel instance,Double waitFrozenMoney) {
		String operator = String.valueOf(taobaoUserId);
		AccountMoneyDto baseMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
		        AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
		PartnerLifecycleItems decoItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
		        PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.END);
		
		if (new BigDecimal("13000.00").compareTo(baseMoney.getMoney())== 0) {//兼容老逻辑，冻结了13000 要拆分冻结10000和3000
			baseMoney.setMoney(new BigDecimal("10000.00"));
			baseMoney.setOperator(operator);
			baseMoney.setOperatorType(OperatorTypeEnum.HAVANA);
			accountMoneyBO.updateAccountMoneyByObjectId(baseMoney);
		}
		if (waitFrozenMoney == null) {
			waitFrozenMoney = this.frozenMoneyConfig.getTPReplenishMoneyAmount();
		}
		addHasFrozenReplienishMoney(instance.getId(),taobaoUserId,waitFrozenMoney,alipayAccount,accountNo);
		updateRelenishMoneyIsHasFrozen(operator, decoItems);
		return true;
	}


	private boolean frozenReplenishMoneyForDecorate(Long taobaoUserId, String accountNo,
			String alipayAccount, PartnerStationRel instance,Double waitFrozenMoney) {
		String operator = String.valueOf(taobaoUserId);
		AccountMoneyDto baseMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
		        AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
		PartnerLifecycleItems decoItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
		        PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
		
		if (new BigDecimal("10000.00").compareTo(baseMoney.getMoney())== 0) {//新增v4
			AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
			        AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
			if (null == decoItems || null == bondMoney
			        || !AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
			    return false;
			}
			
			// 修改生命周期表
			updateRelenishMoneyIsHasFrozen(operator, decoItems);

			// 修改保证金冻结状态
			AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
			accountMoneyUpdateDto.setObjectId(bondMoney.getObjectId());
			accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.REPLENISH_MONEY);
			accountMoneyUpdateDto.setFrozenTime(new Date());
			accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
			accountMoneyUpdateDto.setAlipayAccount(alipayAccount);
			accountMoneyUpdateDto.setAccountNo(accountNo);
			accountMoneyUpdateDto.setOperator(operator);
			accountMoneyUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
			accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);
		}else if (new BigDecimal("13000.00").compareTo(baseMoney.getMoney())== 0) {//兼容老逻辑，冻结了13000 要拆分冻结10000和3000
			baseMoney.setMoney(new BigDecimal("10000.00"));
			baseMoney.setOperator(operator);
			baseMoney.setOperatorType(OperatorTypeEnum.HAVANA);
			accountMoneyBO.updateAccountMoneyByObjectId(baseMoney);
			if (waitFrozenMoney == null) {
				waitFrozenMoney = this.frozenMoneyConfig.getTPReplenishMoneyAmount();
			}
			addHasFrozenReplienishMoney(instance.getId(),taobaoUserId,waitFrozenMoney,alipayAccount,accountNo);
			updateRelenishMoneyIsHasFrozen(operator, decoItems);
		}
		return true;
	}


	private void updateRelenishMoneyIsHasFrozen(String operator,
			PartnerLifecycleItems decoItems) {
		PartnerLifecycleDto lifecycleUpdateDto = new PartnerLifecycleDto();
		lifecycleUpdateDto.setLifecycleId(decoItems.getId());
		lifecycleUpdateDto.setReplenishMoney(PartnerLifecycleReplenishMoneyEnum.HAS_FROZEN);
		lifecycleUpdateDto.setOperator(operator);
		lifecycleUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
		partnerLifecycleBO.updateLifecycle(lifecycleUpdateDto);
	}

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
	public boolean openStation(OpenStationDto openStationDto) {
		// 参数校验
		BeanValidator.validateWithThrowable(openStationDto);
		Long instanceId = openStationDto.getPartnerInstanceId();
		// 检查装修中的生命周期（装修，培训）完成后，才能开业
		checkPartnerLifecycleForOpenStation(instanceId);

		if (openStationDto.isImme()) {
			/*
            // 立即开业
              PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
              if (rel == null || !PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) {
                  throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"合伙人信息不存在");
              }
              // 更新合伙人实例状态为服务中
              partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SERVICING,
                      operator);
              // 更新村点状态为服务中
              stationBO.changeState(rel.getStationId(), StationStatusEnum.DECORATING, StationStatusEnum.SERVICING, operator);
              // 更新开业时间
              partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
                      openStationDto.getOperator());
              // 同步station_apply
              syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

              // 初始化合伙人层级
              initPartnerInstanceLevel(rel);

              // 记录村点状态变化
              sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, openStationDto);
              // 开业包项目事件
              dispachToServiceEvent(openStationDto, instanceId);
           	*/
			PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
			if (rel == null || !PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) {
                 throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"合伙人信息不存在");
            }
			PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(rel);
			partnerInstanceDto.setOpenDate(openStationDto.getOpenDate());
			partnerInstanceDto.copyOperatorDto(openStationDto);
			LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.SERVICING_EVENT);
			stateMachineService.executePhase(phaseEvent);
		} else {// 定时开业
			partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
					openStationDto.getOperator());
			// 同步station_apply
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());
		}
		return true;
	}

    private void initPartnerInstanceLevel(PartnerStationRel instance) {
        PartnerInstanceLevelDto dto = new PartnerInstanceLevelDto();
        dto.setPartnerInstanceId(instance.getId());
        dto.setTaobaoUserId(instance.getTaobaoUserId());
        dto.setStationId(instance.getStationId());
        dto.setCurrentLevel(PartnerInstanceLevelEnum.S_4);
        dto.setEvaluateBy(OperatorDto.DEFAULT_OPERATOR);
        Calendar c = Calendar.getInstance();
        Date today = c.getTime();
        dto.setEvaluateDate(today);

        c.add(Calendar.MONTH, DEFAULT_EVALUATE_INTERVAL);
        int day = c.get(Calendar.DAY_OF_MONTH);
        if (day > 1) {
            c.add(Calendar.MONTH, 1);
        }
        c.set(Calendar.DAY_OF_MONTH, 1);
        dto.setNextEvaluateDate(c.getTime());
        dto.copyOperatorDto(OperatorDto.defaultOperator());
        Station station = stationBO.getStationById(instance.getStationId());
        dto.setCountyOrgId(station.getApplyOrg());
        partnerInstanceLevelBO.addPartnerInstanceLevel(dto);
    }

    /**
     * 检查装修中的生命周期（装修，培训）完成后，才能开业
     *
     * @param instanceId
     */
    private void checkPartnerLifecycleForOpenStation(Long instanceId){
        ValidateUtils.notNull(instanceId);
        PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING,
                PartnerLifecycleCurrentStepEnum.PROCESSING);
        if (items == null) {
            // 没有数据 认为是标准化项目之前的数据，直接可以开业
            return;
        }
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		//4.0 检查补货金和 开业包收货状态
		if(StationModeEnum.V4.getCode().equals(rel.getMode())) {
			if (!PartnerLifecycleReplenishMoneyEnum.HAS_FROZEN.getCode().equals(items.getReplenishMoney())) {
				 throw new AugeBusinessException(AugeErrorCodes.DECORATE_BUSINESS_CHECK_ERROR_CODE,PartnerExceptionEnum.REPLENISHMONEY_NOT_FROZEN.getDesc());
			}
		/*	if (!PartnerLifecycleGoodsReceiptEnum.Y.getCode().equals(items.getGoodsReceipt())) {
				 throw new AugeBusinessException(AugeErrorCodes.DECORATE_BUSINESS_CHECK_ERROR_CODE,PartnerExceptionEnum.GOODSRECEIPT_NOT_DONE.getDesc());
			}*/
		}
//		
//		StationDecorate decorate=stationDecorateBO.getStationDecorateByStationId(rel.getStationId());
//		if (decorate != null
//				&& StationDecoratePaymentTypeEnum.SELF.getCode().equals(
//						decorate.getPaymentType())
//				&& StationDecorateStatusEnum.UNDECORATE.getCode().equals(
//						decorate.getStatus())) {
//			throw new AugeBusinessException(AugeErrorCodes.DECORATE_BUSINESS_CHECK_ERROR_CODE,
//					PartnerExceptionEnum.PARTNER_DECORATE_NOT_PAY.getDesc());
//		}
        //装修完成才可以开业 咨询过邵毅此处代码判断装修是否完成 add in 2017-12-27 业务：（梁子、李靖）产品：艳芳
        if (!PartnerLifecycleDecorateStatusEnum.Y.getCode().equals(items.getDecorateStatus())) {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前服务站没有完成装修");
        }

        PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
        partnerLifecycleDto.setLifecycleId(items.getId());
        partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
        partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
        partnerLifecycleBO.updateLifecycle(partnerLifecycleDto);
    }

    private void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
                                                     OperatorDto operator) {
        PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
                PartnerInstanceEventConverter.convertStateChangeEvent(stateChangeEnum, piDto, operator));
    }

    private void dispachToServiceEvent(OpenStationDto openStationDto, Long instanceId) {
            PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
            PartnerInstanceStateChangeEvent partnerInstanceEvent = new PartnerInstanceStateChangeEvent();
            partnerInstanceEvent.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.format(openStationDto.getOpenDate()));
            partnerInstanceEvent.setOwnOrgId(piDto.getStationDto().getApplyOrg());
            partnerInstanceEvent.setTaobaoUserId(piDto.getTaobaoUserId());
            partnerInstanceEvent.setTaobaoNick(piDto.getPartnerDto().getTaobaoNick());
            partnerInstanceEvent.setStationId(piDto.getStationId());
            partnerInstanceEvent.setStationName(piDto.getStationDto().getStationNum());
            partnerInstanceEvent.setOperator(openStationDto.getOperator());
            EventDispatcherUtil.dispatch("STATION_TO_SERVICE_EVENT", partnerInstanceEvent);
            logger.info("dispachToServiceEvent success partnerInstanceId:" + openStationDto.getPartnerInstanceId());
    }

    @Override
    public boolean checkKyPackage() {
        // TODO:检查开业包
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseByPartner(Long taobaoUserId){
            // 参数校验
            Assert.notNull(taobaoUserId, "taobaoUserId is null");

            // 合伙人实例状态校验
            PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
                    PartnerInstanceStateEnum.SERVICING);
            Assert.notNull(partnerInstance, "没有服务中的合伙人。taobaoUserId = " + taobaoUserId);

            Long instanceId = partnerInstance.getId();
            // FIXME FHH 合伙人主动申请退出时，为什么不校验是否存在淘帮手，非要到审批时再校验
            partnerInstanceChecker.checkCloseApply(instanceId);
            
          /*  // 更新合伙人实例状态为停业中
            closingPartnerInstance(partnerInstance, PartnerInstanceCloseTypeEnum.PARTNER_QUIT, operatorDto);

            // 更新村点状态为停业中
            stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, taobaoUserIdStr);

            // 添加停业生命周期记录
            addClosingLifecycle(operatorDto, partnerInstance, PartnerInstanceCloseTypeEnum.PARTNER_QUIT);

            // 插入停业协议
            addCloseProtocol(taobaoUserId, instanceId, operatorDto);

            // 新增停业申请
            CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
            closeStationApplyDto.setPartnerInstanceId(instanceId);
            closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.PARTNER_QUIT);
            closeStationApplyDto.copyOperatorDto(operatorDto);
            closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);

            // 发送状态变化事件
            PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(
                    PartnerInstanceStateChangeEnum.START_CLOSING, partnerInstanceBO.getPartnerInstanceById(instanceId), operatorDto);
            EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);*/
            
            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(partnerInstance);
            partnerInstanceDto.setOperator(taobaoUserId+"");
            partnerInstanceDto.setOperatorType(OperatorTypeEnum.HAVANA);
            partnerInstanceDto.setCloseType(PartnerInstanceCloseTypeEnum.PARTNER_QUIT);
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.CLOSING_EVENT);
			stateMachineService.executePhase(phaseEvent);
    }

    /**
     * 添加停业协议
     *
     * @param taobaoUserId
     * @param instanceId
     * @param operatorDto
     */
    private void addCloseProtocol(Long taobaoUserId, Long instanceId, OperatorDto operatorDto) {
        PartnerProtocolRelDto proRelDto = new PartnerProtocolRelDto();
        Date quitProDate = new Date();
        proRelDto.setObjectId(instanceId);
        proRelDto.setProtocolTypeEnum(ProtocolTypeEnum.PARTNER_QUIT_PRO);
        proRelDto.setConfirmTime(quitProDate);
        proRelDto.setStartTime(quitProDate);
        proRelDto.setTaobaoUserId(taobaoUserId);
        proRelDto.setTargetType(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
        proRelDto.copyOperatorDto(operatorDto);
        partnerProtocolRelBO.addPartnerProtocolRel(proRelDto);
    }

    /**
     * 合伙人实例 停业中
     *
     * @param partnerInstance
     * @param closeType
     * @param operatorDto
     */
    private void closingPartnerInstance(PartnerStationRel partnerInstance, PartnerInstanceCloseTypeEnum closeType,
                                        OperatorDto operatorDto) {
        PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();

        partnerInstanceDto.setId(partnerInstance.getId());
        partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSING);
        partnerInstanceDto.setCloseType(closeType);
        partnerInstanceDto.copyOperatorDto(operatorDto);
        partnerInstanceDto.setVersion(partnerInstance.getVersion());
        partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
    }

    /**
     * 村点 停业中
     *
     * @param operatorDto
     * @param stationId
     * @param instanceStateChange
     */
    private void closingStation(Long stationId, PartnerInstanceStateChangeEnum instanceStateChange, OperatorDto operatorDto) {
        if (PartnerInstanceStateChangeEnum.DECORATING_CLOSING.equals(instanceStateChange)) {
            stationBO.changeState(stationId, StationStatusEnum.DECORATING, StationStatusEnum.CLOSING, operatorDto.getOperator());
        } else if (PartnerInstanceStateChangeEnum.START_CLOSING.equals(instanceStateChange)) {
            stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSING, operatorDto.getOperator());
        } else {
            throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"partner state is not decorating or servicing.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void confirmClose(ConfirmCloseDto confirmCloseDto)  {
            // 参数校验
            BeanValidator.validateWithThrowable(confirmCloseDto);

            Long instanceId = confirmCloseDto.getPartnerInstanceId();
            //String employeeId = confirmCloseDto.getOperator();
            Boolean isAgree = confirmCloseDto.isAgree();
            PartnerStationRel partnerInstance = partnerInstanceBO.findPartnerInstanceById(instanceId);
            PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(instanceId,
                    PartnerLifecycleBusinessTypeEnum.CLOSING);

            if (!PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstance.getState()) || null == partnerLifecycleItem) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"没有停业申请中的合伙人。ConfirmCloseDto = " + JSON.toJSONString(confirmCloseDto));
            }


            PartnerLifecycleItemCheckResultEnum confirmExecutable = PartnerLifecycleRuleParser.parseExecutable(
                    PartnerInstanceTypeEnum.valueof(partnerInstance.getType()), PartnerLifecycleItemCheckEnum.confirm,
                    partnerLifecycleItem);
            if (!PartnerLifecycleItemCheckResultEnum.EXECUTABLE.equals(confirmExecutable)) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.PARTNER_INSTANCE_ITEM_UNEXECUTABLE.getDesc());
            }
           // PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
           // partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
           // partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
          //  partnerLifecycle.copyOperatorDto(confirmCloseDto);

            if (isAgree) {
                // 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
                //PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerInstance.getType());
                //partnerInstanceHandler.validateClosePreCondition(partnerType, partnerInstance);

                PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(partnerInstance);
                partnerInstanceDto.copyOperatorDto(confirmCloseDto);
                
                LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.CLOSED_EVENT);
    			stateMachineService.executePhase(phaseEvent);
    			
                // 更新合伙人实例，已停业
              /*  PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
                partnerInstanceDto.setId(instanceId);
                partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
                partnerInstanceDto.setServiceEndTime(new Date());
                partnerInstanceDto.copyOperatorDto(confirmCloseDto);
                partnerInstanceDto.setVersion(partnerInstance.getVersion());
                partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

                // 更新村点状态，已停业
                stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, employeeId);

                // 更新生命周期，已确认
                partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
                partnerLifecycleBO.updateLifecycle(partnerLifecycle);

                // 同步station_apply
                syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

                // 发出合伙人实例状态变更事件
                dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, confirmCloseDto);*/
            } else {
            	 PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(partnerInstance);
                 partnerInstanceDto.copyOperatorDto(confirmCloseDto);
            	
                 LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.SERVICING_EVENT);
     			 stateMachineService.executePhase(phaseEvent);
     			
            	/*
                // 更新合伙人实例，服务中
                partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.SERVICING, employeeId);

                // 更新村点状态，服务中
                stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, employeeId);

                // 更新生命周期，拒绝
                partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
                partnerLifecycleBO.updateLifecycle(partnerLifecycle);

                // 删除停业协议
                partnerProtocolRelBO.cancelProtocol(partnerInstance.getTaobaoUserId(), ProtocolTypeEnum.PARTNER_QUIT_PRO, instanceId,
                        PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, employeeId);

                // 删除停业申请单
                closeStationApplyBO.deleteCloseStationApply(instanceId, employeeId);

                // 同步station_apply
                syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

                // 发出合伙人实例状态变更事件
                dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, confirmCloseDto);
            */}
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseByManager(ForcedCloseDto forcedCloseDto) {
        applyCloseInternal(forcedCloseDto, PartnerInstanceCloseTypeEnum.WORKER_QUIT);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseBySystem(ForcedCloseDto forcedCloseDto) {
        applyCloseInternal(forcedCloseDto, PartnerInstanceCloseTypeEnum.SYSTEM_QUIT);
    }

    private void applyCloseInternal(ForcedCloseDto forcedCloseDto, PartnerInstanceCloseTypeEnum closeType) {
    	BeanValidator.validateWithThrowable(forcedCloseDto);
    	PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(forcedCloseDto.getInstanceId());
    	partnerInstanceChecker.checkCloseApply(forcedCloseDto.getInstanceId());
    	PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(rel);
    	partnerInstanceDto.setCloseType(closeType);
    	partnerInstanceDto.copyOperatorDto(forcedCloseDto);
    	Map<String,Object> extensionInfos = Maps.newConcurrentMap();
    	extensionInfos.put("forcedCloseDto", forcedCloseDto);
        LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.CLOSING_EVENT,extensionInfos);
		stateMachineService.executePhase(phaseEvent);
    	/*
        // 参数校验
        BeanValidator.validateWithThrowable(forcedCloseDto);
            Long instanceId = forcedCloseDto.getInstanceId();
            //查询实例是否存在，不存在会抛异常
            PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

            //生成状态变化枚举，装修中-》停业申请中，或者，服务中-》停业申请中
            PartnerInstanceStateChangeEnum instanceStateChange = PartnerInstanceConverter.convertClosingStateChange(partnerStationRel);

            // 校验停业前置条件。例如校验合伙人是否还存在淘帮手存在
            partnerInstanceChecker.checkCloseApply(instanceId);

            // 合伙人实例停业中,退出类型为强制清退
            closingPartnerInstance(partnerStationRel, closeType, forcedCloseDto);

            // 村点停业中
            closingStation(partnerStationRel.getStationId(), instanceStateChange, forcedCloseDto);

            // 添加停业生命周期记录
            addClosingLifecycle(forcedCloseDto, partnerStationRel, closeType);

            // 新增停业申请单
            CloseStationApplyDto closeStationApplyDto = CloseStationApplyConverter.toCloseStationApplyDto(forcedCloseDto, closeType, instanceStateChange);
            closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

            // 通过事件，定时钟，启动停业流程
            dispatchInstStateChangeEvent(instanceId, instanceStateChange, forcedCloseDto);
            // 失效tair
    */}

    /**
     * 添加停业生命周期
     *
     * @param operatorDto
     * @param partnerStationRel
     * @param closeType
     */
    private void addClosingLifecycle(OperatorDto operatorDto, PartnerStationRel partnerStationRel, PartnerInstanceCloseTypeEnum closeType) {
        PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();

        partnerLifecycle.setPartnerInstanceId(partnerStationRel.getId());
        partnerLifecycle.setPartnerType(PartnerInstanceTypeEnum.valueof(partnerStationRel.getType()));
        partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);

        if (PartnerInstanceCloseTypeEnum.WORKER_QUIT.equals(closeType)) {
            partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
        } else if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(closeType)) {
            partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
            partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
        }
        partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
        partnerLifecycle.copyOperatorDto(operatorDto);

        partnerLifecycleBO.addLifecycle(partnerLifecycle);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyQuitByManager(QuitStationApplyDto quitDto)  {
            // 参数校验
            BeanValidator.validateWithThrowable(quitDto);

            Long instanceId = quitDto.getInstanceId();
            String operator = quitDto.getOperator();
            //查询实例是否存在，不存在会抛异常
            PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

            // 校验申请退出的前置条件：是否存在下级合伙人，是否存在未结束订单，是否已经提交过退出
            partnerInstanceChecker.checkQuitApply(instanceId);

            PartnerInstanceTypeEnum instanceType = PartnerInstanceTypeEnum.valueof(instance.getType());
            // 校验是否可以同时撤点
            Boolean isQuitStation = quitDto.getIsQuitStation();
            // 如果选择同时撤点，校验村点上其他人是否都处于退出待解冻、已退出状态
            if (Boolean.TRUE.equals(isQuitStation)) {
                partnerInstanceHandler.validateOtherPartnerQuit(instanceType, instanceId);
            }

            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(instance);
        	partnerInstanceDto.copyOperatorDto(quitDto);
        	Map<String,Object> extensionInfos = Maps.newConcurrentMap();
        	extensionInfos.put("quitApply", quitDto);
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.QUITING_EVENT,extensionInfos);
    		stateMachineService.executePhase(phaseEvent);
            
           /* // 保存退出申请单
            QuitStationApply quitStationApply = QuitStationApplyConverter.convert(quitDto, instance, buildOperatorName(quitDto));
            quitStationApplyBO.saveQuitStationApply(quitStationApply, operator);

            // 合伙人实例状态变更为退出中
            partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING, operator);

            // 村点状态变更为退出中
            if (quitDto.getIsQuitStation()) {
                stationBO.changeState(instance.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING, operator);
            }

            // 不同合伙人不同退出生命周期
            partnerInstanceHandler.handleDifferQuiting(quitDto, PartnerInstanceTypeEnum.valueof(instance.getType()));

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instanceId);

            // 发送合伙人实例状态变化事件
            dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_QUITTING, quitDto);*/

            // 失效tair
    }

    private String buildOperatorName(OperatorDto operatorDto) {
        String operator = operatorDto.getOperator();
        OperatorTypeEnum type = operatorDto.getOperatorType();

        // 小二工号
        if (OperatorTypeEnum.BUC.equals(type)) {
            return emp360Adapter.getName(operator);
        } else if (OperatorTypeEnum.HAVANA.equals(type)) {
            return uicReadAdapter.getFullName(Long.parseLong(operator));
        }
        logger.warn("查询操作人姓名失败，不支持的操作人类型。OperatorTypeEnum=" + (null != type ? type.getDesc() : ""));
        return "";
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long applySettle(PartnerInstanceDto partnerInstanceDto){
       /* ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getType());

            validateSettlable(partnerInstanceDto);

            Long stationId = partnerInstanceDto.getStationId();
            Long instanceId;

            if (stationId == null) {
                stationId = addStation(partnerInstanceDto);
            } else {
                StationDto stationDto = partnerInstanceDto.getStationDto();
                stationDto.setState(StationStateEnum.INVALID);
                stationDto.setStatus(StationStatusEnum.NEW);
                PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
                if (partnerDto != null) {
                    stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
                    stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
                    stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
                }
                updateStation(stationId, partnerInstanceDto);
            }

            Long partnerId = addPartner(partnerInstanceDto);
            instanceId = addPartnerInstanceRel(partnerInstanceDto, stationId, partnerId);

            // 不同类型合伙人，执行不同的生命周期
            partnerInstanceDto.setId(instanceId);
            partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.ADD, instanceId);

            // 记录村点状态变化
            sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
            return instanceId;*/
    	LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,StateMachineEvent.SETTLING_EVENT);
		stateMachineService.executePhase(phaseEvent);
		return partnerInstanceDto.getId();
    }

    private Long addPartnerInstanceRel(PartnerInstanceDto partnerInstanceDto, Long stationId, Long partnerId) {
        partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
        partnerInstanceDto.setApplyTime(new Date());
        partnerInstanceDto.setApplierId(partnerInstanceDto.getOperator());
        partnerInstanceDto.setApplierType(partnerInstanceDto.getOperatorType().getCode());
        partnerInstanceDto.setStationId(stationId);
        partnerInstanceDto.setPartnerId(partnerId);
        partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
        partnerInstanceDto.setVersion(0L);
        // 当前partner_station_rel.isCurrent = n, 并添加新的当前partner_station_rel
        return partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
    }

    private Long addPartner(PartnerInstanceDto partnerInstanceDto) {
        //确保taobaouserId在partner表唯一
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(partnerInstanceDto.getTaobaoUserId());
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        if (partner == null) {
            partnerDto.copyOperatorDto(partnerInstanceDto);
            partnerDto.setState(PartnerStateEnum.NORMAL);
            Long partnerId = partnerBO.addPartner(partnerDto);
            criusAttachmentService.addAttachmentBatch(partnerDto.getAttachments(), partnerId, AttachmentBizTypeEnum.PARTNER, OperatorConverter.convert(partnerInstanceDto));
            return partnerId;
        } else {
            partnerDto.setId(partner.getId());
            partnerDto.setAliLangUserId(partner.getAlilangUserId());
            partnerDto.setState(PartnerStateEnum.NORMAL);
            partnerBO.updatePartner(partnerDto);
            criusAttachmentService.modifyAttachementBatch(partnerDto.getAttachments(), partner.getId(), AttachmentBizTypeEnum.PARTNER, OperatorConverter.convert(partnerInstanceDto));
            return partner.getId();
        }

    }

    private Long addStation(PartnerInstanceDto partnerInstanceDto) {
        StationDto stationDto = partnerInstanceDto.getStationDto();
        stationDto.setState(StationStateEnum.INVALID);
        stationDto.setStatus(StationStatusEnum.NEW);
        stationDto.copyOperatorDto(partnerInstanceDto);

        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        if (partnerDto != null) {
            stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
            stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
            stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
        }

        // 判断服务站编号是否使用中
        Long stationId = partnerInstanceDto.getStationId();
        checkStationNumDuplicate(stationId, stationDto.getStationNum());
        stationId = stationBO.addStation(stationDto);
        if (partnerInstanceDto.getParentStationId() == null) {
            // TP新建站点: parentStationId = stationId
            partnerInstanceDto.setPartnerId(stationId);
        }
        criusAttachmentService.addAttachmentBatch(stationDto.getAttachments(), stationId, AttachmentBizTypeEnum.CRIUS_STATION, OperatorConverter.convert(partnerInstanceDto));
        saveStationFixProtocol(stationDto, stationId);
        return stationId;
    }

    private void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
        switch (type) {
            case ADD:
                syncStationApplyBO.addStationApply(instanceId);
                break;
            case DELETE:
                syncStationApplyBO.deleteStationApply(instanceId);
            default:
                syncStationApplyBO.updateStationApply(instanceId, type);
                break;
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto){
        ValidateUtils.validateParam(partnerInstanceQuitDto);
        Long instanceId = partnerInstanceQuitDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            if (rel != null) {
                if (PartnerInstanceStateEnum.QUIT.getCode().equals(rel.getState())) {
                    return;
                } else if (!PartnerInstanceStateEnum.QUITING.getCode().equals(rel.getState())) {
                    throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"数据异常");
                }
            }
            PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(rel);
			partnerInstanceDto.copyOperatorDto(partnerInstanceQuitDto);
			Map<String,Object> extensionInfos = Maps.newHashMap();
			extensionInfos.put("fromThawTask", true);
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.QUIT_EVENT,extensionInfos);
            stateMachineService.executePhase(phaseEvent);
			
            
            
           /* partnerInstanceHandler.handleQuit(partnerInstanceQuitDto, PartnerInstanceTypeEnum.valueof(rel.getType()));
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);*/
    }

    @Override
    public Long applyResettle(PartnerInstanceDto partnerInstanceDto){
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto){
        ValidateUtils.validateParam(degradeDto);
        Long instanceId = degradeDto.getInstanceId();
        Long parentTaobaoUserId = degradeDto.getParentTaobaoUserId();
        ValidateUtils.notNull(instanceId);
        ValidateUtils.notNull(parentTaobaoUserId);
            PartnerInstanceDto rel = partnerInstanceBO.getPartnerInstanceById(instanceId);
            Assert.notNull(rel, "partner instance is null");
            if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), rel.getType().getCode())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TYPE_FAIL.getDesc());
            }

            if (!PartnerInstanceStateEnum.canDegradeStateCodeList().contains(rel.getState().getCode())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_STATE_FAIL.getDesc());
            }
            int tpaCount = partnerInstanceBO.getActiveTpaByParentStationId(rel.getParentStationId());

            if (tpaCount > 0) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_HAS_TPA.getDesc());
            }

            PartnerStationRel parentRel = partnerInstanceBO.getActivePartnerInstance(parentTaobaoUserId);
            if (parentRel == null) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_ERROR.getDesc());
            }
            // 归属合伙人是否属于服务中状态
            if (!StringUtils.equals(PartnerInstanceStateEnum.SERVICING.getCode(), parentRel.getState())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_NOT_SERVICING.getDesc());
            }

            if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), parentRel.getType())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_TYPE_NOT_TP.getDesc());
            }

            if (parentTaobaoUserId.longValue() == rel.getTaobaoUserId().longValue()) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_SAME.getDesc());
            }

            // 所归属的合伙人的淘帮手不能大于等于5个
            int parentTpaCount = partnerInstanceBO.getActiveTpaByParentStationId(parentRel.getParentStationId());
            Integer maxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(parentRel.getId());
            if (parentTpaCount >= maxChildNum) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_HAS_TPA_MAX.getDesc());
            }

            // 判断是不是归属同一个县
            Station parentStation = stationBO.getStationById(parentRel.getStationId());
            Station station = stationBO.getStationById(rel.getStationId());
            if (parentStation.getApplyOrg().longValue() != station.getApplyOrg().longValue()) {
                throw new AugeBusinessException(AugeErrorCodes.STATION_BUSINESS_CHECK_ERROR_CODE,PartnerInstanceExceptionEnum.DEGRADE_PARTNER_ORG_NOT_SAME.getDesc());
            }
            generalTaskSubmitService.submitDegradePartner(rel, PartnerInstanceConverter.convert(parentRel), degradeDto);
    }

    @SuppressWarnings("static-access")
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto){
       /* ValidateUtils.validateParam(settleSuccessDto);
        Long instanceId = settleSuccessDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            Assert.notNull(rel, "partner instance not exists");
            partnerInstanceHandler.handleSettleSuccess(settleSuccessDto, rel);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);*/
    	ValidateUtils.validateParam(settleSuccessDto);
    	Long instanceId = settleSuccessDto.getInstanceId();
    	PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
    	ValidateUtils.notNull(partnerInstanceDto);
    	partnerInstanceDto.copyOperatorDto(settleSuccessDto);
    	StateMachineEvent sme = null;
    	if("TP".equals(partnerInstanceDto.getType().getCode())||"TPT".equals(partnerInstanceDto.getType().getCode())||"TPS".equals(partnerInstanceDto.getType().getCode())){
    		sme = StateMachineEvent.DECORATING_EVENT;
    	}else{
    		sme = StateMachineEvent.SERVICING_EVENT;
    	}
    	LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,sme);
		stateMachineService.executePhase(phaseEvent);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateSettle(PartnerInstanceDto partnerInstanceDto){
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getStationId());
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(partnerInstanceDto.getStationId());
            Assert.notNull(rel, "partner instance not exists");
            Assert.notNull(rel.getType(), "partner instance type is null");

            boolean canUpdate = partnerInstanceHandler.handleValidateUpdateSettle(rel.getId(),

                    PartnerInstanceTypeEnum.valueof(rel.getType()));
            if (!canUpdate) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前数据不能编辑");
            }
            updateStation(rel.getStationId(), partnerInstanceDto);

            PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            partnerDto.copyOperatorDto(partnerInstanceDto);
            partnerDto.setId(rel.getPartnerId());

            updatePartner(partnerDto);
            partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, rel.getId());

    }

    private void updatePartner(PartnerDto partnerDto) {
        partnerBO.updatePartner(partnerDto);
        criusAttachmentService.modifyAttachementBatch(partnerDto.getAttachments(), partnerDto.getId(), AttachmentBizTypeEnum.PARTNER, OperatorConverter.convert(partnerDto));
    }

    private void updateStation(Long stationId, PartnerInstanceDto partnerInstanceDto) {
        StationDto stationDto = partnerInstanceDto.getStationDto();
        stationDto.copyOperatorDto(partnerInstanceDto);
        stationDto.setId(stationId);
        updateStation(stationDto);
    }

    private void updateStation(StationDto stationDto) {
        Long stationId = stationDto.getId();
        // 判断服务站编号是否使用中
        checkStationNumDuplicate(stationId, stationDto.getStationNum());

        stationBO.updateStation(stationDto);
        saveStationFixProtocol(stationDto, stationId);
        criusAttachmentService.modifyAttachementBatch(stationDto.getAttachments(), stationId, AttachmentBizTypeEnum.CRIUS_STATION, OperatorConverter.convert(stationDto));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void auditSettleByManager(AuditSettleDto auditSettleDto){
        ValidateUtils.validateParam(auditSettleDto);
        Long partnerInstanceId = auditSettleDto.getPartnerInstanceId();
        Boolean isAgree = auditSettleDto.getIsAgree();
        ValidateUtils.notNull(partnerInstanceId);
        ValidateUtils.notNull(isAgree);
            PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
                    PartnerLifecycleCurrentStepEnum.PROCESSING);
            Assert.notNull(items, "PartnerLifecycleItems not exists");

            if (isAgree) {
                PartnerLifecycleDto param = new PartnerLifecycleDto();
                param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
                param.setLifecycleId(items.getId());
                param.copyOperatorDto(auditSettleDto);
                partnerLifecycleBO.updateLifecycle(param);
            } else {
                PartnerLifecycleDto param = new PartnerLifecycleDto();
                param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
                param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
                param.setLifecycleId(items.getId());
                param.copyOperatorDto(auditSettleDto);
                partnerLifecycleBO.updateLifecycle(param);
                // 合伙人实例入驻失败
                partnerInstanceBO.changeState(partnerInstanceId, PartnerInstanceStateEnum.SETTLING, PartnerInstanceStateEnum.SETTLE_FAIL,
                        auditSettleDto.getOperator());
                PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(partnerInstanceId);
                stationBO.changeState(rel.getStationId(), StationStatusEnum.NEW, StationStatusEnum.INVALID, auditSettleDto.getOperator());
            }
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradePartnerInstanceSuccessDto)
           {

        ValidateUtils.validateParam(degradePartnerInstanceSuccessDto);

        Long instanceId = degradePartnerInstanceSuccessDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            Long parentInstanceId = degradePartnerInstanceSuccessDto.getParentInstanceId();
            ValidateUtils.notNull(parentInstanceId);
            PartnerStationRel parentRel = partnerInstanceBO.findPartnerInstanceById(parentInstanceId);
            Assert.notNull(rel, "partner instance not exists");
            Assert.notNull(rel.getType(), "partner instance type is null");
            Assert.notNull(parentRel, "parent partner instance not exists");
            Assert.notNull(parentRel.getType(), "parent partner instance type is null");

            if (PartnerInstanceStateEnum.CLOSING.getCode().equals(rel.getState())
                    || PartnerInstanceStateEnum.CLOSED.getCode().equals(rel.getState())) {
                // 删除停业申请单
                closeStationApplyBO.deleteCloseStationApply(instanceId, degradePartnerInstanceSuccessDto.getOperator());
            }

            if (PartnerInstanceStateEnum.CLOSING.getCode().equals(rel.getState())) {
                // 删除生命周期表
                PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.CLOSING,
                        PartnerLifecycleCurrentStepEnum.PROCESSING);
                PartnerLifecycleDto lifeDto = new PartnerLifecycleDto();
                lifeDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
                lifeDto.setLifecycleId(items.getId());
                lifeDto.copyOperatorDto(degradePartnerInstanceSuccessDto);
                partnerLifecycleBO.updateLifecycle(lifeDto);
            }

            PartnerInstanceDto param = new PartnerInstanceDto();
            param.setId(instanceId);
            param.setBit(-1);
            param.setParentStationId(parentRel.getStationId());
            param.setType(PartnerInstanceTypeEnum.TPA);
            param.setState(PartnerInstanceStateEnum.SERVICING);
            param.copyOperatorDto(degradePartnerInstanceSuccessDto);
            partnerInstanceBO.updatePartnerStationRel(param);

            // 更新station为服务中
            StationDto stationDto = new StationDto();
            stationDto.setStatus(StationStatusEnum.SERVICING);
            stationDto.setId(rel.getStationId());
            stationDto.copyOperatorDto(degradePartnerInstanceSuccessDto);
            stationBO.updateStation(stationDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);

            // 发送降级成功事件
            PartnerInstanceTypeChangeEvent event = new PartnerInstanceTypeChangeEvent();
            event.setPartnerInstanceId(instanceId);
            event.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.TP_DEGREE_2_TPA);
            event.setTaobaoUserId(rel.getTaobaoUserId());
            event.setStationId(rel.getStationId());
            event.setParentTaobaoUserId(parentRel.getTaobaoUserId());
            event.setOperator(degradePartnerInstanceSuccessDto.getOperator());
            event.setOperatorType(degradePartnerInstanceSuccessDto.getOperatorType());
            EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_TYPE_CHANGE_EVENT, event);
    }

    private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
                operator);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void evaluatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto){
            // 根据taobao_user_id和station_id失效以前的评级is_valid＝'n'
            partnerInstanceLevelBO.invalidatePartnerInstanceLevelBefore(partnerInstanceLevelDto);
            // 保存数据库
            partnerInstanceLevelBO.addPartnerInstanceLevel(partnerInstanceLevelDto);
            // 发送评级变化事件: 类型为系统评定
            PartnerInstanceLevelChangeEvent event = PartnerInstanceLevelEventConverter
                    .convertLevelChangeEvent(partnerInstanceLevelDto.getEvaluateType(), partnerInstanceLevelDto);
            EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_LEVEL_CHANGE_EVENT, event);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void changeTP(ChangeTPDto changeTPDto){
        ValidateUtils.notNull(changeTPDto);
            Long partnerInstanceId = changeTPDto.getPartnerInstanceId();
            Long newParentStationId = changeTPDto.getNewParentStationId();

            PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(changeTPDto.getPartnerInstanceId());
            if (!PartnerInstanceTypeEnum.TPA.equals(partnerInstanceDto.getType())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"type is not tpa");
            }
            if (partnerInstanceExtService.validateChildNum(newParentStationId)) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"the partner's tpa number is upper limit");
            }
            Long oldParentStationId = partnerInstanceDto.getParentStationId();
            Long stationId = partnerInstanceDto.getStationId();
            partnerInstanceDto.setParentStationId(newParentStationId);
            partnerInstanceDto.copyOperatorDto(changeTPDto);
            partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
            //菜鸟修改淘帮手归属合伙人关系
            Long parentParnterInstanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(newParentStationId);
            SyncModifyBelongTPForTpaDto belongTp = new SyncModifyBelongTPForTpaDto();
            belongTp.setParentPartnerInstanceId(parentParnterInstanceId);
            belongTp.setPartnerInstanceId(partnerInstanceId);
            belongTp.copyOperatorDto(changeTPDto);
            caiNiaoService.updateBelongTPForTpa(belongTp);
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceId);
            EventDispatcherUtil.dispatch(StationBundleEventConstant.CHANGE_TP_EVENT, buildChangeTPEvent(changeTPDto, oldParentStationId, stationId));
    }

    private ChangeTPEvent buildChangeTPEvent(ChangeTPDto changeTPDto, Long oldParentStationId, Long stationId) {
        ChangeTPEvent event = new ChangeTPEvent();
        event.setNewParentStationId(changeTPDto.getNewParentStationId());
        event.copyOperatorDto(changeTPDto);
        event.setInstanceId(changeTPDto.getPartnerInstanceId());
        event.setOldParentStationId(oldParentStationId);
        event.setStationId(stationId);
        return event;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void reService(Long instanceId, String operator){
        ValidateUtils.notNull(instanceId);
        ValidateUtils.notNull(operator);
        PartnerStationRel psl = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (psl.getIsCurrent().equals("n")) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"the partner is not current");
        }
        PartnerInstanceDto partnerInstanceDto = PartnerInstanceConverter.convert(psl);
		partnerInstanceDto.setOperator(operator);
        LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto, StateMachineEvent.SERVICING_EVENT);
        stateMachineService.executePhase(phaseEvent);
       /* partnerInstanceBO.reService(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.SERVICING, operator);
        stationBO.changeState(psl.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.SERVICING, operator);
        //防止有垃圾数据 导致  staiton实体信息 不一致，更新成  当前人的信息
        StationDto stationDto = new StationDto();
        stationDto.setId(psl.getStationId());
        stationDto.copyOperatorDto(OperatorDto.defaultOperator());
        stationDto.setState(StationStateEnum.NORMAL);
        Partner p = partnerBO.getPartnerById(psl.getPartnerId());
        stationDto.setTaobaoNick(p.getTaobaoNick());
        stationDto.setTaobaoUserId(p.getTaobaoUserId());
        stationDto.setAlipayAccount(p.getAlipayAccount());
        stationBO.updateStation(stationDto);

        // 同步station_apply
        syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
        generalTaskSubmitService.submitCloseToServiceTask(instanceId, psl.getTaobaoUserId(), PartnerInstanceTypeEnum.valueof(psl.getType()), operator);
        // 删除原有停业申请记录
        closeStationApplyBO.deleteCloseStationApply(instanceId, operator);
        //发送已停业到服务中事件
        PartnerInstanceStateChangeEvent event = buildCloseToServiceEvent(psl, operator);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);*/
    }

    private PartnerInstanceStateChangeEvent buildCloseToServiceEvent(PartnerStationRel psl, String operator) {
        PartnerInstanceStateChangeEvent event = new PartnerInstanceStateChangeEvent();
        event.setPartnerType(PartnerInstanceTypeEnum.valueof(psl.getType()));
        event.setTaobaoUserId(psl.getTaobaoUserId());
        event.setStationId(psl.getStationId());
        event.setPartnerInstanceId(psl.getId());
        event.setStateChangeEnum(PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE);
        event.setOperator(operator);
        event.setOperatorType(OperatorTypeEnum.SYSTEM);
        return event;
    }


    @Override
    public void upgradeDecorateLifeCycle(Long instanceId, String operator) {
        Assert.notNull(instanceId);
        Assert.notNull(operator);
        //判断是否已经是3.0标准
        PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING);
        if (item != null) {
            return;
        }
        //初始化装修生命周期
        PartnerStationRel psl = partnerInstanceBO.findPartnerInstanceById(instanceId);
        PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
        partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
        partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
        partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.DECORATING);
        partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
        partnerLifecycleDto.setPartnerInstanceId(psl.getId());
        //初始化培训记录
       /** PartnerCourseRecord record = partnerPeixunBO.initPeixunRecord(psl.getTaobaoUserId(),
                PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
                        .queryAppResourceValue("PARTNER_PEIXUN_CODE",
                                "APPLY_IN"));
        partnerPeixunBO.initPeixunRecord(psl.getTaobaoUserId(),
                PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
                        .queryAppResourceValue("PARTNER_PEIXUN_CODE",
                                "UPGRADE"));
        if (record != null && PartnerPeixunStatusEnum.DONE.getCode().equals(record.getStatus())) {
            partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.Y);
        } else {
            partnerLifecycleDto.setCourseStatus(PartnerLifecycleCourseStatusEnum.N);
        }**/
        // 生成装修记录
        StationDecorateDto stationDecorateDto = new StationDecorateDto();
        stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
        stationDecorateDto.setStationId(psl.getStationId());
        stationDecorateDto.setPartnerUserId(psl.getTaobaoUserId());
        stationDecorateDto.setDecorateType(StationDecorateTypeEnum.NEW);
        stationDecorateDto.setPaymentType(StationDecoratePaymentTypeEnum.SELF);
        stationDecorateBO.addStationDecorate(stationDecorateDto);
        partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
        partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
    }

    @Override
    public void promotePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto){
            PartnerInstanceLevelProcessDto levelProcessDto = new PartnerInstanceLevelProcessDto();
            Date now = new Date();
            levelProcessDto.setApplyTime(now);
            levelProcessDto.setBusinessCode(ProcessBusinessEnum.partnerInstanceLevelAudit.getCode());
            Long instanceId = partnerInstanceLevelDto.getPartnerInstanceId();
            levelProcessDto.setPartnerInstanceId(instanceId);
            levelProcessDto.setBusinessId(instanceId);
            PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(instanceId);
            Long countyOrgId = instance.getStationDto().getApplyOrg();
            levelProcessDto.setCountyOrgId(countyOrgId);
            CountyStation countyStation = countyStationBO.getCountyStationByOrgId(countyOrgId);
            levelProcessDto.setCountyStationName(countyStation.getName());
            levelProcessDto.setCurrentLevel(partnerInstanceLevelDto.getCurrentLevel());
            levelProcessDto.setExpectedLevel(partnerInstanceLevelDto.getExpectedLevel());

            List<CuntaoUser> userLists = cuntaoUserService.getCuntaoUsers(countyOrgId, UserRole.COUNTY_LEADER);
            CuntaoUser countyLeader = userLists.iterator().next();
            levelProcessDto.setEmployeeId(countyLeader.getLoginId());
            levelProcessDto.setEmployeeName(emp360Adapter.getName(countyLeader.getLoginId()));
            levelProcessDto.setPartnerName(instance.getPartnerDto().getName());
            levelProcessDto.setPartnerTaobaoUserId(partnerInstanceLevelDto.getTaobaoUserId());
            levelProcessDto.setStationId(instance.getStationDto().getId());
            levelProcessDto.setStationName(instance.getStationDto().getName());
            levelProcessDto.setScore(partnerInstanceLevelDto.getScore());
            levelProcessDto.setMonthlyIncome(partnerInstanceLevelDto.getMonthlyIncome());
            partnerInstanceLevelDto.setOperator(countyLeader.getLoginId());
            partnerInstanceLevelDto.setOperatorType(OperatorTypeEnum.BUC);
            partnerInstanceLevelDto.setEvaluateBy(countyLeader.getLoginId());
            levelProcessDto.setEvaluateInfo(JSON.toJSONString(partnerInstanceLevelDto));

            generalTaskSubmitService.submitLevelApproveProcessTask(ProcessBusinessEnum.partnerInstanceLevelAudit, levelProcessDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void upgradePartnerInstance(PartnerInstanceUpgradeDto upgradeDto){
        // 参数校验
        BeanValidator.validateWithThrowable(upgradeDto);

        StationDto stationDto = upgradeDto.getStationDto();
        Long stationId = stationDto.getId();
        ValidateUtils.notNull(stationId);
        PartnerDto partnerDto = upgradeDto.getPartnerDto();

        Long partnerId = partnerDto.getId();
        ValidateUtils.notNull(partnerId);
            //村点信息备份
            Map<String, String> feature = partnerTypeChangeApplyBO.backupStationInfo(stationId);
            // 更新村点信息
            String stationNum = stationNumConfigBO.createStationNum(stationDto.getAddress().getProvince(), StationNumConfigTypeEnum.C,0);
            upgradeDto.getStationDto().setStationNum(stationNum);
            stationDto.setStationNum(stationNum);
            
            stationDto.setState(StationStateEnum.INVALID);
            stationDto.setStatus(StationStatusEnum.NEW);
            stationDto.copyOperatorDto(upgradeDto);
            StationValidator.validateStationInfo(stationDto);
            PartnerInstanceDto pid = new PartnerInstanceDto();
            pid.setStationDto(stationDto);
            pid.setPartnerDto(partnerDto);
            lifeCycleValidator.stationModelBusCheck(pid);
            updateStation(stationDto);
            
            stationNumConfigBO.updateSeqNumByStationNum(stationDto.getAddress().getProvince(), StationNumConfigTypeEnum.C, 
          		  stationNum);

            // 更新合伙人信息
            partnerDto.copyOperatorDto(upgradeDto);
            updatePartner(partnerDto);

            // 更新老的实例
            PartnerStationRel tpaInstance = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
            partnerInstanceBO.changeState(tpaInstance.getId(), PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.QUIT,
                    upgradeDto.getOperator());

            // 新生成一个合伙人实例
            Long nextInstanceId = addUpgradedInstance(tpaInstance, upgradeDto);

            //新增类型变更申请单
            PartnerTypeChangeApplyDto applyDto = new PartnerTypeChangeApplyDto();
            applyDto.setPartnerInstanceId(tpaInstance.getId());
            applyDto.setNextPartnerInstanceId(nextInstanceId);
            applyDto.setFeature(feature);
            applyDto.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP);
            applyDto.copyOperatorDto(upgradeDto);
            partnerTypeChangeApplyBO.addPartnerTypeChangeApply(applyDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.ADD, nextInstanceId);
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_STATE, tpaInstance.getId());

            // 发出升级事件
            PartnerInstanceEventUtil.dispatchUpgradeEvent(tpaInstance, upgradeDto);
    }

    /**
     * 生成升级后的合伙人实例
     *
     * @param upgradeDto
     * @param tpaInstance
     * @return
     */
    private Long addUpgradedInstance(PartnerStationRel tpaInstance, PartnerInstanceUpgradeDto upgradeDto) {
        PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();

        Long stationId = tpaInstance.getStationId();
        Long partnerId = tpaInstance.getPartnerId();

        Partner partner = partnerBO.getPartnerById(partnerId);
        PartnerDto partnerDto = upgradeDto.getPartnerDto();

        partnerDto.setTaobaoNick(partner.getTaobaoNick());
        partnerDto.setTaobaoUserId(partner.getTaobaoUserId());

        partnerInstanceDto.setStationId(stationId);
        partnerInstanceDto.setParentStationId(stationId);
        partnerInstanceDto.setPartnerId(partnerId);
        partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
        partnerInstanceDto.setType(PartnerInstanceTypeEnum.TP);
        partnerInstanceDto.setTaobaoUserId(partner.getTaobaoUserId());
        partnerInstanceDto.setPartnerDto(partnerDto);
        partnerInstanceDto.setStationDto(upgradeDto.getStationDto());
        partnerInstanceDto.setApplyTime(new Date());
        // 升级标示
        partnerInstanceDto.setBit(1);
        partnerInstanceDto.setStationDecorateTypeEnum(upgradeDto.getStationDecorateTypeEnum());
        partnerInstanceDto.setStationDecoratePaymentTypeEnum(upgradeDto.getStationDecoratePaymentTypeEnum());

        partnerInstanceDto.copyOperatorDto(upgradeDto);
        partnerInstanceDto.setApplierId(upgradeDto.getOperator());
        partnerInstanceDto.setApplierType(upgradeDto.getOperatorType().getCode());
        partnerInstanceDto.setMode("v4");
        // 新的合伙人实例
        Long nextInstanceId = addPartnerInstanceRel(partnerInstanceDto, stationId, partnerId);

        // 不同类型合伙人，执行不同的生命周期
        partnerInstanceDto.setId(nextInstanceId);
        partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());
        return nextInstanceId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Boolean thawMoney(Long instanceId){
        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (rel == null) {
            return Boolean.TRUE;
        }

        // 获得冻结的金额
        AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
        String frozenMoney = accountMoney.getMoney().toString();

        String accountNo = accountMoney.getAccountNo();

        OperatorDto operatorDto = new OperatorDto();
        operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
        operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
        operatorDto.setOperatorOrgId(0L);
        if (accountNo == null) {
            PaymentAccountDto accountDto = paymentAccountQueryAdapter
                    .queryPaymentAccountByTaobaoUserId(rel.getTaobaoUserId(), operatorDto);
            if (accountDto == null) {
                logger.error("PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:" + instanceId);
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:" + instanceId);
            }
            accountNo = accountDto.getAccountNo();
        }

        generalTaskSubmitService.submitThawMoneyTask(instanceId, accountNo, frozenMoney, operatorDto);

        return Boolean.TRUE;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void thawMoneySuccess(PartnerInstanceThrawSuccessDto partnerInstanceThrawSuccessDto){
        //解冻保证金
        AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
        accountMoneyUpdateDto.setObjectId(partnerInstanceThrawSuccessDto.getInstanceId());
        accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
        accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
        accountMoneyUpdateDto.setThawTime(new Date());
        accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_THAW);
        accountMoneyUpdateDto.copyOperatorDto(partnerInstanceThrawSuccessDto);
        accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void cancelUpgradePartnerInstance(CancelUpgradePartnerInstance cancelDto){
        // 参数校验
        BeanValidator.validateWithThrowable(cancelDto);

            Long instanceId = cancelDto.getInstanceId();
            PartnerLifecycleItems lifeItem = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.PROCESSING);
            if (PartnerLifecycleBondEnum.HAS_FROZEN.getCode().equals(lifeItem.getBond())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"保证金已经冻结，不可以撤销。");
            }
            //查询升级申请单
            PartnerTypeChangeApplyDto applyDto = partnerTypeChangeApplyBO.getPartnerTypeChangeApply(instanceId);
            Long tpaInstanceId = applyDto.getPartnerInstanceId();

            //恢复村点信息，状态为服务中
            StationDto fillStationDto = partnerTypeChangeApplyBO.fillStationDto(applyDto);
            fillStationDto.setState(StationStateEnum.NORMAL);
            fillStationDto.setStatus(StationStatusEnum.SERVICING);
            fillStationDto.copyOperatorDto(cancelDto);
            updateStation(fillStationDto);

            //恢复淘帮手实例
            PartnerInstanceDto tpaInstanceDto = new PartnerInstanceDto();
            tpaInstanceDto.copyOperatorDto(cancelDto);
            tpaInstanceDto.setId(tpaInstanceId);
            tpaInstanceDto.setState(PartnerInstanceStateEnum.SERVICING);
            tpaInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);

            partnerInstanceBO.updatePartnerStationRel(tpaInstanceDto);
            //删除合伙人实例
            partnerInstanceBO.deletePartnerStationRel(instanceId, cancelDto.getOperator());
            //删除合伙人入驻生命周期
            partnerLifecycleBO.deleteLifecycleItems(instanceId, cancelDto.getOperator());
            //删除升级申请单
            partnerTypeChangeApplyBO.deletePartnerTypeChangeApply(applyDto.getId(), cancelDto.getOperator());

            // 同步tp station_apply
            syncStationApply(SyncStationApplyEnum.DELETE, instanceId);
            // 同步tpa station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_STATE, tpaInstanceId);

            //发出撤销升级事件
            PartnerInstanceEventUtil.dispatchCancelUpgradeEvent(tpaInstanceId, fillStationDto.getId(), cancelDto);
    }

    /**
     * 更新服务站地址信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateStationAddress(Long taobaoUserId, StationDto updateStation, boolean isSendMail)
           {
        if (updateStation != null) {
            Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(updateStation.getId());
            PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(instanceId);
            StationDto oldStation = instance.getStationDto();
            StationDto newStation = new StationDto();
            newStation.setId(oldStation.getId());
            newStation.setAddress(updateStation.getAddress());
            newStation.setOperator(String.valueOf(taobaoUserId));
            newStation.setOperatorType(OperatorTypeEnum.HAVANA);
            newStation.setPartnerInstanceIsOnTown(updateStation.getPartnerInstanceIsOnTown());
            stationBO.updateStation(newStation);
            // 同步菜鸟地址更新
            if (isNeedToUpdateCainiaoStation(instance.getState().getCode())) {
                generalTaskSubmitService.submitUpdateCainiaoStation(instanceId, String.valueOf(taobaoUserId));
            }
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
            // 日志
            CuntaoFlowRecordDto record = new CuntaoFlowRecordDto();
            record.setTargetId(oldStation.getId());
            record.setNodeTitle("村服务站地址信息变更");
            record.setOperatorWorkid(newStation.getOperator());
            record.setOperatorName(newStation.getOperatorType().getCode());
            record.setOperateTime(new Date());
            record.setRemarks(PartnerInstanceEventUtil.buildAddressInfo(oldStation, newStation));
            record.setTargetType(CuntaoFlowRecordTargetTypeEnum.SANTONG_DZWL);
            if (updateStation.getFeature() != null) {
                record.setOperateOpinion(updateStation.getFeature().get("st_fk_type"));
            }
            cuntaoFlowRecordQueryService.insertRecord(record);
            if (isSendMail) {
                updateStation.setApplyOrg(oldStation.getApplyOrg());
                updateStation.setStationNum(oldStation.getStationNum());
                sendMailAndOpenPermission(updateStation,instance.getPartnerDto());
            }
        }
    }

    private void sendMailAndOpenPermission(StationDto station,PartnerDto partner) {
            Map<String, Object> contentMap = Maps.newHashMap();
            contentMap.put("station_id", station.getId() + "");
            contentMap.put("station_name", station.getName());
            contentMap.put("station_num", station.getStationNum());
            contentMap.put("partner_name", partner.getName());
            if (station.getFeature() != null) {
                contentMap.put("type", station.getFeature().get("st_fk_type"));
                contentMap.put("description", station.getFeature().get("st_fk_desc"));
            }
            
            List<CuntaoUser> users = cuntaoUserService.getCuntaoUsers(station.getApplyOrg(), UserRole.COUNTY_LEADER);
            List<String> mailList = new ArrayList<String>();
            for(CuntaoUser user: users){
            EnhancedUser enhancedUser = null;
                try {
                    enhancedUser = enhancedUserQueryService.getUser(user.getLoginId());
                    mailList.add(enhancedUser.getEmailAddr());
                    CuntaoUserRole role = new CuntaoUserRole();
                    role.setCreator(user.getLoginId());
                    role.setModifier(user.getLoginId());
                    role.setOrgId(station.getApplyOrg());
                    role.setEndTime(DateUtil.addDays(new Date(), 7));
                    role.setRoleName("LNG_LAT_MANAGER");
                    role.setUserId(user.getLoginId());
                    role.setUserName(enhancedUser.getLastName());
                    cuntaoUserRoleService.addCunUserRole(role);
                } catch (BucException e) {
                    logger.error("Query user failed, user id : " + user.getUserId());
                }
            }
            if(mailList.size() > 0){
                BatchMailDto mailDto = new BatchMailDto();
                mailDto.setMailAddresses(mailList);
                mailDto.setTemplateId(mailConfiguredProperties.getAddressUpdateNotifyMailTemplateId());
                mailDto.setMessageTypeId(mailConfiguredProperties.getAddressUpdateNotifyMailMessageTypeId());
                mailDto.setSourceId(mailConfiguredProperties.getAddressUpdateNotifyMailSourceId());
                mailDto.setOperator(station.getOperator());
                mailDto.setContentMap(contentMap);
                generalTaskSubmitService.submitMailTask(mailDto);
            }
    }

    /**
     * 更新服务站经纬度信息
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateStationLngLat(Long taobaoUserId, StationDto updateStation){
        if (updateStation != null) {
            Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(updateStation.getId());
            PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(instanceId);
            StationDto oldStation = instance.getStationDto();
            StationDto newStation = new StationDto();
            newStation.setId(oldStation.getId());
            newStation.setAddress(updateStation.getAddress());
            if (taobaoUserId != null) {
                newStation.setOperator(String.valueOf(taobaoUserId));
                newStation.setOperatorType(OperatorTypeEnum.HAVANA);
            } else {
                newStation.setOperator(updateStation.getOperator());
                newStation.setOperatorType(OperatorTypeEnum.BUC);
            }
            stationBO.updateStation(newStation);
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
            // 日志
            CuntaoFlowRecordDto record = new CuntaoFlowRecordDto();
            record.setTargetId(oldStation.getId());
            record.setNodeTitle("村服务站经纬度信息变更");
            record.setOperatorWorkid(newStation.getOperator());
            record.setOperatorName(newStation.getOperatorType().getCode());
            record.setOperateTime(new Date());
            record.setRemarks(PartnerInstanceEventUtil.buildLngLatInfo(oldStation, newStation));
            record.setTargetType(CuntaoFlowRecordTargetTypeEnum.SANTONG_DZWL);
            cuntaoFlowRecordQueryService.insertRecord(record);
            if (taobaoUserId != null) {
                partnerLifecycleBO.updateConfirmPosition(instanceId, PartnerLifecyclePositionConfirmEnum.Y);
            }
            // 同步菜鸟地址更新
            SyncModifyLngLatDto lngDto = new SyncModifyLngLatDto();
            lngDto.setPartnerInstanceId(instanceId);
            lngDto.setLng(updateStation.getAddress().getLng());
            lngDto.setLat(updateStation.getAddress().getLat());
            caiNiaoService.modifyLngLatToCainiao(lngDto);
        }
    }

    @Override
    public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto){
        caiNiaoService.closeCainiaoStationForTpa(partnerInstanceId, operatorDto);
    }


	@Override
	public void quitApprove(Long instanceId) {
		try {
			processProcessor.quitApprove(instanceId, ProcessApproveResultEnum.APPROVE_PASS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void closeApprove(Long instanceId) {
		try {
			processProcessor.closeApprove(instanceId, ProcessApproveResultEnum.APPROVE_PASS);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

    /**
     * 为村站或门店申请卖家账号
     * @param taobaoUserId
     */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applySellerAccount(Long taobaoUserId) {
        // TODO 调用UIC生成卖家账号
        Long sellerId = 0L;
        
        PartnerStationRel partnerStationRel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        Assert.notNull(partnerStationRel, "partner instance not exists");
        PartnerInstanceDto instance = new PartnerInstanceDto();
        instance.setId(partnerStationRel.getId());
        instance.setSellerId(sellerId);
        instance.setOperator(String.valueOf(taobaoUserId));
        instance.setOperatorType(OperatorTypeEnum.HAVANA);
        partnerInstanceBO.updatePartnerStationRel(instance);
        
    }


	@Override
	public List<CuntaoBailDetailDto> queryCuntaoBail(Long taobaoUserId) {
		CuntaoBailDetailQueryDto queryDto = new CuntaoBailDetailQueryDto();
		queryDto.setTaobaoUserId(taobaoUserId);
		queryDto.setUserTypeEnum(UserTypeEnum.STORE);
		PagedResultModel<CuntaoBailDetailReturnDto> result = newBailService.getBailDetail(queryDto);
		if(result != null && result.getResult()!= null && result.isSuccess() && CollectionUtils.isNotEmpty(result.getResult().getCuntaoBailDetailDtos())){
			return result.getResult().getCuntaoBailDetailDtos();
		}
		return null;
	}
	/**
     * 冻结铺货保证金（新接口）
     * 
     * @param freezeBondDto
     * @return boolean
     */
    public boolean freezeRePublishBond(FreezeBondDto freezeBondDto) {
        ValidateUtils.validateParam(freezeBondDto);
        Long taobaoUserId = freezeBondDto.getTaobaoUserId();
        String accountNo = freezeBondDto.getAccountNo();
        String alipayAccount = freezeBondDto.getAlipayAccount();
        Double money = freezeBondDto.getMoney();

        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        
        if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState())&& StationModeEnum.V4.getCode().equals(instance.getMode())) {
                return frozenReplenishMoneyForDecorate(taobaoUserId, accountNo, alipayAccount,
                        instance,money);
        }else if ( PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())&& StationModeEnum.V4.getCode().equals(instance.getMode())) {
               return frozenReplenishMoneyForService(taobaoUserId, accountNo, alipayAccount,
                    instance,money);
        }
        return true;
    }


	@Override
	public void commitTrans(PartnerInstanceTransDto transDto) {
		 PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(transDto.getInstanceId());
		 if (rel == null) {
			 throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"rel is null");
		 }
		 
		 if(StationModeEnum.V4.getCode().equals(rel.getMode())) {
			 throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前服务站是天猫优品，不需要转型");
		 }
		 
		Station station = stationBO.getStationById(rel.getStationId());
	  
        String stationNum = stationNumConfigBO.createStationNum(station.getProvince(), StationNumConfigTypeEnum.C,0);
       
        //更新村点信息
		StationDto stationDto = new StationDto();
		stationDto.setName(transDto.getStationDto().getName());
		Address  address  = new  Address();
		if (transDto.getStationDto().getAddress().getAddressDetail() != null) {
			address.setAddressDetail(transDto.getStationDto().getAddress().getAddressDetail());
		}
		if (transDto.getStationDto().getAddress().getVillage() != null && transDto.getStationDto().getAddress().getVillageDetail() != null) {
			address.setVillage(transDto.getStationDto().getAddress().getVillage());
			address.setVillageDetail(transDto.getStationDto().getAddress().getVillageDetail());
		}
		stationDto.setAddress(address);
		stationDto.setStationNum(stationNum);
		stationDto.setId(rel.getStationId());
		stationDto.setPartnerInstanceIsOnTown(transDto.getStationDto().getPartnerInstanceIsOnTown());
		stationDto.copyOperatorDto(transDto);
		stationBO.updateStation(stationDto);
		
		//同步菜鸟
	    generalTaskSubmitService.submitUpdateCainiaoStation(transDto.getInstanceId(), transDto.getOperator());
	    //更新实例为待转型
	    partnerInstanceBO.updateTransStatusByInstanceId(transDto.getInstanceId(), PartnerInstanceTransStatusEnum.WAIT_TRANS, transDto.getOperator());
        stationNumConfigBO.updateSeqNumByStationNum(station.getProvince(), StationNumConfigTypeEnum.C, stationNum);
       
	}


	@Override
	public boolean freezeBondForTrans(FreezeBondDto freezeBondDto) {
		  ValidateUtils.validateParam(freezeBondDto);
        Long taobaoUserId = freezeBondDto.getTaobaoUserId();
        String accountNo = freezeBondDto.getAccountNo();
        String alipayAccount = freezeBondDto.getAlipayAccount();
        Double money = freezeBondDto.getMoney();

        PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
        
        if(PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())) {
            AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                    AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
            if  (bondMoney == null || !PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode().equals(instance.getTransStatus())) {
                throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"数据异常，不能操作");
            }
            String operator = String.valueOf(taobaoUserId);

            // 修改保证金冻结状态
            AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
            accountMoneyUpdateDto.setObjectId(bondMoney.getObjectId());
            accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
            accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
            accountMoneyUpdateDto.setFrozenTime(new Date());
            accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
            accountMoneyUpdateDto.setAlipayAccount(alipayAccount);
            accountMoneyUpdateDto.setAccountNo(accountNo);
            accountMoneyUpdateDto.setOperator(operator);
            accountMoneyUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
            accountMoneyUpdateDto.setMoney(BigDecimal.valueOf(money));
            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);
            
        	PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instance.getId());
        	partnerInstanceDto.copyOperatorDto(freezeBondDto);
        	LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(partnerInstanceDto,StateMachineEvent.DECORATING_EVENT);
    		stateMachineService.executePhase(phaseEvent);
    		
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, instance.getId());
        }
        return true;
	}
}
