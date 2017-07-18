package com.taobao.cun.auge.station.adapter.impl;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.paymentAccount.PaymentAccountQueryService;
import com.taobao.cun.auge.paymentAccount.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.UserTypeEnum;

@Component("paymentAccountQueryAdapter")
public class PaymentAccountQueryAdapterImpl implements PaymentAccountQueryAdapter {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private PaymentAccountQueryService paymentAccountQueryService;

	@Override
	public PaymentAccountDto queryPaymentAccountByNick(String taobaoNick, OperatorDto operator) {
		try {
			AliPaymentAccountDto resultModel = paymentAccountQueryService.queryStationMemberPaymentAccountByNick(taobaoNick);
			return adaptResult(resultModel);
		} catch (Exception e) {
			logger.error("queryPaymentAccountByNick error", e);
			throw new AugeServiceException("PaymentAccountQueryAdapter.queryPaymentAccountByNick error" + e.getMessage());
		}
	}

	@Override
	public PaymentAccountDto queryPaymentAccountByTaobaoUserId(Long taobaoUserId, OperatorDto operator) {
		try {
			AliPaymentAccountDto resultModel = paymentAccountQueryService.queryStationMemberPaymentAccountByUserId(taobaoUserId);
			return adaptResult(resultModel);
		} catch (Exception e) {
			logger.error("queryPaymentAccountByTaobaoUserId error", e);
			throw new AugeServiceException("PaymentAccountQueryAdapter.queryPaymentAccountByTaobaoUserId error" + e.getMessage());
		}
	}

	private PaymentAccountDto adaptResult(AliPaymentAccountDto result)
			throws IllegalAccessException, InvocationTargetException {
		if (null == result) {
			return null;
		}
		PaymentAccountDto augeDto = new PaymentAccountDto();
		BeanUtils.copyProperties(augeDto, result);
		return augeDto;
	}

	private ContextDto buildContext(OperatorDto operator) {
		ContextDto context = new ContextDto();
		context.setAppId(operator.getOperator());
		context.setLoginId(operator.getOperator());
		context.setUserType(UserTypeEnum.HAVANA);
		context.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return context;
	}


}
