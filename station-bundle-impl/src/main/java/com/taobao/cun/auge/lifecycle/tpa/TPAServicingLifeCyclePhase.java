package com.taobao.cun.auge.lifecycle.tpa;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

@Component
@Phase(type="TPA",event=StateMachineEvent.SERVICING_EVENT,desc="淘帮手服务中节点服务")
public class TPAServicingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	@Override
	@PhaseStepMeta(descr="更新淘帮手站点到服务中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long stationId = partnerInstanceDto.getStationId();
		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.SERVICING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(partnerInstanceDto);
		stationBO.updateStation(stationDto);
	}

	@Override
	@PhaseStepMeta(descr="更新淘帮手信息(doNothing)")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		setPartnerInstanceToServicing(partnerInstanceDto,partnerInstanceDto,null);
	}

	@Override
	@PhaseStepMeta(descr="更新淘帮手LifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId = partnerInstanceDto.getId();
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(instanceId, PartnerLifecycleBusinessTypeEnum.SETTLING,
				PartnerLifecycleCurrentStepEnum.PROCESSING);
		if (items != null) {
			PartnerLifecycleDto param = new PartnerLifecycleDto();
			param.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			param.setSystem(PartnerLifecycleSystemEnum.HAS_PROCESS);
			param.setLifecycleId(items.getId());
			param.copyOperatorDto(partnerInstanceDto);
			partnerLifecycleBO.updateLifecycle(param);
		}
	}

	@Override
	@PhaseStepMeta(descr="更新淘帮手服务中扩展信息")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId = partnerInstanceDto.getId();
		sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId = partnerInstanceDto.getId();
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
	}

	/**
	 * 设置关系表为服务中
	 */
	private void setPartnerInstanceToServicing(PartnerInstanceDto rel, OperatorDto operatorDto, Long changePartnerId) {

		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
		Date serviceBeginTime = now.getTime();
		now.add(Calendar.YEAR, 1);
		Date serviceEndTime = now.getTime();

		PartnerInstanceDto piDto = new PartnerInstanceDto();
		piDto.setServiceBeginTime(serviceBeginTime);
		piDto.setServiceEndTime(serviceEndTime);
		piDto.setId(rel.getId());
		piDto.setState(PartnerInstanceStateEnum.SERVICING);
		piDto.setVersion(rel.getVersion());
		piDto.copyOperatorDto(operatorDto);
		if (changePartnerId != null) {
			piDto.setPartnerId(changePartnerId);
		}
		partnerInstanceBO.updatePartnerStationRel(piDto);
	}

}
