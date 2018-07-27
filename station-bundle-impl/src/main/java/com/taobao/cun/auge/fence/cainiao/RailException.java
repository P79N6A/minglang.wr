package com.taobao.cun.auge.fence.cainiao;

import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;

public class RailException extends RuntimeException {

	private static final long serialVersionUID = -4235558769562786954L;

	private RailInfoRequest request;
	
	public RailException(RailInfoRequest request, String message) {
		super(message);
		this.request = request;
	}
	
	public RailException(String message) {
		super(message);
	}

	public RailInfoRequest getRequest() {
		return request;
	}

	public void setRequest(RailInfoRequest request) {
		this.request = request;
	}
}
