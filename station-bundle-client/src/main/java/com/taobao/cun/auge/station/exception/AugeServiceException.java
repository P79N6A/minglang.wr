package com.taobao.cun.auge.station.exception;

import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
@Deprecated
//这个异常和AugeBusinessException功能类似，未来异常统一处理只保留AugeBusinessException和AugeSystemException
public class AugeServiceException extends RuntimeException {

	private static final long serialVersionUID = -6479393095526687858L;

	protected String exceptionCode;
	

	public AugeServiceException() {
		super();
	}
	
	public AugeServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public AugeServiceException(String exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	public AugeServiceException(String exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}
	
	public AugeServiceException(CommonExceptionEnum commonExceptionEnum) {
		this(commonExceptionEnum.getCode(),commonExceptionEnum.getDesc());
	}


	public AugeServiceException(String message) {
		super(message);
	}

	public AugeServiceException(Throwable cause) {
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
