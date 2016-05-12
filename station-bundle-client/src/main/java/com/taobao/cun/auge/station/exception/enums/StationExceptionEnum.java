package com.taobao.cun.auge.station.exception.enums;



/**
 * Created by haihu.fhh on 2016/5/4.
 */
public class StationExceptionEnum extends CommonExceptionEnum {



    private static final long serialVersionUID = -2325045809951918493L;

    //保证金
    public static final StationExceptionEnum STATION_NUM_IS_DUPLICATE = new StationExceptionEnum("STATION_NUM_IS_DUPLICATE", "服务站编号重复");


    static {
        mappings.put("STATION_NUM_IS_DUPLICATE", STATION_NUM_IS_DUPLICATE);
    }

    public StationExceptionEnum(String code, String desc) {
        super(code,desc);
    }

    public StationExceptionEnum() {
    }
}
