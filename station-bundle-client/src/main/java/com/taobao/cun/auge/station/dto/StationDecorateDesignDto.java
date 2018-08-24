package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.taobao.cun.attachment.dto.AttachmentDto;

public class StationDecorateDesignDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 站点ID
	 */
	private Long stationId;
	
	/**
	 * 卖场面积
	 */
	private String mallArea;
	
	/**
	 * 仓库面积
	 */
	private String repoArea;
	
	/**
	 * 租金
	 */
	private BigDecimal rentMoney;
	
	/**
	 * 预算
	 */
	private BigDecimal budgetMoney;
	
	/**
	 * 设计门头附件
	 */
	private List<AttachmentDto> doorAttachments;
	
	/**
	 * 设计背景墙附件
	 */
	private List<AttachmentDto> wallAttachments;
	
	/**
	 * 设计室内图附件
	 */
	private List<AttachmentDto> insideAttachments;

	/**
	 * 操作人
	 */
	private String operator;
	
	public String getMallArea() {
		return mallArea;
	}

	public void setMallArea(String mallArea) {
		this.mallArea = mallArea;
	}

	public String getRepoArea() {
		return repoArea;
	}

	public void setRepoArea(String repoArea) {
		this.repoArea = repoArea;
	}

	public BigDecimal getRentMoney() {
		return rentMoney;
	}

	public void setRentMoney(BigDecimal rentMoney) {
		this.rentMoney = rentMoney;
	}

	public BigDecimal getBudgetMoney() {
		return budgetMoney;
	}

	public void setBudgetMoney(BigDecimal budgetMoney) {
		this.budgetMoney = budgetMoney;
	}

	public List<AttachmentDto> getDoorAttachments() {
		return doorAttachments;
	}

	public void setDoorAttachments(List<AttachmentDto> doorAttachments) {
		this.doorAttachments = doorAttachments;
	}

	public List<AttachmentDto> getWallAttachments() {
		return wallAttachments;
	}

	public void setWallAttachments(List<AttachmentDto> wallAttachments) {
		this.wallAttachments = wallAttachments;
	}

	public List<AttachmentDto> getInsideAttachments() {
		return insideAttachments;
	}

	public void setInsideAttachments(List<AttachmentDto> insideAttachments) {
		this.insideAttachments = insideAttachments;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

}
