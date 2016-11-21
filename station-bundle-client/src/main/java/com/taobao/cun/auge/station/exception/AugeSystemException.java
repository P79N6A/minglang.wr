package com.taobao.cun.auge.station.exception;

import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

public class AugeSystemException extends RuntimeException {

	private static final long serialVersionUID = -6479393095526687858L;

	protected String exceptionCode;
	

	public AugeSystemException() {
		super();
	}
	
	public AugeSystemException(String message, Throwable cause) {
		super(message, cause);
	}

	public AugeSystemException(String exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	public AugeSystemException(String exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}
	
	public AugeSystemException(CommonExceptionEnum commonExceptionEnum) {
		this(commonExceptionEnum.getCode(),commonExceptionEnum.getDesc());
	}


	public AugeSystemException(String message) {
		super(message);
	}

	public AugeSystemException(Throwable cause) {
		super(cause);
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
	
	@Override
	public String toString(){
		return "exceptionCode: "+getExceptionCode() + "message: " + getMessage();
	}

}
