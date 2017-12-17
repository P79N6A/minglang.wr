package com.taobao.cun.auge.company.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class CuntaoServiceVendorDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1998396499820549049L;

	@NotNull(message="公司名称不能为空")
    private String companyName;

	@NotNull(message="联系人旺旺不能为空")
    private String taobaoNick;

	@NotNull(message="联系电话不能为空")
	private String mobile;

	@NotNull
    private String remark;

	@NotNull(message="公司类型不能为空")
    private CuntaoVendorType type;

	/**
	 *支付宝账号
	 */
	private String alipayOutUser;
	
	/**
	 * 公司TaobaoNick账号对应的taobaoUserId
	 */
	private Long taobaoUserId;
	
	private String operator;
	
	private Long id;
	
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}


	public CuntaoVendorType getType() {
		return type;
	}

	public void setType(CuntaoVendorType type) {
		this.type = type;
	}

	public String getAlipayOutUser() {
		return alipayOutUser;
	}

	public void setAlipayOutUser(String alipayOutUser) {
		this.alipayOutUser = alipayOutUser;
	}

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}


	

}
