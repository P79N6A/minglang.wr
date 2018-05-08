package com.taobao.cun.auge.station.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector;
import com.taobao.cun.auge.station.dto.PhaseInfo;
import com.taobao.cun.auge.station.service.RuntimeMetaInfoService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.scxml2.io.SCXMLReader;
import org.apache.commons.scxml2.model.*;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.List;
import java.util.Map;


@Service("runtimeMetaInfoService")
@HSFProvider(serviceInterface = RuntimeMetaInfoService.class, clientTimeout = 3000)
public class RuntimeMetaInfoServiceImpl implements RuntimeMetaInfoService {
    @Override
    public Map<String, List<PhaseInfo>> getPartnerRuntimeInfo() {
        Map<String, List<PhaseInfo>> runtimeInfos = RuntimeMetaInfoCollector.runtimeInfo();
        return runtimeInfos;
    }

    @Override
    public Map<String, List<Map<String, String>>> getStateChangeInfo(String type) {
        Map<String, List<Map<String, String>>> result = Maps.newHashMap();
        try {
            SCXMLReader.Configuration config = new SCXMLReader.Configuration(null, null, null);
            URL url = Thread.currentThread().getContextClassLoader().getResource("statemachine/statemachine-" + type + ".xml");
            SCXML scxml = SCXMLReader.read(url, config);
            scxml.getChildren().forEach(item -> {
                if (item instanceof State) {
                    State state = (State) item;
                    String id = state.getId();
                    List<Map<String, String>> list = Lists.newArrayList();
                    List<Transition> transitionsList = state.getTransitionsList();
                    transitionsList.forEach(transition -> {
                        Map<String, String> map = Maps.newHashMap();
                        String event = transition.getEvent();
                        TransitionTarget target = transition.getTargets().iterator().next();
                        String targetId = target.getId();
                        if (null != event) {
                            map.put(event, targetId);
                            list.add(map);
                        }
                    });
                    result.put(id, list);
                }
            });
        } catch (Exception e) {
            return result;
        }
        return result;
    }

}
