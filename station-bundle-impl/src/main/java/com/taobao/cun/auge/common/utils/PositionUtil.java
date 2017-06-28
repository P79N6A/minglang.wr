package com.taobao.cun.auge.common.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import org.apache.commons.lang.StringUtils;

public class PositionUtil {
	public static String converUp(String val){	
		if(StringUtils.isBlank(val)){
			return "";
		}
		Float a = Float.valueOf(val);
		Double b=a*1e5;		
		return String.valueOf(b.intValue());
	}
	
	public static String converDown(String val){
		if(StringUtils.isBlank(val)){
			return "";
		}
		Double a = Double.valueOf(val);
		DecimalFormat dFormat=new DecimalFormat("#.######");
		Double b=a/1e5;
		return dFormat.format(b);
	}

	
	public static Double div(String v1, int v2, int scale) {  
		   if (scale < 0) {  
		    throw new IllegalArgumentException(  
		      "The scale must be a positive integer or zero");  
		   }  
		   BigDecimal b1 = new BigDecimal(v1);  
		   BigDecimal b2 = new BigDecimal(v2);  
		   return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();  
	}  
}
