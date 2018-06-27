package com.taobao.cun.auge.lifecycle.tp;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecyclePositionConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.validate.StationValidator;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 村小二入驻中阶段组件
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.SETTLING_EVENT,desc="村小二入驻中服务节点")
public class TPSettlingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private StationDecorateBO stationDecorateBO;
	
	@Autowired
	private LifeCycleValidator lifeCycleValidator;
	
	@Autowired
	private StationNumConfigBO stationNumConfigBO;
	
	@Override
	@PhaseStepMeta(descr="创建村小二站点")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		  PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		  lifeCycleValidator.validateSettling(partnerInstanceDto);
		  Long stationId = partnerInstanceDto.getStationId();
          if (stationId == null) {
              //村名基础校验
        	  String stationCategory= partnerInstanceDto.getStationDto().getFeature().get("stationCategory");
        	  if(StringUtils.isNotEmpty(stationCategory)){
        		  partnerInstanceDto.getStationDto().setCategory(stationCategory);
        	  }
              StationValidator.nameFormatCheck(partnerInstanceDto.getStationDto().getName());
              String stationNum = stationNumConfigBO.createStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(), StationNumConfigTypeEnum.C,0);
              partnerInstanceDto.getStationDto().setStationNum(stationNum);
              stationId = addStation(partnerInstanceDto,StationType.STATION.getType());
              stationNumConfigBO.updateSeqNumByStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(), StationNumConfigTypeEnum.C, 
            		  stationNum);
              
          } else {
              StationDto stationDto = partnerInstanceDto.getStationDto();
              stationDto.setState(StationStateEnum.INVALID);
              stationDto.setStatus(StationStatusEnum.NEW);
              PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
              if (partnerDto != null) {
                  stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
                  stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
                  stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
              }
              updateStation(stationId, partnerInstanceDto);
          }
	}
    
	@Override
	@PhaseStepMeta(descr="创建村小二")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		 PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 addPartner(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="创建人村关系")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addPartnerInstanceRel(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="创建lifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addLifecycle(partnerInstanceDto);
	}

	private void addLifecycle(PartnerInstanceDto partnerInstanceDto) {
		PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TP);
		partnerLifecycleDto.copyOperatorDto(partnerInstanceDto);
		partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.SETTLING);
		partnerLifecycleDto.setSettledProtocol(PartnerLifecycleSettledProtocolEnum.SIGNING);
		partnerLifecycleDto.setBond(PartnerLifecycleBondEnum.WAIT_FROZEN);
		partnerLifecycleDto.setSystem(PartnerLifecycleSystemEnum.WAIT_PROCESS);
		partnerLifecycleDto.setPosittionConfirm(PartnerLifecyclePositionConfirmEnum.N);
		partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
		partnerLifecycleDto.setPartnerInstanceId(partnerInstanceDto.getId());
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
	}

	@Override
	@PhaseStepMeta(descr="创建培训装修记录")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		validateDecorateAndPaymentType(partnerInstanceDto);
	
		// 生成装修记录
		StationDecorateDto stationDecorateDto = new StationDecorateDto();
		stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationDecorateDto.setStationId(partnerInstanceDto.getStationId());
		stationDecorateDto.setPartnerUserId(partnerInstanceDto.getTaobaoUserId());
		stationDecorateDto.setDecorateType(partnerInstanceDto.getStationDecorateTypeEnum());
		stationDecorateDto.setPaymentType(partnerInstanceDto.getStationDecoratePaymentTypeEnum());
		stationDecorateBO.addStationDecorate(stationDecorateDto);
	}

	@Override
	@PhaseStepMeta(descr="触发入驻中事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		this.sendPartnerInstanceStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="同步老模型")
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.ADD, partnerInstanceDto.getId());
	}

	private void validateDecorateAndPaymentType(PartnerInstanceDto partnerInstanceDto) {
		ValidateUtils.notNull(partnerInstanceDto);
		ValidateUtils.notNull(partnerInstanceDto.getId());
		ValidateUtils.notNull(partnerInstanceDto.getTaobaoUserId());
		ValidateUtils.notNull(partnerInstanceDto.getStationId());

		StationDecorateTypeEnum decorate = partnerInstanceDto.getStationDecorateTypeEnum();
		StationDecoratePaymentTypeEnum pay = partnerInstanceDto.getStationDecoratePaymentTypeEnum();
		ValidateUtils.notNull(decorate);
		ValidateUtils.notNull(pay);

		if (decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.SELF)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.GOV_PART)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN, pay, StationDecoratePaymentTypeEnum.GOV_ALL)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.NEW, pay, StationDecoratePaymentTypeEnum.NONE)
				|| decoratePaymentTypeEquals(decorate, StationDecorateTypeEnum.ORIGIN_UPGRADE, pay, StationDecoratePaymentTypeEnum.NONE)) {
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_PARAM_ERROR_CODE,"illegal decorate_type & payment_type combination");
		}
	}
	
	private boolean decoratePaymentTypeEquals(StationDecorateTypeEnum decorate, StationDecorateTypeEnum decorateExpect,
            StationDecoratePaymentTypeEnum pay, StationDecoratePaymentTypeEnum payExpect) {
		return decorateExpect.getCode().equals(decorate.getCode()) && payExpect.getCode().equals(pay.getCode());
	}

	

	
}
