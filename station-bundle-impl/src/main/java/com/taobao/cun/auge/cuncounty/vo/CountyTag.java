package com.taobao.cun.auge.cuncounty.vo;

import com.google.common.collect.Lists;

import java.util.List;

public class CountyTag {
    private Long countyId;

    private List<String> tags = Lists.newArrayList();

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
