package com.taobao.cun.auge.station.convert;

import com.taobao.cun.auge.common.OperatorDto;

public final class OperatorConverter {

	private OperatorConverter() {

	}

	public static com.taobao.cun.common.operator.OperatorDto convert(OperatorDto operatorDto) {
		com.taobao.cun.common.operator.OperatorDto commonOperator = new com.taobao.cun.common.operator.OperatorDto();

		commonOperator.setOperator(operatorDto.getOperator());
		commonOperator.setOperatorOrgId(operatorDto.getOperatorOrgId());
		if (null != operatorDto.getOperatorType()) {
			commonOperator.setOperatorType(
					com.taobao.cun.common.operator.OperatorTypeEnum.valueof(operatorDto.getOperatorType().getCode()));
		}
		return commonOperator;
	}
}