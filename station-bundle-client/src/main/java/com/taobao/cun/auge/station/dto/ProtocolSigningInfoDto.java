package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class ProtocolSigningInfoDto implements Serializable{

	private static final long serialVersionUID = 4929591636675459662L;
	//是否已经签约
	private boolean hasSigned;
	//协议
	private PartnerProtocolRelDto protocl;
	//合伙人实例
	private PartnerInstanceDto partnerInstance;
	
	public boolean getHasSigned() {
		return hasSigned;
	}
	public void setHasSigned(boolean hasSigned) {
		this.hasSigned = hasSigned;
	}
	public PartnerProtocolRelDto getProtocl() {
		return protocl;
	}
	public void setProtocl(PartnerProtocolRelDto protocl) {
		this.protocl = protocl;
	}
	public PartnerInstanceDto getPartnerInstance() {
		return partnerInstance;
	}
	public void setPartnerInstance(PartnerInstanceDto partnerInstance) {
		this.partnerInstance = partnerInstance;
	}
}
