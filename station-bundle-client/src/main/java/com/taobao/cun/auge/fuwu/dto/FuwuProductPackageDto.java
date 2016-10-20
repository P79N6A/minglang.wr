package com.taobao.cun.auge.fuwu.dto;

import java.io.Serializable;
import java.util.List;

public class FuwuProductPackageDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String key;
	private String name;
	private String icon;
	private String iconMiddle;
	private String iconSmall;
	private String iconLarge;
	private String description;
	private String descDetail;
	private double price;
	private double basePrice;
	private List<FuwuProductDto> products;
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescDetail() {
		return descDetail;
	}
	public void setDescDetail(String descDetail) {
		this.descDetail = descDetail;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getBasePrice() {
		return basePrice;
	}
	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}
	public List<FuwuProductDto> getProducts() {
		return products;
	}
	public void setProducts(List<FuwuProductDto> products) {
		this.products = products;
	}
	public String getIconMiddle() {
		return iconMiddle;
	}
	public void setIconMiddle(String iconMiddle) {
		this.iconMiddle = iconMiddle;
	}
	public String getIconSmall() {
		return iconSmall;
	}
	public void setIconSmall(String iconSmall) {
		this.iconSmall = iconSmall;
	}
	public String getIconLarge() {
		return iconLarge;
	}
	public void setIconLarge(String iconLarge) {
		this.iconLarge = iconLarge;
	}
	
	

}
