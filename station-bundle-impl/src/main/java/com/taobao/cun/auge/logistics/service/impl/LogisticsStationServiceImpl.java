package com.taobao.cun.auge.logistics.service.impl;

import java.util.List;

import javax.annotation.Resource;

import com.alibaba.fastjson.JSONObject;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRelExample;
import com.taobao.cun.auge.dal.domain.LogisticsStation;
import com.taobao.cun.auge.dal.domain.LogisticsStationExample;
import com.taobao.cun.auge.dal.mapper.CuntaoCainiaoStationRelMapper;
import com.taobao.cun.auge.dal.mapper.LogisticsStationMapper;
import com.taobao.cun.auge.logistics.convert.LogisticsStationConverter;
import com.taobao.cun.auge.logistics.dto.LogisticsStationDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationPageQueryDto;
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.auge.logistics.service.LogisticsStationService;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.common.exceptions.ServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("logisticsStationService")
@HSFProvider(serviceInterface= LogisticsStationService.class)
public class LogisticsStationServiceImpl implements LogisticsStationService {

	private static final Logger logger = LoggerFactory.getLogger(LogisticsStationServiceImpl.class);

	@Resource
	LogisticsStationBO logisticsStationBO;

	@Resource
	LogisticsStationMapper logisticsStationMapper;
	
	@Resource
	CuntaoCainiaoStationRelMapper cuntaoCainiaoStationRelMapper;
	@Override
	public Long addLogisticsStation(LogisticsStationDto stationDto) {
		return logisticsStationBO.addLogisticsStation(stationDto);
	}

	@Override
	public PageDto<LogisticsStationDto> findLogisticsStationByPage(LogisticsStationPageQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			throw new ServiceException("LogisticsStationQueryDto is null ");
		}
		return logisticsStationBO.findLogisticsStationByPage(condiDto);
	}

	@Override
	public LogisticsStationDto findLogisticsStation(LogisticsStationQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			throw new ServiceException("LogisticsStationQueryDto is null ");
		}

		try {
			return logisticsStationBO.findLogisticsStation(condiDto);
		} catch (Exception e) {
			String msg = "findLogisticsStation error,LogisticsStationQueryDto =" + JSONObject.toJSONString(condiDto);
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
	}

	@Override
	public Boolean deleteLogisticsStation(Long cainiaoStationId, String modifier) {
		if (cainiaoStationId == null) {
			throw new ServiceException("cainiaoStationId is null");
		}
		if (StringUtils.isEmpty(modifier)) {
			throw new ServiceException("modifier is null");
		}

		try {
			return logisticsStationBO.deleteLogisticsStation(cainiaoStationId, modifier);
		} catch (Exception e) {
			String msg = "findLogisticsStation error,cainiaoStationId =" + cainiaoStationId;
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
	}

	@Override
	public Boolean updateLogisticsStation(LogisticsStationDto stationDto) {
		ValidateUtils.validateParam(stationDto);
		try {
			return logisticsStationBO.updateLogisticsStation(stationDto);
		} catch (Exception e) {
			String msg = "findLogisticsStation error,LogisticsStationDto =" + JSONObject.toJSONString(stationDto);
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
	}

	@Override
	public LogisticsStationDto findLogisticsStation(Long cainiaoStationId) {
		LogisticsStationExample example = new LogisticsStationExample();
		example.createCriteria().andCainiaoStationIdEqualTo(cainiaoStationId);
		try {
			List<LogisticsStation>  results = logisticsStationMapper.selectByExample(example);
			if(results != null && !results.isEmpty()){
				return LogisticsStationConverter.convert(results.iterator().next());
			}
			return null;
		} catch (Exception e) {
			String msg = "findLogisticsStation error cainiaoStationId["+cainiaoStationId+"]";
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
	}

	@Override
	public LogisticsStationDto findLogisticsStationByStationId(Long stationId) {
		CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
		example.createCriteria().andObjectIdEqualTo(stationId).andTypeEqualTo("STATION").andIsDeletedEqualTo("n");
		try {
			List<CuntaoCainiaoStationRel> results = cuntaoCainiaoStationRelMapper.selectByExample(example);
			if(results != null && !results.isEmpty()){
				CuntaoCainiaoStationRel cainiaoStationRel = results.iterator().next();
				return LogisticsStationConverter.convert(logisticsStationMapper.selectByPrimaryKey(cainiaoStationRel.getLogisticsStationId()));
			}
			return null;
		} catch (Exception e) {
			String msg = "findLogisticsStationByStationId error stationId["+stationId+"]";
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
	}

	@Override
	public Long findStationIdByCainiaoStationId(Long cainiaoStationId) {
		LogisticsStationExample example = new LogisticsStationExample();
		example.createCriteria().andCainiaoStationIdEqualTo(cainiaoStationId).andIsDeletedEqualTo("n");
		try {
			List<LogisticsStation>  results = logisticsStationMapper.selectByExample(example);
			if(results != null && !results.isEmpty()){
				LogisticsStation logisticsStation = results.iterator().next();
				{
					CuntaoCainiaoStationRelExample relExample = new CuntaoCainiaoStationRelExample();
					relExample.createCriteria().andLogisticsStationIdEqualTo(logisticsStation.getId()).andIsDeletedEqualTo("n");
					List<CuntaoCainiaoStationRel> cainiaoStationRels = cuntaoCainiaoStationRelMapper.selectByExample(relExample);
					if(cainiaoStationRels != null && !cainiaoStationRels.isEmpty()){
						return cainiaoStationRels.iterator().next().getObjectId();
					}
				}
			}
		} catch (Exception e) {
			String msg = "findStationIdByCainiaoStationId error cainiaoStationId["+cainiaoStationId+"]";
			logger.error(msg, e);
			throw new ServiceException(msg,e);
		}
		return null;
	}
}
