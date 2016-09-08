package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.dto.PartnerOnlinePeixunDto;
import com.taobao.cun.auge.station.dto.PartnerPeixunDto;
import com.taobao.cun.auge.station.service.PartnerPeixunService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
/**
 * 
 * @author yi.shaoy
 *
 */
@Service("partnerPeixunService")
@HSFProvider(serviceInterface = PartnerPeixunService.class)
public class PartnerPeixunServiceImpl implements PartnerPeixunService{

	@Autowired
	PartnerPeixunBO partnerPeixunBO;
	
	
	@Override
	public PartnerPeixunDto queryPartnerPeixunProcess(Long userId) {
		return partnerPeixunBO.queryApplyInPeixunRecord(userId);
	}

	@Override
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds) {
		return partnerPeixunBO.queryBatchPeixunRecord(userIds);
	}

	@Override
	public PartnerOnlinePeixunDto queryOnlinePeixunProcess(Long userId) {
		return partnerPeixunBO.queryOnlinePeixunProcess(userId);
	}

}
