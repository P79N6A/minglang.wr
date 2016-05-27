package com.taobao.cun.auge.station.strategy;

import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

public interface PartnerInstanceStrategy {

	public Long applySettle(PartnerInstanceDto partnerInstanceDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException;
	
	public void validateExistValidChildren(Long instanceId)throws AugeServiceException ;
	
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType);
}
