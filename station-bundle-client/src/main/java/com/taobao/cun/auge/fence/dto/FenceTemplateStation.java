package com.taobao.cun.auge.fence.dto;

import java.io.Serializable;

/**
 * Created by xiao on 18/6/17.
 */
public class FenceTemplateStation implements Serializable {

    private static final long serialVersionUID = -3348782058127217645L;

    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
