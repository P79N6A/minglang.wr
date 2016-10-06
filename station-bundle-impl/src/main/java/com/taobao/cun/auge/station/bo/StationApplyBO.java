package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface StationApplyBO {

	public StationApply findStationApplyById(Long stationApplyId)throws AugeServiceException;

	public void reService(Long stationApplyId, String operator) throws AugeServiceException;
}
