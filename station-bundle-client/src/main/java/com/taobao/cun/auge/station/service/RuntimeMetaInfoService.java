package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.PhaseInfo;

import java.util.List;
import java.util.Map;

public interface RuntimeMetaInfoService {

    public  Map<String,List<PhaseInfo>> getPartnerRuntimeInfo();

    public Map<String, List<Map<String, String>>> getStateChangeInfo(String type);

}
