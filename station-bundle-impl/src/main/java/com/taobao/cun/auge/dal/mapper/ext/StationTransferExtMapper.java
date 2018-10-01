package com.taobao.cun.auge.dal.mapper.ext;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taobao.cun.auge.station.transfer.dto.TransferStation;

public interface StationTransferExtMapper {
	List<TransferStation> getTransferStations(@Param("orgId")Long orgId);
	
	void updateSubStationTransferState(@Param("stationIds") List<Long> stationIds, @Param("tansferState")String tansferState);
}
