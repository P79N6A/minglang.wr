package com.taobao.cun.auge.station.bo.impl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.StationDecorateOrderBO;
import com.taobao.cun.auge.station.dto.StationDecorateOrderDto;
import com.taobao.tc.domain.dataobject.BizOrderDO;
import com.taobao.tc.domain.dataobject.PayOrderDO;
import com.taobao.tc.domain.query.QueryBizOrderDO;
import com.taobao.tc.domain.result.BatchQueryOrderInfoResultDO;
import com.taobao.tc.domain.result.SingleQueryResultDO;
import com.taobao.tc.refund.domain.RefundDO;
import com.taobao.tc.service.TcBaseService;

@Service("stationDecorateOrderBO")
public class StationDecorateOrderBOImpl implements StationDecorateOrderBO {

	private static final Logger logger = LoggerFactory.getLogger(StationDecorateOrderBOImpl.class);
	
	@Autowired
	private TcBaseService tcBaseService;
	
	@Value("${stationDecorateOrder.amount}")
	private long orderAmount;
	
	/* (non-Javadoc)
	 * @see com.taobao.cun.auge.station.bo.impl.StationDecorateOrderBO#getDecorateOrderById(java.lang.Long)
	 */
	@Override
	public Optional<StationDecorateOrderDto> getDecorateOrderById(Long bizOrderId){
		try {
			SingleQueryResultDO queryResultDO = tcBaseService.getBizOrderById(bizOrderId);
			StationDecorateOrderDto orderDto = getStationDecorateOrder(queryResultDO.getBizOrder());
			return Optional.ofNullable(orderDto);
		} catch (Exception e) {
			logger.error("getDecorateOrderById error bizOrderId[{}]",bizOrderId,e);
		}
		return Optional.empty();
	}

	private StationDecorateOrderDto getStationDecorateOrder(BizOrderDO mainOrder) {
		StationDecorateOrderDto orderDto = new StationDecorateOrderDto();
		orderDto.setPaid(mainOrder.isPaid());
		List<BizOrderDO> items = mainOrder.getDetailOrderList();
		BizOrderDO subOrder = items.stream().findFirst().get();
		orderDto.setRefund(mainOrder.getRefundStatus() == RefundDO.STATUS_END_REFUND);
		orderDto.setBizOrderId(mainOrder.getBizOrderId());
		orderDto.setTotalFee(mainOrder.getTotalFee());
		orderDto.setAuctionPrice(mainOrder.getAuctionPrice());
		orderDto.setAuctionId(subOrder.getAuctionId());
		orderDto.setAuctionPicUrl(subOrder.getAuctionPictUrl());
		orderDto.setAuctionTitle(subOrder.getAuctionTitle());
		orderDto.setBuyAmount(subOrder.getBuyAmount());
		orderDto.setCreateDate(mainOrder.getGmtCreate());
		return orderDto;
	}
	
	/* (non-Javadoc)
	 * @see com.taobao.cun.auge.station.bo.impl.StationDecorateOrderBO#getByDecorateOrder(java.lang.Long, java.lang.Long, java.lang.Long)
	 */
	@Override
	public Optional<StationDecorateOrderDto> getDecorateOrder(Long sellerTaobaoUserId, Long buyerTaobaoUserId) {
		try {
			logger.info("getDecorateOrder sellerTaobaoUserId[{}],buyerTaobaoUserId[{}]",sellerTaobaoUserId,buyerTaobaoUserId);
			QueryBizOrderDO query = new QueryBizOrderDO();
			query.setSellerNumId(new long[] { sellerTaobaoUserId });
			query.setBuyerNumId(new long[] { buyerTaobaoUserId });
			BatchQueryOrderInfoResultDO batchQueryResult = tcBaseService.queryMainAndDetail(query);
			Optional<BizOrderDO> paidOrder = batchQueryResult.getOrderList().stream()
					.map(orderInfo -> orderInfo.getBizOrderDO())
					.filter(bizOrder -> (bizOrder.getAuctionPrice() == orderAmount && bizOrder.isPaid()  )).findFirst();
			if(paidOrder.isPresent()){
				return Optional.ofNullable(getStationDecorateOrder(paidOrder.get()));
			}
			Optional<BizOrderDO> order = batchQueryResult.getOrderList().stream()
					.map(orderInfo -> orderInfo.getBizOrderDO())
					.filter(bizOrder -> (bizOrder.getAuctionPrice() == orderAmount  && bizOrder.getPayStatus() == PayOrderDO.STATUS_NOT_PAY )).findFirst();
			if(order.isPresent()){
				return Optional.ofNullable(getStationDecorateOrder(order.get()));
			}
			Optional<BizOrderDO> refund = batchQueryResult.getOrderList().stream()
					.map(orderInfo -> orderInfo.getBizOrderDO())
					.filter(bizOrder -> (bizOrder.getAuctionPrice() == orderAmount  && bizOrder.getRefundStatus() == RefundDO.STATUS_END_REFUND )).findFirst();
			if(order.isPresent()){
				return Optional.ofNullable(getStationDecorateOrder(refund.get()));
			}
		} catch (Exception e) {
			logger.error("getByDecorateOrder error sellerTaobaoUserId[{}],buyerTaobaoUserId[{}]",sellerTaobaoUserId,buyerTaobaoUserId,e);
		}
		return Optional.empty();
	}
}
