package com.taobao.cun.auge.level.dto;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class TownLevelUpdateApplyDto implements Serializable {
	private static final long serialVersionUID = -7470629011614224159L;
	private Long id;
	@NotNull(message="镇域分层ID不能为空")
	private Long townLevelId;
	@Min(value=1, message="镇人口数必须是大于0的数值")
	private long population;
	@NotBlank(message="申请变更原因不能为空")
	@Length(min=10, max=512, message="原因最少10个字符，最多512个字符")
	private String reason;
	@NotBlank(message="凭证不能为空")
	private String attachments;
	@NotBlank(message="申请人不能为空")
	private String creator;
	
	public Long getTownLevelId() {
		return townLevelId;
	}
	public void setTownLevelId(Long townLevelId) {
		this.townLevelId = townLevelId;
	}
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
	public long getPopulation() {
		return population;
	}
	public void setPopulation(long population) {
		this.population = population;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getAttachments() {
		return attachments;
	}
	public void setAttachments(String attachments) {
		this.attachments = attachments;
	}
}
