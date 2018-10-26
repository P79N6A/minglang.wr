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
		ultimateTransferHandlerManager.getHandlers(getHandlerGroup()).forEach(handler->{
			handler.transfer(countyStation, operator, opOrgId);
		});
	}

	/**
	 * 获取处理器分组
	 * @return
	 */
	protected abstract String getHandlerGroup();
}
