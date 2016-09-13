package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.event.PartnerInstanceLevelChangeEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEvaluateTypeEnum;

/**
 * Created by jingxiao.gjx on 2016/8/2.
 */
public class PartnerInstanceLevelEventConverter {
	private PartnerInstanceLevelEventConverter() {
	}

	public static PartnerInstanceLevelChangeEvent convertLevelChangeEvent(
			PartnerInstanceLevelEvaluateTypeEnum evaluateType,
			PartnerInstanceLevelDto partnerInstanceLevelDto) {
		PartnerInstanceLevelChangeEvent result = new PartnerInstanceLevelChangeEvent();

		result.setPartnerInstanceId(partnerInstanceLevelDto.getPartnerInstanceId());
		result.setTaobaoUserId(partnerInstanceLevelDto.getTaobaoUserId());
		result.setStationId(partnerInstanceLevelDto.getStationId());

		result.setPreLevel(partnerInstanceLevelDto.getPreLevel());
		result.setCurrentLevel(partnerInstanceLevelDto.getCurrentLevel());
		result.setExpectedLevel(partnerInstanceLevelDto.getExpectedLevel());

		result.setEvaluateBy(partnerInstanceLevelDto.getEvaluateBy());
		result.setEvaluateDate(partnerInstanceLevelDto.getEvaluateDate());
		result.setEvaluateType(evaluateType);

		result.setOperator(partnerInstanceLevelDto.getOperator());
		result.setOperatorOrgId(partnerInstanceLevelDto.getOperatorOrgId());
		result.setOperatorType(partnerInstanceLevelDto.getOperatorType());

		return result;
	}
}
