package com.taobao.cun.auge.lifecycle.tpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
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
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.CloseStationApplyConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 淘帮手停业中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TPA",event=StateMachineEvent.CLOSING_EVENT,desc="淘帮手停业中服务节点")
public class TPAClosingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private CloseStationApplyBO closeStationApplyBO;
	
	@Override
	@PhaseStepMeta(descr="更新淘帮手站点状态到停业中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Station station = stationBO.getStationById(partnerInstanceDto.getStationId());
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.valueof(station.getStatus()), StationStatusEnum.CLOSING, partnerInstanceDto.getOperator());
	}

	@Override
	@PhaseStepMeta(descr="更新淘帮手信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新淘帮手实例状态到停业中")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		//外部调用需要设置addCloseType
	    closingPartnerInstance(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="创建停业中lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addClosingLifecycle(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="创建停业协议，创建停业申请")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		ForcedCloseDto forcedCloseDto = (ForcedCloseDto) context.getExtension("forcedCloseDto");
		PartnerInstanceStateChangeEnum instanceStateChange = PartnerInstanceStateChangeEnum.START_CLOSING;
		CloseStationApplyDto closeStationApplyDto = CloseStationApplyConverter.toCloseStationApplyDto(forcedCloseDto,
				partnerInstanceDto.getCloseType(), instanceStateChange);
		closeStationApplyBO.addCloseStationApply(closeStationApplyDto);

	}

	@Override
	@PhaseStepMeta(descr="触发停业中事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(
        PartnerInstanceStateChangeEnum.START_CLOSING, partnerInstanceBO.getPartnerInstanceById(partnerInstanceDto.getId()), partnerInstanceDto);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceDto.getId());
	}

	/**
     * 合伙人实例 停业中
     *
     * @param partnerInstance
     * @param closeType
     * @param operatorDto
     */
    private void closingPartnerInstance(PartnerInstanceDto partnerInstance) {
    	partnerInstance.setState(PartnerInstanceStateEnum.CLOSING);
        partnerInstanceBO.updatePartnerStationRel(partnerInstance);
    }
    
    
    /**
     * 添加停业生命周期
     *
     * @param operatorDto
     * @param partnerStationRel
     * @param closeType
     */
    private void addClosingLifecycle(PartnerInstanceDto partnerInstance) {
        PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();

        partnerLifecycle.setPartnerInstanceId(partnerInstance.getId());
        partnerLifecycle.setPartnerType(partnerInstance.getType());
        partnerLifecycle.setBusinessType(PartnerLifecycleBusinessTypeEnum.CLOSING);

        partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
        partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
        partnerLifecycle.copyOperatorDto(partnerInstance);

        partnerLifecycleBO.addLifecycle(partnerLifecycle);
    }

   
   
}
