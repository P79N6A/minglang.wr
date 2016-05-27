package com.taobao.cun.auge.station.service.impl;

import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.dto.PageDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerInstanceQueryService.class)
public class PartnerInstanceQueryServiceImpl implements PartnerInstanceQueryService {

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;

	@Override
	public PartnerInstanceDto queryInfo(Long partnerInstanceId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PartnerInstanceDto querySafedInfo(Long partnerInstanceId) throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition) {
		PageHelper.startPage(1, 10);
		Page<PartnerInstance> page = partnerStationRelMapper.findPartnerInstances();
		PageDto<PartnerInstanceDto> result = PageDtoUtil.convert(page, PartnerInstanceConverter.convert(page));

		return result;
	}

}
