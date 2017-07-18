package com.taobao.cun.auge.station.exception;

/**
 * 业务异常，异常信息，直接可以显示给用户的
 */
public class AugeBusinessException extends RuntimeException {

	private static final long serialVersionUID = -6479393095526687858L;

	protected String exceptionCode;
	
	public AugeBusinessException() {
		super();
	}
	

	public AugeBusinessException(String exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	public AugeBusinessException(String exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}


	public AugeBusinessException(Throwable cause) {
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