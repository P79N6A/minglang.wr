package com.taobao.cun.auge.company;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeState;
import com.taobao.cun.auge.company.dto.CuntaoCompanyEmployeeType;
import com.taobao.cun.auge.dal.domain.CuntaoCompany;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public class CompanyAndManagerInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private CuntaoCompany cuntaoCompany;
	
	
	private CuntaoEmployee manager;
	
	private Long id;
	
	private CuntaoCompanyEmployeeState state;
	
	private CuntaoCompanyEmployeeType type;

	public CuntaoCompany getCuntaoCompany() {
		return cuntaoCompany;
	}

	public void setCuntaoCompany(CuntaoCompany cuntaoCompany) {
		this.cuntaoCompany = cuntaoCompany;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CuntaoCompanyEmployeeState getState() {
		return state;
	}

	public void setState(CuntaoCompanyEmployeeState state) {
		this.state = state;
	}

	public CuntaoCompanyEmployeeType getType() {
		return type;
	}

	public void setType(CuntaoCompanyEmployeeType type) {
		this.type = type;
	}

	public CuntaoEmployee getManager() {
		return manager;
	}

	public void setManager(CuntaoEmployee manager) {
		this.manager = manager;
	}
	
}
