package com.taobao.cun.auge.station.check.impl.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.ddy.service.DdyLbsQueryService;
import com.taobao.cun.auge.ddy.service.XzCheckResult;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.check.StationTransChecker;
import com.taobao.cun.auge.station.dto.PartnerInstanceTransDto;
import com.taobao.cun.auge.station.enums.StationBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 罗盘规则检查
 * @author quanzhu.wangqz
 *
 */
@Component
public class TransDdyChecker implements StationTransChecker {

	@Autowired
	private DdyLbsQueryService ddyLbsQueryService;

	@Override
	public void check(PartnerInstanceTransDto t) {
		XzCheckResult xzCheckResult = ddyLbsQueryService.xzCheck(
				PositionUtil.converDown(t.getStationDto().getAddress().getLng()),
				PositionUtil.converDown(t.getStationDto().getAddress().getLat()),
				getIndustry(t.getType().getToBizType().getCode()));
		if (xzCheckResult != null && xzCheckResult.getXzStations() != null
				&& xzCheckResult.getXzStations().size() > 0) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,
					"1公里范围已经有村淘同类网点，请重新选择合适的门店地址。");
		}
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
