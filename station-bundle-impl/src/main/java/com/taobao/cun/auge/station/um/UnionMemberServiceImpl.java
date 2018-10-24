package com.taobao.cun.auge.station.um;

import com.alibaba.fastjson.JSON;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.lifecycle.validator.UmLifeCycleValidator;
import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.lock.TairManualReleaseDistributeLock;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.payment.account.utils.PaymentAccountDtoUtil;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberAddDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberCheckDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberStateChangeDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 优盟增删改服务
 *
 * @author haihu.fhh
 */
@Service("unionMemberService")
@HSFProvider(serviceInterface = UnionMemberService.class, clientTimeout = 10000)
public class UnionMemberServiceImpl implements UnionMemberService {

    private static final Logger logger = LoggerFactory.getLogger(UnionMemberService.class);

    @Autowired
    private PaymentAccountQueryService paymentAccountQueryService;

    @Autowired
    private TaobaoAccountBo taobaoAccountBo;

    @Autowired
    private PartnerInstanceQueryService partnerInstanceQueryService;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private PartnerBO partnerBO;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private StateMachineService stateMachineService;

    @Autowired
    private UmLifeCycleValidator umLifeCycleValidator;

    @Autowired
    private ManualReleaseDistributeLock distributeLock;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public AliPaymentAccountDto checkUnionMember(UnionMemberCheckDto checkDto) {
        BeanValidator.validateWithThrowable(checkDto);
        try {
            Long parentStationId = checkDto.getParentStationId();
            String taobaoNick = checkDto.getTaobaoNick();

            AliPaymentAccountDto paymentAccountDto = paymentAccountQueryService
                .queryStationMemberPaymentAccountByNick(
                    taobaoNick);
            Long taobaoUserId = paymentAccountDto.getTaobaoUserId();

            if (taobaoAccountBo.isTaobaoBuyerOrSellerBlack(taobaoUserId)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "经过综合评定，该账号存在安全风险，更换其他账号。");
            }

            if (taobaoAccountBo.isAlipayRiskUser(paymentAccountDto)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "经过综合评定，该账号存在安全风险，更换其他账号！");
            }


            PartnerInstanceDto piDto = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
            if (piDto != null) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该账号已经合作，不能重复使用");
            }
            //PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(
            //    parentStationId);
            //if (partnerInstance == null || partnerInstance.getStationDto() == null) {
            //    throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "村小二站点不存在");
            //}
            PaymentAccountDtoUtil.hidepaymentAccount(paymentAccountDto);
            return paymentAccountDto;
        } catch (AugeBusinessException e) {
            logger.warn(JSON.toJSONString(checkDto), e);
            throw e;
        } catch (Exception e) {
            logger.error(JSON.toJSONString(checkDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Long addUnionMember(UnionMemberAddDto addDto) {
        BeanValidator.validateWithThrowable(addDto);
        String taobaoNick = addDto.getTaobaoNick();
        try {
            boolean lockResult = distributeLock.lock("unionMember-addUnionMember", taobaoNick, 100);
            if (!lockResult) {
                logger.error("distributeLock failed: {}", JSON.toJSONString(addDto));
                throw new AugeSystemException("请勿重复提交");
            }

            AliPaymentAccountDto aliPaymentAccountDto = paymentAccountQueryService
                    .queryStationMemberPaymentAccountByNick(taobaoNick);
            Long taobaoUserId = aliPaymentAccountDto.getTaobaoUserId();

            PartnerInstanceDto existedPiDto = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
            if (existedPiDto != null) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该账号已经合作，不能重复使用");
            }

            Station parentStationDto = stationBO.getStationById(addDto.getParentStationId());
            String parentCityCode = parentStationDto.getCity();
            Address address = addDto.getAddress();
            //优盟店铺地址必须和当前村小二在同一个行政市域内（第二级地址保持一致）
            if (null != parentCityCode && !parentCityCode.equals(address.getCity())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,
                    "优盟店地址必须和当前村小二在同一个行政市域内");
            }

            StationDto sDto = new StationDto();
            //和村小二一个组织
            sDto.setApplyOrg(parentStationDto.getApplyOrg());
            sDto.setAddress(address);
            sDto.setName(addDto.getStationName());
            sDto.setFormat(addDto.getFormat());
            sDto.setCovered(String.valueOf(addDto.getCovered()));
            sDto.setDescription(addDto.getDescription());
//            LatitudeUtil.buildPOI(address);

            PartnerInstanceDto piDto = new PartnerInstanceDto();
            piDto.setOperator(addDto.getOperator());
            piDto.setOperatorOrgId(addDto.getOperatorOrgId());
            piDto.setOperatorType(addDto.getOperatorType());

            PartnerDto pDto = new PartnerDto();
            pDto.setTaobaoUserId(taobaoUserId);
            pDto.setTaobaoNick(taobaoNick);
            pDto.setName(aliPaymentAccountDto.getFullName());
            pDto.setAlipayAccount(aliPaymentAccountDto.getAlipayId());
            pDto.setIdenNum(aliPaymentAccountDto.getIdCardNumber());
            pDto.setMobile(addDto.getMobile());
            pDto.setBusinessType(PartnerBusinessTypeEnum.PARTTIME);

            piDto.setType(PartnerInstanceTypeEnum.UM);
            piDto.setTaobaoUserId(taobaoUserId);
            piDto.setStationDto(sDto);
            piDto.setPartnerDto(pDto);
            piDto.setParentStationId(addDto.getParentStationId());

            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(piDto, StateMachineEvent.SETTLING_EVENT);
            stateMachineService.executePhase(phaseEvent);
            return piDto.getStationDto().getId();
        } catch (AugeBusinessException e) {
            logger.warn(JSON.toJSONString(addDto), e);
            throw e;
        } catch (Exception e) {
            logger.error(JSON.toJSONString(addDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }finally {
            //distributeLock.unlock("unionMember-addUnionMember", taobaoNick);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateUnionMember(UnionMemberUpdateDto updateDto) {
        BeanValidator.validateWithThrowable(updateDto);
        try {
            String operator = updateDto.getOperator();
            Long parentTaobaoUserId = Long.valueOf(operator);
            //所属村小二
            PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
                parentTaobaoUserId);

            //优盟实例
            Long stationId = updateDto.getStationId();
            PartnerInstanceDto umInstanceDto = getUmPartnerInstanceDto(updateDto, stationId);
            Long parentStationId = umInstanceDto.getParentStationId();

            if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "不能修改非自己名下的优盟店");
            }

            String parentCityCode = partnerInstanceDto.getStationDto().getAddress().getCity();
            Address address = updateDto.getAddress();
            //优盟店铺地址必须和当前村小二在同一个行政市域内（第二级地址保持一致）
            if (null != parentCityCode && !parentCityCode.equals(address.getCity())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,
                    "优盟店地址必须和当前村小二在同一个行政市域内");
            }

            //前置条件校验
            umLifeCycleValidator.validateUpdate(updateDto, umInstanceDto.getPartnerDto().getName());

            StationDto stationDto = new StationDto();

            stationDto.setId(stationId);
            stationDto.setName(updateDto.getStationName());
            stationDto.setAddress(address);
//            LatitudeUtil.buildPOI(address);

            stationDto.setFormat(updateDto.getFormat());
            if (null != updateDto.getCovered()) {
                stationDto.setCovered(String.valueOf(updateDto.getCovered()));
            }
            stationDto.setDescription(updateDto.getDescription());
            stationDto.copyOperatorDto(updateDto);

            stationBO.updateStation(stationDto);

            //更新优盟手机号
            updateUnionMemberMobile(updateDto);
        } catch (AugeBusinessException e) {
            logger.warn(JSON.toJSONString(updateDto), e);
            throw e;
        } catch (Exception e) {
            logger.error(JSON.toJSONString(updateDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }

    /**
     * 更新优盟手机号
     *
     * @param updateDto
     */
    private void updateUnionMemberMobile(UnionMemberUpdateDto updateDto) {
        String mobile = updateDto.getMobile();
        //当前优盟只能修改手机号
        if (StringUtils.isNotBlank(mobile)) {
            //校验手机号格式
            PartnerValidator.validatePartnerMobile(mobile);

            Long stationId = updateDto.getStationId();
            PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);

            Long partnerId = rel.getPartnerId();
            //验证手机号唯一性
            if (!partnerInstanceBO.judgeMobileUseble(null, partnerId, mobile)) {
                throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该手机号已被使用");
            }
            //更换优盟手机号
            PartnerDto partnerDto = new PartnerDto();
            partnerDto.copyOperatorDto(updateDto);
            partnerDto.setId(partnerId);
            partnerDto.setMobile(mobile);
            partnerBO.updatePartner(partnerDto);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void openOrCloseUnionMember(UnionMemberStateChangeDto stateChangeDto) {
        BeanValidator.validateWithThrowable(stateChangeDto);
        try {
            String operator = stateChangeDto.getOperator();
            Long parentTaobaoUserId = Long.valueOf(operator);
            Long stationId = stateChangeDto.getStationId();

            //所属村小二实例
            PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
                parentTaobaoUserId);

            //优盟实例
            PartnerInstanceDto umInstanceDto = getUmPartnerInstanceDto(stateChangeDto, stationId);
            Long parentStationId = umInstanceDto.getParentStationId();

            if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "不能管理非自己名下的优盟店");
            }

            PartnerInstanceStateEnum nowStateEnum = umInstanceDto.getState();
            UnionMemberStateEnum targetStateEnum = stateChangeDto.getState();

            //组装操作人
            umInstanceDto.copyOperatorDto(stateChangeDto);

            //开通
            if (UnionMemberStateEnum.SERVICING.equals(targetStateEnum)) {
                //当前状态必须为未开通和已关闭
                if (PartnerInstanceStateEnum.SETTLING.equals(nowStateEnum) || PartnerInstanceStateEnum.CLOSED.equals(
                    nowStateEnum)) {
                    LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(umInstanceDto,
                        StateMachineEvent.SERVICING_EVENT);
                    stateMachineService.executePhase(phaseEvent);
                } else {
                    throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "优盟店当前状态不可开通");
                }
                //关闭
            } else if (UnionMemberStateEnum.CLOSED.equals(targetStateEnum)) {
                //只有已开通，可以关闭
                if (PartnerInstanceStateEnum.SERVICING.equals(nowStateEnum)) {
                    LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(umInstanceDto,
                        StateMachineEvent.CLOSED_EVENT);
                    stateMachineService.executePhase(phaseEvent);
                } else {
                    throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "优盟店当前状态不可关闭");
                }
            }
        } catch (AugeBusinessException e) {
            logger.warn(JSON.toJSONString(stateChangeDto), e);
            throw e;
        } catch (Exception e) {
            logger.error(JSON.toJSONString(stateChangeDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }

    private PartnerInstanceDto getUmPartnerInstanceDto(OperatorDto operatorDto, Long stationId) {
        Long instanceId = partnerInstanceBO.findPartnerInstanceIdByStationId(stationId);

        PartnerInstanceCondition condition = new PartnerInstanceCondition();
        condition.setInstanceId(instanceId);
        condition.setNeedPartnerInfo(Boolean.TRUE);
        condition.setNeedStationInfo(Boolean.TRUE);
        condition.setNeedDesensitization(Boolean.FALSE);
        condition.setNeedPartnerLevelInfo(Boolean.FALSE);
        condition.copyOperatorDto(operatorDto);

        return partnerInstanceQueryService.queryInfo(condition);
    }

    @Override
    public void deleteUnionMember(Long stationId, OperatorDto operatorDto) {
        BeanValidator.validateWithThrowable(operatorDto);
        if (null == stationId || 0 == stationId) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "stationId is null");
        }

        try {
            //村小二账号
            String operator = operatorDto.getOperator();
            Long parentTaobaoUserId = Long.valueOf(operator);

            //所属村小二实例
            PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
                parentTaobaoUserId);

            //优盟实例
            PartnerInstanceDto umInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(
                stationId);
            Long parentStationId = umInstanceDto.getParentStationId();

            if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "不能删除非自己名下的优盟店");
            }

            PartnerInstanceStateEnum umState = umInstanceDto.getState();
            if (PartnerInstanceStateEnum.SERVICING.equals(umState)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "优盟店已开通，不能删除");
            }

            Long umInstanceId = umInstanceDto.getId();

            partnerInstanceBO.deletePartnerStationRel(umInstanceId, operator);
            stationBO.deleteStation(stationId, operator);
        } catch (AugeBusinessException e) {
            logger.warn("stationId=" + stationId + ",operator = " + JSON.toJSONString(operatorDto), e);
            throw e;
        } catch (Exception e) {
            logger.error("stationId=" + stationId + ",operator = " + JSON.toJSONString(operatorDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }
}
