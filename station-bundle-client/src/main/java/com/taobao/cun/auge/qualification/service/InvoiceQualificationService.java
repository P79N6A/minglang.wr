package com.taobao.cun.auge.qualification.service;

public interface InvoiceQualificationService {

	/**
	 * 保存结算发票信息
	 * @param settleInvoiceInfo
	 */
	public boolean saveSettleInvoiceInfo(InvoiceQualification settleInvoiceInfo);

	public InvoiceQualification queryInvoiceQualification(Long taobaoUserId);
}
