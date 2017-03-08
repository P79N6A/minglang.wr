package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

/**
 * Created by xiao on 17/2/20.
 *
 * @author xiao
 * @date 2017/02/20
 */
public class PartnerPeixunSupplierDto implements Serializable{

    private static final long serialVersionUID = -883488547033683393L;

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
