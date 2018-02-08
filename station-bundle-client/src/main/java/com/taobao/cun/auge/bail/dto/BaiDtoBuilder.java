package com.taobao.cun.auge.bail.dto;

import java.math.BigDecimal;

import com.taobao.cun.auge.station.dto.AlipayStandardBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoFreezeBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoUnFreezeBailDto;
import com.taobao.cun.settle.bail.enums.BailOperateTypeEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;

/**
 * Created by xujianhui on 16/12/27.
 * 冻结和解冻参数dto构建类
 */
public class BaiDtoBuilder {

    public static final String OUT_ORDER_NO_PRE = "CT";
    public static final BigDecimal ONE_HUNDRED = new BigDecimal("100");

    public static CuntaoFreezeBailDto generateFreezeBailDto(Long taobaoUserId, UserTypeEnum userType, String alipayId,
                                                            BailOperateTypeEnum operateType, BigDecimal frozenMoney,
                                                            String reason, String source, String outOrderNo, String agreementNo) {
        CuntaoFreezeBailDto freezeBailDto = new CuntaoFreezeBailDto();
        freezeBailDto.setTaobaoUserId(taobaoUserId);
        freezeBailDto.setAlipayId(alipayId);
        freezeBailDto.setUserTypeEnum(userType);
        Long amountCent = frozenMoney.multiply(ONE_HUNDRED).longValue();
        freezeBailDto.setAmount(amountCent);
        freezeBailDto.setBailOperateTypeEnum(operateType);
        freezeBailDto.setOutOrderId(outOrderNo);
        freezeBailDto.setReason(reason);
        freezeBailDto.setSource(source);
        freezeBailDto.setAgreementNo(agreementNo);
        return freezeBailDto;
    }

    public static CuntaoUnFreezeBailDto generateUnfreezeBailDto(Long taobaoUserId, UserTypeEnum userType, String alipayId,
                                                                BailOperateTypeEnum operateType, BigDecimal unfrozenMoney,
                                                                String reason, String source, String outOrderNo) {
        CuntaoUnFreezeBailDto unFreezeBailDto = new CuntaoUnFreezeBailDto();
        unFreezeBailDto.setTaobaoUserId(taobaoUserId);
        unFreezeBailDto.setUserTypeEnum(userType);
        unFreezeBailDto.setAlipayId(alipayId);
        unFreezeBailDto.setBailOperateTypeEnum(operateType);
        Long amountCent = unfrozenMoney.multiply(ONE_HUNDRED).longValue();
        unFreezeBailDto.setAmount(amountCent);
        unFreezeBailDto.setSource(source);
        unFreezeBailDto.setReason(reason);
        unFreezeBailDto.setOutOrderId(outOrderNo);
        return unFreezeBailDto;
    }

    public static CuntaoUnFreezeBailDto buildFrom(Long taobaoUserId, AlipayStandardBailDto alipayStandardBailDto) {
        BigDecimal unfreezeMoney = new BigDecimal(alipayStandardBailDto.getAmount());
        return generateUnfreezeBailDto(taobaoUserId, UserTypeEnum.PARTNER,
                alipayStandardBailDto.getUserAccount(), BailOperateTypeEnum.QUIT_UNFREEZE, unfreezeMoney,
                "村淘保证金解冻", "stationBundle", alipayStandardBailDto.getOutOrderNo());
    }

    public static CuntaoFreezeBailDto buildFreezeBailDtoFrom(Long taobaoUserId, AlipayStandardBailDto alipayStandardBailDto) {
        BigDecimal unfreezeMoney = new BigDecimal(alipayStandardBailDto.getAmount());
        return generateFreezeBailDto(taobaoUserId, UserTypeEnum.PARTNER,
                alipayStandardBailDto.getUserAccount(), BailOperateTypeEnum.ACTIVE_FREEZE, unfreezeMoney,
                "村淘保证金冻结", "stationBundle", alipayStandardBailDto.getOutOrderNo(), null);
    }

    public static Long parseInstanceIdFromOutOrderNo(String outSignNo) {
        if(outSignNo.startsWith(OUT_ORDER_NO_PRE)){
            return Long.parseLong(outSignNo.substring(OUT_ORDER_NO_PRE.length()));
        }else {
            return Long.parseLong(outSignNo);
        }
    }

    public static String generateOutOrderNoByInstanceId(Long partnerInstanceId) {
        return OUT_ORDER_NO_PRE + partnerInstanceId;
    }

    public static void main(String[] args) {
        System.out.println(new BigDecimal("1.00").multiply(new BigDecimal("100")).longValue());
    }

}
