package com.taobao.cun.auge.station.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.util.CollectionUtil;

public final class PartnerInstanceStateEnumUtil {

	private PartnerInstanceStateEnumUtil() {

	}

	public static List<String> extractCode(List<PartnerInstanceStateEnum> states) {
		if (CollectionUtil.isEmpty(states)) {
			return Collections.<String> emptyList();
		}
		List<String> codes = new ArrayList<String>(states.size());
		for (PartnerInstanceStateEnum state : states) {
			codes.add(state.getCode());
		}
		return codes;
	}

}
