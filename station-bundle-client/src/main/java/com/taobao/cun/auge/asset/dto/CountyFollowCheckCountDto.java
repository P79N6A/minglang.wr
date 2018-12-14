package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 县跟踪盘点总数信息
 * @author quanzhu.wangqz
 *
 */
public class CountyFollowCheckCountDto implements Serializable{

	private static final long serialVersionUID = 4562158628905291847L;
	/**
	 * 已盘总数
	 */
	private Long doneCount =0L;
	
	/**
	 * 未盘总数
	 */
	private Long doingCount;
	
	/**
	 * 已盘村点数
	 */
	private Long doneStationCount; 
	
	/**
	 * 未盘村点数
	 */
	private Long doingStationCount;
	
	/**
	 * 已盘详情 key:AssetCheckInfoCategoryTypeEnum 的code value:总数
	 */
	private List<Map<String,Long>> doneDetail;
	
	/**
	 * 未盘详情 key:AssetCheckInfoCategoryTypeEnum 的code value:总数
	 */
	private List<Map<String,Long>> doingDetail;
	
	public List<Map<String, Long>> getDoneDetail() {
		return doneDetail;
	}

	public void setDoneDetail(List<Map<String, Long>> doneDetail) {
		this.doneDetail = doneDetail;
	}

	public List<Map<String, Long>> getDoingDetail() {
		return doingDetail;
	}

	public void setDoingDetail(List<Map<String, Long>> doingDetail) {
		this.doingDetail = doingDetail;
	}

	public Long getDoneCount() {
		return doneCount;
	}

	public void setDoneCount(Long doneCount) {
		this.doneCount = doneCount;
	}

	public Long getDoingCount() {
		return doingCount;
	}

	public void setDoingCount(Long doingCount) {
		this.doingCount = doingCount;
	}

	public Long getDoneStationCount() {
		return doneStationCount;
	}

	public void setDoneStationCount(Long doneStationCount) {
		this.doneStationCount = doneStationCount;
	}

	public Long getDoingStationCount() {
		return doingStationCount;
	}

	public void setDoingStationCount(Long doingStationCount) {
		this.doingStationCount = doingStationCount;
	}
}
