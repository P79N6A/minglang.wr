package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 县盘点总数信息
 * @author quanzhu.wangqz
 *
 */
public class CountyCheckCountDto implements Serializable{

	private static final long serialVersionUID = 4562158628905291847L;
	/**
	 * it资产已盘总数
	 */
	private Long itDoneCount;
	/**
	 * it资产已盘详情 key:AssetCheckInfoCategoryTypeEnum 的code value:总数
	 */
	private Map<String,Long> itDoneDetail;
	/**
	 * 行政资产已盘总数
	 */
	private Long adminDoneCount; 
	/**
	 * 行政资产已盘详情 key:AssetCheckInfoCategoryTypeEnum 的code value:总数
	 */
	private Map<String,Long> adminDoneDetailt;
	/**
	 * it资产未盘总数
	 */
	private Long itDoingCount;
	/**
	 * 行政资产未盘总数
	 */
	private Long adminDoingCount;
	public Long getItDoneCount() {
		return itDoneCount;
	}
	public void setItDoneCount(Long itDoneCount) {
		this.itDoneCount = itDoneCount;
	}
	public Map<String, Long> getItDoneDetail() {
		return itDoneDetail;
	}
	public void setItDoneDetail(Map<String, Long> itDoneDetail) {
		this.itDoneDetail = itDoneDetail;
	}
	public Long getAdminDoneCount() {
		return adminDoneCount;
	}
	public void setAdminDoneCount(Long adminDoneCount) {
		this.adminDoneCount = adminDoneCount;
	}
	public Map<String, Long> getAdminDoneDetailt() {
		return adminDoneDetailt;
	}
	public void setAdminDoneDetailt(Map<String, Long> adminDoneDetailt) {
		this.adminDoneDetailt = adminDoneDetailt;
	}
	public Long getItDoingCount() {
		return itDoingCount;
	}
	public void setItDoingCount(Long itDoingCount) {
		this.itDoingCount = itDoingCount;
	}
	public Long getAdminDoingCount() {
		return adminDoingCount;
	}
	public void setAdminDoingCount(Long adminDoingCount) {
		this.adminDoingCount = adminDoingCount;
	}
}
