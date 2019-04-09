package com.taobao.cun.auge.cuncounty.exception;

/**
 * 不合法的状态
 * 
 * @author chengyu.zhoucy
 *
 */
public class IllegalCountyStateException extends RuntimeException {

	private static final long serialVersionUID = 5651708727407799048L;
	
	public IllegalCountyStateException(String message) {
		super(message);
	}

}
