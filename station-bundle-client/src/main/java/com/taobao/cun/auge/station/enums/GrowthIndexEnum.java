package com.taobao.cun.auge.station.enums;

public enum GrowthIndexEnum {

    AVERAGE_MONTHLY_EARNINGS("ame", "月平均收入"),
    PRODUCT_ONE_GMV_RATIO("pogr", "1.0商品收入占比"),
    REPURCHASE_RATIO("rr", "复购率"),
    ACTIVE_APP_BUYER_COUNT("aabc", "app活跃村民数"),
    LOYALTY_BUYER("lb", "忠实村民数"),
    VISIT_PRODUCT_COUNT("vpc", "访问商品数"),
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
