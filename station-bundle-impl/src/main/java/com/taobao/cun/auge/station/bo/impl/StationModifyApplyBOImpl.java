package com.taobao.cun.auge.station.bo.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.StationModifyApply;
import com.taobao.cun.auge.dal.mapper.StationModifyApplyMapper;
import com.taobao.cun.auge.station.bo.StationModifyApplyBO;
import com.taobao.cun.auge.station.convert.StationModifyApplyConverter;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.StationModifyApplyDto;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyBusitypeEnum;
import com.taobao.cun.auge.station.enums.StationModifyApplyStatusEnum;
import com.taobao.cun.auge.station.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;

@Component("stationModifyApplyBO")
public class StationModifyApplyBOImpl implements StationModifyApplyBO {
	
	@Autowired
	private StationModifyApplyMapper stationModifyApplyMapper;
	
	@Autowired
	private ProcessService processService;
	 
	@Override
	public Long addStationModifyApply(StationModifyApplyDto dto) {
		StationModifyApply s = StationModifyApplyConverter.toStationModifyApply(dto);
		DomainUtils.beforeInsert(s, dto.getOperator());
		stationModifyApplyMapper.insert(s);
		
		//启动流程
		StartProcessDto startProcessDto =new StartProcessDto();
		startProcessDto.setBusiness(ProcessBusinessEnum.stationInfoApply);
		startProcessDto.setBusinessId(s.getId());
		startProcessDto.setBusinessName("村点名称修改");
		startProcessDto.setBusinessOrgId(dto.getOperatorOrgId());
//		Map<String,String> initData = new HashMap<>();
//	    initData.put("orgId", String.valueOf(dto.getOperatorOrgId()));
//		startProcessDto.setJsonParams(JSONObject.toJSONString(initData));
		startProcessDto.copyOperatorDto(dto);
		processService.startApproveProcess(startProcessDto);
		return s.getId();
	}

	@Override
	public void audit(Long id, StationModifyApplyStatusEnum status) {
		// TODO Auto-generated method stub

	}

	@Override
	public StationModifyApplyDto getApplyInfoByType(
			StationModifyApplyBusitypeEnum type, Long stationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StationModifyApplyDto getApplyInfoById(Long Id) {
		// TODO Auto-generated method stub
		return null;
	}

}
