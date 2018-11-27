package com.taobao.cun.auge.station.check.impl.trans;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.ddy.service.DdyLbsQueryService;
import com.taobao.cun.auge.ddy.service.XzCheckResult;
import com.taobao.cun.auge.ddy.service.XzStation;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.StationBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 罗盘规则检查
 * 
 * @author quanzhu.wangqz
 *
 */
@Component
public class TransDdyChecker implements StationTransChecker {
	@Autowired
	private DdyLbsQueryService ddyLbsQueryService;
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
		Station s = stationBO.getStationById(r.getStationId());
		XzCheckResult xzCheckResult = ddyLbsQueryService.xzCheck(PositionUtil.converDown(s.getLng()),
				PositionUtil.converDown(s.getLat()), getIndustry(t.getType().getToBizType().getCode()));
		if (xzCheckResult != null && CollectionUtils.isNotEmpty(xzCheckResult.getXzStations())) {
			List<XzStation> xzList = filterXzStations(xzCheckResult.getXzStations(), s.getId());
			if (CollectionUtils.isNotEmpty(xzList)) {
				throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
						"1公里范围已经有村淘同类网点，请重新选择合适的门店地址。");
			}
		}
	}

	private String subS(String biz) {
		if (biz == null || biz.length() < 2) {
			return biz;
		}
		if ("S".equalsIgnoreCase(biz.substring(0, 1))) {
			return biz.substring(1, biz.length());
		}
		return biz;
	}

	private List<XzStation> filterXzStations(List<XzStation> xzStations, Long id) {
		List<XzStation> res = new ArrayList<XzStation>();
		for (int i = 0; i < xzStations.size(); i++) {
			try {
				XzStation xz = xzStations.get(i);
				Long stationId = Long.parseLong(subS(xz.getBizId()));
				if (!stationId.equals(id)) {
					res.add(xz);
				}
			} catch (NumberFormatException e) {
			}
		}
		return res;
	}

	private String getIndustry(String bizType) {
		if (StationBizTypeEnum.YOUPIN.getCode().equals(bizType)) {
			return "FMCG";
		} else if (StationBizTypeEnum.YOUPIN_ELEC.getCode().equals(bizType)
				|| StationBizTypeEnum.TPS_ELEC.getCode().equals(bizType)) {
			return "ELEC";
		}
		return null;
	}

}
