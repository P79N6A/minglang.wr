package com.taobao.cun.auge.station.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;

@Component("partnerInstanceValidator")
public class PartnerInstanceValidator {

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	TradeAdapter tradeAdapter;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	/**
	 * 校验申请退出的前置条件
	 *
	 * @param instance
	 * @throws AugeServiceException
	 */
	public void validateApplyQuitPreCondition(PartnerStationRel instance) throws AugeServiceException {
		Long instanceId = instance.getId();
		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new RuntimeException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST.getDesc());
		}

		// 校验是否存在未结束的订单
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		tradeAdapter.validateNoEndTradeOrders(partner.getTaobaoUserId(), instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		partnerInstanceHandler.validateExistChildrenForQuit(PartnerInstanceTypeEnum.valueof(instance.getType()),
				instanceId);
		
		//校验资产是否归还
		partnerInstanceHandler.validateAssetBack(PartnerInstanceTypeEnum.valueof(instance.getType()),
				instanceId);
	}

}
