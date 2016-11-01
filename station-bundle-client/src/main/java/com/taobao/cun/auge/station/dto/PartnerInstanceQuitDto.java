package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 实例退出dto
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceQuitDto extends OperatorDto implements Serializable {
	
	private static final long serialVersionUID = -3494792623384321459L;
	
	  /**
     * 主键
     */
    private Long instanceId;
    /**
     * 是否同步退出服务站  true：退出
     */
   // private Boolean isQuitStation = Boolean.TRUE;
 
	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	/*public Boolean getIsQuitStation() {
		return isQuitStation;
	}

	public void setIsQuitStation(Boolean isQuitStation) {
		this.isQuitStation = isQuitStation;
	}*/
}
