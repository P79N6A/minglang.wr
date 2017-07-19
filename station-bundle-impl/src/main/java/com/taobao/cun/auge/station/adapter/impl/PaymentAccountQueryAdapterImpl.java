package com.taobao.cun.auge.station.adapter.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.payment.account.PaymentAccountQueryService;
import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;

@Component("paymentAccountQueryAdapter")
public class PaymentAccountQueryAdapterImpl implements PaymentAccountQueryAdapter {

	@Autowired
	private PaymentAccountQueryService paymentAccountQueryService;

	@Override
	public PaymentAccountDto queryPaymentAccountByNick(String taobaoNick, OperatorDto operator) {
			AliPaymentAccountDto resultModel= paymentAccountQueryService.queryStationMemberPaymentAccountByNick(taobaoNick);
			return adaptResult(resultModel);
	}

	@Override
	public PaymentAccountDto queryPaymentAccountByTaobaoUserId(Long taobaoUserId, OperatorDto operator) {
			AliPaymentAccountDto resultModel = paymentAccountQueryService.queryStationMemberPaymentAccountByUserId(taobaoUserId);
			return adaptResult(resultModel);
		
	}

	private PaymentAccountDto adaptResult(AliPaymentAccountDto result){
		if (null == result) {
			return null;
		}
		PaymentAccountDto augeDto = new PaymentAccountDto();
		BeanUtils.copyProperties(augeDto, result);
		return augeDto;
	}


}
