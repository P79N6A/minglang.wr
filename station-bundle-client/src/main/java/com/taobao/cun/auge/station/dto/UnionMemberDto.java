package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.cun.auge.common.Address;

/**
 * 优盟实体dto
 *
 * @author haihu.fhh
 */
public class UnionMemberDto implements Serializable {

    private static final long serialVersionUID = 6581735268960126634L;
    /**
     * 合作店id
     */
    private Long stationId;

    /**
     * 合作店编号
     */
    private String stationNum;

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
     * 优盟归属村站id
     */
    private Long parentStationId;

    /**
     * 优盟归属村站信息
     */
    private StationDto parentStationDto;

    /**
     * 县服务中心orgId
     */
    private Long orgId;

    /**
     * 支付宝账号
     */
    private String alipayAccount;

    /**
     * 淘宝user_id
     */
    private Long taobaoUserId;

    /**
     * 淘宝nick
     */
    private String taobaoNick;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号码
     */
    private String idenNum;

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

    public String getStationNum() {
        return stationNum;
    }

    public void setStationNum(String stationNum) {
        this.stationNum = stationNum;
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

    public Long getParentStationId() {
        return parentStationId;
    }

    public void setParentStationId(Long parentStationId) {
        this.parentStationId = parentStationId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getAlipayAccount() {
        return alipayAccount;
    }

    public void setAlipayAccount(String alipayAccount) {
        this.alipayAccount = alipayAccount;
    }

    public Long getTaobaoUserId() {
        return taobaoUserId;
    }

    public void setTaobaoUserId(Long taobaoUserId) {
        this.taobaoUserId = taobaoUserId;
    }

    public String getTaobaoNick() {
        return taobaoNick;
    }

    public void setTaobaoNick(String taobaoNick) {
        this.taobaoNick = taobaoNick;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdenNum() {
        return idenNum;
    }

    public void setIdenNum(String idenNum) {
        this.idenNum = idenNum;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public StationDto getParentStationDto() {
        return parentStationDto;
    }

    public void setParentStationDto(StationDto parentStationDto) {
        this.parentStationDto = parentStationDto;
    }
}
