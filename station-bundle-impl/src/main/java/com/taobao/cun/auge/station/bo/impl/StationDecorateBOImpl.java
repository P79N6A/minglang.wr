package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.AppResource;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.dal.domain.StationDecorateExample;
import com.taobao.cun.auge.dal.domain.StationDecorateExample.Criteria;
import com.taobao.cun.auge.dal.mapper.StationDecorateMapper;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.service.CuntaoOrgService;
import com.taobao.cun.auge.org.service.OrgRangeType;
import com.taobao.cun.auge.station.bo.AppResourceBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.StationDecorateConverter;
import com.taobao.cun.auge.station.dto.AttachementDeleteDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;


@Component("stationDecorateBO")
public class StationDecorateBOImpl implements StationDecorateBO {
	
	private static final Logger logger = LoggerFactory.getLogger(StationDecorateBO.class);

	@Autowired
	StationDecorateMapper stationDecorateMapper;
	@Autowired
	AppResourceBO appResourceBO;
	@Autowired
	CuntaoOrgService cuntaoOrgService;
	@Autowired
	StationBO stationBO;
	@Autowired
	AttachementBO attachementBO;
	
	@Override
	public StationDecorate addStationDecorate(StationDecorateDto stationDecorateDto)
			throws AugeServiceException {
		ValidateUtils.notNull(stationDecorateDto);
		Long stationId = stationDecorateDto.getStationId();
		ValidateUtils.notNull(stationId);
		StationDecorate sd = this.getStationDecorateByStationId(stationId);
		if (sd != null) {
			return sd;
		}
		
		StationDecorate record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
		//添加店铺id
		if (record.getShopId() ==null) {
			record.setShopId(getShopId(stationId));
		}
		DomainUtils.beforeInsert(record, stationDecorateDto.getOperator());
		stationDecorateMapper.insert(record);
		return record;
	}
	
	private String getShopId(Long stationId) {
		Station station = stationBO.getStationById(stationId);
		if (station == null) {
			logger.error("stationBO.getStationById is null"+stationId);
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		
		Long largeAreaOrgId = getLargeAreaOrgId(station);
		return getShop(String.valueOf(largeAreaOrgId));
	}

	private Long getLargeAreaOrgId(Station station) {
		
		CuntaoOrgDto coDto = cuntaoOrgService.getCuntaoOrg(station.getApplyOrg());
		Long largeAreaOrgId = null;
		CuntaoOrgDto cunOrg = coDto;
		
		while(true){ 
			if(OrgRangeType.LARGE_AREA.type.equals(cunOrg.getOrgRangeType())){ 
				largeAreaOrgId = coDto.getId();
				break;
			}
			cunOrg = cunOrg.getParent();
			if (cunOrg == null) {
				break;
			}		
		}
		if (largeAreaOrgId == null) {
			logger.error("getCuntaoOrg error: stationId"+station.getId());
			throw new RuntimeException("getCuntaoOrg error: stationId"+station.getId());
		}
		return largeAreaOrgId;
	}
	
	private String getShop(String key) {
		AppResource resource = appResourceBO.queryAppResource("decorate_shop", key);
		if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
			return resource.getValue();
		}
		logger.error("getShop error: key"+key);
		throw new RuntimeException("getShop error: key"+key);
	}

	@Override
	public List<StationDecorateDto> getStationDecorateListForSchedule(int fetchNum)
			throws AugeServiceException {
		List<StationDecorateDto> returnList = new ArrayList<StationDecorateDto>();
		if (fetchNum < 0) {
			return returnList;
		}
		
		StationDecorateExample example = new StationDecorateExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		List<String> statusList = new ArrayList<String>();
		statusList.add(StationDecorateStatusEnum.UNDECORATE.getCode());
		statusList.add(StationDecorateStatusEnum.DECORATING.getCode());
		criteria.andStatusIn(statusList);
		
		PageHelper.startPage(1, fetchNum);
		List<StationDecorate> sdList = stationDecorateMapper.selectByExample(example);
	
		return StationDecorateConverter.toStationDecorateDtos(sdList);
	}

	@Override
	public void updateStationDecorate(StationDecorateDto stationDecorateDto)
			throws AugeServiceException {
		ValidateUtils.validateParam(stationDecorateDto);
		ValidateUtils.notNull(stationDecorateDto.getId());
		StationDecorate record = StationDecorateConverter.toStationDecorate(stationDecorateDto);
		DomainUtils.beforeUpdate(record, stationDecorateDto.getOperator());
		
		//更新附件
		if (stationDecorateDto.getAttachements() != null) {
			List<Long> attIds = attachementBO.modifyAttachementBatch(stationDecorateDto.getAttachements(), stationDecorateDto.getId(), AttachementBizTypeEnum.STATION_DECORATE, stationDecorateDto);
			
		}
		stationDecorateMapper.updateByPrimaryKeySelective(record);
	}
	public static void main(String[] args) {
		List<Long> aaa =new ArrayList<Long>();
		aaa.add(1l);
		aaa.add(1l);
		aaa.add(1l);
		aaa.add(1l);
		StringBuilder sb = new StringBuilder();
		for (Long a :aaa) {
			sb.append(a).append(":");
		}
		System.out.println(sb.toString());
	}
	

	@Override
	public StationDecorateDto getStationDecorateDtoByStationId(Long stationId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateStatus(Long id, StationDecorateStatusEnum statusEnum,
			OperatorDto operatorDto) throws AugeServiceException {
		// TODO Auto-generated method stub

	}

	@Override
	public StationDecorate getStationDecorateByStationId(Long stationId)
			throws AugeServiceException {
		return null;
	}

}
