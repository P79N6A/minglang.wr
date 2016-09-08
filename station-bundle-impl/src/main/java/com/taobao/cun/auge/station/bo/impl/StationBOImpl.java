package com.taobao.cun.auge.station.bo.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.dal.mapper.StationExtMapper;
import com.taobao.cun.auge.station.condition.StationCondition;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.domain.StationExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("stationBO")
public class StationBOImpl implements StationBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StationBO.class);

	@Autowired
	StationMapper stationMapper;

	@Autowired
	StationExtMapper stationExtMapper;

	@Override
	public Station getStationById(Long stationId) throws AugeServiceException {
		ValidateUtils.notNull(stationId);
		return stationMapper.selectByPrimaryKey(stationId);
	}
	
	@Override
	public List<Station> getStationById(List<Long> stationIds) throws AugeServiceException {
		if (CollectionUtils.isEmpty(stationIds)) {
			return Collections.<Station> emptyList();
		}

		StationExample example = new StationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andIdIn(stationIds);

		return stationMapper.selectByExample(example);
	}

	@Override
	public Station getStationByStationNum(String stationNum)
			throws AugeServiceException {
		ValidateUtils.notNull(stationNum);
		StationExample example = new StationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationNumEqualTo(stationNum);
		return ResultUtils.selectOne(stationMapper.selectByExample(example));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void changeState(Long stationId, StationStatusEnum preStatus, StationStatusEnum postStatus, String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(preStatus);
		ValidateUtils.notNull(postStatus);
		ValidateUtils.notNull(operator);
		Station station = getStationById(stationId);
		if (!StringUtils.equals(preStatus.getCode(), station.getStatus())) {
			logger.warn("村点状态不正确。当前状态为" + station.getStatus());
			throw new AugeServiceException(StationExceptionEnum.STATION_STATUS_CHANGED);
		}
		Station record = new Station();
		record.setId(stationId);
		record.setStatus(postStatus.getCode());
		if (StationStatusEnum.QUIT.equals(postStatus)) {
			record.setState(StationStateEnum.INVALID.getCode());
		}
		DomainUtils.beforeUpdate(record, operator);
		stationMapper.updateByPrimaryKeySelective(record);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addStation(StationDto stationDto) throws AugeServiceException {
		ValidateUtils.notNull(stationDto);
		Station record = StationConverter.toStation(stationDto);
		Date now = new Date();
		record.setGmtCreate(now);
		record.setGmtModified(now);
		record.setCreater(stationDto.getOperator());
		record.setModifier(stationDto.getOperator());
		record.setIsDeleted("n");
		record.setVersion(0L);
		stationMapper.insert(record);
		return record.getId();
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateStation(StationDto stationDto) throws AugeServiceException {
		ValidateUtils.notNull(stationDto);
		ValidateUtils.notNull(stationDto.getId());
		Station oldRecord = getStationById(stationDto.getId());
		if (oldRecord == null) {
			throw new AugeServiceException(StationExceptionEnum.STATION_NOT_EXIST);
		}
		Station record = StationConverter.toStation(stationDto);
		
		//更新Feature
		if (StringUtils.isNotEmpty(oldRecord.getFeature())) {
			Map<String, String> sourceMap = FeatureUtil.toMap(oldRecord.getFeature());
			if (sourceMap != null && sourceMap.size()>0) {
				if (stationDto.getFeature() != null) {
					sourceMap.putAll(stationDto.getFeature());
				}
				record.setFeature(FeatureUtil.toString(sourceMap));
			}else {
				record.setFeature(FeatureUtil.toString(stationDto.getFeature()));
			}
		}else {
			record.setFeature(FeatureUtil.toString(stationDto.getFeature()));
		}
		
		DomainUtils.beforeUpdate(record, stationDto.getOperator());
		stationMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int getStationCountByStationNum(String stationNum)
			throws AugeServiceException {
		ValidateUtils.notNull(stationNum);
		StationExample example = new StationExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStationNumEqualTo(stationNum);
		criteria.andStatusNotIn(Lists.newArrayList(StationStatusEnum.QUIT.getCode(),StationStatusEnum.INVALID.getCode()));
		return ResultUtils.selectCount(stationMapper.selectByExample(example));
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void deleteStation(Long stationId,String operator)
			throws AugeServiceException {
		ValidateUtils.notNull(stationId);
		Station rel = new Station();
		rel.setId(stationId);
		DomainUtils.beforeDelete(rel, operator);
		stationMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public List<Station> getStationsByName(StationCondition stationCondition) throws AugeServiceException {
		ValidateUtils.notNull(stationCondition);
		StationExtExample stationExtExample = new StationExtExample();
		stationExtExample.setName(stationCondition.getName());
		stationExtExample.setOrgIdPath(stationCondition.getOrgIdPath());
		stationExtExample.setStatus(stationCondition.getStationStatusEnum().getCode());
		stationExtExample.setPageSize(stationCondition.getPageSize());
		stationExtExample.setPageStart(stationCondition.getPageStart());
		return stationExtMapper.getStationsByName(stationExtExample);
	}


}
