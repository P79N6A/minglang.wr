package com.taobao.cun.auge.lifecycle.tpa;

import java.util.Date;

import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.common.BaseLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.annotation.Phase;
import com.taobao.cun.auge.lifecycle.annotation.PhaseMeta;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 淘帮手已停业阶段组件
 *
 */
@Component
@Phase(type="TPA",event=StateMachineEvent.CLOSED_EVENT,desc="淘帮手已停业服务节点")
public class TPAClosedLifeCyclePhase extends BaseLifeCyclePhase {

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	@Autowired
	private AssetBO assetBO;
	
	@Override
	@PhaseMeta(descr="更新淘帮手站点状态到已停业")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, partnerInstanceDto.getOperator());
	}

	@Override
	@PhaseMeta(descr="更新淘帮手信息（无操作）")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseMeta(descr="更新淘帮手实例状态到已停业")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
		partnerInstanceDto.setServiceEndTime(new Date());
		partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
	}

	@Override
	@PhaseMeta(descr="更新lifecycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(partnerInstanceDto.getId(),PartnerLifecycleBusinessTypeEnum.CLOSING);
		PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
		partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
		partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
		partnerLifecycle.copyOperatorDto(partnerInstanceDto);
		partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_PASS);
        partnerLifecycleBO.updateLifecycle(partnerLifecycle);
	}

	@Override
	@PhaseMeta(descr="")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		assetBO.setAssetRecycleIsY(partnerInstanceDto.getStationId(), partnerInstanceDto.getTaobaoUserId());
		
	}

	@Override
	@PhaseMeta(descr="触发已停业事件变更")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 // 发出合伙人实例状态变更事件
        dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.CLOSED, partnerInstanceDto);
	}

	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
	        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
	        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
	                operator);
	        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	 }
	

}
