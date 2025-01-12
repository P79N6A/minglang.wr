package com.taobao.cun.auge.tag;

public enum UserTag {

	TP_USER_TAG("usertag11#4096"),
	
	 TP_USER_TAG2("cuntao_village"),
	
	TPA_USER_TAG("usertag17#8192"),
	
	TPT_USER_TAG("usertag19#36028797018963968"),
	
	SAMPLE_SELLER_TAG("cuntao_sample_seller"),
	
	CUNTAO_YEAR_TAG("cuntao_year2018"),
	
	CUNTAO_MER_TAG("usertag15#4"),
	
	TPS_USER_TAG("cuntao_store"),

	UM_USER_TAG("cuntao_youmeng"),

	STATION_NEW_CUSTOMER_TAG("new_ctp_user"),
	
	SELLER_HQZY_TAG("cuntao_huoquan_zhuanyi");


	private UserTag(String tag){
		 this.tag = tag;
	}
	 
	 private String tag;

	public String getTag() {
		return tag;
	}
	 
	public static UserTag valueOfTag(String tag){
		for(UserTag userTag : UserTag.values()){
			if(userTag.getTag().equals(tag)){
				return userTag;
			}
		}
		return null;
	}
	 
}
