package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceStrategy {

	public Long applySettle(PartnerInstanceDto partnerInstanceDto)throws AugeServiceException;
	
	public Long applySettleNewly(PartnerInstanceDto partnerInstanceDto)throws AugeServiceException;
	
	public void delete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,PartnerStationRel rel) throws AugeServiceException;
	
	public void validateExistValidChildren(Long instanceId)throws AugeServiceException ;
	
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType);
	
	public void quit(PartnerInstanceQuitDto partnerInstanceQuitDto)throws AugeServiceException;
}
