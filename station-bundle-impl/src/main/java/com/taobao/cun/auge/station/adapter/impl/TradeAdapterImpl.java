package com.taobao.cun.auge.station.adapter.impl;

import java.util.Date;

import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.bo.TaobaoTradeBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.trade.dto.TaobaoNoEndTradeDto;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.refund.domain.RefundDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("tradeAdapter")
public class TradeAdapterImpl implements TradeAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeAdapter.class);

	@Autowired
	TaobaoTradeBO taobaoTradeBO;

	@Override
	public void validateNoEndTradeOrders(Long taobaoUserId, Date endDate) throws AugeBusinessException,AugeSystemException{
		TaobaoNoEndTradeDto taobaoNoEndTradeDto = taobaoTradeBO.findNoEndTradeOrders(taobaoUserId,endDate);

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
			logger.warn("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
			throw new AugeBusinessException(AugeErrorCodes.TRADE_BUSINESS_CHECK_ERROR_CODE,"村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
		}
	}
}
