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
import com.taobao.cun.auge.station.strategy.PartnerInstanceStrategy;
import com.taobao.cun.auge.station.strategy.TpStrategy;
import com.taobao.cun.auge.station.strategy.TpaStrategy;
import com.taobao.cun.auge.station.strategy.TpsStrategy;
import com.taobao.cun.auge.station.strategy.TptStrategy;
import com.taobao.cun.auge.station.strategy.TpvStrategy;
import com.taobao.cun.auge.station.strategy.UmStrategy;

@Component("partnerInstanceHandler")
public class PartnerInstanceHandler implements InitializingBean{
	
	@Autowired
	private TpaStrategy tpaStrategy;
	@Autowired
	private TpvStrategy tpvStrategy;
	@Autowired
	private TpStrategy tpStrategy;
	
	@Autowired
	private TptStrategy tptStrategy;

	@Autowired
	private UmStrategy umStrategy;
	@Autowired
	private TpsStrategy tpsStrategy;
	
	Map<String,PartnerInstanceStrategy> strategy = new HashMap<String,PartnerInstanceStrategy>();
	@Override
	public void afterPropertiesSet() throws Exception {
		strategy.put(PartnerInstanceTypeEnum.TPA.getCode(), tpaStrategy);
		strategy.put(PartnerInstanceTypeEnum.TPV.getCode(), tpvStrategy);
		strategy.put(PartnerInstanceTypeEnum.TP.getCode(), tpStrategy);
		strategy.put(PartnerInstanceTypeEnum.TPT.getCode(), tptStrategy);
		strategy.put(PartnerInstanceTypeEnum.UM.getCode(), umStrategy);
		strategy.put(PartnerInstanceTypeEnum.TPS.getCode(), tpsStrategy);
	}
	
	/**
	 * 申请入驻
	 * @param instanceDto
	 * @param typeEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleApplySettle(PartnerInstanceDto instanceDto,PartnerInstanceTypeEnum typeEnum){
		strategy.get(typeEnum.getCode()).applySettle(instanceDto);
	}
	
	/**
	 * 入驻中编辑，只能在生命周期待办任务,没有开始的时候编辑
	 * 
	 * @param instanceDto
	 * @param typeEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Boolean handleValidateUpdateSettle(Long instanceId,PartnerInstanceTypeEnum typeEnum){
		return strategy.get(typeEnum.getCode()).validateUpdateSettle(instanceId);
	}
	
	/**
	 * 申请退出
	 * 
	 * @param quitDto
	 * @param typeEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDifferQuiting(QuitStationApplyDto quitDto, PartnerInstanceTypeEnum typeEnum)
			{
		strategy.get(typeEnum.getCode()).applyQuit(quitDto, typeEnum);
	}

	/**
	 * 审核退出
	 * 
	 * @param partnerInstanceId
	 * @param typeEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDifferQuitAuditPass(Long partnerInstanceId,
			PartnerInstanceTypeEnum partnerInstanceTypeEnum) {
		strategy.get(partnerInstanceTypeEnum.getCode()).handleDifferQuitAuditPass(partnerInstanceId);
	}
	
	/**
	 * 正式退出
	 * @param partnerInstanceQuitDto
	 * @param typeEnum
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleQuit(PartnerInstanceQuitDto partnerInstanceQuitDto,PartnerInstanceTypeEnum typeEnum){
		strategy.get(typeEnum.getCode()).quit(partnerInstanceQuitDto);
	}
	
	public void handleApplySettleNewly(PartnerInstanceDto instanceDto,PartnerInstanceTypeEnum typeEnum){
		strategy.get(typeEnum.getCode()).applySettleNewly(instanceDto);
	}
	
	
	/**
	 * 实例删除
	 * @param deleteDto
	 * @param rel
	 */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleDelete(PartnerInstanceDeleteDto deleteDto,PartnerStationRel rel){
		strategy.get(rel.getType()).delete(deleteDto, rel);
	}
	
	public void validateExistChildrenForQuit(PartnerInstanceTypeEnum typeEnum,PartnerStationRel instance) {
		strategy.get(typeEnum.getCode()).validateExistChildrenForQuit(instance);
	}
	
	public void validateClosePreCondition(PartnerInstanceTypeEnum typeEnum,PartnerStationRel partnerStationRel) {
		strategy.get(typeEnum.getCode()).validateClosePreCondition(partnerStationRel);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void handleSettleSuccess(PartnerInstanceSettleSuccessDto settleSuccessDto,PartnerStationRel rel) {
		strategy.get(rel.getType()).settleSuccess(settleSuccessDto,rel);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void startClosing(Long instanceId, String stationName, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto){
		strategy.get(typeEnum.getCode()).startClosing(instanceId, stationName, operatorDto);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void partnerClosing(Long instanceId, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto){
		strategy.get(typeEnum.getCode()).partnerClosing(instanceId, operatorDto);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void autoClosing(Long instanceId, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto){
		strategy.get(typeEnum.getCode()).autoClosing(instanceId, operatorDto);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void closed(Long instanceId, Long taobaoUserId,String taobaoNick, PartnerInstanceTypeEnum typeEnum,OperatorDto operatorDto){
		strategy.get(typeEnum.getCode()).closed(instanceId, taobaoUserId,taobaoNick, typeEnum,operatorDto);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void startQuiting(Long instanceId, String stationName, PartnerInstanceTypeEnum typeEnum, OperatorDto operatorDto){
		strategy.get(typeEnum.getCode()).startQuiting(instanceId, stationName, operatorDto);
	}

	public void validateAssetBack(PartnerInstanceTypeEnum typeEnum, Long instanceId) {
		strategy.get(typeEnum.getCode()).validateAssetBack(instanceId);
	}

	public void validateOtherPartnerQuit(PartnerInstanceTypeEnum typeEnum, Long instanceId) {
		strategy.get(typeEnum.getCode()).validateOtherPartnerQuit(instanceId);
	}
	
	public void startService(Long instanceId, Long taobaoUserId, PartnerInstanceTypeEnum typeEnum,
			OperatorDto operatorDto) {
		strategy.get(typeEnum.getCode()).startService(instanceId, taobaoUserId, operatorDto);
	}
}
