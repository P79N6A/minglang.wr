package com.taobao.cun.auge.inspection.dto;

import java.io.Serializable;

public class InspectionStatusSummaryDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1929018629681535996L;

	/**
	 * 全部
	 */
	private Integer totalInspectionNum;
	
	/**
	 * 待巡检
	 */
	private Integer unInspectionNum;
	
	/**
	 * 未自检
	 */
	private Integer unPartnerInspectionNum;
	
	/**
	 * 待审核
	 */
	private Integer toAuditInspectionNum;

	public Integer getTotalInspectionNum() {
		return totalInspectionNum;
	}

	public void setTotalInspectionNum(Integer totalInspectionNum) {
		this.totalInspectionNum = totalInspectionNum;
	}

	public Integer getUnInspectionNum() {
		return unInspectionNum;
	}

	public void setUnInspectionNum(Integer unInspectionNum) {
		this.unInspectionNum = unInspectionNum;
	}

	public Integer getUnPartnerInspectionNum() {
		return unPartnerInspectionNum;
	}

	public void setUnPartnerInspectionNum(Integer unPartnerInspectionNum) {
		this.unPartnerInspectionNum = unPartnerInspectionNum;
	}

	public Integer getToAuditInspectionNum() {
		return toAuditInspectionNum;
	}

	public void setToAuditInspectionNum(Integer toAuditInspectionNum) {
		this.toAuditInspectionNum = toAuditInspectionNum;
	}
}
