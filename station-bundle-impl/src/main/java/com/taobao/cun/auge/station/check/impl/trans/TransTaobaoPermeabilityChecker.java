package com.taobao.cun.auge.station.check.impl.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.TownBlacknameService;
import com.taobao.cun.auge.station.service.UserFilterService;

/**
 * 手淘渗透率是否大于30%
 * 
 * @author quanzhu.wangqz
 *
 */
@Component
public class TransTaobaoPermeabilityChecker implements StationTransChecker {
	@Autowired
	private TownBlacknameService townBlacknameService;
	@Autowired
	private UserFilterService userFilterService;
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
	private StationBO stationBO;

	@Override
	public void check(PartnerInstanceTransDto t) {
		PartnerStationRel r = partnerInstanceBO.findPartnerInstanceById(t.getInstanceId());
		if (r == null || r.getTaobaoUserId() == null) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "查询不到指定站点。");
		}
		if (userFilterService.isMatch("partner-tbid-whitename", String.valueOf(r.getTaobaoUserId()))) {
			return;
		}
		Station s = stationBO.getStationById(r.getStationId());
		;
		if (townBlacknameService.isBlackname(s.getCountyDetail(), s.getTownDetail())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE, "所属行政区域，手淘渗透率超过30%,不可转型升级。");
		}
	}

}
