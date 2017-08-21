package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * Created by xiao on 17/8/21.
 */
public class AssetSignDto extends OperatorDto implements Serializable{

    private static final long serialVersionUID = -757424770912097035L;

    private Long incomeId;

    public Long getIncomeId() {
        return incomeId;
    }

    public void setIncomeId(Long incomeId) {
        this.incomeId = incomeId;
    }
}
