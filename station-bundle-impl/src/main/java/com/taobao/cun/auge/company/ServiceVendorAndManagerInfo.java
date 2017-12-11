package com.taobao.cun.auge.company;

import java.io.Serializable;

import com.taobao.cun.auge.company.dto.CuntaoVendorEmployeeState;
import com.taobao.cun.auge.company.dto.CuntaoEmployeeIdentifier;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public class ServiceVendorAndManagerInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private CuntaoServiceVendor cuntaoServiceVendor;
	
	
	private CuntaoEmployee manager;
	
	private Long id;
	
	private CuntaoVendorEmployeeState state;
	
	private CuntaoEmployeeIdentifier type;

	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CuntaoVendorEmployeeState getState() {
		return state;
	}

	public void setState(CuntaoVendorEmployeeState state) {
		this.state = state;
	}

	public CuntaoEmployeeIdentifier getType() {
		return type;
	}

	public void setType(CuntaoEmployeeIdentifier type) {
		this.type = type;
	}

	public CuntaoEmployee getManager() {
		return manager;
	}

	public void setManager(CuntaoEmployee manager) {
		this.manager = manager;
	}

	public CuntaoServiceVendor getCuntaoServiceVendor() {
		return cuntaoServiceVendor;
	}

	public void setCuntaoServiceVendor(CuntaoServiceVendor cuntaoServiceVendor) {
		this.cuntaoServiceVendor = cuntaoServiceVendor;
	}
	
}
