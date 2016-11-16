package com.taobao.cun.auge.fuwu.dto;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yi.shaoy
 *
 */
/**
 * @author yi.shaoy
 *
 */
public class FuwuProductDto implements Serializable {
	private static final long serialVersionUID = 1L;
	private String mkey;
	private String code;
	private String groupKey;
	private String name;
	private String icon;
	private String iconMiddle;
	private String iconSmall;
	private String iconLarge;
	private double price;
	private double basePrice;
    private String description;
    private String descDetail;
    private String payUrl;
    
    
	public String getMkey() {
		return mkey;
	}
	public void setMkey(String mkey) {
		this.mkey = mkey;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getGroupKey() {
		return groupKey;
	}
	public void setGroupKey(String groupKey) {
		this.groupKey = groupKey;
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
	public String getPayUrl() {
		return payUrl;
	}
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
    
	
}
