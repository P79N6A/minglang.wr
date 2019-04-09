package com.taobao.cun.auge.cuncounty.dto;

/**
 * 县点所属组织信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyOrgDto {
	private Long id;
	
	private Long parentId;
	
	private String name;
	
	private String fullIdPath;
	
	private String fullNamePath;
	
	private String orgRangeType;

	public String getOrgRangeType() {
		return orgRangeType;
	}

	public void setOrgRangeType(String orgRangeType) {
		this.orgRangeType = orgRangeType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullIdPath() {
		return fullIdPath;
	}

	public void setFullIdPath(String fullIdPath) {
		this.fullIdPath = fullIdPath;
	}

	public String getFullNamePath() {
		return fullNamePath;
	}

	public void setFullNamePath(String fullNamePath) {
		this.fullNamePath = fullNamePath;
	}
}
