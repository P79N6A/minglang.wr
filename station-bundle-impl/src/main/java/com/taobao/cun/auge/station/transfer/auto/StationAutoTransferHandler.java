package com.taobao.cun.auge.station.transfer.auto;

import java.util.List;

import javax.annotation.Priority;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.transfer.StationTransferBo;
import com.taobao.cun.auge.station.transfer.dto.TransferStation;
import com.taobao.cun.auge.station.transfer.state.StationTransferStateMgrBo;

/**
 * 处理村点,子站点转交：淘帮手、优盟
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
@Priority(200)
public class StationAutoTransferHandler implements AutoTransferHandler {
	@Resource
	private StationTransferStateMgrBo stationTransferStateMgrBo;
	@Resource
	private StationTransferBo stationTransferBo;
	@Resource
	private BizActionLogBo bizActionLogBo;
	@Resource
	private StationBO stationBO;
	
	@Override
	public void transfer(CountyStation countyStation) {
		List<TransferStation> transferStations = stationTransferBo.getTransferableStations(countyStation.getOrgId());
		for(TransferStation transferStation : transferStations) {
			bizActionLogBo.addLog(transferStation.getStationId(), "station", "auto", 0L, OrgDeptType.extdept.name(), BizActionEnum.station_transfer_finished);
		}
		stationTransferStateMgrBo.autoTransfer(countyStation.getOrgId());
		stationBO.updateStationDeptByOrgId(countyStation.getOrgId(), OrgDeptType.opdept);
	}

}
