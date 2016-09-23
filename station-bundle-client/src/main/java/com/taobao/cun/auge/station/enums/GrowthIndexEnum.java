package com.taobao.cun.auge.station.enums;

public enum GrowthIndexEnum {

    AVERAGE_MONTHLY_EARNINGS("ame", "月平均收入"),
    PRODUCT_ONE_GMV("pog", "1.0商品GMV"),
    APP_REVENUE_RATIO("arr", "APP收入占比"),
    BUYER_COUNT("bc", "购买村民数"),
    BUYCOUNT_PER_BUYER("bpb", "人均购买次数"),
    INCREMENT_APP_BINDED("iab", "新增APP绑定"),
    ;
    private String key;
    private String desc;
    private GrowthIndexEnum(String key, String desc){
        this.key = key;
        this.desc = desc;
    }
    
    public String getKey() {
        return key;
    }
    
    public String getDesc() {
        return desc;
    }
    
}
