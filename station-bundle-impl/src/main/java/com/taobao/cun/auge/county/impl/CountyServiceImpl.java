package com.taobao.cun.auge.county.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.county.CountyService;
import com.taobao.cun.auge.county.bo.CountyBO;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.cun.dto.BucContext;
import com.taobao.cun.dto.station.CountyStationDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("CountyService")
@HSFProvider(serviceInterface = CountyService.class)
public class CountyServiceImpl implements CountyService{

	private static final Logger logger = LoggerFactory.getLogger(CountyServiceImpl.class);

	@Autowired
	CountyBO countyBO;
	
	@Override
	public CountyDto saveCountyStation(String operator,CountyDto countyDto) {
		logger.info("saveCountyStation"+JSON.toJSONString(countyDto));
		try {
			CountyDto rst = countyBO.saveCountyStation(operator,countyDto);
			return rst;
		} catch (Exception e){
			logger.error("保存县点失败："+JSON.toJSONString(countyDto),e);
			throw new AugeBusinessException("保存县点失败："+e);
		}
	}

	@Override
	public List<CountyDto> getProvinceList(List<Long> areaOrgIds) {
		return countyBO.getProvinceList(areaOrgIds);
	}
	
	public List<CountyDto> getCountyStationByProvince(String provinceCode){
		return countyBO.getCountyStationByProvince(provinceCode);
	}

	public List<CountyDto> getCountyStationList(List<Long> areaIds){
		return countyBO.getCountyStationList(areaIds);
	}

	public CountyDto getCountyStation(Long id){
		return countyBO.getCountyStation(id);
	}

	public CountyDto getCountyStationByOrgId(Long id){
		return countyBO.getCountyStationByOrgId(id);
	}
}
