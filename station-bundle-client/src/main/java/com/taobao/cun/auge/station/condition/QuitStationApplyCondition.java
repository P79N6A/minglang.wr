package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import com.taobao.cun.auge.station.enums.AssertUseStateEnum;

public class QuitStationApplyCondition implements Serializable {

	private static final long serialVersionUID = -449672555158774209L;
	private Long instanceId;
	private String state;
	private String revocationAppFormFileName;
	private String loanProveFileName;
	private String approvalFileName;
	private String otherDescription;
	private String submittedPeopleName; // 撤点小二名称
	private AssertUseStateEnum assertUseState;
	private String loanHasClose;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRevocationAppFormFileName() {
		return revocationAppFormFileName;
	}

	public void setRevocationAppFormFileName(String revocationAppFormFileName) {
		this.revocationAppFormFileName = revocationAppFormFileName;
	}

	public String getLoanProveFileName() {
		return loanProveFileName;
	}

	public void setLoanProveFileName(String loanProveFileName) {
		this.loanProveFileName = loanProveFileName;
	}

	public String getApprovalFileName() {
		return approvalFileName;
	}

	public void setApprovalFileName(String approvalFileName) {
		this.approvalFileName = approvalFileName;
	}

	public String getOtherDescription() {
		return otherDescription;
	}

	public void setOtherDescription(String otherDescription) {
		this.otherDescription = otherDescription;
	}

	public String getSubmittedPeopleName() {
		return submittedPeopleName;
	}

	public void setSubmittedPeopleName(String submittedPeopleName) {
		this.submittedPeopleName = submittedPeopleName;
	}

	public AssertUseStateEnum getAssertUseState() {
		return assertUseState;
	}

	public void setAssertUseState(AssertUseStateEnum assertUseState) {
		this.assertUseState = assertUseState;
	}

	public String getLoanHasClose() {
		return loanHasClose;
	}

	public void setLoanHasClose(String loanHasClose) {
		this.loanHasClose = loanHasClose;
	}

}