package com.taobao.cun.auge.lifecycle.tp;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceCloseTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleConfirmEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;

/**
 * 村小二服务中阶段组件
 * @author zhenhuan.zhangzh
 *
 */
@Component
@Phase(type="TP",event=StateMachineEvent.SERVICING_EVENT,desc="村小二停业中服务节点")
public class TPServicingLifeCyclePhase extends AbstractLifeCyclePhase{

	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private PartnerInstanceLevelBO partnerInstanceLevelBO;
	
	@Autowired
	private PartnerLifecycleBO partnerLifecycleBO;
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private CloseStationApplyBO closeStationApplyBO;
	
	@Autowired
	private PartnerBO partnerBO;
	
	@Autowired
	private GeneralTaskSubmitService generalTaskSubmitService;
	
	private static final int DEFAULT_EVALUATE_INTERVAL = 6;
	@Override
	@PhaseStepMeta(descr="更新村小二站点信息到服务中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Station station = stationBO.getStationById(partnerInstanceDto.getStationId());
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.valueof(station.getStatus()), StationStatusEnum.SERVICING, partnerInstanceDto.getOperator());
		if(PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())){
			   //防止有垃圾数据 导致  staiton实体信息 不一致，更新成  当前人的信息
	        StationDto stationDto = new StationDto();
	        stationDto.setId(partnerInstanceDto.getStationId());
	        stationDto.copyOperatorDto(OperatorDto.defaultOperator());
	        stationDto.setState(StationStateEnum.NORMAL);
	        Partner p = partnerBO.getPartnerById(partnerInstanceDto.getPartnerId());
	        stationDto.setTaobaoNick(p.getTaobaoNick());
	        stationDto.setTaobaoUserId(p.getTaobaoUserId());
	        stationDto.setAlipayAccount(p.getAlipayAccount());
	        stationBO.updateStation(stationDto);
		}
		
	
	}

	@Override
	@PhaseStepMeta(descr="更新村小二信息(无操作)")
	public void createOrUpdatePartner(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二实例信息")
	public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		//已停业恢复到服务中
		if(PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())){
	        	partnerInstanceBO.reService(partnerInstanceDto.getId(), PartnerInstanceStateEnum.CLOSED, PartnerInstanceStateEnum.SERVICING, partnerInstanceDto.getOperator());
	     }else{
	    	   partnerInstanceBO.changeState(partnerInstanceDto.getId(), partnerInstanceDto.getState(), PartnerInstanceStateEnum.SERVICING,
	           		partnerInstanceDto.getOperator());
	     }
     
        //装修中到服务中更新开业事件
        if(PartnerInstanceStateEnum.DECORATING.getCode().equals(context.getSourceState())){
        	Date openDate = partnerInstanceDto.getOpenDate();
            // 更新开业时间
            partnerInstanceBO.updateOpenDate(partnerInstanceDto.getId(), openDate,partnerInstanceDto.getOperator());
        }
      
	}

	@Override
	@PhaseStepMeta(descr="更新村小二LifeCycleItems")
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
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
		}
		//do nothing
	}

	@Override
	@PhaseStepMeta(descr="更新村小二扩展业务")
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		if(PartnerInstanceStateEnum.DECORATING.getCode().equals(context.getSourceState())){
			initPartnerInstanceLevel(partnerInstanceDto);
		}else if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			   // 合伙人停业审核拒绝了，删除停业协议
			  //合伙人发起的删除停业协议
			 if (PartnerInstanceCloseTypeEnum.PARTNER_QUIT.equals(partnerInstanceDto.getCloseType())){
				 partnerProtocolRelBO.cancelProtocol(partnerInstanceDto.getTaobaoUserId(), ProtocolTypeEnum.PARTNER_QUIT_PRO, partnerInstanceDto.getId(),
			     PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceDto.getOperator());
			 }
	       
	        // 删除停业申请单
	        closeStationApplyBO.deleteCloseStationApply(partnerInstanceDto.getId(), partnerInstanceDto.getOperator());
		}else if(PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())){
			 closeStationApplyBO.deleteCloseStationApply(partnerInstanceDto.getId(), partnerInstanceDto.getOperator());
			 generalTaskSubmitService.submitCloseToServiceTask(partnerInstanceDto.getId(), partnerInstanceDto.getTaobaoUserId(), partnerInstanceDto.getType(), partnerInstanceDto.getOperator());
		}
		
	}

	@Override
	@PhaseStepMeta(descr="触发服务中状态变更事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId = partnerInstanceDto.getId();
		if(PartnerInstanceStateEnum.DECORATING.getCode().equals(context.getSourceState())){
			 //记录村点状态变化
	        sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, partnerInstanceDto);
	         //开业包项目事件
	        dispachToServiceEvent(partnerInstanceDto, instanceId);
		}else if(PartnerInstanceStateEnum.CLOSING.getCode().equals(context.getSourceState())){
			 dispatchInstStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSING_REFUSED, partnerInstanceDto);
		}else if(PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())){
			 PartnerInstanceStateChangeEvent event = buildCloseToServiceEvent(partnerInstanceDto, partnerInstanceDto.getOperator());
		     EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
		}
		
		
	}

	@Override
	@PhaseStepMeta
	public void syncStationApply(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		syncStationApply(SyncStationApplyEnum.UPDATE_BASE, partnerInstanceDto.getId());
	}

	
	private void dispachToServiceEvent(PartnerInstanceDto partnerInstanceDto, Long instanceId) {
        PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent partnerInstanceEvent = new PartnerInstanceStateChangeEvent();
        partnerInstanceEvent.setExecDate(com.taobao.cun.auge.common.utils.DateUtil.format(partnerInstanceDto.getOpenDate()));
        partnerInstanceEvent.setOwnOrgId(piDto.getStationDto().getApplyOrg());
        partnerInstanceEvent.setTaobaoUserId(piDto.getTaobaoUserId());
        partnerInstanceEvent.setTaobaoNick(piDto.getPartnerDto().getTaobaoNick());
        partnerInstanceEvent.setStationId(piDto.getStationId());
        partnerInstanceEvent.setStationName(piDto.getStationDto().getStationNum());
        partnerInstanceEvent.setOperator(partnerInstanceDto.getOperator());
        EventDispatcherUtil.dispatch("STATION_TO_SERVICE_EVENT", partnerInstanceEvent);
	}
	
	private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange, OperatorDto operator) {
	        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
	        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange, partnerInstanceDto,
	                operator);
	        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
	  }
	  
	  
	 private void initPartnerInstanceLevel(PartnerInstanceDto partnerInstanceDto) {
	        PartnerInstanceLevelDto dto = new PartnerInstanceLevelDto();
	        dto.setPartnerInstanceId(partnerInstanceDto.getId());
	        dto.setTaobaoUserId(partnerInstanceDto.getTaobaoUserId());
	        dto.setStationId(partnerInstanceDto.getStationId());
	        dto.setCurrentLevel(PartnerInstanceLevelEnum.S_4);
	        dto.setEvaluateBy(OperatorDto.DEFAULT_OPERATOR);
	        Calendar c = Calendar.getInstance();
	        Date today = c.getTime();
	        dto.setEvaluateDate(today);

	        c.add(Calendar.MONTH, DEFAULT_EVALUATE_INTERVAL);
	        int day = c.get(Calendar.DAY_OF_MONTH);
	        if (day > 1) {
	            c.add(Calendar.MONTH, 1);
	        }
	        c.set(Calendar.DAY_OF_MONTH, 1);
	        dto.setNextEvaluateDate(c.getTime());
	        dto.copyOperatorDto(OperatorDto.defaultOperator());
	        Station station = stationBO.getStationById(partnerInstanceDto.getStationId());
	        dto.setCountyOrgId(station.getApplyOrg());
	        partnerInstanceLevelBO.addPartnerInstanceLevel(dto);
	    }
	 
	 private PartnerInstanceStateChangeEvent buildCloseToServiceEvent(PartnerInstanceDto partnerInstanceDto, String operator) {
	        PartnerInstanceStateChangeEvent event = new PartnerInstanceStateChangeEvent();
	        event.setPartnerType(partnerInstanceDto.getType());
	        event.setTaobaoUserId(partnerInstanceDto.getTaobaoUserId());
	        event.setStationId(partnerInstanceDto.getStationId());
	        event.setPartnerInstanceId(partnerInstanceDto.getId());
	        event.setStateChangeEnum(PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE);
	        event.setOperator(operator);
	        event.setOperatorType(OperatorTypeEnum.SYSTEM);
	        return event;
	    }
}
