package com.taobao.cun.auge.station.service.impl;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
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
	
	@Value("${crm.peixun.course.url}")
	private String courseUrl;
	
	@Value("${crm.peixun.order.url}")
	private String orderUrl;
	
	@Override
	public PartnerPeixunDto queryPartnerPeixunProcess(Long userId) {
		PartnerPeixunDto dto= partnerPeixunBO.queryApplyInPeixunRecord(userId);
		if(dto!=null){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
			if(dto.getGmtDone()!=null){
				dto.setGmtDoneDesc(sdf.format(dto.getGmtDone()));
			}
			if(dto.getGmtOrder()!=null){
				dto.setGmtOrderDesc(sdf.format(dto.getGmtOrder()));
			}
			dto.setMyOrderUrl(orderUrl);
			dto.setCourseDetailUrl(courseUrl);
		}
		return dto;
	}

	@Override
	public List<PartnerPeixunDto> queryBatchPeixunPocess(List<Long> userIds) {
		return partnerPeixunBO.queryBatchPeixunRecord(userIds);
	}

}
