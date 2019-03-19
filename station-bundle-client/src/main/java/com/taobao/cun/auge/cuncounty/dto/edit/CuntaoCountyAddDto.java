package com.taobao.cun.auge.cuncounty.dto.edit;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 添加县服务中心
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyAddDto {
	@NotBlank(message="县服务中心名称不能为空")
    private String name;
	@NotBlank(message="县行政CODE不能为空")
	private String countyCode;
	@NotNull(message="所属组织不能为空")
    private Long parentOrgId;
	@NotBlank(message="操作人不能为空")
    private String operator;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCountyCode() {
		return countyCode;
	}
	public void setCountyCode(String countyCode) {
		this.countyCode = countyCode;
	}
	public Long getParentOrgId() {
		return parentOrgId;
	}
	public void setParentOrgId(Long parentOrgId) {
		this.parentOrgId = parentOrgId;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
}