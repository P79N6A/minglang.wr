package com.taobao.cun.auge.lifecycle.controller;


import com.taobao.cun.auge.lifecycle.RuntimeMetaInfoCollector;
import com.taobao.cun.auge.station.dto.PhaseInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/runtime")
public class RuntimeMetaInfoController {

	@RequestMapping("/meta")
    public @ResponseBody Map<String,List<PhaseInfo>> meta() {
		//元数据收集
		Map<String,List<PhaseInfo>> runtimeInfos = RuntimeMetaInfoCollector.runtimeInfo();
		return runtimeInfos;
    }
}
