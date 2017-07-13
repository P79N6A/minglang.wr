package com.taobao.cun.auge.county.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PositionUtil;
import com.taobao.cun.auge.county.CountyService;
import com.taobao.cun.auge.county.bo.CountyBO;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyPOI;
import com.taobao.cun.auge.county.dto.CountyQueryCondition;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.auge.dal.domain.CountyStation;
import com.taobao.cun.auge.dal.domain.CountyStationExample;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.mapper.CountyStationMapper;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
@Service("CountyService")
@HSFProvider(serviceInterface = CountyService.class)
public class CountyServiceImpl implements CountyService{

	private static final Logger logger = LoggerFactory.getLogger(CountyServiceImpl.class);

	@Autowired
	CountyBO countyBO;
	
	@Autowired
	CountyStationMapper countyStationMapper;
	
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	@Override
	public CountyDto saveCountyStation(String operator,CountyDto countyDto) {
		logger.info("saveCountyStation"+JSON.toJSONString(countyDto));
			CountyDto rst = countyBO.saveCountyStation(operator,countyDto);
			return rst;
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

	public CountyDto getCountyStation(Long id,Boolean isMobile){
		CountyDto dto= countyBO.getCountyStation(id);
		if(isMobile){
			return convertForMobile(dto);
		}else{
			return dto;
		}
	}

	public List<Long> getCountiesByOrgId(List<Long> orgIds){
		List<CountyDto> countyDtos = countyBO.getCountyStationByOrgIds(orgIds);
		List<Long> counties = new ArrayList<Long>(countyDtos.size());
		for(CountyDto countyDto:countyDtos){
			if(null == countyDto){
				continue;
			}
			if (StringUtil.isNotEmpty(countyDto.getCounty())){
				counties.add(Long.valueOf(countyDto.getCounty()));
			}
		}
		return counties;
	}

	public CountyDto getCountyStationByOrgId(Long id){
		return countyBO.getCountyStationByOrgId(id);
	}
	
	public PageDto<CountyDto> getCountyStationList(CountyStationQueryCondition queryCondition){
		PageDto<CountyDto> result=countyBO.getCountyStationList(queryCondition);
		if(queryCondition.isMobile()){
			List<CountyDto> dtos=new ArrayList<CountyDto>();
			for(CountyDto dto:result.getItems()){
				dtos.add(convertForMobile(dto));
			}
			result.setItems(dtos);
		}
		return result;
	}
	
	private  CountyDto convertForMobile(CountyDto countyDto) {
		if (null == countyDto) {
			return null;
		}
		CountyDto returnDto = new CountyDto();

		returnDto.setId(countyDto.getId());
		returnDto.setName(countyDto.getName());
		returnDto.setEmployeeId(countyDto.getEmployeeId());
		// 地址合并
		returnDto.setAddressDetail(convertAddress(countyDto));
		returnDto.setManageStatus(countyDto.getManageStatus());
		returnDto.setCreateTime(countyDto.getCreateTime());
		returnDto.setParentId(countyDto.getParentId());
		returnDto.setParentName(countyDto.getParentName());
		returnDto.setLogisticsOperator(countyDto.getLogisticsOperator());
		returnDto.setLogisticsPhone(countyDto.getLogisticsPhone());
		returnDto.setAcreage(countyDto.getAcreage());
		returnDto.setWarehouseNum(countyDto.getWarehouseNum());
		returnDto.setLeasingModel(countyDto.getLeasingModel());
		returnDto.setOfficeDetail(countyDto.getOfficeDetail());
		returnDto.setFreeDeadline(countyDto.getFreeDeadline());
		returnDto.setSelfCosts(countyDto.getSelfCosts());
		returnDto.setEmployeeName(countyDto.getEmployeeName());
		return returnDto;
	}

	private String convertAddress(CountyDto countyDto) {
		if (null == countyDto) {
			return "";
		} else {
			StringBuilder address = new StringBuilder();
			// 省
			if (StringUtil.isNotEmpty(countyDto.getProvinceDetail())) {
				address.append(countyDto.getProvinceDetail());
			}
			// 市
			if (StringUtil.isNotEmpty(countyDto.getCityDetail())) {
				address.append(countyDto.getCityDetail());
			}
			// 县
			if (StringUtil.isNotEmpty(countyDto.getCountyDetail())) {
				address.append(countyDto.getCountyDetail());
			}
			// 镇
			if (StringUtil.isNotEmpty(countyDto.getTownDetail())) {
				address.append(countyDto.getTownDetail());
			}
			// 详情
			if (StringUtil.isNotEmpty(countyDto.getAddressDetail())) {
				address.append(countyDto.getAddressDetail());
			}
			return address.toString();
		}
	}

	@Override
	public PageDto<CountyDto> queryCountyStation(
			CountyQueryCondition queryCondition) {
		return countyBO.queryCountyStation(queryCondition);
	}
	
	public CountyDto startOperate(String operator,CountyDto countyDto){
		logger.info("startOperate"+JSON.toJSONString(countyDto));
			CountyDto rst = countyBO.startOperate(operator,countyDto);
			return rst;
	}

	@Override
	public CountyPOI queryCountyPOI(Long countyAreaId) {
		CountyPOI poi = new CountyPOI();
		CountyStationExample example= new CountyStationExample();
		example.createCriteria().andCountyEqualTo(countyAreaId.toString()).andIsDeletedEqualTo("n");
		List<CountyStation> countyStations = countyStationMapper.selectByExample(example);
		CountyStation countyStation  = null;
		//海南等地区，直接省管理县所以没有county编码，所以按市来查询
		if(CollectionUtils.isEmpty(countyStations)){
			example.clear();
			example.createCriteria().andCityEqualTo(countyAreaId.toString()).andIsDeletedEqualTo("n");
			countyStations = countyStationMapper.selectByExample(example);
			if(CollectionUtils.isEmpty(countyStations)){
				throw new AugeBusinessException("can not find county by areaId["+countyAreaId+"]");
			}
		}
		countyStation = countyStations.iterator().next();
		fixPOI(countyStation);
		CuntaoCainiaoStationRel rel =	cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(countyStation.getId(), CuntaoCainiaoStationRelTypeEnum.COUNTY_STATION);
		poi.setLat(countyStation.getLat());
		poi.setLng(countyStation.getLng());
		poi.setCainaoStationId(rel.getCainiaoStationId());
		return poi;
	}
	
	
	private void fixPOI(CountyStation countyStation) {
		if(StringUtils.isEmpty(countyStation.getLat())||StringUtils.isEmpty(countyStation.getLng())){
			String lastDivisionId = "";
			if (StringUtils.isNotBlank(countyStation.getTown())&& !"0".equals(countyStation.getTown())) {
				lastDivisionId = countyStation.getTown();
			} else if (StringUtils.isNotBlank(countyStation.getCounty())) {
				lastDivisionId = countyStation.getCounty();
			} else if (StringUtils.isNotBlank(countyStation.getCity())) {
				lastDivisionId = countyStation.getCity();
			} else if (StringUtils.isNotBlank(countyStation.getProvince())) {
				lastDivisionId = countyStation.getProvince();
			}
			Map<String, String> map = findLatitude(lastDivisionId, StringUtils.trim(countyStation.getAddressDetail()));
			String lng = map.get("lng");
			String lat = map.get("lat");
			CountyStation county = new CountyStation();
			county.setId(countyStation.getId());
			county.setLat(PositionUtil.converUp(lat));
			county.setLng(PositionUtil.converUp(lng));
			countyStation.setLat(PositionUtil.converUp(lat));
			countyStation.setLng(PositionUtil.converUp(lng));
			countyStationMapper.updateByPrimaryKeySelective(county);
		}
	}
	
	public static Map<String, String> findLatitude(String lastDivisionId, String addressDetail) {
		if (StringUtil.isEmpty(lastDivisionId)) {
			return Collections.<String, String> emptyMap();
		}
		try {
			StringBuilder url = new StringBuilder("http://lsp.wuliu.taobao.com/locationservice/addr/geo_coding.do?");
			url.append("lastDivisionId=").append(lastDivisionId).append("&addr=").append(addressDetail);
			HttpClient httpCLient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url.toString());
			HttpResponse response = httpCLient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String answer = EntityUtils.toString(entity);
			String json = answer.split("=")[1];
			return (Map<String, String>) JSON.parse(json.split("}")[0] + "}");
		} catch (Exception e) {
			logger.error("lastDivisionId = " + lastDivisionId + " ,addressDetail = " + addressDetail, e);
			return Collections.<String, String> emptyMap();
		}
	}
}
