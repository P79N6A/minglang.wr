package com.taobao.cun.auge.logistics.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
import com.taobao.cun.auge.logistics.dto.LogisticsStationQueryDto;
import com.taobao.cun.auge.logistics.service.LogisticsStationService;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.common.exceptions.ServiceException;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.common.resultmodel.ResultModelUtil;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

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
	public ResultModel<Long> addLogisticsStation(LogisticsStationDto stationDto) {
		return ResultModelUtil.success(logisticsStationBO.addLogisticsStation(stationDto));
	}

	@Override
	public PageDto<LogisticsStationDto> findLogisticsStationByPage(LogisticsStationQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			throw new ServiceException("LogisticsStationQueryDto is null ");
		}
		return logisticsStationBO.findLogisticsStationByPage(condiDto);
	}

	@Override
	public ResultModel<LogisticsStationDto> findLogisticsStation(LogisticsStationQueryDto condiDto) {
		if (null == condiDto) {
			logger.error("LogisticsStationQueryDto is null ");
			return ResultModelUtil.unSucceed(new ServiceException("LogisticsStationQueryDto is null "));
		}

		try {
			LogisticsStationDto stationDto = logisticsStationBO.findLogisticsStation(condiDto);

			return ResultModelUtil.success(stationDto);
		} catch (Exception e) {
			String msg = "findLogisticsStation error,LogisticsStationQueryDto =" + JSONObject.toJSONString(condiDto);
			logger.error(msg, e);
			return ResultModelUtil.unSucceed(e);
		}
	}

	@Override
	public ResultModel<Boolean> deleteLogisticsStation(Long cainiaoStationId, String modifier) {
		if (cainiaoStationId == null) {
			return ResultModelUtil.unSucceed(new ServiceException("cainiaoStationId is null"));
		}
		if (StringUtils.isEmpty(modifier)) {
			return ResultModelUtil.unSucceed(new ServiceException("modifier is null"));
		}

		try {
			return ResultModelUtil.success(logisticsStationBO.deleteLogisticsStation(cainiaoStationId, modifier));
		} catch (Exception e) {
			String msg = "findLogisticsStation error,cainiaoStationId =" + cainiaoStationId;
			logger.error(msg, e);
			return ResultModelUtil.unSucceed(e);
		}
	}

	@Override
	public ResultModel<Boolean> updateLogisticsStation(LogisticsStationDto stationDto) {
		ValidateUtils.validateParam(stationDto);
		try {
			return ResultModelUtil.success(logisticsStationBO.updateLogisticsStation(stationDto));
		} catch (Exception e) {
			String msg = "findLogisticsStation error,LogisticsStationDto =" + JSONObject.toJSONString(stationDto);
			logger.error(msg, e);
			return ResultModelUtil.unSucceed(e);
		}
	}

	@Override
	public ResultModel<LogisticsStationDto> findLogisticsStation(Long cainiaoStationId) {
		LogisticsStationExample example = new LogisticsStationExample();
		example.createCriteria().andCainiaoStationIdEqualTo(cainiaoStationId);
		try {
			List<LogisticsStation>  results = logisticsStationMapper.selectByExample(example);
			if(results != null && !results.isEmpty()){
				return ResultModelUtil.success(LogisticsStationConverter.convert(results.iterator().next()));
			}
		} catch (Exception e) {
			logger.error("findLogisticsStation error cainiaoStationId["+cainiaoStationId+"]", e);
			return ResultModelUtil.unSucceed(e);
		}
		return ResultModelUtil.unSucceed("LogisticsStationDto not exist by cainiaoStationId["+cainiaoStationId+"]");
	}

	@Override
	public ResultModel<LogisticsStationDto> findLogisticsStationByStationId(Long stationId) {
		CuntaoCainiaoStationRelExample example = new CuntaoCainiaoStationRelExample();
		example.createCriteria().andObjectIdEqualTo(stationId).andTypeEqualTo("STATION").andIsDeletedEqualTo("n");
		try {
			List<CuntaoCainiaoStationRel> results = cuntaoCainiaoStationRelMapper.selectByExample(example);
			if(results != null && !results.isEmpty()){
				CuntaoCainiaoStationRel cainiaoStationRel = results.iterator().next();
				return ResultModelUtil.success(LogisticsStationConverter.convert(logisticsStationMapper.selectByPrimaryKey(cainiaoStationRel.getLogisticsStationId())));
			}
		} catch (Exception e) {
			logger.error("findLogisticsStationByStationId error stationId["+stationId+"]", e);
			return ResultModelUtil.unSucceed(e);
		}
		return ResultModelUtil.unSucceed("LogisticsStationDto not exist by stationId["+stationId+"]");
	}

	@Override
	public ResultModel<Long> findStationIdByCainiaoStationId(Long cainiaoStationId) {
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
						return ResultModelUtil.success(cainiaoStationRels.iterator().next().getObjectId());
					}
				}
			}
		} catch (Exception e) {
			logger.error("findStationIdByCainiaoStationId error cainiaoStationId["+cainiaoStationId+"]", e);
			return ResultModelUtil.unSucceed(e);
		}
		return ResultModelUtil.unSucceed("stationId not exist by cainiaoStationId["+cainiaoStationId+"]");
	}
}
