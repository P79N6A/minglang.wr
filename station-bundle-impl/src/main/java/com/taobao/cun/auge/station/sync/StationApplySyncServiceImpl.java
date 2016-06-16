package com.taobao.cun.auge.station.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.StationApplyMapper;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.service.StationApplySyncService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = StationApplySyncService.class)
public class StationApplySyncServiceImpl implements StationApplySyncService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private StationApplySyncBO syncStationApplyBO;
	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	@Autowired
	StationApplyMapper stationApplyMapper;

	@Override
	public void syncToStationApply(Long partnerInstanceId) {
		PartnerStationRel instance = partnerStationRelMapper.selectByPrimaryKey(partnerInstanceId);
		if (instance == null) {
			return;
		}
		Long stationApplyId = instance.getStationApplyId();
		if (null == stationApplyId) {
			syncStationApplyBO.addStationApply(partnerInstanceId);
			return;
		}

		StationApply stationApply = stationApplyMapper.selectByPrimaryKey(stationApplyId);
		if (null == stationApply) {
			String msg = "sync update failed, station_apply record not exist: " + partnerInstanceId;
			logger.error(msg);
			throw new RuntimeException(msg);
		}
		syncStationApplyBO.updateStationApply(partnerInstanceId, SyncStationApplyEnum.UPDATE_ALL);
	}

}
