package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

public class PartnerFlowerNameApplyDto implements Serializable{

	private static final long serialVersionUID = 1L;
	private Long taobaoUserId;
	private String flowerName;
	private String flowerNamePinyin;
	private String nameMeaning;
	private String nameSource;
	private String status;
	private Long id;
	
	public Long getTaobaoUserId() {
		return taobaoUserId;
	}
	public void setTaobaoUserId(Long taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}
	public String getFlowerName() {
		return flowerName;
	}
	public void setFlowerName(String flowerName) {
		this.flowerName = flowerName;
	}
	public String getFlowerNamePinyin() {
		return flowerNamePinyin;
	}
	public void setFlowerNamePinyin(String flowerNamePinyin) {
		this.flowerNamePinyin = flowerNamePinyin;
	}
	public String getNameMeaning() {
		return nameMeaning;
	}
	public void setNameMeaning(String nameMeaning) {
		this.nameMeaning = nameMeaning;
	}
	public String getNameSource() {
		return nameSource;
	}
	public void setNameSource(String nameSource) {
		this.nameSource = nameSource;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
}
