package com.taobao.cun.auge.lifecycle.tp;

import java.util.Date;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.CloseStationApplyConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.ForcedCloseDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleQuitProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 村小二停业中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.CLOSING_EVENT,desc="村小二停业中服务节点")
public class TPClosingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private CloseStationApplyBO closeStationApplyBO;
	
	@Override
	@PhaseStepMeta(descr="更新村小二站点状态到停业中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Station station = stationBO.getStationById(partnerInstanceDto.getStationId());
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.valueof(station.getStatus()), StationStatusEnum.CLOSING, partnerInstanceDto.getOperator());
	}

	@Override
	@PhaseStepMeta(descr="更新村小二信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二实例状态到停业中")
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
		 if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(partnerInstanceDto.getCloseType())) {
			 //合伙人申请停业插入停业协议
			 addCloseProtocol(partnerInstanceDto);
			 // 新增停业申请
	         CloseStationApplyDto closeStationApplyDto = new CloseStationApplyDto();
	         closeStationApplyDto.setPartnerInstanceId(partnerInstanceDto.getId());
	         closeStationApplyDto.setType(partnerInstanceDto.getCloseType());
	         closeStationApplyDto.copyOperatorDto(partnerInstanceDto);
	         closeStationApplyBO.addCloseStationApply(closeStationApplyDto);
		 }else{
			 ForcedCloseDto forcedCloseDto = (ForcedCloseDto)context.getExtension("forcedCloseDto");
			  //生成状态变化枚举，装修中-》停业申请中，或者，服务中-》停业申请中
	         PartnerInstanceStateChangeEnum instanceStateChange = convertClosingStateChange(context);
			 CloseStationApplyDto closeStationApplyDto = CloseStationApplyConverter.toCloseStationApplyDto(forcedCloseDto, partnerInstanceDto.getCloseType(), instanceStateChange);
	         closeStationApplyBO.addCloseStationApply(closeStationApplyDto);
		 }
		
	}

	@Override
	@PhaseStepMeta(descr="触发停业中事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 PartnerInstanceStateChangeEnum instanceStateChange = convertClosingStateChange(context);
		PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(instanceStateChange, partnerInstanceBO.getPartnerInstanceById(partnerInstanceDto.getId()), partnerInstanceDto);
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
    	PartnerInstanceDto rel = new PartnerInstanceDto();
    	rel.setId(partnerInstance.getId());
    	rel.setVersion(partnerInstance.getVersion());
    	rel.setState(PartnerInstanceStateEnum.CLOSING);
    	rel.setCloseType(partnerInstance.getCloseType());
    	rel.copyOperatorDto(partnerInstance);
        partnerInstanceBO.updatePartnerStationRel(rel);
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

        if (PartnerInstanceCloseTypeEnum.WORKER_QUIT.equals(partnerInstance.getCloseType())) {
            partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.TO_START);
        } else if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(partnerInstance.getCloseType())) {
            partnerLifecycle.setQuitProtocol(PartnerLifecycleQuitProtocolEnum.SIGNED);
            partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.WAIT_CONFIRM);
        }
        partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
        partnerLifecycle.copyOperatorDto(partnerInstance);

        partnerLifecycleBO.addLifecycle(partnerLifecycle);
    }

 
    /**
     * 添加停业协议
     *
     * @param taobaoUserId
     * @param instanceId
     * @param operatorDto
     */
    private void addCloseProtocol(PartnerInstanceDto partnerInstance) {
        PartnerProtocolRelDto proRelDto = new PartnerProtocolRelDto();
        Date quitProDate = new Date();
        proRelDto.setObjectId(partnerInstance.getId());
        proRelDto.setProtocolTypeEnum(ProtocolTypeEnum.PARTNER_QUIT_PRO);
        proRelDto.setConfirmTime(quitProDate);
        proRelDto.setStartTime(quitProDate);
        proRelDto.setTaobaoUserId(partnerInstance.getTaobaoUserId());
        proRelDto.setTargetType(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
        proRelDto.copyOperatorDto(partnerInstance);
        partnerProtocolRelBO.addPartnerProtocolRel(proRelDto);
    }
    
    
    private static PartnerInstanceStateChangeEnum convertClosingStateChange(LifeCyclePhaseContext context) {
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(context.getSourceState())) {
			return PartnerInstanceStateChangeEnum.DECORATING_CLOSING;
		} else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState())) {
			return PartnerInstanceStateChangeEnum.START_CLOSING;
		} else {
			// 状态校验,只有装修中，或者服务中可以停业
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"只有服务中、装修中服务站才可以申请停业");
		}
	}
}
