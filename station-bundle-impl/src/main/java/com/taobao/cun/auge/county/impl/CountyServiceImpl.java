package com.taobao.cun.auge.county.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.county.CountyService;
import com.taobao.cun.auge.county.bo.CountyBO;
import com.taobao.cun.auge.county.dto.CountyDto;
import com.taobao.cun.auge.county.dto.CountyStationQueryCondition;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.bops.mobile.convert.CountyAddressConvert;
import com.taobao.cun.bops.mobile.dto.county.CountyDetailDto;
import com.taobao.cun.dto.station.CountyStationDto;
import com.taobao.cun.settle.common.model.PagedResultModel;
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

	public CountyDto getCountyStation(Long id,Boolean isMobile){
		CountyDto dto= countyBO.getCountyStation(id);
		if(isMobile){
			return convertForMobile(dto);
		}else{
			return dto;
		}
	}

	public CountyDto getCountyStationByOrgId(Long id){
		return countyBO.getCountyStationByOrgId(id);
	}
	
	public PagedResultModel<List<CountyDto>> getCountyStationList(CountyStationQueryCondition queryCondition){
		return countyBO.getCountyStationList(queryCondition); 
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
		return countyDto;
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
}
