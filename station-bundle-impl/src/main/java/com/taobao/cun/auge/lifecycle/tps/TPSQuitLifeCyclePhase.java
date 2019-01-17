package com.taobao.cun.auge.lifecycle.tps;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerApplyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.StationService;

/**
 * 村小二已退出阶段组件
 *
 */
@Component
@Phase(type="TPS",event=StateMachineEvent.QUIT_EVENT,desc="村小二退出中服务节点")
public class TPSQuitLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private AccountMoneyBO accountMoneyBO;
	
	@Autowired
	private QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	private PartnerApplyBO partnerApplyBO;
	
	@Autowired
	private GeneralTaskSubmitService generalTaskSubmitService;
	
	@Autowired
	private StationService stationService;

	@Override
	@PhaseStepMeta(descr="更新村小二站点状态到已停业")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
	
		Boolean fromAuditflow = (Boolean)context.getExtensionWithDefault("fromAuditflow",false);
		//村点退出阶段逻辑非常恶心，保证金解冻后PartnerInstance的状态才变成已退出，但是审批通过后村点状态已经是已退出了，在这个阶段实例状态和村点状态是不一致的。
		//来自审批流的请求才更新村点状态，和老逻辑保持一致。
		if(fromAuditflow){
			QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(partnerInstanceDto.getId());
			
			// 村点已撤点
			if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
				stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.QUITING, StationStatusEnum.QUIT, partnerInstanceDto.getOperator());
			}else {
				List<Long> sIds = new ArrayList<Long>();
				sIds.add(partnerInstanceDto.getStationId());
				stationService.updateStationCategory(sIds, "INVALID");
			}
		}
		

	}

	@Override
	@PhaseStepMeta(descr="更新村小二信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二实例状态到退出中")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Boolean fromThawTask = (Boolean)context.getExtensionWithDefault("fromThawTask",false);
		if(fromThawTask){
			partnerInstanceBO.changeState(partnerInstanceDto.getId(), PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.QUIT,partnerInstanceDto.getOperator());
		}
		
	}

	@Override
	@PhaseStepMeta(descr="更新退出中lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceDto.getId(), PartnerLifecycleBusinessTypeEnum.QUITING,PartnerLifecycleCurrentStepEnum.PROCESSING);
		Boolean fromAuditflow = (Boolean)context.getExtensionWithDefault("fromAuditflow",false);
		if(fromAuditflow){
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param = new PartnerLifecycleDto();
				param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
				param.setLifecycleId(items.getId());
				partnerLifecycleBO.updateLifecycle(param);
			}
		}
		Boolean fromThawTask = (Boolean)context.getExtensionWithDefault("fromThawTask",false);
		if(fromThawTask){
			if (items != null) {
				PartnerLifecycleDto param = new PartnerLifecycleDto();
				param.setBond(PartnerLifecycleBondEnum.HAS_THAW);
				param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
				param.setLifecycleId(items.getId());
				param.copyOperatorDto(partnerInstanceDto);
				partnerLifecycleBO.updateLifecycle(param);
			}
		}
	}

	@Override
	@PhaseStepMeta(descr="解冻保证金")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		
		//任务流触发，更新保证金解冻状态
		Boolean fromThawTask = (Boolean)context.getExtensionWithDefault("fromThawTask",false);
		if(fromThawTask){
			thawMoney(partnerInstanceDto,partnerInstanceDto.getId());
		}
		//审批流触发，恢复合伙人申请单状态
		Boolean fromAuditflow = (Boolean)context.getExtensionWithDefault("fromAuditflow",false);
		if(fromAuditflow){
			PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(partnerInstanceDto.getId());
			PartnerApplyDto partnerApplyDto = new PartnerApplyDto();
			partnerApplyDto.setTaobaoUserId(instance.getTaobaoUserId());
			partnerApplyDto.setOperator("system");
			partnerApplyBO.restartPartnerApplyByUserId(partnerApplyDto);
			
			QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(partnerInstanceDto.getId());
			generalTaskSubmitService.submitQuitApprovedTask(partnerInstanceDto.getId(), partnerInstanceDto.getStationId(), partnerInstanceDto.getTaobaoUserId(),
					quitApply.getIsQuitStation());
		}
		
	}

	@Override
	@PhaseStepMeta(descr="触发已退出事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		Boolean fromThawTask = (Boolean)context.getExtensionWithDefault("fromThawTask",false);
		if(fromThawTask){
			PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
			EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convertStateChangeEvent(PartnerInstanceStateChangeEnum.QUIT,
							partnerInstanceBO.getPartnerInstanceById(partnerInstanceDto.getId()), partnerInstanceDto));
		}
	}

	 private void thawMoney(OperatorDto operatorDto, Long instanceId) {
			//解冻保证金
			AccountMoneyDto accountMoneyUpdateDto = new AccountMoneyDto();
			accountMoneyUpdateDto.setObjectId(instanceId);
			accountMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			accountMoneyUpdateDto.setType(AccountMoneyTypeEnum.PARTNER_BOND);
			accountMoneyUpdateDto.setThawTime(new Date());
			accountMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_THAW);
			accountMoneyUpdateDto.copyOperatorDto(operatorDto);
			accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyUpdateDto);
			
			//解冻铺货金保证金
			AccountMoneyDto replenishMoneyUpdateDto = new AccountMoneyDto();
			replenishMoneyUpdateDto.setObjectId(instanceId);
			replenishMoneyUpdateDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			replenishMoneyUpdateDto.setType(AccountMoneyTypeEnum.REPLENISH_MONEY);
			replenishMoneyUpdateDto.setThawTime(new Date());
			replenishMoneyUpdateDto.setState(AccountMoneyStateEnum.HAS_THAW);
			replenishMoneyUpdateDto.copyOperatorDto(operatorDto);
			accountMoneyBO.updateAccountMoneyByObjectId(replenishMoneyUpdateDto);
			
		}
	
}
