package com.taobao.cun.auge.lifecycle.um;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优盟服务中阶段组件
 *
 * @author haihu.fhh
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.SERVICING_EVENT, desc = "优盟服务节点")
public class UMServicingLifeCyclePhase extends AbstractLifeCyclePhase {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private GeneralTaskSubmitService generalTaskSubmitService;

    @Override
    @PhaseStepMeta(descr = "更新村小二站点信息到服务中")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long stationId = partnerInstanceDto.getStationId();
        Station station = stationBO.getStationById(stationId);
        String operator = partnerInstanceDto.getOperator();
        stationBO.changeState(stationId, StationStatusEnum.valueof(station.getStatus()),
            StationStatusEnum.SERVICING, operator);
    }

    @Override
    @PhaseStepMeta(descr = "更新村小二信息(无操作)")
    public void createOrUpdatePartner(LifeCyclePhaseContext context) {
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟实例信息")
    public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        //已停业恢复到服务中
        Long instanceId = partnerInstanceDto.getId();
        String operator = partnerInstanceDto.getOperator();
        if (PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())) {
            partnerInstanceBO.reService(instanceId, PartnerInstanceStateEnum.CLOSED,
                PartnerInstanceStateEnum.SERVICING, operator);
        } else {
            partnerInstanceBO.changeState(instanceId, partnerInstanceDto.getState(),
                PartnerInstanceStateEnum.SERVICING,
                operator);
        }
    }

    @Override
    @PhaseStepMeta(descr = "更新村小二LifeCycleItems")
    public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "更新村小二扩展业务")
    public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        String operatorId = partnerInstanceDto.getOperator();
        generalTaskSubmitService.submitAddUserTagTasks(partnerInstanceDto.getId(), operatorId);
    }

    @Override
    @PhaseStepMeta(descr = "触发服务中状态变更事件")
    public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long instanceId = partnerInstanceDto.getId();
        //未开通  -》 已开通
        if (PartnerInstanceStateEnum.SETTLING.getCode().equals(context.getSourceState())) {
            sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.START_SERVICING,
                partnerInstanceDto);
            //已关闭 -》 已开通
        } else if (PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())) {
            sendPartnerInstanceStateChangeEvent(instanceId, PartnerInstanceStateChangeEnum.CLOSE_TO_SERVICE,
                partnerInstanceDto);
        }

    }
}
