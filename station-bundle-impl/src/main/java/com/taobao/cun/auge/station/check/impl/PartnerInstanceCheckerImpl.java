package com.taobao.cun.auge.station.check.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.check.PartnerInstanceChecker;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.order.fulfillment.api.CtFulFillStockService;
import com.taobao.cun.order.fulfillment.common.Result;
import com.taobao.cun.order.fulfillment.result.CtFulFillStockDTO;

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
	
	@Autowired
	AccountMoneyBO accountMoneyBO;
	 
	@Autowired
	CtFulFillStockService ctFulFillStockService;
	
	@Autowired
	DiamondConfiguredProperties diamondConfiguredProperties;
	@Autowired
	StoreReadBO storeReadBO;
	@Override
	public void checkCloseApply(Long instanceId) {
		// 查询实例是否存在，不存在会抛异常
		PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceById(instanceId);

		// 生成状态变化枚举，装修中-》停业申请中，或者，服务中-》停业申请中
		PartnerInstanceConverter.convertClosingStateChange(partnerStationRel);

		// 校验停业前置条件。例如校验合伙人是否还存在淘帮手存在
		PartnerInstanceTypeEnum partnerType = PartnerInstanceTypeEnum.valueof(partnerStationRel.getType());
		partnerInstanceHandler.validateClosePreCondition(partnerType, partnerStationRel);
	}

	@Override
	public void checkQuitApply(Long instanceId) {
		// 查询实例是否存在，不存在会抛异常
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(instanceId);

		// 校验是否已经存在退出申请单
		QuitStationApply quitStationApply = quitStationApplyBO.findQuitStationApply(instanceId);
		if (quitStationApply != null) {
			throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE,"您已经提交了退出申请");
		}
		StoreDto store = storeReadBO.getStoreDtoByStationId(instance.getStationId());
		if(store != null && diamondConfiguredProperties.isCheckStoreStock()){
			 Result<List<CtFulFillStockDTO>> stockResult = ctFulFillStockService.listStockByStoreId(store.getShareStoreId()+"");
			 if(stockResult != null && stockResult.getData() != null){
				 for(CtFulFillStockDTO stock : stockResult.getData()){
					 Long quantity = stock.getBhQuantity()+stock.getZTQuantity()+stock.getKSQuantity()+stock.getGapQuantity()+stock.getXTQuantity()+stock.getDPQuantity()+stock.getJSQuantity()+stock.getCCQuantity();
					 if(quantity > 0){
							throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"您的货品库存不为空，暂不能退出");
					 }
				 }
			 }
			
		}
		// 校验是否存在未结束的订单
		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		tradeAdapter.validateNoEndTradeOrders(partner.getTaobaoUserId(), instance.getServiceEndTime());

		// 校验是否还有下一级别的人。例如校验合伙人是否还存在淘帮手存在
		PartnerInstanceTypeEnum instanceType = PartnerInstanceTypeEnum.valueof(instance.getType());
		partnerInstanceHandler.validateExistChildrenForQuit(instanceType, instance);

		// 校验资产是否归还
		partnerInstanceHandler.validateAssetBack(instanceType, instanceId);
		
		//冻结了铺货金  不能退出  临时止血
		/**AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
		        AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
		if (null != bondMoney && AccountMoneyStateEnum.HAS_FROZEN.equals(bondMoney.getState())) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_BUSINESS_CHECK_ERROR_CODE,"您已经冻结了铺货金，暂不能退出");
		}**/
	}
	
	
}
