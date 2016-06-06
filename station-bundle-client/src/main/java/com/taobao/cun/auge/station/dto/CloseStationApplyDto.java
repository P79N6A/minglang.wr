package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;

/**
 * 停业申请dto
 * @author quanzhu.wangqz
 *
 */
public class CloseStationApplyDto  extends OperatorDto implements Serializable {
	
	
	private static final long serialVersionUID = -6320982530549407257L;

	/**
	 * 合伙人实例id
	 */
	@NotNull(message="instanceId not null")
    private Long partnerInstanceId;
    
    /**
     * 停业类型
     */
	@NotNull(message="PartnerInstanceCloseTypeEnum not null")
    private PartnerInstanceCloseTypeEnum type;

    /**
     * 停业原因
     */
	@NotNull(message="closeReason not null")
    private CloseStationApplyCloseReasonEnum closeReason;

    /**
     * 其他原因
     */
    private String otherReason;

	public Long getPartnerInstanceId() {
		return partnerInstanceId;
	}

	public void setPartnerInstanceId(Long partnerInstanceId) {
		this.partnerInstanceId = partnerInstanceId;
	}

	public CloseStationApplyCloseReasonEnum getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(CloseStationApplyCloseReasonEnum closeReason) {
		this.closeReason = closeReason;
	}

	public String getOtherReason() {
		return otherReason;
	}

	public void setOtherReason(String otherReason) {
		this.otherReason = otherReason;
	}

	public PartnerInstanceCloseTypeEnum getType() {
		return type;
	}

	public void setType(PartnerInstanceCloseTypeEnum type) {
		this.type = type;
	}
}
