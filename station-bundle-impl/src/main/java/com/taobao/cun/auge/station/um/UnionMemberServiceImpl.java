package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.common.utils.LatitudeUtil;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.station.um.dto.UnionMemberAddDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberCheckDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberStateChangeDto;
import com.taobao.cun.auge.station.um.dto.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.um.enums.UnionMemberStateEnum;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * 优盟增删改服务
 *
 * @author haihu.fhh
 */
@Service("unionMemberService")
@HSFProvider(serviceInterface = UnionMemberService.class, clientTimeout = 7000)
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

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public AliPaymentAccountDto checkUnionMember(UnionMemberCheckDto checkDto) {
        BeanValidator.validateWithThrowable(checkDto);

        Long parentStationId = checkDto.getParentStationId();
        String taobaoNick = checkDto.getTaobaoNick();

        AliPaymentAccountDto paymentAccountDto = paymentAccountQueryService.queryStationMemberPaymentAccountHideByNick(
            taobaoNick);
        Long taobaoUserId = paymentAccountDto.getTaobaoUserId();

        if (taobaoAccountBo.isTaobaoBuyerOrSellerBlack(taobaoUserId)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该淘宝账号属于淘宝黑名单");
        }

        if (taobaoAccountBo.isAlipayRiskUser(taobaoUserId)) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该淘宝用户支付宝账号属于支付宝风险账号");
        }

        PartnerInstanceDto piDto = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
        if (piDto != null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "该账号已经合作，不能重复添加");
        }

        PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(
            parentStationId);
        if (partnerInstance == null || partnerInstance.getStationDto() == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, "合伙人站点不存在");
        }
        return paymentAccountDto;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Long addUnionMember(UnionMemberAddDto addDto) {
        BeanValidator.validateWithThrowable(addDto);

        String taobaoNick = addDto.getTaobaoNick();

        PartnerDto pDto = new PartnerDto();

        AliPaymentAccountDto aliPaymentAccountDto = paymentAccountQueryService
            .queryStationMemberPaymentAccountByNick(taobaoNick);
        Long taobaoUserId = aliPaymentAccountDto.getTaobaoUserId();

        pDto.setTaobaoUserId(taobaoUserId);
        pDto.setTaobaoNick(taobaoNick);
        pDto.setName(aliPaymentAccountDto.getFullName());
        pDto.setAlipayAccount(aliPaymentAccountDto.getAccountNo());
        pDto.setIdenNum(aliPaymentAccountDto.getIdCardNumber());
        pDto.setMobile(addDto.getMobile());
        pDto.setBusinessType(PartnerBusinessTypeEnum.PARTTIME);

        Station parentStationDto = stationBO.getStationById(addDto.getParentStationId());

        StationDto sDto = new StationDto();
        //和村小二一个组织
        sDto.setApplyOrg(parentStationDto.getApplyOrg());
        sDto.setName(addDto.getStationName());
        sDto.setAddress(addDto.getAddress());
        sDto.setFormat(addDto.getFormat());
        sDto.setCovered(addDto.getCovered());
        sDto.setDescription(addDto.getDescription());
        LatitudeUtil.buildPOI(addDto.getAddress());

        PartnerInstanceDto piDto = new PartnerInstanceDto();
        piDto.setOperator(addDto.getOperator());
        piDto.setOperatorOrgId(addDto.getOperatorOrgId());
        piDto.setOperatorType(addDto.getOperatorType());

        piDto.setType(PartnerInstanceTypeEnum.UM);
        piDto.setTaobaoUserId(taobaoUserId);
        piDto.setStationDto(sDto);
        piDto.setPartnerDto(pDto);
        piDto.setParentStationId(addDto.getParentStationId());

        LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(piDto, StateMachineEvent.SETTLING_EVENT);
        stateMachineService.executePhase(phaseEvent);
        return piDto.getStationDto().getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateUnionMember(UnionMemberUpdateDto updateDto) {
        BeanValidator.validateWithThrowable(updateDto);
        Long stationId = updateDto.getStationId();

        PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);

        Long partnerId = rel.getPartnerId();

        StationDto stationDto = new StationDto();
        stationDto.setId(stationId);
        stationDto.setName(updateDto.getStationName());
        stationDto.setAddress(updateDto.getAddress());
        stationDto.setFormat(updateDto.getFormat());
        stationDto.setCovered(updateDto.getCovered());
        stationDto.setDescription(updateDto.getDescription());
        stationDto.copyOperatorDto(updateDto);
        stationBO.updateStation(stationDto);

        //验证手机号唯一性
        if (!partnerInstanceBO.judgeMobileUseble(null, partnerId, updateDto.getMobile())) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "该手机号已被使用");
        }

        //更换人的信息
        PartnerDto partnerDto = new PartnerDto();
        partnerDto.copyOperatorDto(updateDto);
        partnerDto.setId(partnerId);
        partnerDto.setMobile(updateDto.getMobile());
        partnerBO.updatePartner(partnerDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void openOrCloseUnionMember(UnionMemberStateChangeDto stateChangeDto) {
        BeanValidator.validateWithThrowable(stateChangeDto);

        String operator = stateChangeDto.getOperator();
        Long parentTaobaoUserId = Long.valueOf(operator);
        Long stationId = stateChangeDto.getStationId();

        PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
            parentTaobaoUserId);

        PartnerInstanceDto umInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
        Long parentStationId = umInstanceDto.getParentStationId();

        if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
            throw new AugeServiceException("不能管理非自己名下的优盟合作店");
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
            }
            //关闭
        } else if (UnionMemberStateEnum.CLOSED.equals(targetStateEnum)) {
            LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(umInstanceDto,
                StateMachineEvent.CLOSED_EVENT);
            stateMachineService.executePhase(phaseEvent);
        }

    }

}
