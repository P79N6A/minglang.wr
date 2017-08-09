package com.taobao.cun.auge.opensearch;

import com.alibaba.fastjson.annotation.JSONField;

public class StationBuyerQueryListVo extends BaseOpenSearchListVo {
    @JSONField(name = "station_id")
    private Long stationId; 
    @JSONField(name = "taobao_user_id")
    private Long tbUserId; 
    @JSONField(name = "applier_name")
    private String applierName; 
    @JSONField(name = "name")
    private String stationName; 
    @JSONField(name = "province")
    private String provinceCode; 
    @JSONField(name = "province_detail")
    private String provinceDetail;
    @JSONField(name = "city")
    private String cityCode;
    @JSONField(name = "city_detail")
    private String cityDetail;
    @JSONField(name = "county")
    private String countyCode;
    @JSONField(name = "county_detail")
    private String countyDetail;
    @JSONField(name = "town")
    private String townCode;
    @JSONField(name = "town_detail")
    private String townDetail;
    @JSONField(name = "mobile")
    private String mobile; 

    @JSONField(name = "type")
    private String operatorType;

    public String getOperatorType() {
        return operatorType;
    }

    public void setOperatorType(String operatorType) {
        this.operatorType = operatorType;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Long getTbUserId() {
        return tbUserId;
    }

    public void setTbUserId(Long tbUserId) {
        this.tbUserId = tbUserId;
    }

    public String getApplierName() {
        return applierName;
    }

    public void setApplierName(String applierName) {
        this.applierName = applierName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceDetail() {
        return provinceDetail;
    }

    public void setProvinceDetail(String provinceDetail) {
        this.provinceDetail = provinceDetail;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityDetail() {
        return cityDetail;
    }

    public void setCityDetail(String cityDetail) {
        this.cityDetail = cityDetail;
    }

    public String getCountyCode() {
        return countyCode;
    }

    public void setCountyCode(String countyCode) {
        this.countyCode = countyCode;
    }

    public String getCountyDetail() {
        return countyDetail;
    }

    public void setCountyDetail(String countyDetail) {
        this.countyDetail = countyDetail;
    }

    public String getTownCode() {
        return townCode;
    }

    public void setTownCode(String townCode) {
        this.townCode = townCode;
    }

    public String getTownDetail() {
        return townDetail;
    }

    public void setTownDetail(String townDetail) {
        this.townDetail = townDetail;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
