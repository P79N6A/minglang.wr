package com.taobao.cun.auge.station.transfer;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.mapper.ext.StationTransferExtMapper;
import com.taobao.cun.auge.station.transfer.dto.TransferStation;

/**
 * 站点交接业务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationTransferBo {
	@Resource
	private StationTransferExtMapper stationTransferExtMapper;
	
	/**
	 * 获取县点下所有可交接村点
	 * 
	 * @param countyStationId
	 * @return
	 */
	public List<TransferStation> getTransferableStations(Long orgId){
		return ListUtils.emptyIfNull(stationTransferExtMapper.getTransferableStations(orgId));
	}
	
	public int countServicing(Long orgId) {
		return stationTransferExtMapper.countServicing(orgId);
	}
}
