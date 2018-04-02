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
	 * 计划巡检
	 */
	private Integer planInspectionNum;
	
	/**
	 * 已巡检
	 */
	private Integer hasInspectionNum;

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

	public Integer getPlanInspectionNum() {
		return planInspectionNum;
	}

	public void setPlanInspectionNum(Integer planInspectionNum) {
		this.planInspectionNum = planInspectionNum;
	}

	public Integer getHasInspectionNum() {
		return hasInspectionNum;
	}

	public void setHasInspectionNum(Integer hasInspectionNum) {
		this.hasInspectionNum = hasInspectionNum;
	}
	
	
	
}
