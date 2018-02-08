package com.taobao.cun.auge.station.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.taobao.cun.appResource.service.AppResourceService;
import com.taobao.cun.auge.common.Address;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.KFCServiceConfig;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationStatusChangeEvent;
import com.taobao.cun.auge.event.enums.StationStatusChangeEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.validator.LifeCycleValidator;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.check.StationChecker;
import com.taobao.cun.auge.station.convert.StationEventConverter;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationModeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.StationService;
import com.taobao.cun.auge.station.validate.StationValidator;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service("stationService")
@HSFProvider(serviceInterface = StationService.class)
public class StationServiceImpl implements StationService {

	private static final Logger logger = LoggerFactory.getLogger(StationService.class);

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	ShutDownStationApplyBO shutDownStationApplyBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	
	@Autowired
	StationChecker stationChecker;
	
    @Autowired
    AppResourceService appResourceService;
    
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    
    @Autowired
    private StoreReadBO storeReadBO;
    
    @Autowired
    private LifeCycleValidator lifeCycleValidator;
    
    @Autowired
    KFCServiceConfig kfcServiceConfig;
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void auditQuitStation(Long stationId, ProcessApproveResultEnum approveResult){
		if (null == stationId) {
			logger.error("stationId is null");
			throw new IllegalArgumentException("stationId is null");
		}

		// 使用默认的系统操作人
		OperatorDto operatorDto = OperatorDto.defaultOperator();
		String operator = operatorDto.getOperator();

		// 审批结果
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);
			generalTaskSubmitService.submitShutdownApprovedTask(stationId);
			
			// 发出同意撤点事件
			dispatchStationStatusChangeEvent(stationId, StationStatusChangeEnum.QUIT, operatorDto);
		} else {
			// 删除撤点申请单
			shutDownStationApplyBO.deleteShutDownStationApply(stationId, operator);
			// 村点状态变更为已停业
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
			
			// 发出拒绝撤点事件
			dispatchStationStatusChangeEvent(stationId, StationStatusChangeEnum.QUITTING_REFUSED, operatorDto);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyShutDownStationByManager(ShutDownStationApplyDto shutDownDto){
		BeanValidator.validateWithThrowable(shutDownDto);

		Long stationId = shutDownDto.getStationId();
		
		Station station = stationBO.getStationById(stationId);
		if (null == station) {
			logger.warn("村点不存在。stationId=" + stationId);
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"村点不存在");
		}
	    
		// 校验村点撤点的前提提交
	    stationChecker.checkShutdownApply(stationId);

		// 撤点申请中
		stationBO.changeState(stationId, StationStatusEnum.CLOSED, StationStatusEnum.QUITING,
				shutDownDto.getOperator());

		// 保存申请单
		Long applyId = shutDownStationApplyBO.saveShutDownStationApply(shutDownDto);

		// 插入启动撤点流程的任务
		ApproveProcessTask processTask = new ApproveProcessTask();
		processTask.setBusiness(ProcessBusinessEnum.SHUT_DOWN_STATION);
		processTask.setBusinessId(stationId);
		processTask.setBusinessName(station.getName());
		processTask.setBusinessOrgId(station.getApplyOrg());
		processTask.copyOperatorDto(shutDownDto);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyId", String.valueOf(applyId));
		processTask.setParams(params);
		generalTaskSubmitService.submitApproveProcessTask(processTask);
		
		 //发出撤点申请事件
	    dispatchStationStatusChangeEvent(stationId,StationStatusChangeEnum.START_QUITTING,shutDownDto);
	}
	
	private void dispatchStationStatusChangeEvent(Long stationId, StationStatusChangeEnum statusChangeEnum,	OperatorDto operator) {
		Station station = stationBO.getStationById(stationId);
		StationStatusChangeEvent event = StationEventConverter.convert(statusChangeEnum, station, operator);
		EventDispatcherUtil.dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT, event);
	}

	/**
     * 服务站物流能力信息维护
     * 
     * @param stationDto
     * @param feature key:stMaxStorage,stMaxDoStorage,stStorageArea,stStaffNum
     */
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    public void applyLogisticAbility(StationDto stationDto) {
	    if(stationDto != null && stationDto.getFeature() != null){
	       stationBO.updateStation(stationDto);
	       Map<String,String> res = stationDto.getFeature();
	       StringBuffer sb  = new StringBuffer();
	       sb.append(res.get("stMaxStorage"));
	       sb.append("|");
	       sb.append(res.get("stMaxDoStorage"));
	       sb.append("|");
	       sb.append(res.get("stStorageArea"));
	       sb.append("|");
	       sb.append(res.get("stStaffNum"));
	       sb.append("|");
	       sb.append(res.get("stVAS"));
	       appResourceService.configAppResource("station_logstic_ability", stationDto.getId().toString(), sb.toString(), false, stationDto.getOperator());
	    }
    }

	//根据站点模式获取站点名称后缀
    public String getStationNameSuffix(Long stationId,String key) {
        //stationId为空代表新增的场景，由外围参数决定返回类型MOMBABY|ELEC|FMCG:门店   v4:优品服务站  v3:农村淘宝服务站
        if(stationId == null || stationId == 0L){
            return diamondConfiguredProperties.getStationNameMap().get(key);
        }else{
            PartnerStationRel partnerStationRel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
            if(partnerStationRel == null){
                return null;
            } 
            if(partnerStationRel.getType().equals(PartnerInstanceTypeEnum.TPS.getCode())){
                StoreDto store = storeReadBO.getStoreDtoByStationId(stationId);
                if(store == null){
                    return "";
                }
                return diamondConfiguredProperties.getStationNameMap().get(store.getCategory());
            }else if(partnerStationRel.getType().equals(PartnerInstanceTypeEnum.TP.getCode()) && StationModeEnum.V4.getCode().equals(partnerStationRel.getMode())){
               return diamondConfiguredProperties.getStationNameMap().get(StationModeEnum.V4.getCode());
            }else{
               return diamondConfiguredProperties.getStationNameMap().get(StationModeEnum.V3.getCode());
            }
        }
    }

    public boolean getStationInfoValidateRule(Long instanceId, StationDto station) {
        Assert.notNull(instanceId);
        BeanValidator.validateWithThrowable(station);
        //name && address base validate 
        StationValidator.nameFormatCheck(station.getName());
        //param reset need validate station contain name and address
        PartnerInstanceDto ins = partnerInstanceBO.getPartnerInstanceById(instanceId);
        if(station.getAddress() != null){
        	Address add = station.getAddress();
        	add.setProvince(ins.getStationDto().getAddress().getProvince());
            StationValidator.addressFormatCheck(add);
          
        }else{
            Address add = new Address();
            add.setProvince(ins.getStationDto().getAddress().getProvince());
            station.setAddress(add);
        }
        station.setId(ins.getStationId());
        String nameSuffix = station.getNameSuffix()==null?"":station.getNameSuffix();
        station.setName(station.getName()+nameSuffix);
        ins.setStationDto(station);
        lifeCycleValidator.stationModelBusCheck(ins);
        return true;
    }

}
