package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.Map;

/**
 * 县村盘点总数信息
 * @author quanzhu.wangqz
 *
 */
public class CheckCountDto implements Serializable{

	private static final long serialVersionUID = 4562158628905291847L;
	/**
	 *   已盘总数
	 */
	private Long doneCount = 0L;
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
	public Long getDoneCount() {
		return doneCount;
	}
	public void setDoneCount(Long doneCount) {
		this.doneCount = doneCount;
	}
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
	
}
