package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerChildMaxNumChangeEvent;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.crius.event.client.EventDispatcher;

public final class PartnerChildMaxNumChangeEventConverter {

	private PartnerChildMaxNumChangeEventConverter() {

	}

	public static void dispatchChangeEvent(PartnerInstanceExtDto instanceExtDto,
			PartnerMaxChildNumChangeReasonEnum reason) {
		PartnerChildMaxNumChangeEvent event = new PartnerChildMaxNumChangeEvent();
		event.setPartnerInstanceId(instanceExtDto.getInstanceId());
		event.setChildMaxNum(instanceExtDto.getMaxChildNum());
		event.setBizMonth(instanceExtDto.getChildNumChangDate());
		event.copyOperatorDto(instanceExtDto);
		event.setReason(buildChangeReason(instanceExtDto, reason));
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_CHILD_MAX_NUM_CHANGE_EVENT, event);
	}

	private static String buildChangeReason(PartnerInstanceExtDto instanceExtDto,
			PartnerMaxChildNumChangeReasonEnum reason) {
		Integer childMaxNum = instanceExtDto.getMaxChildNum();
		String bizMonth = instanceExtDto.getChildNumChangDate();

		if (PartnerMaxChildNumChangeReasonEnum.EDIT.equals(reason)) {
			return reason.getDesc() + ",修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0);
		} else if (PartnerMaxChildNumChangeReasonEnum.TPA_PERFORMANCE_REWARD.equals(reason)) {
			return bizMonth + reason.getDesc() + ",修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0);
		} else if (PartnerMaxChildNumChangeReasonEnum.TPA_UPGRADE_REWARD.equals(reason)) {
			return reason.getDesc() + ",修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0);
		} else if (PartnerMaxChildNumChangeReasonEnum.INIT.equals(reason)) {
			return reason.getDesc() + ",修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0);
		} else if (PartnerMaxChildNumChangeReasonEnum.TP_DEGREE_2_TPA.equals(reason)) {
			return reason.getDesc() + ",修改子成员最大配额为" + (null != childMaxNum ? childMaxNum : 0);
		}
		return "";
	}

}