package com.taobao.cun.auge.station.transfer;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.mapper.ext.StationTransferExtMapper;

/**
 * 县点交接
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CountyStationTransferBo {
	@Resource
	private StationTransferExtMapper stationTransferExtMapper;
	
	public List<Long> getAutoTransferableCountyIds(Date date){
		return ListUtils.emptyIfNull(stationTransferExtMapper.selectAutoTransferableCountyIds(date));
	}
}
