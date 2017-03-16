package com.taobao.cun.auge.configuration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by xiao on 16/11/30.
 */
@Component
@RefreshScope
public class DiamondConfiguredProperties {

    @Value("${wisdom.audit.apply}")
    private String apply;

    @Value("${wisdom.audit.pass}")
    private String pass;

    @Value("${wisdom.audit.fail}")
    private String fail;

    @Value("${wisdom.audit.mobile}")
    private String mobile;

    @Value("${train.supplier}")
    private String supplier;

    public String getApply() {
        return apply;
    }

    public String getPass() {
        return pass;
    }

    public String getFail() {
        return fail;
    }

    public String getMobile() {
        return mobile;
    }

    public Map<String, String> getSupplierMap() {
        return JSON.parseObject(supplier, new TypeReference<LinkedHashMap<String, String>>(){});
    }
}
