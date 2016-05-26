package com.taobao.cun.auge.station.adapter.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.trade.TaobaoNoEndTradeDto;
import com.taobao.cun.service.trade.TaobaoTradeOrderQueryService;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.refund.domain.RefundDO;

@Component
public class TradeAdapterImpl implements TradeAdapter {

	@Autowired
	TaobaoTradeOrderQueryService taobaoTradeOrderQueryService;

	@Override
	public void validateNoEndTradeOrders(Long taobaoUserId, Date endDate) throws AugeServiceException {
		ResultModel<TaobaoNoEndTradeDto> taobaoNoEndTradeDtoResultModel = taobaoTradeOrderQueryService.findNoEndTradeOrders(taobaoUserId,endDate);
        if (!taobaoNoEndTradeDtoResultModel.isSuccess()) {
            throw new AugeServiceException(taobaoNoEndTradeDtoResultModel.getException());
        }

        TaobaoNoEndTradeDto taobaoNoEndTradeDto = taobaoNoEndTradeDtoResultModel.getResult();

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