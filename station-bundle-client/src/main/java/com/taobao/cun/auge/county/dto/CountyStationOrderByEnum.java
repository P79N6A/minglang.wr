package com.taobao.cun.auge.county.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


public class CountyStationOrderByEnum implements Serializable {


	private static final long serialVersionUID = 1L;
	public static final CountyStationOrderByEnum CREATE_TIME = new CountyStationOrderByEnum("CREATE_TIME", "gmt_create", "desc");
    public static final CountyStationOrderByEnum MODIFIED_TIME = new CountyStationOrderByEnum("MODIFIED_TIME", "gmt_modified", "desc");
    public static final CountyStationOrderByEnum PARENT_ID = new CountyStationOrderByEnum("PARENT_ID", "parent_id", "desc");

    private static final Map<String, CountyStationOrderByEnum> mappings = new HashMap<String, CountyStationOrderByEnum>();

    static {
        mappings.put("CREATE_TIME", CREATE_TIME);
        mappings.put("MODIFIED_TIME", CREATE_TIME);
        mappings.put("PARENT_ID", PARENT_ID);
    }

    private String code;
    private String orderByFieldName;
    private String direct;

    @SuppressWarnings("unused")
    private CountyStationOrderByEnum() {

    }

    public CountyStationOrderByEnum(String code, String orderByFieldName, String direct) {
        this.code = code;
        this.orderByFieldName = orderByFieldName;
        this.direct = direct;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOrderByFieldName() {
        return orderByFieldName;
    }

    public void setOrderByFieldName(String orderByFieldName) {
        this.orderByFieldName = orderByFieldName;
    }

    public String getDirect() {
        return direct;
    }

    public CountyStationOrderByEnum setDirect(String direct) {
        this.direct = direct;
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof CountyStationOrderByEnum)) {
            return false;
        }

        CountyStationOrderByEnum objType = (CountyStationOrderByEnum) obj;
        return objType.getCode().equals(this.getCode());
    }


    public String toOrderBySQL() {
        return this.getOrderByFieldName() + " " + this.getDirect();
    }

    public static CountyStationOrderByEnum valueof(String code) {
        if (code == null) {
            return null;
        }
        return mappings.get(code);
    }
}
