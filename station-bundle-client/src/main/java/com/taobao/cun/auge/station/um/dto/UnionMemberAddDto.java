package com.taobao.cun.auge.station.um.dto;

import javax.validation.constraints.NotNull;

import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;

/**
 * 新增优盟dto
 *
 * @author haihu.fhh
 */
public class UnionMemberAddDto extends OperatorDto {

    private static final long serialVersionUID = -1247129280485656024L;

    /**
     * 合作店名称
     */
    @NotNull(message = "stationName not null")
    private String stationName;

    /**
     * 地址相关信息
     */
    @NotNull(message = "address not null")
    private Address address;

    /**
     * 店铺类型
     */
    @NotNull(message = "format not null")
    private String format;

    /**
     * 日均人流
     */
    @NotNull(message = "covered not null")
    private String covered;

    /**
     * 合作店简介
     */
    private String description;

    /**
     * 优盟归属村站id
     */
    @NotNull(message = "parentStationId not null")
    private Long parentStationId;

    /**
     * 淘宝nick
     */
    @NotNull(message = "taobaoNick not null")
    private String taobaoNick;

    /**
     * 手机号码
     */
    @NotNull(message = "mobile not null")
    private String mobile;

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

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    public String getTaobaoNick() {
        return taobaoNick;
    }

    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
