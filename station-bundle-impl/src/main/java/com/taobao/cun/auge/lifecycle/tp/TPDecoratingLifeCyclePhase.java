package com.taobao.cun.auge.lifecycle.tp;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.PartnerCourseRecord;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.domain.PartnerStationStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerPeixunStatusEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 村小二装修中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.DECORATING_EVENT,desc="村小二装修中服务节点")
public class TPDecoratingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private StationBO stationBO;
	
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;
    
    @Autowired
    private PartnerLifecycleBO partnerLifecycleBO;
    
    @Autowired
    private StationDecorateBO stationDecorateBO;
    
    @Autowired
    private AppResourceService appResourceService;
	@Override
	@PhaseStepMeta(descr="更新村点信息")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long stationId = partnerInstanceDto.getStationId();
		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.DECORATING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(partnerInstanceDto);
		stationBO.updateStation(stationDto);
	}

	@Override
	@PhaseStepMeta(descr="更新村小二信息")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nonthing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二实例状态到装修中")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		setPartnerInstanceToDecorating(partnerInstanceDto,partnerInstanceDto,null);
	}

	@Override
	@PhaseStepMeta(descr="更新LifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId =partnerInstanceDto.getId();
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
		initPartnerLifeCycleForDecorating(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="更新装修中扩展业务信息")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		//donothing
	}

	@Override
	@PhaseStepMeta(descr="触发装修中事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId =partnerInstanceDto.getId();
		sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_DECORATING, partnerInstanceDto);
		dispacthEvent(partnerInstanceDto, PartnerInstanceStateEnum.DECORATING.getCode());
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId =partnerInstanceDto.getId();
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, instanceId);
	}


	
	/**
	 * 发送装修中事件 给手机端使用
	 * 
	 * @param PartnerStationRel
	 * @param state
	 */
	private void dispacthEvent(PartnerInstanceDto rel, String state) {
			if (rel != null) {
				Station stationDto = stationBO.getStationById(rel.getStationId());
				PartnerStationStateChangeEvent pisc = new PartnerStationStateChangeEvent();
				pisc.setStationId(rel.getStationId());
				pisc.setPartnerInstanceState(state);
				pisc.setStationName(stationDto.getName());
				pisc.setTaobaoUserId(rel.getTaobaoUserId());
				EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_STATION_STATE_CHANGE_EVENT, pisc);
			}
	}
	
	
	
	/**
	 * 构建装修中生命周期
	 *
	 * @param rel
	 */
	private void initPartnerLifeCycleForDecorating(PartnerInstanceDto rel) {
		
		Station s = stationBO.getStationById(rel.getStationId());
		if(containCountyOrgId(s.getApplyOrg())) {
			return;
		}
		
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
		partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.DECORATING);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(rel.getId());
		
	
		
		//装修
		boolean hasDecorateDone = stationDecorateBO.handleAcessDecorating(rel.getStationId());
		if (hasDecorateDone) {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
		}else {
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	private Boolean containCountyOrgId(Long countyOrgId) {

		if (countyOrgId != null) {
			AppResourceDto resource = appResourceService.queryAppResource("gudian_county", "countyid");
			if (resource != null && !StringUtils.isEmpty(resource.getValue())) {
				List<Long> countyIdList = JSON.parseArray(resource.getValue(), Long.class);
				return countyIdList.contains(countyOrgId);

			} else {

				return true;
			}
		}
		return true;
	}
	
	/**
	 * 设置关系表为装修中
	 */
	private void setPartnerInstanceToDecorating(PartnerInstanceDto rel, OperatorDto operatorDto, Long changePartnerId) {

		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
		Date serviceBeginTime = now.getTime();
		now.add(Calendar.YEAR, 1);
		Date serviceEndTime = now.getTime();

		PartnerInstanceDto piDto = new PartnerInstanceDto();
		piDto.setServiceBeginTime(serviceBeginTime);
		piDto.setServiceEndTime(serviceEndTime);
		piDto.setId(rel.getId());
		piDto.setState(PartnerInstanceStateEnum.DECORATING);
		piDto.setVersion(rel.getVersion());
		piDto.setParentStationId(rel.getStationId());
		piDto.copyOperatorDto(operatorDto);
		if (changePartnerId != null) {
			piDto.setPartnerId(changePartnerId);
		}
		partnerInstanceBO.updatePartnerStationRel(piDto);
	}
}
