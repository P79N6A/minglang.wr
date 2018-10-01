package com.taobao.cun.auge.station.transfer.state;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.CountyStationBO;
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
	private CountyStationBO countyStationBO;
	@Resource
	private StationBO stationBO;
	
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

}
