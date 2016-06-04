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
	 * @param partnerInstanceDto
	 * @param partnerInstanceTypeEnum
	 * @throws AugeServiceException
	 */
	public void handleApplySettle(PartnerInstanceDto partnerInstanceDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).applySettle(partnerInstanceDto);
	}
	
	/**
	 * 申请退出
	 * @param quitDto
	 * @param partnerInstanceTypeEnum
	 * @throws AugeServiceException
	 */
	public void handleApplyQuit(QuitStationApplyDto quitDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).applyQuit(quitDto,partnerInstanceTypeEnum);
	}
	
	/**
	 * 审核退出
	 * @param quitDto
	 * @param partnerInstanceTypeEnum
	 * @throws AugeServiceException
	 */
	public void handleAuditQuit(Boolean isAgree,Long partnerInstanceId,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).auditQuit(isAgree,partnerInstanceId);
	}
	
	/**
	 * 正式退出
	 * @param partnerInstanceQuitDto
	 * @param partnerInstanceTypeEnum
	 * @throws AugeServiceException
	 */
	public void handleQuit(PartnerInstanceQuitDto partnerInstanceQuitDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).quit(partnerInstanceQuitDto);
	}
	
	public void handleApplySettleNewly(PartnerInstanceDto partnerInstanceDto,PartnerInstanceTypeEnum partnerInstanceTypeEnum)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).applySettleNewly(partnerInstanceDto);
	}
	
	
	/**
	 * 实例删除
	 * @param partnerInstanceDeleteDto
	 * @param rel
	 * @throws AugeServiceException
	 */
	public void handleDelete(PartnerInstanceDeleteDto partnerInstanceDeleteDto,PartnerStationRel rel)throws AugeServiceException {
		strategy.get(rel.getType()).delete(partnerInstanceDeleteDto, rel);
	}
	
	public void validateExistValidChildren(PartnerInstanceTypeEnum partnerInstanceTypeEnum,Long instanceId)throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).validateExistValidChildren(instanceId);
	}
	
	public ProcessBusinessEnum findProcessBusiness(PartnerInstanceTypeEnum partnerInstanceTypeEnum,ProcessTypeEnum processType){
		return strategy.get(partnerInstanceTypeEnum.getCode()).findProcessBusiness(processType);
	}
}
