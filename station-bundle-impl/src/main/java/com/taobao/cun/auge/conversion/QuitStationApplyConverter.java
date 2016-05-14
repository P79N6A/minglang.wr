package com.taobao.cun.auge.conversion;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.condition.QuitStationApplyCondition;

@Mapper( componentModel = "spring")
@Component
public interface QuitStationApplyConverter {

	QuitStationApplyCondition toCondition(QuitStationApply quitStationApply);
	
	QuitStationApply toQuitStationApply(QuitStationApplyCondition condition);
}
