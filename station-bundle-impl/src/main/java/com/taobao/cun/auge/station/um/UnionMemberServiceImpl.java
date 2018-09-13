package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.LatitudeUtil;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.request.UnionMemberAddDto;
import com.taobao.cun.auge.station.request.UnionMemberCheckDto;
import com.taobao.cun.auge.station.request.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.response.UnionMemberCheckResult;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.jetbrains.annotations.NotNull;
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
    private StateMachineService stateMachineService;

    @Autowired
    private StationNumConfigBO stationNumConfigBO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public UnionMemberCheckResult checkUnionMember(UnionMemberCheckDto checkDto) {
        BeanValidator.validateWithThrowable(checkDto);

        Long parentStationId = checkDto.getParentStationId();
        String taobaoNick = checkDto.getTaobaoNick();

        UnionMemberCheckResult result = new UnionMemberCheckResult();

        //查询支付宝信息，该接口，已经做了账号绑定等校验
        AliPaymentAccountDto aliPaymentAccountDto = paymentAccountQueryService
            .queryStationMemberPaymentAccountHideByNick(taobaoNick);
        Long taobaoUserId = aliPaymentAccountDto.getTaobaoUserId();

        if (taobaoAccountBo.isTaobaoBuyerOrSellerBlack(taobaoUserId)) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝账号属于淘宝黑名单");
            return result;
        }

        if (taobaoAccountBo.isAlipayRiskUser(taobaoUserId)) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝用户支付宝账号属于支付宝风险账号");
            return result;
        }

        PartnerInstanceDto piDto = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
        if (piDto != null) {
            result.setSuccess(false);
            result.setErrorMessage("该账号已经合作，不能重复添加");
            return result;
        }

        PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(
            parentStationId);
        if (partnerInstance == null || partnerInstance.getStationDto() == null) {
            result.setSuccess(false);
            result.setErrorMessage("合伙人站点不存在");
            return result;
        }
        result.setSuccess(true);
        result.setAliPaymentAccountDto(aliPaymentAccountDto);
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public Long addUnionMember(UnionMemberAddDto addDto) {
        PartnerInstanceDto piDto = createUnionMember(addDto);
        return piDto.getStationDto().getId();
    }

    @NotNull
    private PartnerInstanceDto createUnionMember(UnionMemberAddDto addDto) {
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
        return piDto;
    }

    @Override
    public Long addAndOpenUnionMember(UnionMemberAddDto addDto) {
        PartnerInstanceDto instanceDto = createUnionMember(addDto);
        LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(instanceDto,
            StateMachineEvent.SERVICING_EVENT);
        stateMachineService.executePhase(phaseEvent);
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void updateUnionMember(UnionMemberUpdateDto updateDto) {

    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void closeUnionMember(Long stationId, OperatorDto operatorDto) {

    }

}
