package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetCountDto implements Serializable{

    private static final long serialVersionUID = 6998191282561709835L;

    private String name;

    private String number;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
