package com.taobao.cun.auge.opensearch;

import com.alibaba.fastjson.annotation.JSONField;

public class AdministrativeVillageQueryResult {
    @JSONField(name="province_id")
    private String provinceId;
    @JSONField(name="province_name")
    private String provinceName;
    @JSONField(name="city_id")
    private String cityId;
    @JSONField(name="city_name")
    private String cityName;
    @JSONField(name="district_id")
    private String districtId;
    @JSONField(name="district_name")
    private String districtName;
    @JSONField(name="town_id")
    private String townId;
    @JSONField(name="town_name")
    private String townName;
    @JSONField(name = "village_id")
    private String villageId;
    @JSONField(name = "village_name")
    private String villageName;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictId() {
        return districtId;
    }

    public void setDistrictId(String districtId) {
        this.districtId = districtId;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getTownId() {
        return townId;
    }

    public void setTownId(String townId) {
        this.townId = townId;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getVillageId() {
        return villageId;
    }

    public void setVillageId(String villageId) {
        this.villageId = villageId;
    }

    public String getVillageName() {
        return villageName;
    }

    public void setVillageName(String villageName) {
        this.villageName = villageName;
    }
}
