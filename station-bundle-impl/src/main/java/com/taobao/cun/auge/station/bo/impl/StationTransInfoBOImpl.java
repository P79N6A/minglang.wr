package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.StationTransInfo;
import com.taobao.cun.auge.dal.domain.StationTransInfoExample;
import com.taobao.cun.auge.dal.domain.StationTransInfoExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationTransInfoMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.StationTransInfoBO;
import com.taobao.cun.auge.station.dto.StationTransInfoDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component("stationTransInfoBO")
public class StationTransInfoBOImpl implements StationTransInfoBO {
	
	@Autowired
	StationTransInfoMapper stationTransInfoMapper;
	@Override
	public Long addTransInfo(StationTransInfoDto dto) {
		ValidateUtils.notNull(dto);
		StationTransInfo record =toDomain(dto);
		DomainUtils.beforeInsert(record, dto.getOperator());
		StationTransInfo last = getLastTransInfoByStationId(dto.getStationId());
		if (last != null) {
			setLastIsN(last.getId(),  dto.getOperator());
		}
		record.setIsLatest("y");
		record.setStatus(PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode());
		stationTransInfoMapper.insert(record);
		return record.getId();
	}
	
	private void setLastIsN(Long id,String o) {
		StationTransInfo s = new StationTransInfo();
		s.setId(id);
		s.setIsLatest("n");
		DomainUtils.beforeUpdate(s, o);
		stationTransInfoMapper.updateByPrimaryKeySelective(s);
	}
	
	private StationTransInfo toDomain(StationTransInfoDto dto){
		if (dto == null) {
			return null;
		}
		StationTransInfo i = new StationTransInfo();
		i.setFromBizType(dto.getFromBizType());
		i.setId(dto.getId());
		i.setIsLatest(dto.getIsLatest());
		i.setNewBizDate(dto.getNewBizDate());
		i.setOldBizDate(dto.getOldBizDate());
		i.setOperateTime(dto.getOperateTime());
		i.setOperator(dto.getOperator());
		i.setOperatorType(dto.getOperatorType());
		i.setRemark(dto.getRemark());
		i.setStationId(dto.getStationId());
		i.setStatus(dto.getStatus());
		i.setTaobaoUserId(dto.getTaobaoUserId());
		i.setToBizType(dto.getToBizType());
		i.setType(dto.getType());
		i.setFromBizType(dto.getFromBizType());
		return i;
	}
	
	private StationTransInfoDto toDto(StationTransInfo info){
		if (info == null) {
			return null;
		}
		StationTransInfoDto i = new StationTransInfoDto();
		i.setFromBizType(info.getFromBizType());
		i.setId(info.getId());
		i.setIsLatest(info.getIsLatest());
		i.setNewBizDate(info.getNewBizDate());
		i.setOldBizDate(info.getOldBizDate());
		i.setOperateTime(info.getOperateTime());
		i.setOperator(info.getOperator());
		i.setOperatorType(info.getOperatorType());
		i.setRemark(info.getRemark());
		i.setStationId(info.getStationId());
		i.setStatus(info.getStatus());
		i.setTaobaoUserId(info.getTaobaoUserId());
		i.setToBizType(info.getToBizType());
		i.setType(info.getType());
		i.setFromBizType(info.getFromBizType());
		return i;
	}

	@Override
	public StationTransInfo getLastTransInfoByStationId(Long stationId) {
		ValidateUtils.notNull(stationId);
		StationTransInfoExample example = new StationTransInfoExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationIdEqualTo(stationId);
		criteria.andIsLatestEqualTo("y");
		return ResultUtils.selectOne(stationTransInfoMapper.selectByExample(example)); 
	}
	
	@Override
	public StationTransInfoDto getLastTransInfoDtoByStationId(Long stationId) {
		return toDto(getLastTransInfoByStationId(stationId));
	}

	@Override
	public void changeTransing(Long stationId,String operator) {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(operator);
		StationTransInfo s = getLastTransInfoByStationId(stationId);
		if (s == null || !PartnerInstanceTransStatusEnum.WAIT_TRANS.getCode().equals(s.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"当前状态不允许修改，必须待转型");
		}
		s.setStatus(PartnerInstanceTransStatusEnum.TRANS_ING.getCode());
		DomainUtils.beforeUpdate(s, operator);
		stationTransInfoMapper.updateByPrimaryKeySelective(s);

	}

	@Override
	public void finishTrans(Long stationId,Date newBizDate,String operator) {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(operator);
		StationTransInfo s = getLastTransInfoByStationId(stationId);
		if (s == null || !PartnerInstanceTransStatusEnum.TRANS_ING.getCode().equals(s.getStatus())) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"当前状态不允许修改,必须转型中");
		}
		s.setStatus(PartnerInstanceTransStatusEnum.TRANS_DONE.getCode());
		s.setNewBizDate(newBizDate);
		DomainUtils.beforeUpdate(s, operator);
		stationTransInfoMapper.updateByPrimaryKeySelective(s);
	}
}
