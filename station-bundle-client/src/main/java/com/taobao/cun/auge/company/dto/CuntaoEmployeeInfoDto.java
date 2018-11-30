package com.taobao.cun.auge.company.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.company.enums.CuntaoEmployeeIdentifierEnum;

/**
 * 员工信息
 * 
 * @author quanzhu.wangqz
 *
 */
public class CuntaoEmployeeInfoDto implements Serializable {

	
	private static final long serialVersionUID = 530323630204727909L;
	/** 村淘org工人ID */
	private Long employeeId;
	/** taobao nick */
	private String taobaoNick;
	/** taobao user id */
	private Long taobaoUserId;
	/** 支付宝账号 */
	private String alipayAccount;
	/** 姓名 */
	private String name;
	/** 身份证号 */
	private String certNo;
	/** 手机号 */
	private String mobile;
	/** 入职期间 */
	public Date startJobTime;
	/** 离职时间 */
	public Date leaveJobTime;
	/** 工人身份 */
	private List<CuntaoEmployeeIdentifierEnum> identifier;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getAlipayAccount() {
		return alipayAccount;
	}

	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertNo() {
		return certNo;
	}

	public void setCertNo(String certNo) {
		this.certNo = certNo;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Date getStartJobTime() {
		return startJobTime;
	}

	public void setStartJobTime(Date startJobTime) {
		this.startJobTime = startJobTime;
	}

	public Date getLeaveJobTime() {
		return leaveJobTime;
	}

	public void setLeaveJobTime(Date leaveJobTime) {
		this.leaveJobTime = leaveJobTime;
	}

	public List<CuntaoEmployeeIdentifierEnum> getIdentifier() {
		return identifier;
	}

	public void setIdentifier(List<CuntaoEmployeeIdentifierEnum> identifier) {
		this.identifier = identifier;
	}
}
