package com.taobao.cun.auge.qualification.service;

import java.io.Serializable;
/**
 * 新前置入住流程响应接口
 * @author zhenhuan.zhangzh
 *
 */
public class C2BSettlingResponse implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6466883549852558959L;

	/**
	 * 是否是测试用户
	 */
	private boolean testUser;
	
	/**
	 * 是否是新入住用户
	 */
	private boolean isNewSettleUser;
	
	/**
	 * 入住流程步骤
	 */
	private int step;

	/**
	 * 是否成功
	 */
	private boolean successful;
	
	/**
	 * 资质的状态
	 */
	private Integer qualificationStatus;
	
	
	/**
	 * 错误消息
	 */
	private String errorMessage;
	
	/**
	 * 错误码
	 */
	private String errorCode;
	
	private String[] stepNames;
	
	private String stepName;
	
	public boolean isSuccessful() {
		return successful;
	}


	public void setSuccessful(boolean successful) {
		this.successful = successful;
	}


	public String getErrorMessage() {
		return errorMessage;
	}


	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


	public boolean isTestUser() {
		return testUser;
	}

	
	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}


	public boolean isNewSettleUser() {
		return isNewSettleUser;
	}


	public void setNewSettleUser(boolean isNewSettleUser) {
		this.isNewSettleUser = isNewSettleUser;
	}


	public void setTestUser(boolean testUser) {
		this.testUser = testUser;
	}


	public Integer getQualificationStatus() {
		return qualificationStatus;
	}


	public void setQualificationStatus(Integer qualificationStatus) {
		this.qualificationStatus = qualificationStatus;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}


	public String[] getStepNames() {
		return stepNames;
	}


	public void setStepNames(String[] stepNames) {
		this.stepNames = stepNames;
	}


	public String getStepName() {
		return stepName;
	}


	public void setStepName(String stepName) {
		this.stepName = stepName;
	}


	
	
}
