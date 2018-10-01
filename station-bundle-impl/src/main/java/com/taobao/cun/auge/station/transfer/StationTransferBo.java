package com.taobao.cun.auge.station.transfer;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.dal.mapper.ext.StationTransferExtMapper;
import com.taobao.cun.auge.station.transfer.dto.TransferState;

@Component
public class StationTransferBo {
	@Resource
	private StationMapper stationMapper;
	@Resource
	private StationTransferExtMapper stationTransferExtMapper;
	
	public void batchUpdateTransferState(List<Long> ids, String transferState) {
		StationExample example = new StationExample();
		example.createCriteria().andIdIn(ids);
		Station station = new Station();
		station.setTransferState(transferState);
		stationMapper.updateByExampleSelective(station, example);
	}
	
	public void updateSubStationTransferState(List<Long> ids, String transferState) {
		stationTransferExtMapper.updateSubStationTransferState(ids, transferState);
	}
	
	@Transactional
	public void endTransfer(List<Long> stationIds) {
		batchUpdateTransferState(stationIds, TransferState.FINISHED.name());
		updateSubStationTransferState(stationIds, TransferState.FINISHED.name());
	}
	
	public void cancelTransfer(List<Long> stationIds) {
		batchUpdateTransferState(stationIds, TransferState.WAITING.name());
	}
}
