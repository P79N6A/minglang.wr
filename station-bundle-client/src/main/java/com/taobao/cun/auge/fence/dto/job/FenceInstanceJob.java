package com.taobao.cun.auge.fence.dto.job;

import org.hibernate.validator.constraints.NotEmpty;

import com.alibaba.fastjson.annotation.JSONField;

public abstract class FenceInstanceJob {
	/**新建*/
	public static String CREATE_RULE_NEW = "NEW";
	/**覆盖*/
	public static String CREATE_RULE_OVERRIDE = "OVERRIDE";
	
	@JSONField(serialize=false, deserialize=false)
	private Long id;
	@JSONField(serialize=false, deserialize=false)
	private String jobType;
	@NotEmpty(message="创建者不能为空")
	private String creator;
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

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
