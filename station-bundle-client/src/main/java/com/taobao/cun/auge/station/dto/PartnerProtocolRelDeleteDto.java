package com.taobao.cun.auge.station.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

/**
 * 
 * @author quanzhu.wangqz
 *
 */
public class PartnerProtocolRelDeleteDto  extends OperatorDto implements Serializable {

	private static final long serialVersionUID = -5458194521053835714L;


	private List<ProtocolTypeEnum> ProtocolTypeList; 

	private Long objectId;

    private PartnerProtocolRelTargetTypeEnum targetType;

    public List<ProtocolTypeEnum> getProtocolTypeList() {
		return ProtocolTypeList;
	}

	public void setProtocolTypeList(List<ProtocolTypeEnum> protocolTypeList) {
		ProtocolTypeList = protocolTypeList;
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

}
