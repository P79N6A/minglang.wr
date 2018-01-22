package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.alibaba.common.lang.StringUtil;

import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.util.CollectionUtil;

public final class StationExtExampleConverter {
	private StationExtExampleConverter() {

	}

	public static StationExtExample convert(StationCondition stationCondition) {
		StationExtExample stationExtExample = new StationExtExample();

		if (null != stationCondition.getType()) {
			stationExtExample.setType(stationCondition.getType().getCode());
		}

		if (CollectionUtil.isNotEmpty(stationCondition.getStationStatuses())) {
			stationExtExample.setStatuses(extractStationStatuses(stationCondition.getStationStatuses()));
		}
		
		if (CollectionUtil.isNotEmpty(stationCondition.getTypes())) {
			stationExtExample.setTypes(extractTypes(stationCondition.getTypes()));
		}

		if (null != stationCondition.getStationStatusEnum()) {
			stationExtExample.setStatus(stationCondition.getStationStatusEnum().getCode());
		}

		if (StringUtil.isNotBlank(stationCondition.getName())) {
			stationExtExample.setName(stationCondition.getName());
		}

		if (null != stationCondition.getOrgId()) {
			stationExtExample.setOrgId(stationCondition.getOrgId());
		}

		return stationExtExample;
	}
	
	private static List<String> extractStationStatuses(List<StationStatusEnum> statusEnums) {
		if (CollectionUtil.isEmpty(statusEnums)) {
			return Collections.<String> emptyList();
		}
		List<String> statuses = new ArrayList<String>(statusEnums.size());
		for (StationStatusEnum statusEnum : statusEnums) {
			if (null == statusEnum) {
				continue;
			}
			statuses.add(statusEnum.getCode());
		}
		return statuses;
	}
	
	private static List<String> extractTypes(List<PartnerInstanceTypeEnum> typeEnums) {
		if (CollectionUtil.isEmpty(typeEnums)) {
			return Collections.<String> emptyList();
		}
		List<String> statuses = new ArrayList<String>(typeEnums.size());
		for (PartnerInstanceTypeEnum statusEnum : typeEnums) {
			if (null == statusEnum) {
				continue;
			}
			statuses.add(statusEnum.getCode());
		}
		return statuses;
	}
}
