package com.taobao.cun.auge.station.service.impl;

import com.taobao.cun.auge.station.bo.CountyStationBO;
import com.taobao.cun.auge.station.convert.CountyStationConverter;
import com.taobao.cun.auge.station.dto.CountyStationDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.CountyStationQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 县服务中心查询接口 实现类
 * @author quanzhu.wangqz
 *
 */
@Service("countyStationQueryService")
@HSFProvider(serviceInterface= CountyStationQueryService.class, clientTimeout = 8000)
public class CountyStationQueryServiceImpl implements CountyStationQueryService {
	
	private static final Logger logger = LoggerFactory.getLogger(CountyStationQueryService.class);
	
	@Autowired
	CountyStationBO countyStationBO;
	
	@Override
	public CountyStationDto getCountyStationDtoByOrgId(Long orgId)
			throws AugeServiceException {
		return CountyStationConverter.toCountyStationDto(countyStationBO.getCountyStationByOrgId(orgId));
	}

}
