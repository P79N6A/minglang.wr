package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class ProtocolSigningInfoDto implements Serializable {

	private static final long serialVersionUID = 4929591636675459662L;
	// 是否已经签约
	private boolean hasSigned;
	// 协议
	private ProtocolDto protocol;
	// 合伙人实例
	private PartnerInstanceDto partnerInstance;

	public boolean getHasSigned() {
		return hasSigned;
	}

	public void setHasSigned(boolean hasSigned) {
		this.hasSigned = hasSigned;
	}

	public ProtocolDto getProtocol() {
		return protocol;
	}

	public void setProtocol(ProtocolDto protocol) {
		this.protocol = protocol;
	}

	public PartnerInstanceDto getPartnerInstance() {
		return partnerInstance;
	}

	public void setPartnerInstance(PartnerInstanceDto partnerInstance) {
		this.partnerInstance = partnerInstance;
	}
}
