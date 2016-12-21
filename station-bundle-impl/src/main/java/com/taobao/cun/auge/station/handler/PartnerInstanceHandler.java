package com.taobao.cun.auge.station.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.dto.PartnerInstanceDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
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
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleApplySettle(PartnerInstanceDto instanceDto,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).applySettle(instanceDto);
	}
	
	/**
	 * 入驻中编辑，只能在生命周期待办任务,没有开始的时候编辑
	 * 
	 * @param instanceDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean handleValidateUpdateSettle(Long instanceId,PartnerInstanceTypeEnum typeEnum)throws AugeServiceException {
		return strategy.get(typeEnum.getCode()).validateUpdateSettle(instanceId);
	}
	
	/**
	 * 申请退出
	 * 
	 * @param quitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDifferQuiting(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum)
			throws AugeServiceException {
		strategy.get(typeEnum.getCode()).applyQuit(quitDto, typeEnum);
	}

	/**
	 * 审核退出
	 * 
	 * @param partnerInstanceId
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDifferQuitAuditPass(Long partnerInstanceId,
			PartnerInstanceTypeEnum partnerInstanceTypeEnum) throws AugeServiceException {
		strategy.get(partnerInstanceTypeEnum.getCode()).handleDifferQuitAuditPass(partnerInstanceId);
	}
	
	/**
	 * 正式退出
	 * @param partnerInstanceQuitDto
	 * @param typeEnum
	 * @throws AugeServiceException
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
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
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDelete(PartnerInstanceDeleteDto deleteDto,PartnerStationRel rel)throws AugeServiceException {
		strategy.get(rel.getType()).delete(deleteDto, rel);
	}
	
	public void validateExistChildrenForQuit(PartnerInstanceTypeEnum typeEnum,Long instanceId) {
		strategy.get(typeEnum.getCode()).validateExistChildrenForQuit(instanceId);
	}
	
	public void validateClosePreCondition(PartnerInstanceTypeEnum typeEnum,PartnerStationRel partnerStationRel) {
		strategy.get(typeEnum.getCode()).validateClosePreCondition(partnerStationRel);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleSettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel) {
		strategy.get(rel.getType()).settleSuccess(settleSuccessDto,rel);
	}
	
	public void startClosing(Long instanceId, String stationName, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).startClosing(instanceId, stationName, operatorDto);
	}
	
	public void startQuiting(Long instanceId, String stationName, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto)throws AugeServiceException {
		strategy.get(typeEnum.getCode()).startQuiting(instanceId, stationName, operatorDto);
	}

	public void validateAssetBack(PartnerInstanceTypeEnum typeEnum, Long instanceId) {
		strategy.get(typeEnum.getCode()).validateAssetBack(instanceId);
	}

	public void validateOtherPartnerQuit(PartnerInstanceTypeEnum typeEnum, Long instanceId) {
		strategy.get(typeEnum.getCode()).validateOtherPartnerQuit(instanceId);
	}
	
	public void startService(Long instanceId, Long taobaoUserId, PartnerInstanceTypeEnum typeEnum,
			OperatorDto operatorDto) throws AugeServiceException {
		strategy.get(typeEnum.getCode()).startService(instanceId, taobaoUserId, operatorDto);
	}
}
