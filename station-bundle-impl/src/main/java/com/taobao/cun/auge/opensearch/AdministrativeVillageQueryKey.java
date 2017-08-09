package com.taobao.cun.auge.opensearch;

public enum AdministrativeVillageQueryKey {
    PROVINCE("province_id", "province_name"),

    CITY("city_id", "city_name"),

    COUNTY("county_id", "county_name"),

    TOWN("town_id", "town_name"),

    VILLAGE("id", "village_name");

    private static final String CONNECTOR = ":";
    private String code;
    private String name;

    AdministrativeVillageQueryKey(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String toString() {
        return code + CONNECTOR + name;
    }

    public String toCodeQueryString(String value) {
        return code + CONNECTOR + "'" + value + "'";
    }

    public String toNameQueryString(String value) {
        return name + CONNECTOR + "'" + value + "'";
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
