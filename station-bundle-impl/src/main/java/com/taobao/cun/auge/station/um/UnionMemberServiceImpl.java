package com.taobao.cun.auge.station.um;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.lifecycle.validator.UmLifeCycleValidator;
import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.payment.account.utils.PaymentAccountDtoUtil;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.bo.*;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.UnionMemberPageCondition;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.*;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;
import com.taobao.cun.auge.station.validate.PartnerValidator;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private PartnerApplyBO partnerApplyBO;

    @Autowired
    private StationNumConfigBO stationNumConfigBO;

    @Autowired
    private UnionMemberQueryService unionMemberQueryService;

    @Autowired
    private GeneralTaskSubmitService generalTaskSubmitService;

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
            boolean lockResult = distributeLock.lock("unionMember-addUnionMember", taobaoNick, 3);
            if (!lockResult) {
                logger.error("distributeLock failed: {}", JSON.toJSONString(addDto));
                throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE,
                        "请勿重复提交");
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
        } finally {
            distributeLock.unlock("unionMember-addUnionMember", taobaoNick);
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
            Long stationId = stateChangeDto.getStationId();

            //优盟实例
            PartnerInstanceDto umInstanceDto = getUmPartnerInstanceDto(stateChangeDto, stationId);
            Long parentStationId = umInstanceDto.getParentStationId();

            //只有村小二操作时，校验优盟归属权限
            validateUmBelongAuthor(stateChangeDto, parentStationId);

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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void quitUnionMember(Long stationId, OperatorDto operatorDto) {
        BeanValidator.validateWithThrowable(operatorDto);
        if (null == stationId || 0 == stationId) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE, "stationId is null");
        }
        try {
            //优盟实例
            PartnerInstanceDto umInstanceDto = getUmPartnerInstanceDto(operatorDto, stationId);
            Long parentStationId = umInstanceDto.getParentStationId();

            //只有村小二操作时，校验优盟归属权限
            validateUmBelongAuthor(operatorDto, parentStationId);

            PartnerInstanceStateEnum nowStateEnum = umInstanceDto.getState();
            UnionMemberStateEnum targetStateEnum = UnionMemberStateEnum.QUIT;

            //组装操作人
            umInstanceDto.copyOperatorDto(operatorDto);

            if (!UnionMemberStateEnum.QUIT.equals(targetStateEnum)) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "优盟目标状态必须为已退出");
            }
            //只有已关闭，可以退出
            if (PartnerInstanceStateEnum.CLOSED.equals(nowStateEnum)) {
                LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(umInstanceDto,
                        StateMachineEvent.QUIT_EVENT);
                stateMachineService.executePhase(phaseEvent);
            } else {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "优盟店当前状态不可退出");
            }
        } catch (AugeBusinessException e) {
            logger.warn("stationId=" + stationId + "operator=" + JSON.toJSONString(operatorDto), e);
            throw e;
        } catch (Exception e) {
            logger.error("stationId=" + stationId + "operator=" + JSON.toJSONString(operatorDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }

    /**
     * 村小二来操作优盟时，校验优盟归属权限
     */
    private void validateUmBelongAuthor(OperatorDto operatorDto, Long parentStationId) {
        //只有村小二，havana类型才校验，其他不校验
        if (!OperatorTypeEnum.HAVANA.equals(operatorDto.getOperatorType())) {
            return;
        }
        Long parentTaobaoUserId = Long.valueOf(operatorDto.getOperator());
        //所属村小二实例
        PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
                parentTaobaoUserId);

        if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "不能管理非自己名下的优盟店");
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void closeUnionMembers(BatchCloseUnionMemberDto batchCloseUnionMemberDto) {
        BeanValidator.validateWithThrowable(batchCloseUnionMemberDto);

        Long parentStationId = batchCloseUnionMemberDto.getParentStationId();
        String operator = batchCloseUnionMemberDto.getOperator();

        PageDto<UnionMemberDto> umList = getUnionMembers(parentStationId, UnionMemberStateEnum.SERVICING);
        if (CollectionUtils.isEmpty(umList.getItems())) {
            return;
        }
        for (UnionMemberDto unionMemberDto : umList.getItems()) {
            Long umInstanceId = unionMemberDto.getInstanceId();
            Long umStationId = unionMemberDto.getStationDto().getId();

            stationBO.changeState(umStationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSED, operator);
            partnerInstanceBO.changeState(umInstanceId, PartnerInstanceStateEnum.SERVICING, PartnerInstanceStateEnum.CLOSED,
                    operator);
            //去标
            addRemoveTagTask(unionMemberDto, batchCloseUnionMemberDto);
        }
    }

    private void addRemoveTagTask(UnionMemberDto unionMemberDto, OperatorDto operatorDto) {
        Long instanceId = unionMemberDto.getInstanceId();

        String operatorId = operatorDto.getOperator();
        Long taobaoUserId = unionMemberDto.getPartnerDto().getTaobaoUserId();
        String taobaoNick = unionMemberDto.getPartnerDto().getTaobaoNick();
        PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.UM;
        generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId,
                instanceId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void quitUnionMembers(BatchQuitUnionMemberDto batchQuitUnionMemberDto) {
        BeanValidator.validateWithThrowable(batchQuitUnionMemberDto);
        Long parentStationId = batchQuitUnionMemberDto.getParentStationId();
        String operator = batchQuitUnionMemberDto.getOperator();

        PageDto<UnionMemberDto> umList = getUnionMembers(parentStationId, UnionMemberStateEnum.CLOSED);
        if (CollectionUtils.isEmpty(umList.getItems())) {
            return;
        }

        for (UnionMemberDto unionMemberDto : umList.getItems()) {
            Long umInstanceId = unionMemberDto.getInstanceId();
            Long umStationId = unionMemberDto.getStationDto().getId();

            partnerInstanceBO.changeState(umInstanceId, PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUIT,
                    operator);
            stationBO.changeState(umStationId, StationStatusEnum.CLOSED, StationStatusEnum.QUIT, operator);
        }
    }

    /**
     * 根据父站点id，查询某种状态的优盟
     *
     * @param parentStationId
     * @param state
     * @return
     */
    private PageDto<UnionMemberDto> getUnionMembers(Long parentStationId, UnionMemberStateEnum state) {
        UnionMemberPageCondition con = new UnionMemberPageCondition();
        con.setOperator(OperatorTypeEnum.SYSTEM.getCode());
        con.setOperatorType(OperatorTypeEnum.SYSTEM);
        con.setParentStationId(parentStationId);
        con.setState(state);
        con.setPageNum(1);
        con.setPageSize(10000);
        PageDto<UnionMemberDto> umList = unionMemberQueryService.queryByPage(con);
        return umList;
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
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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

            restartPartnerApply(umInstanceDto.getTaobaoUserId());
        } catch (AugeBusinessException e) {
            logger.warn("stationId=" + stationId + ",operator = " + JSON.toJSONString(operatorDto), e);
            throw e;
        } catch (Exception e) {
            logger.error("stationId=" + stationId + ",operator = " + JSON.toJSONString(operatorDto), e);
            throw new AugeSystemException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "系统异常");
        }
    }

    private void restartPartnerApply(Long taobaoUserId) {
        PartnerApplyDto partnerApplyDto = new PartnerApplyDto();
        partnerApplyDto.setTaobaoUserId(taobaoUserId);

        partnerApplyDto.setOperator("system");
        partnerApplyBO.restartPartnerApplyByUserId(partnerApplyDto);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateUmstationNum(String stationNums) {
        if (StringUtils.isEmpty(stationNums)) {
            return;
        }
        String[] stationNumArray = stationNums.split(",");

        for (String stationNum : stationNumArray) {
            StationNumConfigTypeEnum typeEnum = StationNumConfigTypeEnum.UM;

            Station station = stationBO.getStationByStationNum(stationNum);
            if (null == station) {
                continue;
            }

            String province = station.getProvince();
            if (null == province || "" == province) {
                continue;
            }

            String newStationNum = stationNumConfigBO.createUmStationNum(province, typeEnum, 0);

            stationBO.updateStationNum(station.getId(), newStationNum);
        }
    }

    @Override
    public void submitClosedUmTask(Long parentStationId) {
        generalTaskSubmitService.submitClosedUmTask(parentStationId, OperatorDto.defaultOperator());
    }

    @Override
    public void submitQuitUmTask(Long parentStationId) {
        generalTaskSubmitService.submitQuitUmTask(parentStationId, OperatorDto.defaultOperator());
    }

    @Override
    public void testTpClosedEvent(Long instanceId) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(PartnerInstanceStateChangeEnum.CLOSED, partnerInstanceDto,
                OperatorDto.defaultOperator());
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }

    @Override
    public void testTpQuitedEvent(Long instanceId) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(PartnerInstanceStateChangeEnum.QUIT, partnerInstanceDto,
                OperatorDto.defaultOperator());
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }
}
