package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.AssertUseStateEnum;

public class QuitStationApplyDto extends OperatorDto {

	private static final long serialVersionUID = -449672555158774209L;

	@NotNull(message = "instanceId not null")
	private Long instanceId;

	// 线下申请邮件、表格附件名称
	private String revocationAppFormFileName;

	// 审批文件名称
	private String approvalFileName;

	// 撤点说明
	private String otherDescription;

	// 资产使用情况
	private AssertUseStateEnum assertUseState;

	// 贷款证明文件
	private String loanProveFileName;
	
	// 贷款是否结清
	private boolean loanHasClose;

	// 是，退出服务站，否，不退出服务站
	private Boolean isQuitStation;
	
	
	private String submittedPeopleName;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
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

	public AssertUseStateEnum getAssertUseState() {
		return assertUseState;
	}

	public void setAssertUseState(AssertUseStateEnum assertUseState) {
		this.assertUseState = assertUseState;
	}

	public boolean isLoanHasClose() {
		return loanHasClose;
	}

	public void setLoanHasClose(boolean loanHasClose) {
		this.loanHasClose = loanHasClose;
	}

	public Boolean getIsQuitStation() {
		return isQuitStation;
	}

	public void setIsQuitStation(Boolean isQuitStation) {
		this.isQuitStation = isQuitStation;
	}

	public String getSubmittedPeopleName() {
		return submittedPeopleName;
	}

	public void setSubmittedPeopleName(String submittedPeopleName) {
		this.submittedPeopleName = submittedPeopleName;
	}
}
