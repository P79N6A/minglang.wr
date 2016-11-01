package com.taobao.cun.auge.station.exception;

import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;

public class AugeUicTagException extends RuntimeException {

	private static final long serialVersionUID = -3284419394285249761L;
	protected String exceptionCode;

	public AugeUicTagException() {
		super();
	}

	public AugeUicTagException(String message, Throwable cause) {
		super(message, cause);
	}

	public AugeUicTagException(String exceptionCode, String message, Throwable cause) {
		super(message, cause);
		this.exceptionCode = exceptionCode;
	}

	public AugeUicTagException(String exceptionCode, String message) {
		super(message);
		this.exceptionCode = exceptionCode;
	}

	public AugeUicTagException(CommonExceptionEnum commonExceptionEnum) {
		this(commonExceptionEnum.getCode(), commonExceptionEnum.getDesc());
	}

	public AugeUicTagException(String message) {
		super(message);
	}

	public AugeUicTagException(Throwable cause) {
		super(cause);
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}