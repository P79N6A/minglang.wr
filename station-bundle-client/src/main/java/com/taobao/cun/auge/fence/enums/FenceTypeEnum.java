package com.taobao.cun.auge.fence.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 18/6/15.
 */
public class FenceTypeEnum implements Serializable{


    private static final long serialVersionUID = 5525199073000726953L;

    public static final FenceTypeEnum SERVICE = new FenceTypeEnum("SERVICE", "服务围栏");
    public static final FenceTypeEnum LOGISTICS = new FenceTypeEnum("LOGISTICS", "物流围栏");
    public static final FenceTypeEnum SELL = new FenceTypeEnum("SELL", "售卖围栏");
    public static final FenceTypeEnum CHARGE = new FenceTypeEnum("CHARGE", "收费围栏");
    private static final Map<String, FenceTypeEnum> mappings = new HashMap<String, FenceTypeEnum>();

    static {
        mappings.put("SERVICE", SERVICE);
        mappings.put("LOGISTICS", LOGISTICS);
        mappings.put("SELL", SELL);
        mappings.put("CHARGE", CHARGE);
    }

    private String code;

    private String desc;

    public static FenceTypeEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public FenceTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static List<FenceTypeEnum> enums() {
        List<FenceTypeEnum> enumList = new ArrayList<FenceTypeEnum>();
        enumList.add(SERVICE);
        enumList.add(LOGISTICS);
        enumList.add(SELL);
        enumList.add(CHARGE);
        return enumList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

}
