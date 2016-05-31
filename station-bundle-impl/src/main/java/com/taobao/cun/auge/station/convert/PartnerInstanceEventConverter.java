package com.taobao.cun.auge.station.convert;

import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;

/**
 * 合伙人实例状态转化事件
 * 
 * @author quanzhu.wangqz
 *
 */
public final class PartnerInstanceEventConverter {

	private PartnerInstanceEventConverter() {
	}

	public static PartnerInstanceStateChangeEvent convert(PartnerInstanceStateChangeEnum stateChangeEnum,
			PartnerInstanceDto partnerInstanceDto, OperatorDto operator) {

		PartnerInstanceStateChangeEvent event = new PartnerInstanceStateChangeEvent();

		event.setPartnerInstanceId(partnerInstanceDto.getId());
		event.setPartnerType(partnerInstanceDto.getType());

		StationDto stationDto = partnerInstanceDto.getStationDto();
		if (null != stationDto) {
			event.setStationId(stationDto.getId());
			event.setStationName(stationDto.getName());
			event.setOwnOrgId(stationDto.getApplyOrg());
		}

		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (null != partnerDto) {
			event.setTaobaoUserId(partnerDto.getTaobaoUserId());
			event.setTaobaoNick(partnerDto.getTaobaoNick());
		}

		event.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.formatTime(new Date()));
		event.setStateChangeEnum(stateChangeEnum);

		if (null != operator) {
			event.setOperator(operator.getOperator());
			event.setOperatorOrgId(operator.getOperatorOrgId());
			event.setOperatorType(operator.getOperatorType());
		}

		return event;
	}
}
