package com.taobao.cun.auge.inspection.condition;

public class InspectionStationTypes {

	public static final String TPS = "TPS"; //镇店
	
	public static final String TP_V4 = "TP_V4"; //天猫优品服务站
	
	public static final String TP = "TP";//村点
	
	public static String getVersion(String type){
		if(TP_V4.equals(type)){
			return "v4";
		}
		return null;
	}
	
	public static String getType(String type){
		if(TP_V4.equals(type)){
			return "TP";
		}
		return type;
	}
}
