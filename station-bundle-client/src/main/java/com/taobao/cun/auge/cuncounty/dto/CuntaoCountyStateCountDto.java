package com.taobao.cun.auge.cuncounty.dto;

/**
 * 县点按状态统计
 * 
 * @author chengyu.zhoucy
 *
 */
public class CuntaoCountyStateCountDto {
	/**
	 * 县点状态
	 */
	private CuntaoCountyStateEnum cuntaoCountyState;
	/**
	 * 县点数
	 */
	private Long num;
	public CuntaoCountyStateEnum getCuntaoCountyState() {
		return cuntaoCountyState;
	}
	public void setCuntaoCountyState(CuntaoCountyStateEnum cuntaoCountyState) {
		this.cuntaoCountyState = cuntaoCountyState;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
}
