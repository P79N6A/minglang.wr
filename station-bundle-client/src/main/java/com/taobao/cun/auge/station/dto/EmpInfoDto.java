package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class EmpInfoDto implements Serializable {
	
	private static final long serialVersionUID = -2547255130857851240L;
	
	private String workNo; // 工号
	private String nickName;// 花名
	private String name;// 员工姓名
	private String loginAccount;// 域账户
	private String buMail; // 公司邮箱
	private String mobile; // 手机号码

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getWorkNo() {
		return workNo;
	}

	public void setWorkNo(String workNo) {
		this.workNo = workNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLoginAccount() {
		return loginAccount;
	}

	public void setLoginAccount(String loginAccount) {
		this.loginAccount = loginAccount;
	}

	public String getBuMail() {
		return buMail;
	}

	public void setBuMail(String buMail) {
		this.buMail = buMail;
	}
}
