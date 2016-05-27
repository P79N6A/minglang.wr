package com.taobao.cun.auge.station.adapter;

import com.taobao.cun.auge.station.dto.OperatorDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;

public interface PaymentAccountQueryAdapter {
	
	public PaymentAccountDto queryPaymentAccountByNick(String taobaoNick, OperatorDto operator);
	
	public PaymentAccountDto queryPaymentAccountByTaobaoUserId(Long taobaoUserId, OperatorDto operator);


}
