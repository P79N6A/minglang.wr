package com.taobao.cun.auge.lifecycle.tpt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

/**
 * 镇小二停业中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TPT",event=StateMachineEvent.CLOSING_EVENT,desc="镇小二停业中服务节点")
public class TPTClosingLifeCyclePhase extends AbstractLifeCyclePhase{

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
	@PhaseStepMeta(descr="更新镇小二站点状态到停业中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Station station = stationBO.getStationById(partnerInstanceDto.getStationId());
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.valueof(station.getState()), StationStatusEnum.CLOSING, partnerInstanceDto.getOperator());
	}

	@Override
	@PhaseStepMeta(descr="更新镇小二信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新镇小二实例状态到停业中")
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
	         PartnerInstanceStateChangeEnum instanceStateChange = convertClosingStateChange(partnerInstanceDto);
			 CloseStationApplyDto closeStationApplyDto = CloseStationApplyConverter.toCloseStationApplyDto(forcedCloseDto, partnerInstanceDto.getCloseType(), instanceStateChange);
	         closeStationApplyBO.addCloseStationApply(closeStationApplyDto);
		 }
		
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
    
    
    private static PartnerInstanceStateChangeEnum convertClosingStateChange(PartnerInstanceDto partnerInstance) {
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(partnerInstance.getState())) {
			return PartnerInstanceStateChangeEnum.DECORATING_CLOSING;
		} else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerInstance.getState())) {
			return PartnerInstanceStateChangeEnum.START_CLOSING;
		} else {
			// 状态校验,只有装修中，或者服务中可以停业
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"只有服务中、装修中服务站才可以申请停业");
		}
	}
}
