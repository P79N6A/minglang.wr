package com.taobao.cun.auge.cuncounty.dto.edit;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 政府联系人
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovContactAddDto {
	@NotBlank(message="姓名不能为空")
    private String name;
	@NotBlank(message="职务不能为空")
    private String position;
	@NotBlank(message="部门不能为空")
    private String department;
    @NotBlank(message="电话不能为空")
    private String telephone;
    @NotNull(message="县服务中心不能为空")
    private Long countyId;
    @NotBlank(message="创建人不能为空")
    private String operator;
    /**
     * 备注
     */
    private String memo;

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Long getCountyId() {
		return countyId;
	}

	public void setCountyId(Long countyId) {
		this.countyId = countyId;
	}

}