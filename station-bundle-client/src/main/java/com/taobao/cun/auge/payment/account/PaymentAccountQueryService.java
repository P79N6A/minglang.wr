package com.taobao.cun.auge.payment.account;

import com.taobao.cun.auge.payment.account.dto.AliPaymentAccountDto;

public interface PaymentAccountQueryService {

	public AliPaymentAccountDto queryStationMemberPaymentAccountByUserId(Long userId);

	public AliPaymentAccountDto queryStationMemberPaymentAccountByNick(String nick);
	
	public AliPaymentAccountDto queryStationMemberPaymentAccountHideByNick(String nick);

	public AliPaymentAccountDto queryStationMemberPaymentAccountHideByUserId(Long userId);
}
