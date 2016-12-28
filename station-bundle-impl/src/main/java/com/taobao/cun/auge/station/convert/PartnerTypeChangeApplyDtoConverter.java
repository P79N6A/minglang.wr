package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApply;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum.ChangeEnum;
import com.taobao.cun.auge.station.dto.PartnerTypeChangeApplyDto;

public final class PartnerTypeChangeApplyDtoConverter {

	private PartnerTypeChangeApplyDtoConverter() {

	}

	public static PartnerTypeChangeApply convert(PartnerTypeChangeApplyDto applyDto) {
		if (null == applyDto) {
			return null;
		}

		PartnerTypeChangeApply apply = new PartnerTypeChangeApply();

		apply.setPartnerInstanceId(applyDto.getPartnerInstanceId());
		apply.setNextPartnerInstanceId(applyDto.getNextPartnerInstanceId());
		apply.setFeature(FeatureUtil.toString(applyDto.getFeature()));

		if (null != applyDto.getTypeChangeEnum()) {
			apply.setType(applyDto.getTypeChangeEnum().getType().name());
		}
		return apply;
	}
	
	
	public static PartnerTypeChangeApplyDto convert(PartnerTypeChangeApply apply){
		if (null == apply) {
			return null;
		}

		PartnerTypeChangeApplyDto applyDto = new PartnerTypeChangeApplyDto();

		applyDto.setPartnerInstanceId(apply.getPartnerInstanceId());
		applyDto.setNextPartnerInstanceId(apply.getNextPartnerInstanceId());
		applyDto.setFeature(FeatureUtil.toMap(apply.getFeature()));
		applyDto.setTypeChangeEnum(PartnerInstanceTypeChangeEnum.valueof(ChangeEnum.valueOf(apply.getType())));
		
		return applyDto;
	}
}
