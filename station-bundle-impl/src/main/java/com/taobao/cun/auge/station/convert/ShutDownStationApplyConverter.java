package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.dal.domain.ShutDownStationApply;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;

public final class ShutDownStationApplyConverter {

	private ShutDownStationApplyConverter() {

	}

	public static ShutDownStationApply convert(ShutDownStationApplyDto dto) {
		if (null == dto) {
			return null;
		}

		ShutDownStationApply record = new ShutDownStationApply();

		record.setStationId(dto.getStationId());
		record.setReason(dto.getReason());
		record.setApplierId(dto.getApplierId());
		record.setApplyTime(dto.getApplyTime());

		return record;
	}
	
	public static ShutDownStationApplyDto convert(ShutDownStationApply record) {
		if (null == record) {
			return null;
		}

		ShutDownStationApplyDto dto = new ShutDownStationApplyDto();

		dto.setId(record.getId());
		dto.setStationId(record.getStationId());
		dto.setReason(record.getReason());
		dto.setApplierId(record.getApplierId());
		dto.setApplyTime(record.getApplyTime());

		return dto;
	}

}
