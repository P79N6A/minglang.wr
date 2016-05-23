package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.bo.TradeBO;
import com.taobao.cun.auge.station.dto.TaobaoNoEndTradeDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.TaobaoTradeOrderQueryService;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.refund.domain.RefundDO;

@Component
public class TradeBOImpl implements TradeBO {

	@Autowired
	TaobaoTradeOrderQueryService taobaoTradeOrderQueryService;

	@Override
	// FIXME FHH 调用了center的接口，后续需要迁移
	public void validateNoEndTradeOrders(Long buyerId, Date endDate) throws AugeServiceException {
		TaobaoNoEndTradeDto taobaoNoEndTradeDto = taobaoTradeOrderQueryService.findNoEndTradeOrders(buyerId, endDate);

		taobaoNoEndTradeDto.getBatchQueryOrderInfoResultDO();
		if (taobaoNoEndTradeDto.isExistsNoEndOrder()) {
			StringBuilder build = new StringBuilder();
			for (OrderInfoTO info : taobaoNoEndTradeDto.getBatchQueryOrderInfoResultDO().getOrderList()) {
				build.append(info.getBizOrderDO().getBizOrderId());
				build.append(info.getBizOrderDO().getAuctionTitle());
				build.append("\n");
			}
			for (RefundDO refund : taobaoNoEndTradeDto.getBatchRefundResultDO().getRefundList()) {
				build.append("退款中:\n");
				build.append(refund.getBizOrderId());
				build.append(refund.getAuctionTitle());
				build.append("\n");
			}
			throw new AugeServiceException("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
		}
	}
}
