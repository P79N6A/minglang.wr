package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class StationCondition extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -5161645049551081998L;

	/**
	 * 村服务站id
	 */
	private Long id;
	
	/**
	 * 是否需要村点附件信息
	 */
	private Boolean needAttachementInfo = Boolean.TRUE;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getNeedAttachementInfo() {
		return needAttachementInfo;
	}

	public void setNeedAttachementInfo(Boolean needAttachementInfo) {
		this.needAttachementInfo = needAttachementInfo;
	}
}
