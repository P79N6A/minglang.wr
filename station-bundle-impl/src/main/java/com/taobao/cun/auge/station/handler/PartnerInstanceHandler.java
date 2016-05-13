package com.taobao.cun.auge.station.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.strategy.PartnerInstanceStrategy;
import com.taobao.cun.auge.station.strategy.TpStrategy;
import com.taobao.cun.auge.station.strategy.TpaStrategy;
import com.taobao.cun.auge.station.strategy.TpvStrategy;

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
	
	public Long handleApplySettle(PartnerInstanceCondition condition,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		return strategy.get(partnerInstanceTypeEnum.getCode()).applySettle(condition, partnerInstanceTypeEnum);
	}
	
	public void validateExistServiceChildren(PartnerInstanceTypeEnum partnerInstanceTypeEnum,Long instanceId)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).validateExistServiceChildren(instanceId);
	}
}
