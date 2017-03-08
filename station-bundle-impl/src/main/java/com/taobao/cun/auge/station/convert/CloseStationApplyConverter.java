package com.taobao.cun.auge.station.convert;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.dal.domain.CloseStationApply;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.enums.CloseStationApplyCloseReasonEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

/**
 * 停业申请转换
 * @author quanzhu.wangqz
 *
 */
public class CloseStationApplyConverter {
	
	public static CloseStationApplyDto toCloseStationApplyDto(CloseStationApply closeStationApply) {
		if (closeStationApply == null) {
			return null;
		}

		CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();

		closeStationApplyDto.setId(closeStationApply.getId());
		closeStationApplyDto.setCloseReason(CloseStationApplyCloseReasonEnum.valueof(closeStationApply.getCloseReason()));
		closeStationApplyDto.setOtherReason(closeStationApply.getOtherReason());
		closeStationApplyDto.setPartnerInstanceId(closeStationApply.getPartnerInstanceId());
		closeStationApplyDto.setType(PartnerInstanceCloseTypeEnum.valueof(closeStationApply.getType()));
		//对历史数据的保护，没有历史状态，作为服务中处理
		if (StringUtil.isBlank(closeStationApply.getInstanceState())) {
			closeStationApplyDto.setInstanceState(PartnerInstanceStateEnum.SERVICING);
		} else {
			closeStationApplyDto.setInstanceState(PartnerInstanceStateEnum.valueof(closeStationApply.getInstanceState()));
		}
		return closeStationApplyDto;
	}

	public static CloseStationApply toCloseStationApply(CloseStationApplyDto closeStationApplyDto) {
		if (closeStationApplyDto == null) {
			return null;
		}

		CloseStationApply closeStationApply = new CloseStationApply();

		closeStationApply.setCloseReason(closeStationApplyDto.getCloseReason() == null ? null:closeStationApplyDto.getCloseReason().getCode());
		closeStationApply.setOtherReason(closeStationApplyDto.getOtherReason());
		closeStationApply.setPartnerInstanceId(closeStationApplyDto.getPartnerInstanceId());
		closeStationApply.setType(closeStationApplyDto.getType() == null ? null:closeStationApplyDto.getType().getCode());
		if (null != closeStationApplyDto.getInstanceState()) {
			closeStationApply.setInstanceState(closeStationApplyDto.getInstanceState().getCode());
		}

		return closeStationApply;
	}
	
	public static CloseStationApplyDto toCloseStationApplyDto(ForcedCloseDto forcedCloseDto,PartnerInstanceCloseTypeEnum closeType,PartnerInstanceStateChangeEnum instanceStateChange) {
		CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
		closeStationApplyDto.setCloseReason(forcedCloseDto.getReason());
		closeStationApplyDto.setOtherReason(forcedCloseDto.getRemarks());
		closeStationApplyDto.setPartnerInstanceId(forcedCloseDto.getInstanceId());
		closeStationApplyDto.setType(closeType);
		closeStationApplyDto.setOperator(forcedCloseDto.getOperator());
		closeStationApplyDto.setOperatorOrgId(forcedCloseDto.getOperatorOrgId());
		closeStationApplyDto.setOperatorType(forcedCloseDto.getOperatorType());
		closeStationApplyDto.setInstanceState(instanceStateChange.getPrePartnerInstanceState());
		return closeStationApplyDto;
	}
}
