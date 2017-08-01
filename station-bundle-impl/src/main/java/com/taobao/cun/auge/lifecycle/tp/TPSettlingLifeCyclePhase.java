package com.taobao.cun.auge.lifecycle.tp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerPeixunBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
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
import com.taobao.cun.auge.station.enums.PartnerPeixunCourseTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

/**
 * 合伙人入驻中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class TPSettlingLifeCyclePhase extends AbstractLifeCyclePhase{

	private static final String USER_TYPE = "TP";
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private PartnerPeixunBO partnerPeixunBO;
	
	@Autowired
	private AppResourceService appResourceService;
	
	@Autowired
	private StationDecorateBO stationDecorateBO;
	
	@Autowired
	private LifeCycleValidator lifeCycleValidator;
	
	@Override
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		  PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		  lifeCycleValidator.validateSettling(partnerInstanceDto);
		  Long stationId = partnerInstanceDto.getStationId();
          if (stationId == null) {
              stationId = addStation(partnerInstanceDto);
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
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		 PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 addPartner(partnerInstanceDto);
	}

	@Override
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addPartnerInstanceRel(partnerInstanceDto);
	}

	@Override
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
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		validateDecorateAndPaymentType(partnerInstanceDto);
		Long taobaoUserId = partnerInstanceDto.getTaobaoUserId();
		String taobaoNick = partnerInstanceDto.getPartnerDto().getTaobaoNick();
		// 生成启航班培训记录和成长营培训记录
		partnerPeixunBO.initPeixunRecord(taobaoUserId,
				PartnerPeixunCourseTypeEnum.APPLY_IN, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"APPLY_IN"));
		partnerPeixunBO.initPeixunRecord(taobaoUserId,
				PartnerPeixunCourseTypeEnum.UPGRADE, appResourceService
						.queryAppResourceValue("PARTNER_PEIXUN_CODE",
								"UPGRADE"));
		// 分发启航班试卷
		partnerPeixunBO.dispatchApplyInExamPaper(taobaoUserId, taobaoNick,
				appResourceService.queryAppResourceValue(
						"PARTNER_PEIXUN_ONLINE", "EXAM_ID"));
		// 生成装修记录
		StationDecorateDto stationDecorateDto = new StationDecorateDto();
		stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationDecorateDto.setStationId(partnerInstanceDto.getStationId());
		stationDecorateDto.setPartnerUserId(taobaoUserId);
		stationDecorateDto.setDecorateType(partnerInstanceDto.getStationDecorateTypeEnum());
		stationDecorateDto.setPaymentType(partnerInstanceDto.getStationDecoratePaymentTypeEnum());
		stationDecorateBO.addStationDecorate(stationDecorateDto);
	}

	@Override
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		this.sendPartnerInstanceStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
	}

	@Override
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

	
	@Override
	public String getComponentName() {
		return USER_TYPE+"_"+getPhase()+"_EVENT";
	}

	@Override
	public String getPhase() {
		return "SETTLING";
	}

}
