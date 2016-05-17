package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class AlipayAccountTagDto implements Serializable {
	
	private static final long serialVersionUID = 5395448073340249490L;
	
	public static final String ALIPAY_CUNTAO_TAG_NAME = "CuntaoBusiness";
	public static final String ALIPAY_CUNTAO_BELONG_TO = "Taobao";
	public static final String ALIPAY_TAG_VALUE_Y = "T";// 打标
	public static final String ALIPAY_TAG_VALUE_F = "F";// 解标

	private String tagName; // 标签名

	private String tagValue;// 打标值: T 解标值: F

	private String belongTo;// 归属业务线

	private String logonId;// 这个字段不要用了

	private String userId;// 以2088开头的16位数字2088002007303250

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	public String getBelongTo() {
		return belongTo;
	}

	public void setBelongTo(String belongTo) {
		this.belongTo = belongTo;
	}

	public String getLogonId() {
		return logonId;
	}

	public void setLogonId(String logonId) {
		this.logonId = logonId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
