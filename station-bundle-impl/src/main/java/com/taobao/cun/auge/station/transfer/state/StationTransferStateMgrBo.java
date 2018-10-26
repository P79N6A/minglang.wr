package com.taobao.cun.auge.station.transfer.state;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.dal.mapper.ext.StationTransferExtMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.transfer.dto.StationTransferPhase;
import com.taobao.cun.auge.station.transfer.dto.TransferState;

/**
 * 服务站状态管理
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationTransferStateMgrBo implements TransferStateMgrBo<StationTransferPhase> {
	@Resource
	private StationBO stationBO;
	@Resource
	private StationTransferExtMapper stationTransferExtMapper;
	@Resource
	private StationMapper stationMapper;
	
	@Override
	public StationTransferPhase getTransferPhase(Long id) {
		Station station = stationBO.getStationById(id);
		
		if(station.getTransferState().equals(TransferState.WAITING.name())) {
			return StationTransferPhase.STATION_NOT_TRANS;
		}
		
		if(station.getTransferState().equals(TransferState.TRANSFERING.name())) {
			return StationTransferPhase.STATION_TRANSING;
		}
		
		return StationTransferPhase.STATION_TRANSED;
	}

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
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void endTransfer(List<Long> stationIds) {
		batchUpdateTransferState(stationIds, TransferState.FINISHED.name());
		updateSubStationTransferState(stationIds, TransferState.FINISHED.name());
	}
	
	public void cancelTransfer(List<Long> stationIds) {
		batchUpdateTransferState(stationIds, TransferState.WAITING.name());
	}
	
	public void autoTransfer(Long orgId) {
		stationTransferExtMapper.autoTransferOrgId(orgId);
	}
}
