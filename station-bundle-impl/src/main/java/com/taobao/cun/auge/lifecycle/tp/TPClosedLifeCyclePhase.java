package com.taobao.cun.auge.lifecycle.tp;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.QuitStationApply;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 村小二已停业阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.CLOSED_EVENT,desc="村小二已停业服务节点")
public class TPClosedLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private QuitStationApplyBO quitStationApplyBO;
	@Override
	@PhaseStepMeta(descr="更新村小二站点状态到已停业")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, partnerInstanceDto.getOperator());
		}
		
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			QuitStationApply quitApply = quitStationApplyBO.findQuitStationApply(partnerInstanceDto.getId());
			if (quitApply.getIsQuitStation() == null || "y".equals(quitApply.getIsQuitStation())) {
				stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.QUITING, StationStatusEnum.CLOSED, partnerInstanceDto.getOperator());
			}
		}
	}

	@Override
	@PhaseStepMeta(descr="更新村小二信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二实例状态到已停业")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
			partnerInstanceDto.setServiceEndTime(new Date());
			partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
		}
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			partnerInstanceBO.changeState(partnerInstanceDto.getId(), PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED, partnerInstanceDto.getOperator());
		}
	}

	@Override
	@PhaseStepMeta(descr="创建已停业lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(partnerInstanceDto.getId(),PartnerLifecycleBusinessTypeEnum.CLOSING);
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			partnerLifecycle.copyOperatorDto(partnerInstanceDto);
			if(PartnerInstanceCloseTypeEnum.PARTNER_QUIT.getCode().equals(partnerInstanceDto.getCloseType().getCode())){
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CONFIRM);
			}
			if(PartnerInstanceCloseTypeEnum.WORKER_QUIT.getCode().equals(partnerInstanceDto.getCloseType().getCode())){
				partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
			}
	        partnerLifecycleBO.updateLifecycle(partnerLifecycle);
		}
		
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(partnerInstanceDto.getId(), PartnerLifecycleBusinessTypeEnum.QUITING,
					PartnerLifecycleCurrentStepEnum.PROCESSING);
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setLifecycleId(items.getId());
			partnerLifecycleBO.updateLifecycle(param);
		}
		
	}

	@Override
	@PhaseStepMeta(descr="")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			quitStationApplyBO.deleteQuitStationApply(partnerInstanceDto.getId(), partnerInstanceDto.getOperator());
		}
		
	}

	@Override
	@PhaseStepMeta(descr="触发已停业事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 // 发出合伙人实例状态变更事件
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			 dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.CLOSED, partnerInstanceDto);
		}
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.QUITTING_REFUSED, partnerInstanceDto);
		}
		
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceDto.getId());
		}
		if(PartnerInstanceStateEnum.QUITING.getCode().equals(context.getSourceState())){
			syncStationApply(SyncStationApplyEnum.UPDATE_STATE, partnerInstanceDto.getId());
		}
		
	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
	        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
	        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
	                operator);
	        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	 }
	
	
	
	
}
