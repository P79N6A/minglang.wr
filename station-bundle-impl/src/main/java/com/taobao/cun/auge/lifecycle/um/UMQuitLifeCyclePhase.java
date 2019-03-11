package com.taobao.cun.auge.lifecycle.um;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优盟退出组件
 *
 * @author haihu.fhh
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.QUIT_EVENT, desc = "优盟退出节点")
public class UMQuitLifeCyclePhase extends AbstractLifeCyclePhase {

    @Autowired
    private StationBO stationBO;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Override
    @PhaseStepMeta(descr = "更新优盟站点状态到已退出")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();

        if (PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())) {
            Long stationId = partnerInstanceDto.getStationId();
            String operator = partnerInstanceDto.getOperator();
            stationBO.changeState(stationId, StationStatusEnum.CLOSED, StationStatusEnum.QUIT, operator);
        }
    }

    @Override
    @PhaseStepMeta(descr = "更新优盟实例状态到已退出")
    public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        if (PartnerInstanceStateEnum.CLOSED.getCode().equals(context.getSourceState())) {
            partnerInstanceDto.setState(PartnerInstanceStateEnum.QUIT);
            partnerInstanceBO.updatePartnerStationRel(partnerInstanceDto);
        }
    }
}
