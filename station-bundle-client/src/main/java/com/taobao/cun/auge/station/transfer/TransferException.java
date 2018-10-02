package com.taobao.cun.auge.station.transfer;

public class TransferException extends Exception {

	private static final long serialVersionUID = -3134195314445094033L;
	
	public TransferException(String message) {
		super(message);
	}
	
	public TransferException(String message, Throwable t) {
		super(message, t);
	}

}
