package com.taobao.cun.auge.station.check.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.check.PartnerInstanceChecker;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;

@Component("partnerInstanceChecker")
public class PartnerInstanceCheckerImpl implements PartnerInstanceChecker {

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	TradeAdapter tradeAdapter;

	@Override
	public void checkCloseApply(Long instanceId) throws AugeBusinessException, AugeSystemException {
		// 查询实例是否存在，不存在会抛异常
		PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

		// 生成状态变化枚举，装修中-》停业申请中，或者，服务中-》停业申请中
		PartnerInstanceConverter.convertClosingStateChange(partnerStationRel);

		// 校验停业前置条件。例如校验合伙人是否还存在淘帮手存在
		PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerStationRel.getType());
		partnerInstanceHandler.validateClosePreCondition(partnerType, partnerStationRel);
	}

	@Override
	public void checkQuitApply(Long instanceId) throws AugeBusinessException, AugeSystemException {
		// 查询实例是否存在，不存在会抛异常
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new AugeBusinessException(StationExceptionEnum.QUIT_STATION_APPLY_EXIST.getDesc());
		}

		// 校验是否存在未结束的订单
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		tradeAdapter.validateNoEndTradeOrders(partner.getTaobaoUserId(), instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		PartnerInstanceTypeEnum instanceType = PartnerInstanceTypeEnum.valueof(instance.getType());
		partnerInstanceHandler.validateExistChildrenForQuit(instanceType, instance);

		// 校验资产是否归还
		partnerInstanceHandler.validateAssetBack(instanceType, instanceId);
	}
}
