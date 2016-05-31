package com.taobao.cun.auge.station.strategy;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.bo.LogisticsStationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleLogisticsApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("tpvStrategy")
public class TpvStrategy implements PartnerInstanceStrategy {
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	StationBO stationBO;
	
	@Autowired
	PartnerBO partnerBO;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	@Autowired
	LogisticsStationBO logisticsStationBO;
	
	@Override
	public Long applySettle(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		///构建入驻生命周期
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPV);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setLogisticsApprove(PartnerLifecycleLogisticsApproveEnum.TO_AUDIT);
		partnerLifecycleDto.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_AUDIT);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.ROLE_APPROVE);
		
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
		return partnerInstanceDto.getId();
	}

	@Override
	public void validateExistValidChildren(Long instanceId) {
		
	}
	
	@Override
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType){
		if(ProcessTypeEnum.CLOSING_PRO.equals(processType)){
			return ProcessBusinessEnum.TPV_FORCED_CLOSURE;
		}else if(ProcessTypeEnum.QUITING_PRO.equals(processType)){
			return ProcessBusinessEnum.TPV_QUIT;
		}
		return null;
	}

	@Override
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,
			PartnerStationRel rel) throws AugeServiceException {
		
		if (!StringUtils.equals(PartnerInstanceStateEnum.TEMP.getCode(), rel.getState()) 
				&& !StringUtils.equals(PartnerInstanceStateEnum.SETTLE_FAIL.getCode(), rel.getState())) {
			throw new AugeServiceException(PartnerExceptionEnum.PARTNER_DELETE_FAIL);
		}
		if (partnerInstanceDeleteDto.getIsDeleteStation()) {
			Long stationId =  rel.getStationId();
			Station station = stationBO.getStationById(stationId);
			if (!StringUtils.equals(StationStatusEnum.TEMP.getCode(), station.getStatus())
					 && !StringUtils.equals(StationStatusEnum.INVALID.getCode(), rel.getState())) {
				throw new AugeServiceException(StationExceptionEnum.STATION_DELETE_FAIL);
			}
			stationBO.deleteStation(stationId, partnerInstanceDeleteDto.getOperator());
			
			//删除物流表
			CuntaoCainiaoStationRel csRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			if (csRel != null) {
				Long logisId = csRel.getLogisticsStationId();
				if (logisId != null) {
					logisticsStationBO.delete(logisId, partnerInstanceDeleteDto.getOperator());
				}
				cuntaoCainiaoStationRelBO.deleteCuntaoCainiaoStationRel(stationId, CuntaoCainiaoStationRelTypeEnum.STATION);
			}
			
			
			
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
		partnerLifecycleBO.deleteLifecycleItems(rel.getId(), partnerInstanceDeleteDto.getOperator());
	}
}
