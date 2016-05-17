package com.taobao.cun.auge.station.dto;

import java.io.Serializable;

import com.taobao.refundplatform.common.result.BatchRefundResultDO;
import com.taobao.tc.domain.result.BatchQueryOrderInfoResultDO;

public class TaobaoNoEndTradeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private BatchRefundResultDO batchRefundResultDO;
	
	private BatchQueryOrderInfoResultDO batchQueryOrderInfoResultDO;

	public BatchRefundResultDO getBatchRefundResultDO() {
		return batchRefundResultDO;
	}

	public void setBatchRefundResultDO(BatchRefundResultDO batchRefundResultDO) {
		this.batchRefundResultDO = batchRefundResultDO;
	}

	public BatchQueryOrderInfoResultDO getBatchQueryOrderInfoResultDO() {
		return batchQueryOrderInfoResultDO;
	}

	public void setBatchQueryOrderInfoResultDO(
			BatchQueryOrderInfoResultDO batchQueryOrderInfoResultDO) {
		this.batchQueryOrderInfoResultDO = batchQueryOrderInfoResultDO;
	}
	
	/**
	 *  判断是否存在未完结订单
	 * @return
	 */
	public boolean isExistsNoEndOrder(){
		if(batchQueryOrderInfoResultDO!=null&&batchQueryOrderInfoResultDO.getOrderList().size()>0){
			return true;
		}
		if(batchRefundResultDO!=null&&batchRefundResultDO.getRefundList().size()>0){
			return true;
		}
		
		return false;
	}
}

