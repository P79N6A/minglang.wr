package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.exception.AugeServiceException;
import com.taobao.cun.auge.common.utils.LatitudeUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEvent;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseEventBuilder;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.statemachine.StateMachineService;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.request.UnionMemberAddDto;
import com.taobao.cun.auge.station.request.UnionMemberCheckDto;
import com.taobao.cun.auge.station.request.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.response.UnionMemberCheckResult;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.security.util.SensitiveDataUtil;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;
import org.apache.commons.lang.StringUtils;
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
    private UicReadServiceClient uicReadServiceClient;

    @Autowired
    private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

    private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;

    private static final int PAYMENT_ACCOUNT_TYPE_PSERSON = 2;

    private static final int ALIPAY_COMPANY_PROMOTED_TYPE = 4;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public UnionMemberCheckResult checkUnionMember(UnionMemberCheckDto checkDto) {
        BeanValidator.validateWithThrowable(checkDto);

        Long parentStationId = checkDto.getParentStationId();
        String taobaoNick = checkDto.getTaobaoNick();

        UnionMemberCheckResult result = new UnionMemberCheckResult();

        ResultDO<BaseUserDO> baseUserDOresult = uicReadServiceClient.getBaseUserByNick(taobaoNick);
        if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝账号不存在或状态异常，请与申请人核实!");
            return result;
        }
        BaseUserDO baseUserDO = baseUserDOresult.getModule();
        if (baseUserDO.getUserId() == null || baseUserDO.getUserId() == 0) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝账号不存在或状态异常，请与申请人核实!");
            return result;
        }

        ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient
            .getAccountByUserId(baseUserDO.getUserId());
        if (basePaymentAccountDOResult == null || !basePaymentAccountDOResult.isSuccess()
            || basePaymentAccountDOResult.getModule() == null) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝账号尚未完成支付宝绑定操作，请联系申请人，先在淘宝->账号管理中，完成支付宝账号的绑定，并在支付宝平台完成实名认证操作!");
            return result;
        }
        BasePaymentAccountDO basePaymentAccountDO = basePaymentAccountDOResult.getModule();
        // 校验是不是个人买家
        int accountType = basePaymentAccountDO.getAccountType();
        if (accountType != PAYMENT_ACCOUNT_TYPE_PSERSON) {
            result.setSuccess(false);
            result.setErrorMessage("申请人的支付宝账号并非个人实名认证的支付宝账号（暂不支持企业支付宝等其他形式的账号），请联系申请人做核实!");
            return result;
        }

        // 校验有没有实名认证
        int promotedType = baseUserDO.getPromotedType();
        if (((promotedType & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE) || StringUtils.isBlank(
            baseUserDO.getFullname()) || StringUtils.isBlank(baseUserDO.getIdCardNumber())) {
            result.setSuccess(false);
            result.setErrorMessage("该淘宝账号绑定的支付宝账号未做个人实名认证;请联系申请人,在支付宝平台完成个人实名认证操作!");
            return result;
        }
        Long taobaoUserId = new Long(baseUserDO.getUserId());

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

        AliPaymentAccountDto paymentAccountDto = new AliPaymentAccountDto();
        paymentAccountDto.setAccountNo(basePaymentAccountDO.getAccountNo());
        paymentAccountDto.setTaobaoUserId(taobaoUserId);
        paymentAccountDto.setAlipayId(SensitiveDataUtil.alipayLogonIdHide(basePaymentAccountDO.getOutUser()));
        paymentAccountDto.setIdCardNumber(SensitiveDataUtil.idCardNoHide(baseUserDO.getIdCardNumber()));
        paymentAccountDto.setFullName(SensitiveDataUtil.customizeHide(baseUserDO.getFullname(), 0, baseUserDO.getFullname().length() - 1, 1));

        result.setSuccess(true);
        result.setAliPaymentAccountDto(paymentAccountDto);
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
        ValidateUtils.notNull(stationId);
        ValidateUtils.validateParam(operatorDto);

        String operator = operatorDto.getOperator();
        Long parentTaobaoUserId = Long.valueOf(operator);
        PartnerInstanceDto partnerInstanceDto = partnerInstanceQueryService.getActivePartnerInstance(
            parentTaobaoUserId);

        PartnerInstanceDto umInstanceDto = partnerInstanceQueryService.getCurrentPartnerInstanceByStationId(stationId);
        Long parentStationId = umInstanceDto.getParentStationId();

        if (null != parentStationId && !parentStationId.equals(partnerInstanceDto.getStationId())) {
            throw new AugeServiceException("不能关闭非自己名下的优盟");
        }

        LifeCyclePhaseEvent phaseEvent = LifeCyclePhaseEventBuilder.build(umInstanceDto,
            StateMachineEvent.CLOSED_EVENT);
        stateMachineService.executePhase(phaseEvent);
    }

}
