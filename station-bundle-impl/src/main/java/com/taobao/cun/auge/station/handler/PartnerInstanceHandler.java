package com.taobao.cun.auge.station.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.strategy.PartnerInstanceStrategy;
import com.taobao.cun.auge.station.strategy.TpStrategy;
import com.taobao.cun.auge.station.strategy.TpaStrategy;
import com.taobao.cun.auge.station.strategy.TpvStrategy;

@Component("partnerInstanceHandler")
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
	
	/**
	 * 申请入驻
	 * @param instanceDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	public void handleApplySettle(PartnerInstanceDto instanceDto,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).applySettle(instanceDto);
	}
	
	/**
	 * 申请退出
	 * @param quitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	public void handleApplyQuit(QuitStationApplyDto quitDto,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).applyQuit(quitDto,typeEnum);
	}
	
	/**
	 * 审核退出
	 * @param quitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	public void handleAuditQuit(Boolean isAgree,Long instanceId,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).auditQuit(isAgree,instanceId);
	}
	
	/**
	 * 正式退出
	 * @param partnerInstanceQuitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	public void handleQuit(PartnerInstanceQuitDto partnerInstanceQuitDto,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).quit(partnerInstanceQuitDto);
	}
	
	public void handleApplySettleNewly(PartnerInstanceDto instanceDto,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).applySettleNewly(instanceDto);
	}
	
	
	/**
	 * 实例删除
	 * @param deleteDto
	 * @param rel
	 * @throws AugeServiceException
	 */
	public void handleDelete(PartnerInstanceDeleteDto deleteDto,PartnerStationRel rel)throws AugeServiceException {
		strategy.get(rel.getType()).delete(deleteDto, rel);
	}
	
	public void validateExistValidChildren(PartnerInstanceTypeEnum typeEnum,Long instanceId)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).validateExistValidChildren(instanceId);
	}
	
	public ProcessBusinessEnum findProcessBusiness(PartnerInstanceTypeEnum typeEnum,ProcessTypeEnum processType){
		return strategy.get(typeEnum.getCode()).findProcessBusiness(processType);
	}

	public void handleSettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel) {
		strategy.get(rel.getType()).settleSuccess(settleSuccessDto,rel);
		
	}
}
