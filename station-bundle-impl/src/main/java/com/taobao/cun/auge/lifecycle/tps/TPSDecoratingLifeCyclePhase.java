package com.taobao.cun.auge.lifecycle.tps;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.FrozenMoneyAmountConfig;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.domain.PartnerStationStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationDecorateBO;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleGoodsReceiptEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleReplenishMoneyEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSystemEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.StationDecorateService;
import com.taobao.cun.auge.store.dto.StoreCategory;
import com.taobao.cun.auge.store.dto.StoreCreateDto;
import com.taobao.cun.auge.store.service.StoreException;
import com.taobao.cun.auge.store.service.StoreWriteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 村小二装修中阶段组件
 *
 */
@Component
@Phase(type="TPS",event=StateMachineEvent.DECORATING_EVENT,desc="村小二装修中服务节点")
public class TPSDecoratingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private StationBO stationBO;
	
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;
    
    @Autowired
    private PartnerLifecycleBO partnerLifecycleBO;
    
    @Autowired
    private StoreWriteService storeWriteService;
    
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    
    @Autowired
    private StationDecorateBO stationDecorateBO;
    
    @Autowired
    private FrozenMoneyAmountConfig frozenMoneyConfig;
    
    @Autowired
    private AccountMoneyBO accountMoneyBO;
    
    @Autowired
	private StationDecorateService stationDecorateService;
    
    private static Logger logger = LoggerFactory.getLogger(TPSDecoratingLifeCyclePhase.class);
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
		//initPartnerLifeCycleForDecorating(context,partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="更新装修中扩展业务信息")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		StationDto station = StationConverter.toStationDto(stationBO.getStationById(partnerInstanceDto.getStationId()));
		 try {
         	StoreCreateDto store = new StoreCreateDto();
         	store.setStationId(partnerInstanceDto.getStationId());
         	store.setCreator(partnerInstanceDto.getOperator());
         	store.setStoreCategory(StoreCategory.valueOf(station.getFeature().get("storeCategory")));
         	store.setCategoryId(diamondConfiguredProperties.getStoreCategoryId());
         	store.setName(station.getName());
			storeWriteService.create(store);
			} catch (StoreException e) {
				logger.error("createStoreError e!instanceId["+partnerInstanceDto.getId()+"]",e);
				throw new AugeSystemException(e);
			}
		 
			try {
				//开通1688授权
				stationDecorateService.openAccessCbuMarket(partnerInstanceDto.getTaobaoUserId());
			} catch (Exception e) {
				logger.error("openAccessCbuMarket error,taobaoUserId"+partnerInstanceDto.getTaobaoUserId(),e);
			}
		 //
	}

	@Override
	@PhaseStepMeta(descr="触发装修中事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId =partnerInstanceDto.getId();
		sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_DECORATING, partnerInstanceDto);
		dispacthEvent(partnerInstanceDto, PartnerInstanceStateEnum.DECORATING.getCode());
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
	
	/**
     * 构建装修中生命周期|装修记录|铺货保证金记录
     *
     * @param rel
     */
    private void initPartnerLifeCycleForDecorating(LifeCyclePhaseContext context,PartnerInstanceDto rel) {
        
        PartnerLifecycleDto partnerLifecycleDto = new PartnerLifecycleDto();
        partnerLifecycleDto.setPartnerType(PartnerInstanceTypeEnum.TPS);
        partnerLifecycleDto.copyOperatorDto(OperatorDto.defaultOperator());
        partnerLifecycleDto.setBusinessType(PartnerLifecycleBusinessTypeEnum.DECORATING);
        partnerLifecycleDto.setCurrentStep(PartnerLifecycleCurrentStepEnum.PROCESSING);
        partnerLifecycleDto.setPartnerInstanceId(rel.getId());
        partnerLifecycleDto.setDecorateStatus(PartnerLifecycleDecorateStatusEnum.N);
        partnerLifecycleDto.setGoodsReceipt(PartnerLifecycleGoodsReceiptEnum.Y);
        partnerLifecycleDto.setReplenishMoney(PartnerLifecycleReplenishMoneyEnum.HAS_FROZEN);
        partnerLifecycleBO.addLifecycle(partnerLifecycleDto);
        
        // 生成装修记录
        StationDecorateDto stationDecorateDto = new StationDecorateDto();
        stationDecorateDto.copyOperatorDto(OperatorDto.defaultOperator());
        stationDecorateDto.setStationId(rel.getStationId());
        stationDecorateDto.setPartnerUserId(rel.getTaobaoUserId());
        stationDecorateDto.setDecorateType(StationDecorateTypeEnum.NEW_SELF);
        stationDecorateDto.setPaymentType(StationDecoratePaymentTypeEnum.SELF);
        stationDecorateBO.addStationDecorate(stationDecorateDto);
        
//        //张振环确认门店没有铺货保证金概念
//        Double waitFrozenMoney = this.frozenMoneyConfig.getTPReplenishMoneyAmount();
//        addWaitFrozenReplienishMoney(rel.getId(), rel.getTaobaoUserId(), waitFrozenMoney);
    }
    
    private void addWaitFrozenReplienishMoney(Long instanceId, Long taobaoUserId, Double waitFrozenMoney) {
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

        AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
        if (dupRecord != null) {
            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
        } else {
            accountMoneyBO.addAccountMoney(accountMoneyDto);
        }
    }
}
