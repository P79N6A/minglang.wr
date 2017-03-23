package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

public class InvoiceQualification implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2803912879640267284L;

	/**
	 * 发票地址
	 */
	@NotNull
	private String invoiceFile;
	
	/**
	 * 文件类型
	 */
	@NotNull
	private String invoiceFileType;
	
	/**
	 * taobao用户ID
	 */
	@NotNull
	private Long taobaoUserId;
	
	/**
	 * 发票有效开始时间
	 */
	private Date effectiveStartTime;
	
	/**
	 * 发票有效结束时间
	 */
	private Date effectiveEndTime;

	public String getInvoiceFile() {
		return invoiceFile;
	}

	public void setInvoiceFile(String invoiceFile) {
		this.invoiceFile = invoiceFile;
	}


	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public Date getEffectiveStartTime() {
		return effectiveStartTime;
	}

	public void setEffectiveStartTime(Date effectiveStartTime) {
		this.effectiveStartTime = effectiveStartTime;
	}

	public Date getEffectiveEndTime() {
		return effectiveEndTime;
	}

	public void setEffectiveEndTime(Date effectiveEndTime) {
		this.effectiveEndTime = effectiveEndTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getInvoiceFileType() {
		return invoiceFileType;
	}

	public void setInvoiceFileType(String invoiceFileType) {
		this.invoiceFileType = invoiceFileType;
	}
	

	
	
}
