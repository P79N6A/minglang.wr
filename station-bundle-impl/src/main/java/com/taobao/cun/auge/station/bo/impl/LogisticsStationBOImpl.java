package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

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
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.LogisticsStation;
import com.taobao.cun.auge.dal.domain.LogisticsStationExample;
import com.taobao.cun.auge.dal.domain.LogisticsStationExample.Criteria;
import com.taobao.cun.auge.dal.mapper.LogisticsStationMapper;
import com.taobao.cun.auge.logistics.convert.LogisticsStationConverter;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.auge.logistics.enums.LogisticsStationStateEnum;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.common.exceptions.ServiceException;

@Component("logisticsStationBO")
public class LogisticsStationBOImpl implements LogisticsStationBO {
	private static final Logger logger = LoggerFactory.getLogger(LogisticsStationBO.class);
	@Autowired
	LogisticsStationMapper logisticsStationMapper;
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void delete(Long id, String operator)  {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(operator);
		LogisticsStation rel = new LogisticsStation();
		rel.setId(id);
		DomainUtils.beforeDelete(rel, operator);
		logisticsStationMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public void changeState(Long id, String operator,String targetState)  {
		ValidateUtils.notNull(id);
		ValidateUtils.notNull(operator);
		ValidateUtils.notNull(targetState);
		LogisticsStation rel = new LogisticsStation();
		rel.setId(id);
		rel.setState(targetState);
		DomainUtils.beforeUpdate(rel, operator);
		logisticsStationMapper.updateByPrimaryKeySelective(rel);
	}
	
	@Override
	public Long addLogisticsStation(LogisticsStationDto stationDto) {
		ValidateUtils.validateParam(stationDto);

		LogisticsStation station = LogisticsStationConverter.convert(stationDto);
		DomainUtils.beforeInsert(station, stationDto.getOperator());
		
		logisticsStationMapper.insert(station);

		return station.getId();
	}

	@Override
	public Boolean deleteLogisticsStation(Long cainiaoStationId, String modifier) {
		if (cainiaoStationId == null) {
			throw new ServiceException("cainiaoStationId is null");
		}
		if (StringUtils.isEmpty(modifier)) {
			throw new ServiceException("modifier is null");
		}

		LogisticsStation station = new LogisticsStation();
		DomainUtils.beforeDelete(station, modifier);

		LogisticsStationExample example = new LogisticsStationExample();
		Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
		criteria.andCainiaoStationIdEqualTo(cainiaoStationId);

		logisticsStationMapper.updateByExampleSelective(station, example);

		return Boolean.TRUE;
	}

	@Override
	public Boolean updateLogisticsStation(LogisticsStationDto stationDto) {
		ValidateUtils.validateParam(stationDto);
//		if (null == stationDto.getCainiaoStationId()) {
//			logger.error("cainiao station id is null");
//			throw new ServiceException("cainiao station id is null");
//		}
		LogisticsStation station = LogisticsStationConverter.convert(stationDto);
		DomainUtils.beforeUpdate(station, stationDto.getOperator());
		if(station.getId() != null && station.getId() != 0L){
			logisticsStationMapper.updateByPrimaryKeySelective(station);
		}else{
			LogisticsStationExample example = new LogisticsStationExample();
			Criteria criteria = example.createCriteria().andIsDeletedEqualTo("n");
			criteria.andTaobaoUserIdEqualTo(stationDto.getTaobaoUserId());
			logisticsStationMapper.updateByExampleSelective(station,example);
		}
		return Boolean.TRUE;
	}

	@Override
	public LogisticsStationDto findLogisticsStation(LogisticsStationQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			throw new ServiceException("LogisticsStationQueryDto is null ");
		}

		LogisticsStationExample example = LogisticsStationConverter.convert2Example(condiDto);

		List<LogisticsStation> stations = logisticsStationMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(stations)) {
			return null;
		}
		return LogisticsStationConverter.convert(stations.get(0));
	}

	@Override
	public PageDto<LogisticsStationDto> findLogisticsStationByPage(LogisticsStationQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			throw new ServiceException("LogisticsStationQueryDto is null ");
		}

		LogisticsStationExample example = LogisticsStationConverter.convert2Example(condiDto);
		example.setOrderByClause("gmt_create DESC");

		PageHelper.startPage(condiDto.getPageIndex(), condiDto.getPageSize());
		Page<LogisticsStation> stations = (Page<LogisticsStation>)logisticsStationMapper.selectByExample(example);
        PageDto<LogisticsStationDto> success = PageDtoUtil.success(stations, LogisticsStationConverter.convert(stations));
        return success;
	}

	@Override
	public LogisticsStationDto findToAuditLogisticsStationByTaobaoUserId(Long taobaoUserId) {
		LogisticsStationExample example = new LogisticsStationExample();
		example.createCriteria().andTaobaoUserIdEqualTo(taobaoUserId).andStateEqualTo(LogisticsStationStateEnum.TO_AUDIT.getCode()).andIsDeletedEqualTo("n");
		List<LogisticsStation> logisticsStationList =  logisticsStationMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(logisticsStationList)){
			throw new ServiceException("不存在带审核的物流站点");
		}
		
		return  LogisticsStationConverter.convert(logisticsStationList.iterator().next());
	}

	@Override
	public LogisticsStationDto findLogisticStation(Long id) {
		LogisticsStation logiscticStation = logisticsStationMapper.selectByPrimaryKey(id);
		if(logiscticStation != null){
			return  LogisticsStationConverter.convert(logiscticStation);
		}
		return null;
	}

}
