package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetCountDto implements Serializable{

    private static final long serialVersionUID = 6998191282561709835L;

    private String category;

    private String number;
    
    public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
