package com.taobao.cun.auge.fence.dto.job;

public abstract class FenceInstanceJob {
	private Long id;
	
	private String jobType;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
