package com.taobao.cun.auge.station.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 装修记录审核dto
 * @author quanzhu.wangqz
 *
 */
public class StationDecorateAuditDto  extends OperatorDto {

	private static final long serialVersionUID = 8482954260715426676L;


	/**
	 * 主键id
	 */
	@NotNull(message="stationDecorateId not null")
    private Long id;
    
    
	/**
	 * 是否同意
	 */
	@NotNull(message="isAgree not null")
	private Boolean isAgree;
	
    /**
     * 审计意见
     */
    private String auditOpinion;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getIsAgree() {
		return isAgree;
	}

	public void setIsAgree(Boolean isAgree) {
		this.isAgree = isAgree;
	}

	public String getAuditOpinion() {
		return auditOpinion;
	}

	public void setAuditOpinion(String auditOpinion) {
		this.auditOpinion = auditOpinion;
	}
}
