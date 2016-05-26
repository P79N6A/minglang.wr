package com.taobao.cun.auge.common.utils;

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

}
