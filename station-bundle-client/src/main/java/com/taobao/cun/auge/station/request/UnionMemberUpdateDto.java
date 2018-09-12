package com.taobao.cun.auge.station.request;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;

/**
 * 修改优盟dto
 *
 * @author haihu.fhh
 */
public class UnionMemberUpdateDto extends OperatorDto {
    private static final long serialVersionUID = 1969483725280715807L;

    /**
     * 合作店id
     */
    @NotNull(message = "stationId not null")
    private Long stationId;

    /**
     * 合作店名称
     */
    private String stationName;

    /**
     * 地址相关信息
     */
    private Address address;

    /**
     * 店铺类型
     */
    private String format;

    /**
     * 日均人流
     */
    private String covered;

    /**
     * 合作店简介
     */
    private String description;

    /**
     * 手机号码
     */
    private String mobile;

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCovered() {
        return covered;
    }

    public void setCovered(String covered) {
        this.covered = covered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
