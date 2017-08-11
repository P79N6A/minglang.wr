package com.taobao.cun.auge.lifecycle.tp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
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
import com.taobao.cun.auge.station.adapter.Emp360Adapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.QuitStationApplyConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.QuitStationApplyDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 合伙人入驻中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TPT",event=StateMachineEvent.QUITING_EVENT,desc="村小二退出中服务节点")
public class TPQuitingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private QuitStationApplyBO quitStationApplyBO;
	
	@Autowired
	private Emp360Adapter emp360Adapter;
	
	@Autowired
	private UicReadAdapter uicReadAdapter;
	    
	    
	@Override
	@PhaseStepMeta(descr="更新村小二站点状态到已停业")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		QuitStationApplyDto quitStationApplyDto = (QuitStationApplyDto) context.getExtension("quitApply");
		 if (quitStationApplyDto.getIsQuitStation()) {
             stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.CLOSED, StationStatusEnum.QUITING, partnerInstanceDto.getOperator());
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
		partnerInstanceBO.changeState(partnerInstanceDto.getId(), PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.QUITING, partnerInstanceDto.getOperator());
	}

	@Override
	@PhaseStepMeta(descr="创建退出中lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		PartnerLifecycleDto itemsDO = new PartnerLifecycleDto();
		itemsDO.setPartnerInstanceId(partnerInstanceDto.getId());
		itemsDO.setPartnerType(partnerInstanceDto.getType());
		itemsDO.setBusinessType(PartnerLifecycleBusinessTypeEnum.QUITING);
		itemsDO.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
		itemsDO.setBond(PartnerLifecycleBondEnum.WAIT_THAW);
		itemsDO.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		itemsDO.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleBO.addLifecycle(itemsDO);
	}

	@Override
	@PhaseStepMeta(descr="保存退出申请单")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		 // 保存退出申请单
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		PartnerStationRel instance = partnerInstanceBO.findPartnerInstanceById(partnerInstanceDto.getId());
		QuitStationApplyDto quitDto = (QuitStationApplyDto) context.getExtension("quitApply");
        QuitStationApply quitStationApply = QuitStationApplyConverter.convert(quitDto, instance, buildOperatorName(quitDto));
        quitStationApplyBO.saveQuitStationApply(quitStationApply, partnerInstanceDto.getOperator());
		
	}

	@Override
	@PhaseStepMeta(descr="触发已停业事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 // 发出合伙人实例状态变更事件
		dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.START_QUITTING, partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.UPDATE_ALL, partnerInstanceDto.getId());
	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
	        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
	        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
	                operator);
	        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	 }
	
	
	 private String buildOperatorName(OperatorDto operatorDto) {
	        String operator = operatorDto.getOperator();
	        OperatorTypeEnum type = operatorDto.getOperatorType();

	        // 小二工号
	        if (OperatorTypeEnum.BUC.equals(type)) {
	            return emp360Adapter.getName(operator);
	        } else if (OperatorTypeEnum.HAVANA.equals(type)) {
	            return uicReadAdapter.getFullName(Long.parseLong(operator));
	        }
	        return "";
	    }

	
}
