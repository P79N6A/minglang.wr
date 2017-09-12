package com.taobao.cun.auge.asset.convert;

import java.io.Serializable;

public class AssetOwner  implements Serializable{

	private static final long serialVersionUID = -5234238220300568882L;
	private String orgId;
	private String ownerName;
	private String ownerWorkno;
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public String getOwnerWorkno() {
		return ownerWorkno;
	}
	public void setOwnerWorkno(String ownerWorkno) {
		this.ownerWorkno = ownerWorkno;
	}
	
	
}
