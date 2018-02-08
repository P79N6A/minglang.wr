package com.taobao.cun.auge.lifecycle.controller;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector;
import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector.PhaseInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runtime")
public class RuntimeMetaInfoController {

	@RequestMapping("/meta")
    public @ResponseBody Map<String,List<PhaseInfo>> meta() {
		Map<String,List<PhaseInfo>> runtimeInfos = RuntimeMetaInfoCollector.runtimeInfo();
		return runtimeInfos;
    }
}
