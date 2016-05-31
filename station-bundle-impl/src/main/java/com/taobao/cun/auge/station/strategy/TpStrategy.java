package com.taobao.cun.auge.station.strategy;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("tpStrategy")
public class TpStrategy implements PartnerInstanceStrategy{
	

	private static final Logger logger = LoggerFactory.getLogger(TpStrategy.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	PartnerBO partnerBO;
	
	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		
		//构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
		partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
		return partnerInstanceDto.getId();
	}

	@Override
	public void validateExistValidChildren(Long instanceId) throws AugeServiceException {
		int count = partnerInstanceBO.findChildPartners(instanceId, PartnerInstanceStateEnum.getValidChildPartnersStatus());
		if (count > 0) {
			logger.error(StationExceptionEnum.HAS_CHILDREN_TPA.getDesc());
			throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
		}
	}
	
	@Override
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType){
		if(ProcessTypeEnum.CLOSING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationForcedClosure;
		}else if(ProcessTypeEnum.QUITING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationQuitRecord;
		}
		return null;
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,
			PartnerStationRel rel) throws AugeServiceException {
		
		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}
		if (partnerInstanceDeleteDto.getIsDeleteStation()) {
			Long stationId =  rel.getStationId();
			Station station = stationBO.getStationById(stationId);
			if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())) {
				throw new AugeServiceException(StationExceptionEnum.STATION_DELETE_FAIL);
			}
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
			
		}
		
		if (partnerInstanceDeleteDto.getIsDeletePartner()) {
			Long partnerId =  rel.getPartnerId();
			Partner partner = partnerBO.getPartnerById(partnerId);
			if (!StringUtils.equals(PartnerStateEnum.TEMP.getCode(), partner.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
			}
			partnerBO.deletePartner(partnerId, partnerInstanceDeleteDto.getOperator());
		}
		
		partnerInstanceBO.deletePartnerStationRel(rel.getId(), partnerInstanceDeleteDto.getOperator());
	}
}
