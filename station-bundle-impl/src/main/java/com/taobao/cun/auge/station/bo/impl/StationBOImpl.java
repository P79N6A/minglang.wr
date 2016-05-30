package com.taobao.cun.auge.station.bo.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("stationBO")
public class StationBOImpl implements StationBO {

	@Autowired
	StationMapper stationMapper;

	@Override
	public Station getStationById(Long stationId) throws AugeServiceException {
		ValidateUtils.notNull(stationId, CommonExceptionEnum.PARAM_IS_NULL);
		return stationMapper.selectByPrimaryKey(stationId);
	}

	@Override
	public Station getStationByStationNum(String stationNum)
			throws AugeServiceException {
		ValidateUtils.notNull(stationNum, CommonExceptionEnum.PARAM_IS_NULL);
		Station record = new Station();
		record.setStationNum(stationNum);
		record.setIsDeleted("n");
		return stationMapper.selectOne(record);
	}

	@Override
	public void changeState(Long stationId, StationStatusEnum preStatus,
			StationStatusEnum postStatus, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(stationId, CommonExceptionEnum.PARAM_IS_NULL);
		ValidateUtils.notNull(preStatus, CommonExceptionEnum.PARAM_IS_NULL);
		ValidateUtils.notNull(postStatus, CommonExceptionEnum.PARAM_IS_NULL);
		ValidateUtils.notNull(operator, CommonExceptionEnum.PARAM_IS_NULL);
		Station station = getStationById(stationId);
		if (!StringUtils.equals(preStatus.getCode(), station.getStatus())){
			throw new AugeServiceException(StationExceptionEnum.STATION_NOT_EXIST);
		}
		Station record = new Station();
		record.setId(stationId);
		record.setStatus(postStatus.getCode());
		DomainUtils.beforeUpdate(record, operator);
		stationMapper.updateByPrimaryKey(record);
	}

	@Override
	public Long addStation(StationDto stationDto) throws AugeServiceException {
		ValidateUtils.notNull(stationDto, CommonExceptionEnum.PARAM_IS_NULL);
		Station record = StationConverter.toStation(stationDto);
		DomainUtils.beforeInsert(record, stationDto.getOperator());
		stationMapper.insert(record);
		return record.getId();
	}

	@Override
	public void updateStation(StationDto stationDto) throws AugeServiceException {
		ValidateUtils.notNull(stationDto, CommonExceptionEnum.PARAM_IS_NULL);
		ValidateUtils.notNull(stationDto.getId(), CommonExceptionEnum.PARAM_IS_NULL);
		Station oldRecord = getStationById(stationDto.getId());
		if (oldRecord == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NOT_EXIST);
		}
		Station record = StationConverter.toStation(stationDto);
		
		//更新Feature
		if (StringUtils.isNotEmpty(oldRecord.getFeature())) {
			Map<String, String> sourceMap = FeatureUtil.toMap(oldRecord.getFeature());
			if (sourceMap != null && sourceMap.size()>0) {
				sourceMap.putAll(stationDto.getFeature());
				record.setFeature(FeatureUtil.toString(sourceMap));
			}else {
				record.setFeature(FeatureUtil.toString(stationDto.getFeature()));
			}
		}else {
			record.setFeature(FeatureUtil.toString(stationDto.getFeature()));
		}
		
		DomainUtils.beforeUpdate(record, stationDto.getOperator());
		stationMapper.updateByPrimaryKey(record);
	}

	@Override
	public int getStationCountByStationNum(String stationNum)
			throws AugeServiceException {
		Station record = new Station();
		record.setStationNum(stationNum);
		record.setIsDeleted("n");
		return stationMapper.selectCount(record);
	}
	
	@Override
	public void deleteStation(Long stationId,String operator)
			throws AugeServiceException {
		Station rel = new Station();
		rel.setId(stationId);
		DomainUtils.beforeDelete(rel, operator);
		stationMapper.updateByPrimaryKeySelective(rel);
	}
}
