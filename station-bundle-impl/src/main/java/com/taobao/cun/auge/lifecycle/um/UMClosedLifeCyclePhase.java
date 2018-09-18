package com.taobao.cun.auge.lifecycle.um;

import java.util.Date;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优盟关闭组件
 *
 * @author haihu.fhh
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.CLOSED_EVENT, desc = "优盟关闭节点")
public class UMClosedLifeCyclePhase extends AbstractLifeCyclePhase {

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private GeneralTaskSubmitService generalTaskSubmitService;

    @Override
    @PhaseStepMeta(descr = "更新优盟站点状态到已关闭")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState())) {
            stationBO.changeState(partnerInstanceDto.getStationId(), StationStatusEnum.SERVICING,
                StationStatusEnum.CLOSED, partnerInstanceDto.getOperator());
        }
    }

    @Override
    @PhaseStepMeta(descr = "更新村小二信息（无操作）")
    public void createOrUpdatePartner(LifeCyclePhaseContext context) {
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "更新村小二实例状态到已停业")
    public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState())) {
            partnerInstanceDto.setState(PartnerInstanceStateEnum.CLOSED);
            partnerInstanceDto.setServiceEndTime(new Date());
            partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
        }
    }

    @Override
    @PhaseStepMeta(descr = "创建已停业lifeCycleItems")
    public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
    }

    @Override
    @PhaseStepMeta(descr = "扩展业务")
    public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long taobaoUserId = partnerInstanceDto.getTaobaoUserId();
        String taobaoNick = partnerInstanceDto.getPartnerDto().getTaobaoNick();
        PartnerInstanceTypeEnum partnerType = partnerInstanceDto.getType();
        String operatorId = partnerInstanceDto.getOperator();
        Long instanceId = partnerInstanceDto.getId();
        generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick,
            partnerType, operatorId, instanceId);
    }

    @Override
    @PhaseStepMeta(descr = "触发已停业事件变更")
    public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        // 发出合伙人实例状态变更事件
        if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState())) {
            dispatchInstStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.CLOSED,
                partnerInstanceDto);
        }
    }

    private void dispatchInstStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChange,
                                              OperatorDto operator) {
        PartnerInstanceDto partnerInstanceDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
        PartnerInstanceStateChangeEvent event = PartnerInstanceEventConverter.convertStateChangeEvent(stateChange,
            partnerInstanceDto,
            operator);
        EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT, event);
    }
}
