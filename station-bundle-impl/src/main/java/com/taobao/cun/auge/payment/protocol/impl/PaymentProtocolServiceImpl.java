package com.taobao.cun.auge.payment.protocol.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.payment.protocol.PaymentProtocolService;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.payment.account.dto.CreateAccountDTO;
import com.taobao.payment.account.dto.QueryAccountDTO;
import com.taobao.payment.account.result.AccountQueryResult;
import com.taobao.payment.account.result.CreateAccountResult;
import com.taobao.payment.account.service.AccountManageService;
import com.taobao.payment.account.service.query.AccountQueryService;
import com.taobao.payment.common.domain.AccountBO;
import com.taobao.payment.common.enums.AccountStatusEnum;
import com.taobao.payment.common.enums.BizTypeEnum;
import com.taobao.payment.common.enums.ChannelEnum;

@Service("paymentProtocolService")
@HSFProvider(serviceInterface = PaymentProtocolService.class)
public class PaymentProtocolServiceImpl implements PaymentProtocolService {

	@Autowired
	private AccountManageService accountManageService;
	
	@Autowired
	private AccountQueryService accountQueryService;
	@Autowired
	private PartnerInstanceQueryService partnerInstanceQueryService;
	
	private static Logger logger = LoggerFactory.getLogger(PaymentProtocolServiceImpl.class);
	
	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;
	@Override
	public Result<String> createPaymentProcotolSignUrl(Long taobaoUserId) {
		Result<String> result = null;
		PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance == null){
			result = Result.of(ErrorInfo.of("PARTNER_INSTANCE_NOT_EXITS", null, "合伙人不存在"));
			return result;
		}
		CreateAccountDTO createAccountDTO = new CreateAccountDTO();
		try {
			createAccountDTO.setBizType(BizTypeEnum.CUNTAO);
			createAccountDTO.setChannel(ChannelEnum.ALIPAY_AGREEMENT);
			createAccountDTO.setChannelAccount(partnerInstance.getPartnerDto().getAlipayAccount());
			createAccountDTO.setAttribute("return_url", diamondConfiguredProperties.getPaymentSignReturnUrl());
			CreateAccountResult createAccountResult = accountManageService.createPaymentAccount(taobaoUserId, createAccountDTO);
			if(createAccountResult.isSuccess()){
				result =  Result.of(createAccountResult.getSignUpURL());
				return result;
			}else{
				result = Result.of(ErrorInfo.of("CREATE_PAYMENT_ACCOUNT_ERROR", createAccountResult.getResultCode(), createAccountResult.getResultMessage()));
				return result;
			}
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("CREATE_PAYMENT_ACCOUNT_ERROR", null, "创建支付协议异常"));
			logger.error("CREATE_PAYMENT_ACCOUNT_ERROR",e);
			return result;
		} 
	}

	@Override
	public Result<Boolean> isSignedPaymentProcotol(Long taobaoUserId) {
		QueryAccountDTO queryAccountDTO = new  QueryAccountDTO();
		queryAccountDTO.setBizType(BizTypeEnum.CUNTAO);
		queryAccountDTO.setChannelEnum(ChannelEnum.ALIPAY_AGREEMENT);
		Result<Boolean> result = null;
		try {
			AccountQueryResult accountQueryResult = accountQueryService.queryUserAccount(taobaoUserId, queryAccountDTO);
			if(accountQueryResult.isSuccess()){
				List<AccountBO> accounts = accountQueryResult.getAccountBOList();
				if(accounts!=null && !accounts.isEmpty()){
					if(AccountStatusEnum.VALID.getCode().equals(accounts.iterator().next().getAccountStatus().getCode())){
						return Result.of(Boolean.TRUE);
					}
				}
			}
		    return Result.of(Boolean.FALSE);
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("CHECK_SIGN_PAYMENT_ACCOUNT_ERROR", null, "查询支付协议异常"));
			return result;
		}
	}

}
