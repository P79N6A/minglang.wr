package com.taobao.cun.auge.station.transfer.state;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.transfer.dto.CountyStationTransferPhase;
import com.taobao.cun.auge.station.transfer.dto.TransferState;

/**
 * 县点转移状态管理
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CountyTransferStateMgrBo implements TransferStateMgrBo<CountyStationTransferPhase> {
	@Resource
	private CountyStationBO countyStationBO;
	
	@Override
	public CountyStationTransferPhase getTransferPhase(Long id) {
		CountyStation countyStation = countyStationBO.getCountyStationById(id);
		if(countyStation.getOwnDept().equals(OrgDeptType.opdept.name())){
			return CountyStationTransferPhase.COUNTY_AUTO_TRANSED;
		}
		
		if(countyStation.getTransferState().equals(TransferState.FINISHED.name())) {
			return CountyStationTransferPhase.COUNTY_TRANSED;
		}
		
		if(countyStation.getTransferState().equals(TransferState.TRANSFERING.name())) {
			return CountyStationTransferPhase.COUNTY_TRANSING;
		}
		
		return CountyStationTransferPhase.COUNTY_NOT_TRANS;
	}

}
