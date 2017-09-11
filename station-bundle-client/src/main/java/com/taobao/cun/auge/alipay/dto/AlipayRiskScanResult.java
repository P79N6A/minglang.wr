package com.taobao.cun.auge.alipay.dto;

import java.io.Serializable;

public class AlipayRiskScanResult implements Serializable {

	private static final long serialVersionUID = -1463782466804426622L;
	private boolean success;
	private String resultCode;
	private String errorMsg;
	private AlipayRiskScanData data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public AlipayRiskScanData getData() {
		return data;
	}

	public void setData(AlipayRiskScanData data) {
		this.data = data;
	}

	public static AlipayRiskScanResult success(boolean hasRisk, String msg) {
		AlipayRiskScanResult result = new AlipayRiskScanResult();
		result.setSuccess(true);
		AlipayRiskScanData data = new AlipayRiskScanData();
		data.setRisk(hasRisk);
		data.setDetail(msg);
		result.setData(data);
		return result;
	}

	public static AlipayRiskScanResult unSucceed(String resultCode, String errorMsg) {
		AlipayRiskScanResult result = new AlipayRiskScanResult();
		result.setSuccess(false);
		result.setResultCode(resultCode);
		result.setErrorMsg(errorMsg);
		return result;
	}

}
