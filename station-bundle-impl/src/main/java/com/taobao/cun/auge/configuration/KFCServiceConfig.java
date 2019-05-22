package com.taobao.cun.auge.configuration;

import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;
import com.taobao.kfc.client.lite.service.LiteMergeSearchService;

/**
 * @author alibaba lengs
 *
 */
@Component
public class KFCServiceConfig {
    private final static Logger logger = LoggerFactory.getLogger(KFCServiceConfig.class);
    
    @Resource
    private LiteMergeSearchService liteMergeSearchService;
    
    private static String firstApply = "xinxianquanpingbi";
    private static String secondApply = "pingbiciku";

    public Map<String, String> kfcCheck(String str) {
       return Maps.newHashMap();
    }

    public boolean isProhibitedWord(String key) {
        Map<String, String> tempMap = kfcCheck(key);
        if (tempMap == null || tempMap.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean isProhibitedWord_byMulti(String... params) {
        if (params == null || params.length <= 0){
            return false;
        }
        for (String param : params) {
            Map<String, String> tempMap = kfcCheck(param);
            if (tempMap != null && !tempMap.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
