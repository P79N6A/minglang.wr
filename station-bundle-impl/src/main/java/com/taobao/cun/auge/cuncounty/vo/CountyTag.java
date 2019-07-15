package com.taobao.cun.auge.cuncounty.vo;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyTagEnum;

import java.util.List;

public class CountyTag {
    private Long countyId;

    private List<CuntaoCountyTagEnum> tags = Lists.newArrayList();

    public Long getCountyId() {
        return countyId;
    }

    public void setCountyId(Long countyId) {
        this.countyId = countyId;
    }

    public List<CuntaoCountyTagEnum> getTags() {
        return tags;
    }

    public void setTags(List<CuntaoCountyTagEnum> tags) {
        this.tags = tags;
    }
}
