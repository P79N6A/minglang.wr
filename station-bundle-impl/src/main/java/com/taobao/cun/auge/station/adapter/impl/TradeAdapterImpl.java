package com.taobao.cun.auge.station.adapter.impl;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.station.adapter.TradeAdapter;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.trade.TaobaoNoEndTradeDto;
import com.taobao.cun.service.trade.TaobaoTradeOrderQueryService;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.refund.domain.RefundDO;

@Component("tradeAdapter")
public class TradeAdapterImpl implements TradeAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(TradeAdapter.class);

	@Autowired
	TaobaoTradeOrderQueryService taobaoTradeOrderQueryService;

	@Override
	public void validateNoEndTradeOrders(Long taobaoUserId, Date endDate) throws AugeBusinessException,AugeSystemException{
		ResultModel<TaobaoNoEndTradeDto> taobaoNoEndTradeDtoResultModel = taobaoTradeOrderQueryService.findNoEndTradeOrders(taobaoUserId,endDate);
        if (!taobaoNoEndTradeDtoResultModel.isSuccess()) {
			logger.error("查询未结束订单失败。taobaoUserId= " + taobaoUserId + " endDate" + DateUtil.format(endDate),taobaoNoEndTradeDtoResultModel.getException());
			throw new AugeSystemException(CommonExceptionEnum.SYSTEM_ERROR);
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
			logger.warn("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
			throw new AugeBusinessException("村掌柜仍有未完成的代购单（交易订单确认收货）、待退款（退款完结），请联系掌柜核实" + build.toString());
		}
	}
}
