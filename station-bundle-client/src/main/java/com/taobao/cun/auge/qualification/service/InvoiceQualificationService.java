package com.taobao.cun.auge.qualification.service;

import java.util.List;

public interface InvoiceQualificationService {

	/**
	 * 保存结算发票信息
	 * @param settleInvoiceInfo
	 */
	public boolean saveSettleInvoiceInfo(InvoiceQualification settleInvoiceInfo);

	public List<InvoiceQualification> queryInvoiceQualification(Long taobaoUserId);
}
