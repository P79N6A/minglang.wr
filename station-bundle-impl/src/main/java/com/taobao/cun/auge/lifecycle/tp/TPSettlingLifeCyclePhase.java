package com.taobao.cun.auge.lifecycle.tp;

import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseDSL;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.common.BaseLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.annotation.Phase;
import com.taobao.cun.auge.lifecycle.annotation.PhaseMeta;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
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
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.station.validate.StationValidator;

/**
 * 村小二入驻中阶段组件
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.SETTLING_EVENT,desc="村小二入驻中服务节点")
public class TPSettlingLifeCyclePhase extends BaseLifeCyclePhase {

    private static final Logger logger = LoggerFactory.getLogger(TPSettlingLifeCyclePhase.class);

	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private StationDecorateBO stationDecorateBO;
	
	@Autowired
	private LifeCycleValidator lifeCycleValidator;
	
	@Autowired
	private StationNumConfigBO stationNumConfigBO;
	
	@Autowired
	private StationDecorateService stationDecorateService;

	@PhaseMeta(descr="入驻条件校验：参数、支付宝实名认证、手机号重复")
	public void validateSettling(LifeCyclePhaseContext context){
		lifeCycleValidator.validateSettling(context.getPartnerInstance());
	}

	@Override
	@PhaseMeta(descr="创建村小二站点")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		  PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		  Long stationId = partnerInstanceDto.getStationId();
          if (stationId == null) {
              //村名基础校验
        	  if(partnerInstanceDto.getStationDto().getFeature() != null){
        		  String stationCategory= partnerInstanceDto.getStationDto().getFeature().get("stationCategory");
            	  if(StringUtils.isNotEmpty(stationCategory)){
            		  partnerInstanceDto.getStationDto().setCategory(stationCategory);
            	  }else {
            		  partnerInstanceDto.getStationDto().setCategory("");
            	  }
        	  }else {
        		  partnerInstanceDto.getStationDto().setCategory("");
        	  }
              StationValidator.nameFormatCheck(partnerInstanceDto.getStationDto().getName());
              String stationNum = stationNumConfigBO.createStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(), StationNumConfigTypeEnum.C,0);
              partnerInstanceDto.getStationDto().setStationNum(stationNum);
              stationId = addStation(partnerInstanceDto,StationType.STATION.getType());
              addCreateLog(partnerInstanceDto);
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
	@PhaseMeta(descr="创建村小二")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		 PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		 addPartner(partnerInstanceDto);
	}

	@Override
	@PhaseMeta(descr="创建人村关系")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		addPartnerInstanceRel(partnerInstanceDto);
	}

	@PhaseMeta(descr="扩展生命周期元素：入驻协议、保证金、小二审核、装修确认")
	public void buildLifeCycleItems(LifeCyclePhaseContext context) {
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

	@PhaseMeta(descr="创建培训装修记录")
	public void executeExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		
		validateDecorateAndPaymentType(partnerInstanceDto);
	
		// 生成装修记录
		StationDecorateDto stationDecorateDto = new StationDecorateDto();
		stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
		stationDecorateDto.setStationId(partnerInstanceDto.getStationId());
		stationDecorateDto.setPartnerUserId(partnerInstanceDto.getTaobaoUserId());
		stationDecorateDto.setDecorateType(StationDecorateTypeEnum.NEW_SELF);
		stationDecorateDto.setPaymentType(partnerInstanceDto.getStationDecoratePaymentTypeEnum());
		stationDecorateBO.addStationDecorate(stationDecorateDto);
		
	}

	@PhaseMeta(descr="开通1688授权")
	public void openAccessCbuMarket(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		try {
			//开通1688授权
			stationDecorateService.openAccessCbuMarket(partnerInstanceDto.getTaobaoUserId());
		} catch (Exception e) {
			logger.error("openAccessCbuMarket error,taobaoUserId"+partnerInstanceDto.getTaobaoUserId(),e);
		}
	}


		@Override
	@PhaseMeta(descr="触发入驻中事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		this.sendPartnerInstanceStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
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
	}

	public LifeCyclePhaseDSL createPhaseDSL() {
		LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
		dsl.then(this::createOrUpdateStation);
		dsl.then(this::createOrUpdatePartner);
		dsl.then(this::createOrUpdatePartnerInstance);
		dsl.then(this::buildLifeCycleItems);
		dsl.then(this::executeExtensionBusiness);
		dsl.then(this::openAccessCbuMarket);
		dsl.then(this::triggerStateChangeEvent);
		return dsl;
	}
	

	
}
