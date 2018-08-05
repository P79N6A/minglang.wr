package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.station.bo.dto.FenceStationQueryCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;

public abstract class AbstractConditionFenceInstanceJobExecutor<F extends FenceInstanceJob> extends AbstractFenceInstanceJobExecutor<F> {
	protected List<Station> getFenceStations(String condition) {
		FenceStationQueryCondition fenceStationQueryCondition = JSON.parseObject(condition, FenceStationQueryCondition.class);
		
		if(CollectionUtils.isEmpty(fenceStationQueryCondition.getStationTypes())) {
			fenceStationQueryCondition.setStationTypes(Lists.newArrayList("TP", "TPS"));
		}
		
		if(CollectionUtils.isEmpty(fenceStationQueryCondition.getStationStatus())) {
			fenceStationQueryCondition.setStationStatus(Lists.newArrayList(
					PartnerInstanceStateEnum.SERVICING.getCode(), 
					PartnerInstanceStateEnum.DECORATING.getCode(),
					PartnerInstanceStateEnum.CLOSING.getCode()));
		}
		
		if(CollectionUtils.isEmpty(fenceStationQueryCondition.getStationLocations())) {
			fenceStationQueryCondition.setStationLocations(Lists.newArrayList("town", "village"));
		}
		
		return stationBo.getFenceStations(fenceStationQueryCondition);
	}
}
