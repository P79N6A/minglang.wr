package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerProtocolRelDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -5458194521053835714L;
	
	private Long protocolId;
	
	public Long getProtocolId() {
		return protocolId;
	}

	public void setProtocolId(Long protocolId) {
		this.protocolId = protocolId;
	}

	private Long taobaoUserId;
	
	private ProtocolTypeEnum protocolTypeEnum;

    private Date confirmTime;

    private Date startTime;

    private Date endTime;

    private Long objectId;

    private PartnerProtocolRelTargetTypeEnum targetType;

	public Long getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}


	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public PartnerProtocolRelTargetTypeEnum getTargetType() {
		return targetType;
	}

	public void setTargetType(PartnerProtocolRelTargetTypeEnum targetType) {
		this.targetType = targetType;
	}

	public ProtocolTypeEnum getProtocolTypeEnum() {
		return protocolTypeEnum;
	}

	public void setProtocolTypeEnum(ProtocolTypeEnum protocolTypeEnum) {
		this.protocolTypeEnum = protocolTypeEnum;
	}
	
	
}
