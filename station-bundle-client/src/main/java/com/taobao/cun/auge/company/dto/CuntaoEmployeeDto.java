package com.taobao.cun.auge.company.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

public class CuntaoEmployeeDto extends OperatorDto{

	/**
	 * 
	 */
	private static final long serialVersionUID = -680626161153855515L;

	@NotNull(message = "员工姓名为空")
	private String name;
	
	private String taobaoNick;

	@NotNull(message = "员工手机号为空")
	private String mobile;
	
	private Long id;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
