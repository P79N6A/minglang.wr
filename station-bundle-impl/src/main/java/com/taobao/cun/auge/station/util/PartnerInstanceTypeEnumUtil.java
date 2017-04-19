package com.taobao.cun.auge.station.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.util.CollectionUtil;

public final class PartnerInstanceTypeEnumUtil {
	private PartnerInstanceTypeEnumUtil() {

	}

	public static List<String> extractCode(List<PartnerInstanceTypeEnum> instanceTypes) {
		if (CollectionUtil.isEmpty(instanceTypes)) {
			return Collections.<String> emptyList();
		}
		List<String> codes = new ArrayList<String>(instanceTypes.size());
		for (PartnerInstanceTypeEnum type : instanceTypes) {
			codes.add(type.getCode());
		}

		return codes;
	}
}
