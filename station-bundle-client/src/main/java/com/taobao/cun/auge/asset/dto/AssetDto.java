package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 17/5/19.
 */
public class AssetDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = -757424770912097035L;

    private String aliNo;

    public String getAliNo() {
        return aliNo;
    }

    public void setAliNo(String aliNo) {
        this.aliNo = aliNo;
    }
}
