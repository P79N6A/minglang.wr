package com.taobao.cun.auge.fence.instance.rule;

import java.util.List;

/**
 * 范围规则
 * 
 * @author chengyu.zhoucy
 *
 */
public class RangeFenceRule extends FenceRule {
	/**
	 * 距离
	 */
	private Integer distance;
	/**
	 * 文本过滤
	 */
	private List<String> textFilter;
	/**
	 * 行政区划
	 */
	private String division;

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public List<String> getTextFilter() {
		return textFilter;
	}

	public void setTextFilter(List<String> textFilter) {
		this.textFilter = textFilter;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}
}
