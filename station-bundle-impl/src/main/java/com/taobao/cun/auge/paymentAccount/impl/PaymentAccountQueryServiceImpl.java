package com.taobao.cun.auge.paymentAccount.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.paymentAccount.PaymentAccountQueryService;
import com.taobao.cun.auge.paymentAccount.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.BaseUserDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import com.taobao.uic.common.service.userinfo.client.UicReadServiceClient;

@Service("paymentAccountQueryService")
@HSFProvider(serviceInterface = PaymentAccountQueryService.class)
public class PaymentAccountQueryServiceImpl implements PaymentAccountQueryService{

	@Autowired
	UicReadServiceClient uicReadServiceClient;
	@Autowired
	UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

	private static final int ALIPAY_PSERON_PROMOTED_TYPE = 512;

	private static final int PAYMENT_ACCOUNT_TYPE_PSERSON = 2;
	private static final int PAYMENT_ACCOUNT_TYPE_ORG = 1;

	private static final int PAYMENT_ACCOUNT_ACTIVE = 1;

	
	@Override
	public AliPaymentAccountDto queryStationMemberPaymentAccountByNick(String taobaoNick) {
		AliPaymentAccountDto paymentAccountDto = queryStationMemberPaymentAccount(taobaoNick, null);
		return paymentAccountDto;
	}
	
	public AliPaymentAccountDto queryStationMemberPaymentAccount(String loginId, Long userId) throws AugeBusinessException {

		ResultDO<BaseUserDO> baseUserDOresult = null;
		if (userId != null && userId != 0) {
			baseUserDOresult = uicReadServiceClient.getBaseUserByUserId(userId);
		} else if (StringUtils.isNotBlank(loginId)) {
			baseUserDOresult = uicReadServiceClient.getBaseUserByNick(loginId);
		} else {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"淘宝账号不能为空!");
		}
		if (baseUserDOresult == null || !baseUserDOresult.isSuccess() || baseUserDOresult.getModule() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"该淘宝账号不存在或状态异常，请与申请人核实!");
		}
		BaseUserDO baseUserDO = baseUserDOresult.getModule();
		if (baseUserDO.getUserId() == null || baseUserDO.getUserId() == 0) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"该淘宝账号尚未完成支付宝绑定操作，请联系申请人，先在淘宝->账号管理中，完成支付宝账号的绑定，并在支付宝平台完成实名认证操作!");
		}

		ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(baseUserDO.getUserId());
		if (basePaymentAccountDOResult == null || !basePaymentAccountDOResult.isSuccess() || basePaymentAccountDOResult.getModule() == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE,"该淘宝账号尚未完成支付宝绑定操作，请联系申请人，先在淘宝->账号管理中，完成支付宝账号的绑定，并在支付宝平台完成实名认证操作!");
		}
		BasePaymentAccountDO basePaymentAccountDO = basePaymentAccountDOResult.getModule();
		// 校验是不是个人买家
		int accountType = basePaymentAccountDO.getAccountType();
		int activeStatus = basePaymentAccountDO.getActiveStatus();
		if (accountType != PAYMENT_ACCOUNT_TYPE_PSERSON) {
			throw new AugeBusinessException(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE,"申请人的支付宝账号并非个人实名认证的支付宝账号（暂不支持企业支付宝等其他形式的账号），请联系申请人做核实!");
		}
		// 校验有没有实名认证
		int promotedType = baseUserDO.getPromotedType();
		if (((promotedType & ALIPAY_PSERON_PROMOTED_TYPE) != ALIPAY_PSERON_PROMOTED_TYPE) || StringUtils.isBlank(baseUserDO.getFullname()) || StringUtils.isBlank(baseUserDO.getIdCardNumber())) {
			throw new AugeBusinessException(AugeErrorCodes.ALIPAY_BUSINESS_CHECK_ERROR_CODE,"该淘宝账号绑定的支付宝账号未做个人实名认证;请联系申请人,在支付宝平台完成个人实名认证操作!");
		}
		AliPaymentAccountDto paymentAccountDto = new AliPaymentAccountDto();
		paymentAccountDto.setFullName(baseUserDO.getFullname());
		paymentAccountDto.setIdCardNumber(baseUserDO.getIdCardNumber());
		paymentAccountDto.setAccountNo(basePaymentAccountDO.getAccountNo());
		paymentAccountDto.setAlipayId(basePaymentAccountDO.getOutUser());
		paymentAccountDto.setTaobaoUserId(new Long(baseUserDO.getUserId()));
		return paymentAccountDto;
	}
	
	public AliPaymentAccountDto queryStationMemberPaymentAccountByUserId(Long userId){
		AliPaymentAccountDto paymentAccountDto = queryStationMemberPaymentAccount(null, userId);
		return paymentAccountDto;
	}

}
