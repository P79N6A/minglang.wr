package com.taobao.cun.auge.lifecycle;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public class PhaseKey {

	private static final String SPLIT = "_";

	private String userType;
	
	private String event;
	
	public PhaseKey(String userType,String event){
		this.userType = userType;
		this.event = event;
	}
	
	public String getKey(){
		return userType +SPLIT+event;
	}
}
