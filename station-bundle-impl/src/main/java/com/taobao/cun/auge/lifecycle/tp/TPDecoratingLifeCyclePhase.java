package com.taobao.cun.auge.lifecycle.tp;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.taobao.cun.auge.lifecycle.common.BaseLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseDSL;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.appResource.dto.AppResourceDto;
import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.FrozenMoneyAmountConfig;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationTransInfo;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.domain.PartnerStationStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.annotation.Phase;
import com.taobao.cun.auge.lifecycle.annotation.PhaseMeta;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.bo.StationTransInfoBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationBizTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.store.service.StoreWriteService;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;

/**
 * 村小二装修中阶段组件
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.DECORATING_EVENT,desc="村小二装修中服务节点")
public class TPDecoratingLifeCyclePhase extends BaseLifeCyclePhase {

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
    
    @Autowired
    AccountMoneyBO accountMoneyBO;
    
    @Autowired
    private FrozenMoneyAmountConfig frozenMoneyConfig;
    
    @Autowired
    private StoreWriteService storeWriteService;
    
    @Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private CloseStationApplyBO closeStationApplyBO;
	
    @Autowired
    private CuntaoNewBailService cuntaoNewBailService;
    
    private static Logger logger = LoggerFactory.getLogger(TPDecoratingLifeCyclePhase.class);
    
    @Autowired
    private StationTransInfoBO stationTransInfoBO;

	public LifeCyclePhaseDSL createPhaseDSL() {
		LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
		dsl.then(this::createOrUpdateStation);
		dsl.then(this::createOrUpdatePartnerInstance);
		dsl.then(this::buildLifeCycleItems);
		dsl.then(this::createOrUpdateExtensionBusiness);
		dsl.then(this::triggerStateChangeEvent);
		return dsl;
	}

	@Override
	@PhaseMeta(descr="更新村点信息")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long stationId = partnerInstanceDto.getStationId();
		StationDto stationDto = new StationDto();
		stationDto.setState(StationStateEnum.NORMAL);
		stationDto.setStatus(StationStatusEnum.DECORATING);
		stationDto.setId(stationId);
		stationDto.copyOperatorDto(partnerInstanceDto);
		
		if (PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerInstanceDto.getState().getCode()) && PartnerInstanceTransStatusEnum.WAIT_TRANS.equals(partnerInstanceDto.getTransStatusEnum())){
			StationTransInfo sti = stationTransInfoBO.getLastTransInfoByStationId(stationId);
		    if (sti != null && StationBizTypeEnum.YOUPIN_ELEC.getCode().equals(sti.getToBizType())) {
		    	stationDto.setCategory("ELEC");
		    }
		}
		stationBO.updateStation(stationDto);
	}


	@Override
	@PhaseMeta(descr="更新村小二实例状态到装修中")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstanceDto.getState().getCode())){
			partnerInstanceBO.changeState(partnerInstanceDto.getId(), partnerInstanceDto.getState(), PartnerInstanceStateEnum.DECORATING,partnerInstanceDto.getOperator());
		}else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(partnerInstanceDto.getState().getCode()) && PartnerInstanceTransStatusEnum.WAIT_TRANS.equals(partnerInstanceDto.getTransStatusEnum())){
			//服务站转型，主状态是服务中，  转型状态 已经 在关系更新的时候变为转型中
			setPartnerInstanceToDecoratingForTrans(partnerInstanceDto,partnerInstanceDto,null);
		}else{
			setPartnerInstanceToDecorating(partnerInstanceDto,partnerInstanceDto,null);
		}
	}

	@PhaseMeta(descr="更新LifeCycleItems")
	public void buildLifeCycleItems(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(partnerInstanceDto.getState().getCode())){
			PartnerLifecycleItems partnerLifecycleItem = partnerLifecycleBO.getLifecycleItems(partnerInstanceDto.getId(),PartnerLifecycleBusinessTypeEnum.CLOSING);
			PartnerLifecycleDto partnerLifecycle = new PartnerLifecycleDto();
			partnerLifecycle.setLifecycleId(partnerLifecycleItem.getId());
			partnerLifecycle.setCurrentStep(PartnerLifecycleCurrentStepEnum.END);
			partnerLifecycle.copyOperatorDto(partnerInstanceDto);
			if(PartnerInstanceCloseTypeEnum.PARTNER_QUIT.getCode().equals(partnerInstanceDto.getCloseType().getCode())){
				partnerLifecycle.setConfirm(PartnerLifecycleConfirmEnum.CANCEL);
			}
			if(PartnerInstanceCloseTypeEnum.WORKER_QUIT.getCode().equals(partnerInstanceDto.getCloseType().getCode())){
				partnerLifecycle.setRoleApprove(PartnerLifecycleRoleApproveEnum.AUDIT_NOPASS);
			}
	        partnerLifecycleBO.updateLifecycle(partnerLifecycle);
		}else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState()) && PartnerInstanceTransStatusEnum.WAIT_TRANS.equals(partnerInstanceDto.getTransStatusEnum())){
			initPartnerLifeCycleForDecorating(context,partnerInstanceDto);
		}else{
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
			initPartnerLifeCycleForDecorating(context,partnerInstanceDto);
		}
	}

	@Override
	@PhaseMeta(descr="扩展业务：创建共享门店")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			   // 合伙人停业审核拒绝了，删除停业协议
			  //合伙人发起的删除停业协议
			 if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(partnerInstanceDto.getCloseType())){
				 partnerProtocolRelBO.cancelProtocol(partnerInstanceDto.getTaobaoUserId(), ProtocolTypeEnum.PARTNER_QUIT_PRO, partnerInstanceDto.getId(),
			     PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceDto.getOperator());
			 }
	       
	        // 删除停业申请单
	        closeStationApplyBO.deleteCloseStationApply(partnerInstanceDto.getId(), partnerInstanceDto.getOperator());
		}else{
			storeWriteService.createSupplyStoreByStationId(partnerInstanceDto.getStationId());
//			if(!result){
//				logger.error("createStationSupplyStore error["+partnerInstanceDto.getStationId()+"]");
//			}
			//转型过来的 不控制
			try {
				storeWriteService.addWhiteListForSHRH(partnerInstanceDto.getTaobaoUserId());
			} catch (Exception e) {
			}
		}
		
	}

	@Override
	@PhaseMeta(descr="触发装修中事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId =partnerInstanceDto.getId();
		if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, partnerInstanceDto);
		}else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState()) && PartnerInstanceTransStatusEnum.WAIT_TRANS.equals(partnerInstanceDto.getTransStatusEnum())){
			//服务站转型，主状态是服务中，  转型状态 已经 在关系更新的时候变为转型中
			sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.TRANS_DECORATING, partnerInstanceDto);
		}else{
			sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_DECORATING, partnerInstanceDto);
			dispacthEvent(partnerInstanceDto, PartnerInstanceStateEnum.DECORATING.getCode());
		}
	}

	/**
	 * 发送装修中事件 给手机端使用
	 * 
	 * @param rel
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
	private void initPartnerLifeCycleForDecorating(LifeCyclePhaseContext context,PartnerInstanceDto rel) {
		
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
		if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState()) && PartnerInstanceTransStatusEnum.WAIT_TRANS.equals(rel.getTransStatusEnum())){
			// 生成装修记录
			StationDecorateDto stationDecorateDto = new StationDecorateDto();
			stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
			stationDecorateDto.setStationId(rel.getStationId());
			stationDecorateDto.setPartnerUserId(rel.getTaobaoUserId());
			stationDecorateDto.setDecorateType(StationDecorateTypeEnum.NEW_SELF);
			stationDecorateDto.setPaymentType(StationDecoratePaymentTypeEnum.SELF);
			stationDecorateBO.addStationDecorate(stationDecorateDto);
			
			partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
		}else {
			boolean hasDecorateDone = stationDecorateBO.handleAcessDecorating(rel.getStationId());
			if (hasDecorateDone) {
				partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.Y);
			}else {
				partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
			}
		}
		partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
		
	}
	
	 private void addWaitFrozenReplienishMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney, String accountNo, String alipayId) {
	        ValidateUtils.notNull(instanceId);
	        AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
	        accountMoneyDto.setMoney(BigDecimal.valueOf(waitFrozenMoney));
	        accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
	        accountMoneyDto.setOperatorType(OperatorTypeEnum.HAVANA);
	        accountMoneyDto.setObjectId(instanceId);
	        accountMoneyDto.setState(AccountMoneyStateEnum.WAIT_FROZEN);
	        accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
	        accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
	        accountMoneyDto.setType(AccountMoneyTypeEnum.REPLENISH_MONEY);
	        if(!StringUtils.isEmpty(accountNo)){
	            accountMoneyDto.setAlipayAccount(accountNo);
	        }
	        if(!StringUtils.isEmpty(alipayId)){
                accountMoneyDto.setAccountNo(alipayId);
            }
	        AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
	                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
	        if (dupRecord != null) {
	            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
	        } else {
	            accountMoneyBO.addAccountMoney(accountMoneyDto);
	        }
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
	
	private void setPartnerInstanceToDecoratingForTrans(PartnerInstanceDto rel, OperatorDto operatorDto, Long changePartnerId) {
//		Calendar now = Calendar.getInstance();// 得到一个Calendar的实例
//		Date serviceBeginTime = now.getTime();
//		now.add(Calendar.YEAR, 1);
//		Date serviceEndTime = now.getTime();

		PartnerInstanceDto piDto = new PartnerInstanceDto();
//		piDto.setServiceBeginTime(serviceBeginTime);
//		piDto.setServiceEndTime(serviceEndTime);
		piDto.setId(rel.getId());
		piDto.setState(PartnerInstanceStateEnum.DECORATING);
		piDto.setVersion(rel.getVersion());
		piDto.setParentStationId(rel.getStationId());
		piDto.setTransStatusEnum(PartnerInstanceTransStatusEnum.TRANS_ING);
		piDto.setMode(StationModeEnum.V4.getCode());
		piDto.copyOperatorDto(operatorDto);
		if (changePartnerId != null) {
			piDto.setPartnerId(changePartnerId);
		}
		partnerInstanceBO.updatePartnerStationRel(piDto);
		//更新转型记录
		stationTransInfoBO.changeTransing(rel.getStationId(), operatorDto.getOperator());
		//上下文需要
		rel.setMode(StationModeEnum.V4.getCode());
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
