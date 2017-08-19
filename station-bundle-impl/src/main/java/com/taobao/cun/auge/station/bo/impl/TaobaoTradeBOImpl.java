package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.bo.TaobaoTradeBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.trade.dto.TaobaoNoEndTradeDto;
import com.taobao.refundplatform.client.read.RefundReadService;
import com.taobao.refundplatform.common.domain.option.OperateOption;
import com.taobao.refundplatform.common.domain.query.QueryRefundDO;
import com.taobao.refundplatform.common.result.BatchRefundResultDO;
import com.taobao.tc.domain.dataobject.OrderInfoTO;
import com.taobao.tc.domain.query.BizQueryOptionDO;
import com.taobao.tc.domain.query.QueryBizOrderDO;
import com.taobao.tc.domain.result.BatchQueryOrderInfoResultDO;
import com.taobao.tc.refund.domain.RefundDO;
import com.taobao.trade.platform.api.query.BuyerQueryService;

@Component("taobaoTradeBO")
public class TaobaoTradeBOImpl implements TaobaoTradeBO{
	
	@Autowired
	private BuyerQueryService buyerQueryService;
	@Autowired
	private RefundReadService refundReadService;

	@Override
	public TaobaoNoEndTradeDto findNoEndTradeOrders(Long buyerId, Date endDate){
	        TaobaoNoEndTradeDto data = new TaobaoNoEndTradeDto();
	        QueryBizOrderDO query = new QueryBizOrderDO();
	        query.setStatus(new int[]{0});
	        query.setPayStatus(new int[]{1, 2});
	        query.setExcludedBizType(new int[]{2000,1100,1110,1650,6012});
	        BizQueryOptionDO option = new BizQueryOptionDO();
	        option.setShowDetail(true);
	        option.setShowSnapShot(BizQueryOptionDO.ALL_SNAPSHOT);
	        QueryRefundDO refundQuery = new QueryRefundDO();
	        refundQuery.setBuyerId(buyerId);
	        refundQuery.setRefundStatusList(new int[]{RefundDO.STATUS_WAIT_BUYER_RETURN_GOODS, RefundDO.STATUS_WAIT_SELLER_CONFIRM_GOODS, RefundDO.STATUS_WAIT_SELLER_AGREE,RefundDO.STATUS_SELLER_REFUSE_BUYER});
	        OperateOption refundOption = new OperateOption();
	        try {
	            BatchQueryOrderInfoResultDO batchQueryOrderInfoResultDO = buyerQueryService.queryBatch(buyerId, query, option);
	            BatchRefundResultDO batchRefundResultDO = refundReadService.queryRefundListForBuyer(buyerId, refundQuery, refundOption);
	            if (endDate != null) {
	                if (batchQueryOrderInfoResultDO != null && batchQueryOrderInfoResultDO.getOrderList() != null) {
	                    List<OrderInfoTO> orderInfoTOList = batchQueryOrderInfoResultDO.getOrderList();
	                    List<OrderInfoTO> orderInfoTOArrayList = new ArrayList<OrderInfoTO>();
	                    for (OrderInfoTO orderInfoTO : orderInfoTOList) {
	                        if (orderInfoTO.getBizOrderDO() != null && orderInfoTO.getBizOrderDO().getAttribute("cuntao") != null) {
	                            Date gmtCreate = orderInfoTO.getBizOrderDO().getGmtCreate();
	                            if (gmtCreate.before(endDate)) {
	                                orderInfoTOArrayList.add(orderInfoTO);
	                            }
	                        }
	                    }
	                    batchQueryOrderInfoResultDO.setOrderList(orderInfoTOArrayList);
	                }
	                if (batchRefundResultDO != null && batchRefundResultDO.getRefundList() != null) {
	                    List<RefundDO> refunds = batchRefundResultDO.getRefundList();
	                    List<RefundDO> refundsFinal = new ArrayList<RefundDO>();
	                    for (RefundDO refundDO : refunds) {
	                        Date gmtCreate = refundDO.getGmtCreate();
	                        if (refundDO.getAttribute("cuntao") != null && gmtCreate.before(endDate)) {
	                            refundsFinal.add(refundDO);
	                        }
	                    }
	                    batchRefundResultDO.setRefundList(refundsFinal);
	                }
	            }
	            data.setBatchQueryOrderInfoResultDO(batchQueryOrderInfoResultDO);
	            data.setBatchRefundResultDO(batchRefundResultDO);
	            return data;
	        } catch (Exception e) {
	           throw new AugeBusinessException(e);
	        }
	}
}
