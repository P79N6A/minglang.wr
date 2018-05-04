package com.taobao.cun.auge.station.service;

import java.util.List;
import java.util.Map;

public interface RuntimeMetaInfoService {

    public Map<String, List<Map<String, String>>> getPartnerRuntimeInfo();

    public Map<String, List<Map<String, String>>> getStateChangeInfo(String type);

}
