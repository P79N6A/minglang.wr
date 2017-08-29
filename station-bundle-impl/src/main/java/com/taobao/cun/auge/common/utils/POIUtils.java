package com.taobao.cun.auge.common.utils;

import java.math.BigDecimal;

public class POIUtils {

	public static Double toStanardPOI(String poi) {  
		   BigDecimal b1 = new BigDecimal(poi);  
		   BigDecimal b2 = new BigDecimal(100000);  
		   return b1.divide(b2, 5, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}  
}
