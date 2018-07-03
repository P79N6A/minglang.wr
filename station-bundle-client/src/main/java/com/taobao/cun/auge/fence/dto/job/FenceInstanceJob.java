package com.taobao.cun.auge.fence.dto.job;

public abstract class FenceInstanceJob {
	private String jobType;
	
	public FenceInstanceJob() {
		setJobType(getClass().getSimpleName());
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
}
