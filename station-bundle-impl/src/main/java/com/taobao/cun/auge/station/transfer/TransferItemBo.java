package com.taobao.cun.auge.station.transfer;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.TransferItem;
import com.taobao.cun.auge.dal.domain.TransferItemExample;
import com.taobao.cun.auge.dal.mapper.TransferItemMapper;

/**
 * 交接项纪录，包括交接县点、村点开始结束时间，审批后是拒绝还是通过，供数据分析用
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TransferItemBo {
	@Resource
	private TransferItemMapper transferItemMapper;
	
	public void insert(TransferItem transferItem) {
		transferItemMapper.insert(transferItem);
	}
	
	public void updateStateByJobId(Long jobId, String state) {
		TransferItemExample example = new TransferItemExample();
		example.createCriteria().andJobIdEqualTo(jobId);
		
		TransferItem record = new TransferItem();
		record.setGmtEndTime(new Date());
		record.setState(state);
		
		transferItemMapper.updateByExampleSelective(record, example);
	}
}
