package com.taobao.cun.auge.lifecycle.um;

import java.util.Date;

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
    @PhaseStepMeta(descr = "更新优盟站点信息到服务中")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long stationId = partnerInstanceDto.getStationId();
        StationStatusEnum curStatus = partnerInstanceDto.getStationDto().getStatus();
        String operator = partnerInstanceDto.getOperator();
        stationBO.changeState(stationId, curStatus, StationStatusEnum.SERVICING, operator);
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟信息(无操作)")
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
            setPartnerInstanceToServicing(partnerInstanceDto);
        }
    }

    /**
     * 第一次进入服务中
     *
     * @param rel
     */
    private void setPartnerInstanceToServicing(PartnerInstanceDto rel) {
        PartnerInstanceDto piDto = new PartnerInstanceDto();
        Date serviceBeginTime = new Date();
        piDto.setServiceBeginTime(serviceBeginTime);
        piDto.setOpenDate(serviceBeginTime);
        piDto.setId(rel.getId());
        piDto.setState(PartnerInstanceStateEnum.SERVICING);
        piDto.setVersion(rel.getVersion());
        piDto.copyOperatorDto(rel);

        partnerInstanceBO.updatePartnerStationRel(piDto);
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟LifeCycleItems")
    public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
        //do nothing
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟扩展业务")
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
