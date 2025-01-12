package com.taobao.cun.auge.station.bo.impl;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.domain.StationExample.Criteria;
import com.taobao.cun.auge.dal.example.StationExtExample;
import com.taobao.cun.auge.dal.mapper.StationExtMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.dto.FenceInitingStationQueryCondition;
import com.taobao.cun.auge.station.bo.dto.FenceStationQueryCondition;
import com.taobao.cun.auge.station.condition.StationCondition;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.convert.StationExtExampleConverter;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.validate.StationValidator;

@Component("stationBO")
public class StationBOImpl implements StationBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StationBO.class);

	@Autowired
	StationMapper stationMapper;

	@Autowired
	StationExtMapper stationExtMapper;
	
	@Autowired
	private CuntaoOrgServiceClient cuntaoOrgServiceClient;

	@Override
	public Station getStationById(Long stationId){
		ValidateUtils.notNull(stationId);
		return stationMapper.selectByPrimaryKey(stationId);
	}
	
	@Override
	public List<Station> getStationById(List<Long> stationIds){
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
	public Station getStationByStationNum(String stationNum){
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
			 {
		ValidateUtils.notNull(stationId);
		ValidateUtils.notNull(preStatus);
		ValidateUtils.notNull(postStatus);
		ValidateUtils.notNull(operator);
		Station station = getStationById(stationId);
		if (!StringUtils.equals(preStatus.getCode(), station.getStatus())) {
			logger.warn("村点状态不正确。当前状态为" + station.getStatus());
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"村点状态不正确。当前状态为" + station.getStatus());
		}
		Station record = new Station();
		record.setId(stationId);
		record.setStatus(postStatus.getCode());
		 if (StationStatusEnum.QUIT.equals(postStatus)) {
			 record.setState(StationStateEnum.INVALID.getCode());
		 } else if (StationStatusEnum.SERVICING.equals(postStatus)) {
			 record.setState(StationStateEnum.NORMAL.getCode());
		 }
		DomainUtils.beforeUpdate(record, operator);
		stationMapper.updateByPrimaryKeySelective(record);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Long addStation(StationDto stationDto){
		ValidateUtils.notNull(stationDto);
		Station record = StationConverter.toStation(stationDto);
		Date now = new Date();
		record.setGmtCreate(now);
		record.setGmtModified(now);
		record.setCreater(stationDto.getOperator());
		record.setModifier(stationDto.getOperator());
		record.setIsDeleted("n");
		record.setVersion(0L);
		buildTestTag(record);
		stationMapper.insert(record);
		return record.getId();
	}

	private void buildTestTag(Station record) {
		try {
			CuntaoOrgDto largearea = cuntaoOrgServiceClient.getAncestor(record.getApplyOrg(), OrgRangeType.LARGE_AREA);
			if (largearea != null && largearea.getId().equals(500004L)) {
				Map<String,String> m = new HashMap<String,String>();
				m.put("isTest", "y");
				if (StringUtils.isNotEmpty(record.getFeature())) {
					Map<String, String> sourceMap = FeatureUtil.toMap(record.getFeature());
					if (sourceMap != null && sourceMap.size()>0) {
						sourceMap.putAll(m);
						record.setFeature(FeatureUtil.toString(sourceMap));
					}else {
						record.setFeature(FeatureUtil.toString(m));
					}
				}else {
					record.setFeature(FeatureUtil.toString(m));
				}
			}
		} catch (Exception e) {
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateStation(StationDto stationDto){
		ValidateUtils.notNull(stationDto);
		ValidateUtils.notNull(stationDto.getId());
		validateAddress(stationDto);
		Station oldRecord = getStationById(stationDto.getId());
		if (oldRecord == null) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"station is null");
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

	private void validateAddress(StationDto stationDto) {
		Address address = stationDto.getAddress();
		if (address != null) {
			String addressDetail = address.buildAddressDetail();
			if (StringUtils.isNotEmpty(addressDetail)) {
				if (!StationValidator.isSpecialStr(addressDetail,StationValidator.RULE_REGEX_ADDRESS) || !StationValidator.isContainChinese(addressDetail)) {
					throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"地址不可含有特殊字符,并且最少一个汉字");
				}
			}
		}
	}

	@Override
	public int getStationCountByStationNum(String stationNum)
			 {
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
			 {
		ValidateUtils.notNull(stationId);
		Station rel = new Station();
		rel.setId(stationId);
		DomainUtils.beforeDelete(rel, operator);
		stationMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public List<Station> getTpStationsByName(StationCondition stationCondition){
		ValidateUtils.notNull(stationCondition);
		StationExtExample stationExtExample = new StationExtExample();
		stationExtExample.setName(stationCondition.getName());
		stationExtExample.setOrgIdPath(stationCondition.getOrgIdPath());
		stationExtExample.setStatus(stationCondition.getStationStatusEnum().getCode());
		stationExtExample.setPageSize(stationCondition.getPageSize());
		stationExtExample.setPageStart(stationCondition.getPageStart());
		return stationExtMapper.getTpStationsByName(stationExtExample);
	}
	
	@Override
    public Page<Station> getStations(StationCondition stationCondition) {
		ValidateUtils.notNull(stationCondition);

		StationExtExample stationExtExample = StationExtExampleConverter.convert(stationCondition);
		PageHelper.startPage(stationCondition.getPageStart(), stationCondition.getPageSize());
		return (Page<Station>) stationExtMapper.selectByExample(stationExtExample);
	}

	@Override
    public int getSameNameInProvinceCnt(String stationName, String province) {
        ValidateUtils.notNull(stationName);
        ValidateUtils.notNull(province);
        StationExample example = new StationExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andNameEqualTo(stationName);
        criteria.andProvinceEqualTo(province);
        criteria.andStatusNotIn(Lists.newArrayList(StationStatusEnum.QUIT.getCode(),StationStatusEnum.INVALID.getCode()));
        return ResultUtils.selectCount(stationMapper.selectByExample(example));
    }

	@Override
	public List<Station> getFenceStations(FenceStationQueryCondition fenceStationQueryCondition) {
		return stationExtMapper.getFenceStations(fenceStationQueryCondition);
	}

	@Override
	public List<Station> getFenceInitingStations(FenceInitingStationQueryCondition fenceStationQueryCondition) {
		return stationExtMapper.getFenceInitingStations(fenceStationQueryCondition);
	}
	
	@Override
	public void updateStationDeptByOrgId(Long orgId, OrgDeptType orgDeptType) {
		Station record = new Station();
		record.setOwnDept(orgDeptType.name());
		record.setGmtModified(new Date());
		StationExample example = new StationExample();
		example.createCriteria().andApplyOrgEqualTo(orgId);
		stationMapper.updateByExampleSelective(record, example);
	}

	@Override
	public void updateStationNum(Long stationId, String stationNum) {
		Station record = new Station();

		record.setId(stationId);
		record.setStationNum(stationNum);

		DomainUtils.beforeUpdate(record, "system");
		stationMapper.updateByPrimaryKeySelective(record);
	}
	@Override
	public int getServicingNumByOrgId(Long orgId) {
		return stationExtMapper.getServicingNumByOrg(orgId);
	}

	@Override
	public List<Station> getServicingStations(){
		return stationExtMapper.getServicingStations();
	}
}
