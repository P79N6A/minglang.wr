package com.taobao.cun.auge.fence.cainiao;

import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;

public class RailException extends RuntimeException {

	private static final long serialVersionUID = -4235558769562786954L;

	private RailInfoRequest railInfoRequest;
	
	public RailException(RailInfoRequest request, String message) {
		super(message);
		this.railInfoRequest = request;
	}
	
	public RailException(String message) {
		super(message);
	}

	public RailInfoRequest getRailInfoRequest() {
		return railInfoRequest;
	}

	public void setRailInfoRequest(RailInfoRequest railInfoRequest) {
		this.railInfoRequest = railInfoRequest;
	}
}
