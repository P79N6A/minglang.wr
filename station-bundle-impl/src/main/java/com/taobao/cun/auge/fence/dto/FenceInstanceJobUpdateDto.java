package com.taobao.cun.auge.fence.dto;

import java.util.Date;

/**
 * 更新任务信息
 * 
 * @author chengyu.zhoucy
 *
 */
public class FenceInstanceJobUpdateDto {
	private Long id;
	
	private String state;
	
	private Integer instanceNum = 0;

    private Date gmtStartTime;

    private Date gmtEndTime;
    
    private String errorMsg;

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getInstanceNum() {
		return instanceNum;
	}

	public void setInstanceNum(Integer instanceNum) {
		this.instanceNum = instanceNum;
	}

	public Date getGmtStartTime() {
		return gmtStartTime;
	}

	public void setGmtStartTime(Date gmtStartTime) {
		this.gmtStartTime = gmtStartTime;
	}

	public Date getGmtEndTime() {
		return gmtEndTime;
	}

	public void setGmtEndTime(Date gmtEndTime) {
		this.gmtEndTime = gmtEndTime;
	}
}
