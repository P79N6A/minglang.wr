package com.taobao.cun.auge.fence.dto.job;

public abstract class AbstractFenceInstanceJob {
	private String jobType;
	
	public AbstractFenceInstanceJob() {
		setJobType(getClass().getSimpleName());
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
}
