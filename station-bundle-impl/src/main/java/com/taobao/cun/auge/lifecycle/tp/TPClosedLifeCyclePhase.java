package com.taobao.cun.auge.lifecycle.tp;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
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
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
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
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.StationDecorateService;

/**
 * 合伙人入驻中阶段组件
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
	private StationDecorateService stationDecorateService;
	
	@Autowired
	private PartnerPeixunBO partnerPeixunBO;
	@Override
	@PhaseStepMeta(descr="更新村小二站点状态到已停业")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		//验证关闭临时放在这里，后面单独处理
		validateClosePreCondition(partnerInstanceDto);
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, partnerInstanceDto.getOperator());
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
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
		partnerInstanceDto.setServiceEndTime(new Date());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="创建已停业lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
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

	@Override
	@PhaseStepMeta(descr="")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		 
		
	}

	@Override
	@PhaseStepMeta(descr="触发已停业事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 // 发出合伙人实例状态变更事件
        dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.CLOSED, partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceDto.getId());
	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
	        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
	        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
	                operator);
	        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	 }
	
	
	
	private void validateClosePreCondition(PartnerInstanceDto partnerInstanceDto) {
		List<PartnerInstanceStateEnum> states = PartnerInstanceStateEnum.getPartnerStatusForValidateClose();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(partnerInstanceDto.getId(), states);

		if (CollectionUtils.isNotEmpty(children)) {
			throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"该合伙人下存在未停业的淘帮手，请先将其淘帮手停业后，才可以停业合伙人");
		}
		
		//如果是从装修中停业，则需要判断村点是否退出了装修
		if (PartnerInstanceStateEnum.DECORATING.getCode().equals(partnerInstanceDto.getState())) {
				stationDecorateService.judgeDecorateQuit(partnerInstanceDto.getStationId());
		}
		//判断培训课程否是已经退款或签到
		partnerPeixunBO.validateQuitable(partnerInstanceDto.getTaobaoUserId());
	}
}
