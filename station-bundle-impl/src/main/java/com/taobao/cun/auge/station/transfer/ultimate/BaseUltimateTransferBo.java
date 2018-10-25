package com.taobao.cun.auge.station.transfer.ultimate;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.transfer.ultimate.handle.UltimateTransferHandlerManager;

/**
 * 县域最终交接
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public abstract class BaseUltimateTransferBo {
	@Resource
	private CountyStationBO countyStationBO;
	@Resource
	private UltimateTransferHandlerManager ultimateTransferHandlerManager;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void transfer(Long countyStationId, String operator, Long opOrgId) {
		CountyStation countyStation = countyStationBO.getCountyStationById(countyStationId);
		ultimateTransferHandlerManager.getHandlers().forEach(handler->{
			handler.transfer(countyStation, operator, opOrgId);
		});
		
		afterTransferProcess(countyStationId, operator, opOrgId);
	}

	/**
	 * 交接处理完后续流程
	 * @param countyStationId
	 * @param operator
	 * @param opOrgId
	 */
	protected abstract void afterTransferProcess(Long countyStationId, String operator, Long opOrgId);
}
