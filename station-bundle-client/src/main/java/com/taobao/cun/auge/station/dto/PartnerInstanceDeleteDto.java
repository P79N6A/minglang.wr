package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 实例删除dto
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceDeleteDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = -3494792623384321459L;
	
	  /**
     * 主键
     */
    private Long instanceId;
    /**
     * 是否同步删除服务站  true：删除
     */
    private Boolean isDeleteStation = Boolean.TRUE;
    
    /**
     * 是否同步删除合伙人  true：删除
     */
    private Boolean isDeletePartner = Boolean.TRUE;
    
    
    private Boolean is
    
    

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Boolean getIsDeleteStation() {
		return isDeleteStation;
	}

	public void setIsDeleteStation(Boolean isDeleteStation) {
		this.isDeleteStation = isDeleteStation;
	}

	public Boolean getIsDeletePartner() {
		return isDeletePartner;
	}

	public void setIsDeletePartner(Boolean isDeletePartner) {
		this.isDeletePartner = isDeletePartner;
	}
}
