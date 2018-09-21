package com.taobao.cun.auge.lifecycle.um;

import java.util.Date;

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
            Long stationId = partnerInstanceDto.getStationId();
            String operator = partnerInstanceDto.getOperator();
            stationBO.changeState(stationId, StationStatusEnum.SERVICING, StationStatusEnum.CLOSED, operator);
        }
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟信息")
    public void createOrUpdatePartner(LifeCyclePhaseContext context) {
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟实例状态到已停业")
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
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "扩展业务")
    public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();

        Long instanceId = partnerInstanceDto.getId();
        String operatorId = partnerInstanceDto.getOperator();
        //因为上面的partnerInstanceDto 里taobaoNick被脱敏了，所以重新查一遍
        PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(instanceId);
        Long taobaoUserId = instance.getTaobaoUserId();
        String taobaoNick = instance.getPartnerDto().getTaobaoNick();
        PartnerInstanceTypeEnum partnerType = instance.getType();
        generalTaskSubmitService.submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId,
            instanceId);
    }

    @Override
    @PhaseStepMeta(descr = "触发已停业事件变更")
    public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        // 发出合伙人实例状态变更事件
        if (PartnerInstanceStateEnum.SERVICING.getCode().equals(context.getSourceState())) {
            sendPartnerInstanceStateChangeEvent(partnerInstanceDto.getId(), PartnerInstanceStateChangeEnum.CLOSED,
                partnerInstanceDto);
        }
    }
}
