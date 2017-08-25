package com.taobao.cun.auge.store.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建仓库的DTO
 *
 */
public class InventoryStoreCreateDto implements Serializable{
	private static final long serialVersionUID = 6288183021002325541L;

	/**
	 * 仓库CODE,最好不要自己指定，让系统自动生成
	 */
	private String code;
	
	/**
	 * 仓库名称
	 */
	@NotEmpty(message="仓库名称不能为空")
	private String name;
	
	/**
	 * 仓库别名
	 */
	private String alias;
	
	/**
	 * 优先级
	 */
	private int priority = 0;
	
	/**
	 * 淘宝用户ID
	 */
	@NotNull(message="淘宝用户ID不能为空")
	private Long userId;
	
	/**
	 * 行政CODE，取到叶子节点的CODE即可，例如到县一级就是县的CODE
	 */
	@NotNull(message="地址CODE不能为空")
	private Long areaId;
	
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}
}
