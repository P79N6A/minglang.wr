package com.taobao.cun.auge.station.strategy;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;

@Component("tpvStrategy")
public class TpvStrategy implements PartnerInstanceStrategy {

	@Override
	public Long applySettle(ApplySettleDto applySettleDto,
			PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void validateExistValidChildren(Long instanceId) {
		// TODO Auto-generated method stub
		
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
}
