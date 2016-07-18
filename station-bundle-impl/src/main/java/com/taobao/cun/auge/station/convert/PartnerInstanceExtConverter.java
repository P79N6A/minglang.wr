package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;

public final class PartnerInstanceExtConverter {

	private PartnerInstanceExtConverter() {

	}

	public static PartnerInstanceExt convert(PartnerInstanceExtDto instanceExtDto) {

		if (null == instanceExtDto) {
			return null;
		}
		PartnerInstanceExt instanceExt = new PartnerInstanceExt();

		instanceExt.setPartnerInstanceId(instanceExtDto.getInstanceId());
		instanceExt.setMaxChildNum(instanceExtDto.getMaxChildNum());

		return instanceExt;
	}

}
