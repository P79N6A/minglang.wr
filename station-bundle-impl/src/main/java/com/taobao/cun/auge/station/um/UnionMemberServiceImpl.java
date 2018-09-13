package com.taobao.cun.auge.station.um;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.bo.TaobaoAccountBo;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.request.UnionMemberAddDto;
import com.taobao.cun.auge.station.request.UnionMemberCheckDto;
import com.taobao.cun.auge.station.request.UnionMemberUpdateDto;
import com.taobao.cun.auge.station.response.UnionMemberCheckResult;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
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
    public Long addUnionMember(UnionMemberAddDto addDto) {
        return null;
    }

    @Override
    public void updateUnionMember(UnionMemberUpdateDto updateDto) {

    }

    @Override
    public void closeUnionMember(Long stationId, OperatorDto operatorDto) {

    }

}
