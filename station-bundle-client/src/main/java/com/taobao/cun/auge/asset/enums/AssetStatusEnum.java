package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by xiao on 17/5/17.
 */
public class AssetStatusEnum implements Serializable{

    private static final long serialVersionUID = -4134780817945719435L;

    /**
     * 注意不要取U序号之后的枚举类，有问题联系87872
     */
    public static final AssetStatusEnum SIGN = new AssetStatusEnum("SIGN", "待签收");

    public static final AssetStatusEnum USE = new AssetStatusEnum("USE", "使用中");

    public static final AssetStatusEnum TRANSFER = new AssetStatusEnum("TRANSFER", "转移中");

    public static final AssetStatusEnum PEND = new AssetStatusEnum("PEND", "待转移");

    public static final AssetStatusEnum DISTRIBUTE = new AssetStatusEnum("DISTRIBUTE", "分发中");

    public static final AssetStatusEnum SCRAP = new AssetStatusEnum("SCRAP", "已报废");

    private static final Map<String, AssetStatusEnum> mappings = new HashMap<String, AssetStatusEnum>();

    static {
        mappings.put("SIGN", SIGN);
        mappings.put("USE", USE);
        mappings.put("TRANSFER", TRANSFER);
        mappings.put("DISTRIBUTE", DISTRIBUTE);
        mappings.put("SCRAP", SCRAP);
        mappings.put("PEND", PEND);
    }

    private String code;

    private String desc;

    public static AssetStatusEnum valueOf(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }
        return mappings.get(code);
    }

    public AssetStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
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

    public static List<String> getValidStatusList() {
        List<String> list = new ArrayList<String>();
        list.add(USE.getCode());
        list.add(TRANSFER.getCode());
        list.add(DISTRIBUTE.getCode());
        list.add(PEND.getCode());
        return list;
    }

    public static List<String> getCanCountySignStatusList() {
        List<String> list = new ArrayList<String>();
        list.add(SIGN.getCode());
        list.add(TRANSFER.getCode());
        return list;
    }


}
