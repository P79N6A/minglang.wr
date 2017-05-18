package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.client.operator.DefaultOperator;

/**
 * Created by xiao on 17/5/18.
 */
public class AssetOperatorDto extends DefaultOperator implements Serializable{

    private static final long serialVersionUID = 5303374647072334439L;

    private String workNo;

    public String getWorkNo() {
        return workNo;
    }

    public void setWorkNo(String workNo) {
        this.workNo = workNo;
    }
}
