package com.taobao.cun.auge.lifecycle.tps;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecyclePositionConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.validate.StationValidator;
import com.taobao.cun.auge.store.dto.StoreCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 村小二入驻中阶段组件
 *
 */
@Component
@Phase(type="TPS",event=StateMachineEvent.SETTLING_EVENT,desc="村小二入驻中服务节点")
public class TPSSettlingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private LifeCycleValidator lifeCycleValidator;
	
	@Autowired
	private StationNumConfigBO stationNumConfigBO;
	
	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;
	@Override
	@PhaseStepMeta(descr="创建村小二站点")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		  PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		  lifeCycleValidator.validateSettling(partnerInstanceDto);
		  Long stationId = partnerInstanceDto.getStationId();
          if (stationId == null) {
              StationValidator.nameFormatCheck(partnerInstanceDto.getStationDto().getName());
              String storeCategory= partnerInstanceDto.getStationDto().getFeature().get("storeCategory");
              Assert.notNull(storeCategory,"storeCategroy is  null");
              StationNumConfigTypeEnum typeEnum = null;
              if(StoreCategory.ELEC.getCategory().equals(storeCategory)){
            	  typeEnum = StationNumConfigTypeEnum.D;
              }else if (StoreCategory.MOMBABY.getCategory().equals(storeCategory)) {
            	  typeEnum = StationNumConfigTypeEnum.M;
              }
              
              String stationNum = stationNumConfigBO.createStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(), typeEnum, 0);
              partnerInstanceDto.getStationDto().setStationNum(stationNum);
              stationId = addStation(partnerInstanceDto,StationType.STATION.getType()|StationType.STORE.getType());
              stationNumConfigBO.updateSeqNumByStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(), typeEnum, stationNum);
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
		partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPS);
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

	
	
	

	
}
