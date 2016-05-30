package com.taobao.cun.auge.station.condition;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 查询实例条件
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceCondition extends OperatorDto implements Serializable{

	private static final long serialVersionUID = 9015126136586505699L;
	
	private Long instanceId;
	
	/**
	 * 是否需要村点信息
	 */
	private Boolean needStationInfo = Boolean.TRUE;
	
	/**
	 * 是否需要合伙人信息
	 */
	private Boolean needPartnerInfo = Boolean.TRUE;
	
	/**
	 * 是否需要脱敏
	 */
	private Boolean needDesensitization = Boolean.TRUE;

	public Long getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(Long instanceId) {
		this.instanceId = instanceId;
	}

	public Boolean getNeedStationInfo() {
		return needStationInfo;
	}

	public void setNeedStationInfo(Boolean needStationInfo) {
		this.needStationInfo = needStationInfo;
	}

	public Boolean getNeedPartnerInfo() {
		return needPartnerInfo;
	}

	public void setNeedPartnerInfo(Boolean needPartnerInfo) {
		this.needPartnerInfo = needPartnerInfo;
	}

	public Boolean getNeedDesensitization() {
		return needDesensitization;
	}

	public void setNeedDesensitization(Boolean needDesensitization) {
		this.needDesensitization = needDesensitization;
	}
}
