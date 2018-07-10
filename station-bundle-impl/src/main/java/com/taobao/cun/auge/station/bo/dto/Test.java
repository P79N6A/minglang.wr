package com.taobao.cun.auge.station.bo.dto;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;

public class Test {

	public static void main(String[] args) {
		FenceStationQueryCondition fenceStationQueryCondition = new FenceStationQueryCondition();
		fenceStationQueryCondition.setFullIdPath("/1/500001/169/700054");
		fenceStationQueryCondition.setStationLevels(Lists.newArrayList("S5", "S6"));
		fenceStationQueryCondition.setStationTypes(Lists.newArrayList("TP"));
		fenceStationQueryCondition.setStationStatus(Lists.newArrayList("SERVICING"));
		System.out.println(JSON.toJSONString(fenceStationQueryCondition));
	}

}
