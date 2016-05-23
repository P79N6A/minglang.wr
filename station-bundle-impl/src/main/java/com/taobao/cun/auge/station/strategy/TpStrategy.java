package com.taobao.cun.auge.station.strategy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component("tpStrategy")
public class TpStrategy implements PartnerInstanceStrategy{
	

	private static final Logger logger = LoggerFactory.getLogger(TpStrategy.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Override
	public Long applySettle(ApplySettleDto applySettleDto, PartnerInstanceTypeEnum partnerInstanceTypeEnum)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static List<PartnerInstanceStateEnum>  getValidChildPartnersStatus(){
		ArrayList<PartnerInstanceStateEnum > listValidStatus = new ArrayList<PartnerInstanceStateEnum >();
		listValidStatus.add(PartnerInstanceStateEnum.TEMP);
		listValidStatus.add(PartnerInstanceStateEnum.SETTLING);
		listValidStatus.add(PartnerInstanceStateEnum.SERVICING);
		listValidStatus.add(PartnerInstanceStateEnum.CLOSING);
		return listValidStatus;
	}	
	

	@Override
	public void validateExistValidChildren(Long instanceId) throws AugeServiceException {
		int count = partnerInstanceBO.findChildPartners(instanceId, getValidChildPartnersStatus());
		if (count > 0) {
			logger.error(StationExceptionEnum.HAS_CHILDREN_TPA.getDesc());
			throw new AugeServiceException(StationExceptionEnum.HAS_CHILDREN_TPA);
		}
		
	}
	
	@Override
	public ProcessBusinessEnum findProcessBusiness(ProcessTypeEnum processType){
		if(ProcessTypeEnum.CLOSING_PRO.equals(processType)){
			return ProcessBusinessEnum.stationForcedClosure;
		}else if(ProcessTypeEnum.QUIT_PRO.equals(processType)){
			return ProcessBusinessEnum.stationQuitRecord;
		}
		return null;
	}
}
