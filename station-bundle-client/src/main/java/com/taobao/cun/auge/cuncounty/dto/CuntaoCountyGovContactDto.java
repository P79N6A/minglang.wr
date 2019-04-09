package com.taobao.cun.auge.cuncounty.dto;

/**
 * 政府联系人
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyGovContactDto {
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 职务
     */
    private String position;
    /**
     * 部门
     */
    private String department;
    /**
     * 电话
     */
    private String telephone;

    /**
     * 备注
     */
    private String memo;

    private Long countyId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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