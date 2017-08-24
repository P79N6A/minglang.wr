package com.taobao.cun.auge.store.dto;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 创建仓库的DTO
 *
 */
public class InventoryStoreCreateDto {
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
	private Long userId;
	
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
