package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

/**
 * 停业申请dto
 * @author quanzhu.wangqz
 *
 */
public class CloseStationApplyDto  extends OperatorDto implements Serializable {
	
	
	private static final long serialVersionUID = -6320982530549407257L;
	
	//申请单主键
	private Long id;

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
     * 
     * 因为合伙人主动停业时，没有原因，所以为非必空
     */
    private CloseStationApplyCloseReasonEnum closeReason;

    /**
     * 其他原因
     */
    private String otherReason;
    
    //合伙人实例当前状态
    private PartnerInstanceStateEnum instanceState;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public PartnerInstanceStateEnum getInstanceState() {
		return instanceState;
	}

	public void setInstanceState(PartnerInstanceStateEnum instanceState) {
		this.instanceState = instanceState;
	}
}
