package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementTypeIdEnum;

/**
 * 删除附件dto
 * @author quanzhu.wangqz
 *
 */
public class AttachementDeleteDto extends OperatorDto implements Serializable {

private static final long serialVersionUID = -2236478973303620397L;
	
    /**
     * 附件类型id
     */
    private AttachementTypeIdEnum attachementTypeId;

    /**
     * 外部id,如station_apply表id
     */
    private Long objectId;

    /**
     * 业务类型
     */
    private AttachementBizTypeEnum bizType;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public AttachementTypeIdEnum getAttachementTypeId() {
		return attachementTypeId;
	}

	public void setAttachementTypeId(AttachementTypeIdEnum attachementTypeId) {
		this.attachementTypeId = attachementTypeId;
	}

	public AttachementBizTypeEnum getBizType() {
		return bizType;
	}

	public void setBizType(AttachementBizTypeEnum bizType) {
		this.bizType = bizType;
	}
}
