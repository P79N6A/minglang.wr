package com.taobao.cun.auge.station.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.dto.ApplySettleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.strategy.PartnerInstanceStrategy;
import com.taobao.cun.auge.station.strategy.TpStrategy;
import com.taobao.cun.auge.station.strategy.TpaStrategy;
import com.taobao.cun.auge.station.strategy.TpvStrategy;

@Component("PartnerInstanceHandler")
public class PartnerInstanceHandler implements InitializingBean{
	
	@Autowired
	private TpaStrategy tpaStrategy;
	@Autowired
	private TpvStrategy tpvStrategy;
	@Autowired
	private TpStrategy tpStrategy;
	
	Map<String,PartnerInstanceStrategy> strategy = new HashMap<String,PartnerInstanceStrategy>();
	@Override
	public void afterPropertiesSet() throws Exception {
		strategy.put(PartnerInstanceTypeEnum.TPA.getCode(), tpaStrategy);
		strategy.put(PartnerInstanceTypeEnum.TPV.getCode(), tpvStrategy);
		strategy.put(PartnerInstanceTypeEnum.TP.getCode(), tpStrategy);
	}
	
	public Long handleApplySettle(ApplySettleDto applySettleDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		return strategy.get(partnerInstanceTypeEnum.getCode()).applySettle(applySettleDto, partnerInstanceTypeEnum);
	}
	
	public void validateExistValidChildren(PartnerInstanceTypeEnum partnerInstanceTypeEnum,Long instanceId)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).validateExistValidChildren(instanceId);
	}
	
	public ProcessBusinessEnum findProcessBusiness(PartnerInstanceTypeEnum partnerInstanceTypeEnum,ProcessTypeEnum processType){
		return strategy.get(partnerInstanceTypeEnum.getCode()).findProcessBusiness(processType);
	}
}
