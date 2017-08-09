package com.taobao.cun.auge.lifecycle.tp;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceLevelEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 合伙人入驻中阶段组件
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
	
	private static final int DEFAULT_EVALUATE_INTERVAL = 6;
	@Override
	@PhaseStepMeta(descr="更新村小二站点信息到服务中")
	public void createOrUpdateStation(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.DECORATING, StationStatusEnum.SERVICING, partnerInstanceDto.getOperator());
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
		Date openDate = partnerInstanceDto.getOpenDate();
		  // 更新合伙人实例状态为服务中
        partnerInstanceBO.changeState(partnerInstanceDto.getId(), PartnerInstanceStateEnum.DECORATING, PartnerInstanceStateEnum.SERVICING,
        		partnerInstanceDto.getOperator());
        // 更新开业时间
        partnerInstanceBO.updateOpenDate(partnerInstanceDto.getId(), openDate,partnerInstanceDto.getOperator());
        
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
		//do nothing
	}

	@Override
	@PhaseStepMeta
	public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		initPartnerInstanceLevel(partnerInstanceDto);
	}

	@Override
	@PhaseStepMeta(descr="触发服务中状态变更事件")
	public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
		PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
		Long instanceId = partnerInstanceDto.getId();
		 // 记录村点状态变化
        sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING, partnerInstanceDto);
        // 开业包项目事件
        dispachToServiceEvent(partnerInstanceDto, instanceId);
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
}
