package com.taobao.cun.auge.station.condition;

import com.taobao.cun.auge.common.OperatorDto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查询实例条件
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceCondition extends OperatorDto implements Serializable {

	private static final long serialVersionUID = 9015126136586505699L;
	
	/**
	 * 实例id
	 */
	@NotNull(message="instanceId is null")
	private Long instanceId;

	/**
	 * 是否需要村点信息
	 */
	private Boolean needStationInfo = Boolean.FALSE;

	/**
	 * 是否需要合伙人信息
	 */
	private Boolean needPartnerInfo = Boolean.FALSE;

	/**
	 * 是否需要脱敏
	 */
	private Boolean needDesensitization = Boolean.FALSE;

	/**
	 * 是否需要合伙人分层信息
	 */
	private Boolean needPartnerLevelInfo = Boolean.FALSE;

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

	public Boolean getNeedPartnerLevelInfo() {
		return needPartnerLevelInfo;
	}

	public void setNeedPartnerLevelInfo(Boolean needPartnerLevelInfo) {
		this.needPartnerLevelInfo = needPartnerLevelInfo;
	}

	public PartnerInstanceCondition() {
	}

	public PartnerInstanceCondition(boolean needStationInfo, boolean needPartnerInfo, boolean needDesensitization) {
		this.needStationInfo = needStationInfo;
		this.needPartnerInfo = needPartnerInfo;
		this.needDesensitization = needDesensitization;
	}

}
