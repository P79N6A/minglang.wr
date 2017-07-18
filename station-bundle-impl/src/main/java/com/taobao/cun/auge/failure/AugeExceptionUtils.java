package com.taobao.cun.auge.failure;

import com.taobao.cun.auge.station.exception.AugeBusinessException;

public class AugeExceptionUtils {

	public static void rethrowAugeBusinessException(String errorCode,String errorMessage){
		throw new AugeBusinessException(errorCode,errorMessage);
	}
}
