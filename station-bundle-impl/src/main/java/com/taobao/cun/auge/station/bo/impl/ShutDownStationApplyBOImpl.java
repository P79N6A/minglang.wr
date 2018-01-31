package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.ShutDownStationApply;
import com.taobao.cun.auge.dal.domain.ShutDownStationApplyExample;
import com.taobao.cun.auge.dal.domain.ShutDownStationApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.ShutDownStationApplyMapper;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.convert.ShutDownStationApplyConverter;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.vipserver.client.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("shutDownStationApplyBO")
public class ShutDownStationApplyBOImpl implements ShutDownStationApplyBO{

	@Autowired
	ShutDownStationApplyMapper  shutDownStationApplyMapper;

	@Override
	public Long saveShutDownStationApply(ShutDownStationApplyDto dto) {
		if(null == dto){
			throw new IllegalArgumentException("ShutDownStationApplyDto is null");
		}
		ShutDownStationApply record = ShutDownStationApplyConverter.convert(dto);
		
		DomainUtils.beforeInsert(record, dto.getApplierId());
		shutDownStationApplyMapper.insertSelective(record);
		return record.getId();
	}

	@Override
	public ShutDownStationApplyDto findShutDownStationApply(Long stationId) {
		if(null == stationId){
			throw new IllegalArgumentException("stationId is null");
		}
		ShutDownStationApplyExample example = new ShutDownStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationIdEqualTo(stationId);
		List<ShutDownStationApply> applyes = shutDownStationApplyMapper.selectByExample(example);

		if (CollectionUtils.isEmpty(applyes)) {
			return null;
		}

		return ShutDownStationApplyConverter.convert(applyes.get(0));
	}
	
	@Override
	public ShutDownStationApplyDto findShutDownStationApplyById(Long applyId){
		if(null == applyId){
			throw new IllegalArgumentException("applyId is null");
		}
		ShutDownStationApply apply = shutDownStationApplyMapper.selectByPrimaryKey(applyId);
		return ShutDownStationApplyConverter.convert(apply);
	}

	@Override
	public void deleteShutDownStationApply(Long stationId, String operator) {
		if(null == stationId){
			throw new IllegalArgumentException("stationId is null");
		}
		ShutDownStationApplyExample example = new ShutDownStationApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andStationIdEqualTo(stationId);
		criteria.andIsDeletedEqualTo("n");
		
		ShutDownStationApply record = new ShutDownStationApply();
		
		DomainUtils.beforeDelete(record, operator);
		shutDownStationApplyMapper.updateByExampleSelective(record, example);
	}
}
