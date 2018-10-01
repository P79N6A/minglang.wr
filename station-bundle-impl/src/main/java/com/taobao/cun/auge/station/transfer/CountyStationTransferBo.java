package com.taobao.cun.auge.station.transfer;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.dal.mapper.ext.StationTransferExtMapper;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.auge.station.transfer.dto.TransferStation;

@Component
public class CountyStationTransferBo {
	@Resource
	private StationTransferExtMapper stationTransferExtMapper;
	@Resource
	private CountyStationBO countyStationBO;
	@Resource
	private CountyStationMapper countyStationMapper;
	/**
	 * 获取县点下所有村点
	 * @param countyStationId
	 * @return
	 */
	public List<TransferStation> getTransferStations(Long countyStationId){
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
		return stationTransferExtMapper.getTransferStations(countyStation.getOrgId());
	}
	
	public void updateTransferState(Long id, String transferState) {
		CountyStation countyStation = countyStationMapper.selectByPrimaryKey(id);
		countyStation.setTransferState(transferState);
		countyStation.setGmtModified(new Date());
		countyStationMapper.updateByPrimaryKey(countyStation);
	}
	
	public void endTransfer(Long countyStationId) {
		updateTransferState(countyStationId, TransferState.FINISHED.name());
	}
	
	public void cancelTransfer(Long countyStationId) {
		updateTransferState(countyStationId, "WAITING");
	}
}
