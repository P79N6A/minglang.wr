package com.taobao.cun.auge.paymentAccount;

import com.taobao.cun.auge.paymentAccount.dto.AliPaymentAccountDto;

public interface PaymentAccountQueryService {

	public AliPaymentAccountDto queryStationMemberPaymentAccountByNick(String taobaoNick);
	
	public AliPaymentAccountDto queryStationMemberPaymentAccountByUserId(Long userId);
}
