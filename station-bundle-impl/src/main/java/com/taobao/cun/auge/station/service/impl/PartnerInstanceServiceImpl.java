package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.ali.com.google.common.collect.Maps;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.FrozenMoneyAmountConfig;
import com.taobao.cun.auge.configuration.MailConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
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
import com.taobao.cun.auge.flowRecord.dto.CuntaoFlowRecordDto;
import com.taobao.cun.auge.flowRecord.enums.CuntaoFlowRecordTargetTypeEnum;
import com.taobao.cun.auge.flowRecord.service.CuntaoFlowRecordQueryService;
import com.taobao.cun.auge.org.dto.CuntaoUser;
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
import com.taobao.cun.auge.station.check.PartnerInstanceChecker;
import com.taobao.cun.auge.station.convert.CloseStationApplyConverter;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AuditSettleDto;
import com.taobao.cun.auge.station.dto.BatchMailDto;
import com.taobao.cun.auge.station.dto.CancelUpgradePartnerInstance;
import com.taobao.cun.auge.station.dto.ChangeTPDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
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
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecyclePositionConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerInstanceExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
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
import com.taobao.cun.auge.user.service.CuntaoUserService;
import com.taobao.cun.auge.user.service.UserRole;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 合伙人实例服务接口
 *
 * @author quanzhu.wangqz
 */
@Service("partnerInstanceService")
@HSFProvider(serviceInterface = PartnerInstanceService.class)
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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long addTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        throw new AugeServiceException("deprecated method, call applySettle() instead");
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
    public Long updateTemp(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        throw new AugeServiceException("deprecated method, call updateSettle() instead");
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

    private String getErrorMessage(String methodName, String param, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("PartnerInstanceService-Error|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
                .append(error);
        return sb.toString();
    }

    private String getAugeExceptionErrorMessage(String methodName, String param, String error) {
        StringBuilder sb = new StringBuilder();
        sb.append("PartnerInstanceService|").append(methodName).append("(.param=").append(param).append(").").append("errorMessage:")
                .append(error);
        return sb.toString();
    }


    private void validateSettlable(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        ValidateUtils.notNull(partnerInstanceDto);
        StationDto stationDto = partnerInstanceDto.getStationDto();
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        ValidateUtils.notNull(stationDto);
        StationValidator.validateStationInfo(stationDto);
        PartnerValidator.validatePartnerInfo(partnerDto);

        OperatorDto operator = new OperatorDto();
        operator.copyOperatorDto(partnerInstanceDto);
        PaymentAccountDto paDto = paymentAccountQueryAdapter.queryPaymentAccountByNick(partnerDto.getTaobaoNick(), operator);
        if (!partnerDto.getAlipayAccount().equals(paDto.getAlipayId())) {
            throw new AugeServiceException(PartnerExceptionEnum.PARTNER_ALIPAYACCOUNT_NOTEQUAL);
        }
        if (!partnerDto.getName().equals(paDto.getFullName()) || !partnerDto.getIdenNum().equals(paDto.getIdCardNumber())) {
            throw new AugeServiceException(PartnerExceptionEnum.PARTNER_PERSION_INFO_NOTEQUAL);
        }

        // 判断淘宝账号是否使用中
        PartnerStationRel existPartnerInstance = partnerInstanceBO.getActivePartnerInstance(paDto.getTaobaoUserId());
        if (null != existPartnerInstance) {
            throw new AugeServiceException(PartnerExceptionEnum.PARTNER_TAOBAOUSERID_HAS_USED);
        }
        //判断手机号是否已经被使用
        //逻辑变更只判断入驻中、装修中、服务中，退出中用户
        if (!partnerInstanceBO.judgeMobileUseble(partnerDto.getTaobaoUserId(), null, partnerDto.getMobile())) {
            throw new AugeServiceException(PartnerExceptionEnum.MOBILE_HAS_USED);
        }
        // 入驻老村点，村点状态为已停业
        Long stationId = stationDto.getId();
        if (stationId != null) {
            Station station = stationBO.getStationById(stationId);
            if (station != null && !StationStatusEnum.CLOSED.getCode().equals(station.getStatus())) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_MUST_BE_CLOSED);
            }
			/*PartnerStationRel currentRel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
			if (currentRel != null && !PartnerInstanceStateEnum.CLOSED.getCode().equals(currentRel.getState())) {
				throw new AugeServiceException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_MUST_BE_CLOSED);
			}*/
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
                throw new AugeServiceException(StationExceptionEnum.STATION_NUM_IS_DUPLICATE);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateByPartner(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
        PartnerValidator.validateParnterUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getPartnerDto());
        StationValidator.validateStationUpdateInfoByPartner(partnerInstanceUpdateServicingDto.getStationDto());
        Long stationId = partnerInstanceUpdateServicingDto.getStationDto().getStationId();
        ValidateUtils.notNull(stationId);

        try {
            Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);
            partnerInstanceUpdateServicingDto.setId(instanceId);
            updateInternal(partnerInstanceUpdateServicingDto);
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void update(PartnerInstanceUpdateServicingDto partnerInstanceUpdateServicingDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceUpdateServicingDto);
        ValidateUtils.notNull(partnerInstanceUpdateServicingDto.getId());
        PartnerValidator.validateParnterCanUpdateInfo(partnerInstanceUpdateServicingDto.getPartnerDto());
        StationValidator.validateStationCanUpdateInfo(partnerInstanceUpdateServicingDto.getStationDto());
        try {
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
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("update", JSONObject.toJSONString(partnerInstanceUpdateServicingDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
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
            throw new AugeServiceException(PartnerExceptionEnum.MOBILE_HAS_USED);
        }
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.copyOperatorDto(partnerInstanceUpdateServicingDto);
        partnerDto.setId(partnerId);
        partnerDto.setMobile(pDto.getMobile());
        partnerDto.setEmail(pDto.getEmail());
        partnerDto.setBusinessType(pDto.getBusinessType());
        partnerBO.updatePartner(partnerDto);

        criusAttachmentService.modifyAttachementBatch(pDto.getAttachments(), partnerId, AttachmentBizTypeEnum.PARTNER,
                OperatorConverter.convert(partnerInstanceUpdateServicingDto));
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceDeleteDto);
        Long instanceId = partnerInstanceDeleteDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
        try {
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            if (rel == null || StringUtils.isEmpty(rel.getType())) {
                throw new NullPointerException("partner instance not exists");
            }
            partnerInstanceHandler.handleDelete(partnerInstanceDeleteDto, rel);
            // 同步删除
            syncStationApplyBO.deleteStationApply(rel.getId());
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("delete", JSONObject.toJSONString(partnerInstanceDeleteDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("delete", JSONObject.toJSONString(partnerInstanceDeleteDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    private void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version, ProtocolTypeEnum protocolTypeEnum, boolean needFrozenMoney) {
        ValidateUtils.notNull(taobaoUserId);
        ValidateUtils.notNull(waitFrozenMoney);
        try {
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
                throw new AugeServiceException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_ITEM_UNEXECUTABLE);
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
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId), augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("signSettledProtocol", String.valueOf(taobaoUserId), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void signC2BSettledProtocol(Long taobaoUserId, boolean signedC2BProtocol, boolean isFrozenMoney) throws AugeServiceException {
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
    public void signSettledProtocol(Long taobaoUserId, Double waitFrozenMoney, Long version) throws AugeServiceException {
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
    public void signManageProtocol(Long taobaoUserId, Long version) throws AugeServiceException {
        ValidateUtils.notNull(taobaoUserId);
        try {
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
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("signManageProtocol", String.valueOf(taobaoUserId), augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("signManageProtocol", String.valueOf(taobaoUserId), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean freezeBond(Long taobaoUserId, Double frozenMoney) throws AugeServiceException {
        FreezeBondDto freezeBondDto = new FreezeBondDto();
        freezeBondDto.setOperatorType(OperatorTypeEnum.HAVANA);
        freezeBondDto.setOperator(String.valueOf(taobaoUserId));

        freezeBondDto.setTaobaoUserId(taobaoUserId);
        return freezeBond(freezeBondDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean freezeBond(FreezeBondDto freezeBondDto) throws AugeServiceException {
        ValidateUtils.validateParam(freezeBondDto);
        Long taobaoUserId = freezeBondDto.getTaobaoUserId();
        String accountNo = freezeBondDto.getAccountNo();
        String alipayAccount = freezeBondDto.getAlipayAccount();

        try {
            PartnerStationRel instance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            PartnerLifecycleItems settleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
                    PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.PROCESSING);
            AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
                    AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
            if (!PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState()) || null == settleItems || null == bondMoney
                    || !AccountMoneyStateEnum.WAIT_FROZEN.equals(bondMoney.getState())) {
                throw new AugeServiceException(PartnerExceptionEnum.PARTNER_STATE_NOT_APPLICABLE);
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

            // 流转日志, 合伙人入驻
            // bulidRecordEventForPartnerEnter(stationApplyDetailDto);

            return true;
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("freezeBond", String.valueOf(taobaoUserId), augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("freezeBond", String.valueOf(taobaoUserId), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public boolean openStation(OpenStationDto openStationDto) throws AugeServiceException {
        // 参数校验
        BeanValidator.validateWithThrowable(openStationDto);
        Long instanceId = openStationDto.getPartnerInstanceId();
        String operator = openStationDto.getOperator();
        try {
            // 检查装修中的生命周期（装修，培训）完成后，才能开业
            checkPartnerLifecycleForOpenStation(instanceId);

            if (openStationDto.isImme()) {// 立即开业
                PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
                if (rel == null || !PartnerInstanceStateEnum.DECORATING.getCode().equals(rel.getState())) {
                    throw new AugeServiceException(PartnerExceptionEnum.NO_RECORD);
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
            } else {// 定时开业
                partnerInstanceBO.updateOpenDate(openStationDto.getPartnerInstanceId(), openStationDto.getOpenDate(),
                        openStationDto.getOperator());
                // 同步station_apply
                syncStationApply(SyncStationApplyEnum.UPDATE_BASE, openStationDto.getPartnerInstanceId());
            }
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("openStation", JSONObject.toJSONString(openStationDto), augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("openStation", JSONObject.toJSONString(openStationDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
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
    private void checkPartnerLifecycleForOpenStation(Long instanceId) throws AugeServiceException {
        ValidateUtils.notNull(instanceId);
        PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.DECORATING,
                PartnerLifecycleCurrentStepEnum.PROCESSING);
        if (items == null) {
            // 没有数据 认为是标准化项目之前的数据，直接可以开业
            return;
        }
        if (!PartnerLifecycleCourseStatusEnum.Y.getCode().equals(items.getCourseStatus())) {
            throw new AugeServiceException(PartnerExceptionEnum.PARTNER_NOT_FINISH_COURSE);
        }
        //判断装修是否未付款
//		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
//		StationDecorate decorate=stationDecorateBO.getStationDecorateByStationId(rel.getStationId());
//		if (decorate != null
//				&& StationDecoratePaymentTypeEnum.SELF.getCode().equals(
//						decorate.getPaymentType())
//				&& StationDecorateStatusEnum.UNDECORATE.getCode().equals(
//						decorate.getStatus())) {
//			throw new AugeServiceException(
//					PartnerExceptionEnum.PARTNER_DECORATE_NOT_PAY);
//		}
        if (!PartnerLifecycleDecorateStatusEnum.Y.getCode().equals(items.getDecorateStatus())) {
            throw new AugeServiceException(StationExceptionEnum.STATION_NOT_FINISH_DECORATE);
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
        try {
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
        } catch (Exception e) {
            String error = getErrorMessage("dispachToServiceEvent", JSONObject.toJSONString(openStationDto), e.getMessage());
            logger.error(error, e);
        }
    }

    @Override
    public boolean checkKyPackage() {
        // TODO:检查开业包
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseByPartner(Long taobaoUserId) throws AugeServiceException {
        try {
            // 参数校验
            Assert.notNull(taobaoUserId, "taobaoUserId is null");

            // 合伙人实例状态校验
            PartnerStationRel partnerInstance = partnerInstanceBO.getPartnerInstanceByTaobaoUserId(taobaoUserId,
                    PartnerInstanceStateEnum.SERVICING);
            Assert.notNull(partnerInstance, "没有服务中的合伙人。taobaoUserId = " + taobaoUserId);

            Long instanceId = partnerInstance.getId();
            Long stationId = partnerInstance.getStationId();

            String taobaoUserIdStr = String.valueOf(taobaoUserId);
            OperatorDto operatorDto = new OperatorDto();
            operatorDto.setOperator(taobaoUserIdStr);
            operatorDto.setOperatorType(OperatorTypeEnum.HAVANA);

            // FIXME FHH 合伙人主动申请退出时，为什么不校验是否存在淘帮手，非要到审批时再校验
            partnerInstanceChecker.checkCloseApply(instanceId);
            // 更新合伙人实例状态为停业中
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
            EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);

        } catch (AugeServiceException | AugeBusinessException e) {
            String error = getAugeExceptionErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId), e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("applyCloseByPartner", String.valueOf(taobaoUserId), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
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
            throw new AugeServiceException("partner state is not decorating or servicing.");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void confirmClose(ConfirmCloseDto confirmCloseDto) throws AugeBusinessException, AugeSystemException {
        try {
            // 参数校验
            BeanValidator.validateWithThrowable(confirmCloseDto);

            Long instanceId = confirmCloseDto.getPartnerInstanceId();
            String employeeId = confirmCloseDto.getOperator();
            Boolean isAgree = confirmCloseDto.isAgree();
            PartnerStationRel partnerInstance = partnerInstanceBO.findPartnerInstanceById(instanceId);
            PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(instanceId,
                    PartnerLifecycleBusinessTypeEnum.CLOSING);

            if (!PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstance.getState()) || null == partnerLifecycleItem) {
                throw new RuntimeException("没有停业申请中的合伙人。ConfirmCloseDto = " + JSON.toJSONString(confirmCloseDto));
            }

            Long stationId = partnerInstance.getStationId();

            PartnerLifecycleItemCheckResultEnum confirmExecutable = PartnerLifecycleRuleParser.parseExecutable(
                    PartnerInstanceTypeEnum.valueof(partnerInstance.getType()), PartnerLifecycleItemCheckEnum.confirm,
                    partnerLifecycleItem);
            if (!PartnerLifecycleItemCheckResultEnum.EXECUTABLE.equals(confirmExecutable)) {
                throw new RuntimeException(PartnerInstanceExceptionEnum.PARTNER_INSTANCE_ITEM_UNEXECUTABLE.getDesc());
            }
            PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
            partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
            partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
            partnerLifecycle.copyOperatorDto(confirmCloseDto);

            if (isAgree) {
                // 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
                PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerInstance.getType());
                partnerInstanceHandler.validateClosePreCondition(partnerType, partnerInstance);

                // 更新合伙人实例，已停业
                PartnerInstanceDto partnerInstanceDto = new PartnerInstanceDto();
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
                dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSED, confirmCloseDto);
            } else {
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
            }
        } catch (AugeBusinessException e) {
            String error = getAugeExceptionErrorMessage("confirmClose", JSONObject.toJSONString(confirmCloseDto), e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("confirmClose", JSONObject.toJSONString(confirmCloseDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseByManager(ForcedCloseDto forcedCloseDto) throws AugeBusinessException, AugeSystemException {
        applyCloseInternal(forcedCloseDto, PartnerInstanceCloseTypeEnum.WORKER_QUIT);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyCloseBySystem(ForcedCloseDto forcedCloseDto) {
        applyCloseInternal(forcedCloseDto, PartnerInstanceCloseTypeEnum.SYSTEM_QUIT);
    }

    private void applyCloseInternal(ForcedCloseDto forcedCloseDto, PartnerInstanceCloseTypeEnum closeType) {
        // 参数校验
        BeanValidator.validateWithThrowable(forcedCloseDto);
        try {

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
        } catch (AugeBusinessException e) {
            String error = getAugeExceptionErrorMessage("applyCloseInternal", "ForcedCloseDto =" + JSON.toJSONString(forcedCloseDto), e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("applyCloseInternal", "ForcedCloseDto =" + JSON.toJSONString(forcedCloseDto), e.getMessage());
            logger.error(error, e);
            throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

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
    public void applyQuitByManager(QuitStationApplyDto quitDto) throws AugeBusinessException, AugeSystemException {
        try {
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

            // 保存退出申请单
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
            dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_QUITTING, quitDto);

            // 失效tair
        } catch (AugeBusinessException|AugeServiceException e) {
            String error = getAugeExceptionErrorMessage("applyQuitByManager", "QuitStationApplyDto =" + JSON.toJSONString(quitDto),
                    e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("applyQuitByManager", "QuitStationApplyDto =" + JSON.toJSONString(quitDto), e.getMessage());
            logger.error(error, e);
            throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
        }
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
    public Long applySettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getType());

        try {
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
            return instanceId;
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("applySettle", JSON.toJSONString(partnerInstanceDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("applySettle", JSON.toJSONString(partnerInstanceDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
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
    public void quitPartnerInstance(PartnerInstanceQuitDto partnerInstanceQuitDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceQuitDto);
        Long instanceId = partnerInstanceQuitDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
        try {
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            if (rel == null || StringUtils.isEmpty(rel.getType())) {
                if (PartnerInstanceStateEnum.QUIT.getCode().equals(rel.getState())) {
                    return;
                } else if (!PartnerInstanceStateEnum.QUITING.getCode().equals(rel.getState())) {
                    throw new RuntimeException(CommonExceptionEnum.DATA_UNNORMAL.getDesc());
                }
            }
            partnerInstanceHandler.handleQuit(partnerInstanceQuitDto, PartnerInstanceTypeEnum.valueof(rel.getType()));
            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("quitPartnerInstance", JSON.toJSONString(partnerInstanceQuitDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("quitPartnerInstance", JSON.toJSONString(partnerInstanceQuitDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Override
    public Long applyResettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        return null;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void degradePartnerInstance(PartnerInstanceDegradeDto degradeDto) throws AugeServiceException {
        ValidateUtils.validateParam(degradeDto);
        Long instanceId = degradeDto.getInstanceId();
        Long parentTaobaoUserId = degradeDto.getParentTaobaoUserId();
        ValidateUtils.notNull(instanceId);
        ValidateUtils.notNull(parentTaobaoUserId);
        try {
            PartnerInstanceDto rel = partnerInstanceBO.getPartnerInstanceById(instanceId);
            Assert.notNull(rel, "partner instance is null");
            if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), rel.getType().getCode())) {
                throw new RuntimeException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TYPE_FAIL.getDesc());
            }

            if (!PartnerInstanceStateEnum.canDegradeStateCodeList().contains(rel.getState().getCode())) {
                throw new RuntimeException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_STATE_FAIL.getDesc());
            }
            int tpaCount = partnerInstanceBO.getActiveTpaByParentStationId(rel.getParentStationId());

            if (tpaCount > 0) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_HAS_TPA);
            }

            PartnerStationRel parentRel = partnerInstanceBO.getActivePartnerInstance(parentTaobaoUserId);
            if (parentRel == null) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_ERROR);
            }
            // 归属合伙人是否属于服务中状态
            if (!StringUtils.equals(PartnerInstanceStateEnum.SERVICING.getCode(), parentRel.getState())) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_NOT_SERVICING);
            }

            if (!StringUtils.equals(PartnerInstanceTypeEnum.TP.getCode(), parentRel.getType())) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_TYPE_NOT_TP);
            }

            if (parentTaobaoUserId.longValue() == rel.getTaobaoUserId().longValue()) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_TAOBAOUSERID_SAME);
            }

            // 所归属的合伙人的淘帮手不能大于等于5个
            int parentTpaCount = partnerInstanceBO.getActiveTpaByParentStationId(parentRel.getParentStationId());
            Integer maxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(parentRel.getId());
            if (parentTpaCount >= maxChildNum) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_TARGET_PARTNER_HAS_TPA_MAX);
            }

            // 判断是不是归属同一个县
            Station parentStation = stationBO.getStationById(parentRel.getStationId());
            Station station = stationBO.getStationById(rel.getStationId());
            if (parentStation.getApplyOrg().longValue() != station.getApplyOrg().longValue()) {
                throw new AugeServiceException(PartnerInstanceExceptionEnum.DEGRADE_PARTNER_ORG_NOT_SAME);
            }
            generalTaskSubmitService.submitDegradePartner(rel, PartnerInstanceConverter.convert(parentRel), degradeDto);
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("degradePartnerInstance", JSON.toJSONString(degradeDto), augeException.toString());
            logger.error(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("degradePartnerInstance", JSON.toJSONString(degradeDto), e.getMessage());
            logger.warn(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void applySettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto) throws AugeServiceException {
        ValidateUtils.validateParam(settleSuccessDto);
        Long instanceId = settleSuccessDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
        try {
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
            Assert.notNull(rel, "partner instance not exists");
            partnerInstanceHandler.handleSettleSuccess(settleSuccessDto, rel);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
        } catch (Exception e) {
            String error = getErrorMessage("applySettleSuccess", JSON.toJSONString(settleSuccessDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateSettle(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getStationDto());
        ValidateUtils.notNull(partnerInstanceDto.getPartnerDto());
        ValidateUtils.notNull(partnerInstanceDto.getStationId());
        try {
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(partnerInstanceDto.getStationId());
            Assert.notNull(rel, "partner instance not exists");
            Assert.notNull(rel.getType(), "partner instance type is null");

            boolean canUpdate = partnerInstanceHandler.handleValidateUpdateSettle(rel.getId(),

                    PartnerInstanceTypeEnum.valueof(rel.getType()));
            if (!canUpdate) {
                throw new AugeServiceException(CommonExceptionEnum.RECORD_CAN_NOT_UPDATE);
            }
            updateStation(rel.getStationId(), partnerInstanceDto);

            PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            partnerDto.copyOperatorDto(partnerInstanceDto);
            partnerDto.setId(rel.getPartnerId());

            updatePartner(partnerDto);
            partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);

            // 同步station_apply
            syncStationApply(SyncStationApplyEnum.UPDATE_ALL, rel.getId());
        } catch (Exception e) {
            String error = getErrorMessage("updateSettle", JSONObject.toJSONString(partnerInstanceDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }

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
    public void auditSettleByManager(AuditSettleDto auditSettleDto) throws AugeServiceException {
        ValidateUtils.validateParam(auditSettleDto);
        Long partnerInstanceId = auditSettleDto.getPartnerInstanceId();
        Boolean isAgree = auditSettleDto.getIsAgree();
        ValidateUtils.notNull(partnerInstanceId);
        ValidateUtils.notNull(isAgree);
        try {
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
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("auditSettleByManager", JSONObject.toJSONString(auditSettleDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void degradePartnerInstanceSuccess(DegradePartnerInstanceSuccessDto degradePartnerInstanceSuccessDto)
            throws AugeServiceException {

        ValidateUtils.validateParam(degradePartnerInstanceSuccessDto);

        Long instanceId = degradePartnerInstanceSuccessDto.getInstanceId();
        ValidateUtils.notNull(instanceId);
        try {
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
        } catch (Exception e) {
            String error = getErrorMessage("degradePartnerInstanceSuccess", JSON.toJSONString(degradePartnerInstanceSuccessDto),
                    e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }

    }

    private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
                operator);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void evaluatePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) throws AugeServiceException {
        try {
            // 根据taobao_user_id和station_id失效以前的评级is_valid＝'n'
            partnerInstanceLevelBO.invalidatePartnerInstanceLevelBefore(partnerInstanceLevelDto);
            // 保存数据库
            partnerInstanceLevelBO.addPartnerInstanceLevel(partnerInstanceLevelDto);
            // 发送评级变化事件: 类型为系统评定
            PartnerInstanceLevelChangeEvent event = PartnerInstanceLevelEventConverter
                    .convertLevelChangeEvent(partnerInstanceLevelDto.getEvaluateType(), partnerInstanceLevelDto);
            EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_LEVEL_CHANGE_EVENT, event);
        } catch (Exception e) {
            logger.error("EvaluatePartnerInstanceLevelError:" + JSON.toJSONString(partnerInstanceLevelDto), e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void changeTP(ChangeTPDto changeTPDto) throws AugeServiceException {
        ValidateUtils.notNull(changeTPDto);
        try {
            Long partnerInstanceId = changeTPDto.getPartnerInstanceId();
            Long newParentStationId = changeTPDto.getNewParentStationId();

            PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(changeTPDto.getPartnerInstanceId());
            if (!PartnerInstanceTypeEnum.TPA.equals(partnerInstanceDto.getType())) {
                throw new AugeServiceException("type is not tpa");
            }
            if (partnerInstanceExtService.validateChildNum(newParentStationId)) {
                throw new AugeServiceException("the partner's tpa number is upper limit");
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
        } catch (AugeServiceException | AugeBusinessException augeException) {
            String error = getAugeExceptionErrorMessage("changeTP", JSONObject.toJSONString(changeTPDto),
                    augeException.toString());
            logger.warn(error, augeException);
            throw augeException;
        } catch (Exception e) {
            String error = getErrorMessage("changeTP", JSONObject.toJSONString(changeTPDto), e.getMessage());
            logger.error(error, e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }

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
    public void reService(Long instanceId, String operator) throws AugeServiceException {
        ValidateUtils.notNull(instanceId);
        ValidateUtils.notNull(operator);
        PartnerStationRel psl = partnerInstanceBO.findPartnerInstanceById(instanceId);
        if (psl.getIsCurrent().equals("n")) {
            throw new AugeServiceException("the partner is not current");
        }
        partnerInstanceBO.reService(instanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.SERVICING, operator);
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
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
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
        PartnerCourseRecord record = partnerPeixunBO.initPeixunRecord(psl.getTaobaoUserId(),
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
        }
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
    public void promotePartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) throws AugeServiceException {
        try {
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
        } catch (Exception e) {
            logger.error("PromotePartnerInstanceLevelError:" + JSON.toJSONString(partnerInstanceLevelDto), e);
            throw new AugeServiceException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void upgradePartnerInstance(PartnerInstanceUpgradeDto upgradeDto) throws AugeServiceException, AugeSystemException {
        // 参数校验
        BeanValidator.validateWithThrowable(upgradeDto);

        StationDto stationDto = upgradeDto.getStationDto();
        Long stationId = stationDto.getId();
        ValidateUtils.notNull(stationId);
        PartnerDto partnerDto = upgradeDto.getPartnerDto();

        Long partnerId = partnerDto.getId();
        ValidateUtils.notNull(partnerId);
        try {
            //村点信息备份
            Map<String, String> feature = partnerTypeChangeApplyBO.backupStationInfo(stationId);
            // 更新村点信息
            stationDto.setState(StationStateEnum.INVALID);
            stationDto.setStatus(StationStatusEnum.NEW);
            stationDto.copyOperatorDto(upgradeDto);
            updateStation(stationDto);

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
        } catch (AugeServiceException | AugeBusinessException e) {
            String error = getAugeExceptionErrorMessage("upgradePartnerInstance", "PartnerInstanceUpgradeDto =" + JSON.toJSONString(upgradeDto), e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("upgradePartnerInstance", "PartnerInstanceUpgradeDto =" + JSON.toJSONString(upgradeDto), e.getMessage());
            logger.error(error, e);
            throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
        }
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
        // 新的合伙人实例
        Long nextInstanceId = addPartnerInstanceRel(partnerInstanceDto, stationId, partnerId);

        // 不同类型合伙人，执行不同的生命周期
        partnerInstanceDto.setId(nextInstanceId);
        partnerInstanceHandler.handleApplySettle(partnerInstanceDto, partnerInstanceDto.getType());
        return nextInstanceId;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Boolean thawMoney(Long instanceId) throws AugeServiceException, AugeSystemException {
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
                throw new AugeServiceException("PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:" + instanceId);
            }
            accountNo = accountDto.getAccountNo();
        }

        generalTaskSubmitService.submitThawMoneyTask(instanceId, accountNo, frozenMoney, operatorDto);

        return Boolean.TRUE;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void thawMoneySuccess(PartnerInstanceThrawSuccessDto partnerInstanceThrawSuccessDto) throws AugeServiceException, AugeSystemException {
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
    public void cancelUpgradePartnerInstance(CancelUpgradePartnerInstance cancelDto) throws AugeServiceException, AugeSystemException {
        // 参数校验
        BeanValidator.validateWithThrowable(cancelDto);

        try {
            Long instanceId = cancelDto.getInstanceId();
            PartnerLifecycleItems lifeItem = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING, PartnerLifecycleCurrentStepEnum.PROCESSING);
            if (PartnerLifecycleBondEnum.HAS_FROZEN.getCode().equals(lifeItem.getBond())) {
                throw new AugeServiceException("保证金已经冻结，不可以撤销。");
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
        } catch (AugeServiceException | AugeBusinessException e) {
            String error = getAugeExceptionErrorMessage("cancelUpgradePartnerInstance", "CancelUpgradePartnerInstance =" + JSON.toJSONString(cancelDto), e.toString());
            logger.warn(error, e);
            throw e;
        } catch (Exception e) {
            String error = getErrorMessage("cancelUpgradePartnerInstance", "CancelUpgradePartnerInstance =" + JSON.toJSONString(cancelDto), e.getMessage());
            logger.error(error, e);
            throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
        }
    }

    /**
     * 更新服务站地址信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateStationAddress(Long taobaoUserId, StationDto updateStation, boolean isSendMail)
            throws AugeServiceException {
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
                sendMail(updateStation);
            }
        }
    }

    private void sendMail(StationDto station) {
        try {
            Map<String, Object> contentMap = Maps.newHashMap();
            contentMap.put("station_id", station.getId() + "");
            contentMap.put("station_name", station.getName());
            if (station.getFeature() != null) {
                contentMap.put("type", station.getFeature().get("st_fk_type"));
                contentMap.put("description", station.getFeature().get("st_fk_desc"));
            }

            BatchMailDto mailDto = new BatchMailDto();
            mailDto.setMailAddresses(mailConfiguredProperties.getAddressUpdateNotifyMailList());
            mailDto.setTemplateId(mailConfiguredProperties.getAddressUpdateNotifyMailTemplateId());
            mailDto.setMessageTypeId(mailConfiguredProperties.getAddressUpdateNotifyMailMessageTypeId());
            mailDto.setSourceId(mailConfiguredProperties.getAddressUpdateNotifyMailSourceId());
            mailDto.setOperator(station.getOperator());
            mailDto.setContentMap(contentMap);

            generalTaskSubmitService.submitMailTask(mailDto);
        } catch (Exception e) {
            logger.error("updateStationAddress [sendMail] address = {}, {}", String.join(",", mailConfiguredProperties.getAddressUpdateNotifyMailList()), e);
            throw new AugeServiceException("updateStationAddress [sendMail] error: " + e.getMessage());
        }
    }

    /**
     * 更新服务站经纬度信息
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateStationLngLat(Long taobaoUserId, StationDto updateStation) throws AugeServiceException {
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
            if (isNeedToUpdateCainiaoStation(instance.getState().getCode())) {
                SyncModifyLngLatDto lngDto = new SyncModifyLngLatDto();
                lngDto.setPartnerInstanceId(instanceId);
                lngDto.setLng(updateStation.getAddress().getLng());
                lngDto.setLat(updateStation.getAddress().getLat());
                caiNiaoService.modifyLngLatToCainiao(lngDto);
            }
        }
    }

    @Override
    public void closeCainiaoStationForTpa(Long partnerInstanceId, OperatorDto operatorDto) throws AugeServiceException {
        caiNiaoService.closeCainiaoStationForTpa(partnerInstanceId, operatorDto);
    }
}
