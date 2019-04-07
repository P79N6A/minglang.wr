package com.taobao.cun.auge.lifecycle.um;

import com.taobao.cun.auge.lifecycle.LifeCyclePhaseDSL;
import com.taobao.cun.auge.lifecycle.common.CommonLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.annotation.Phase;
import com.taobao.cun.auge.lifecycle.annotation.PhaseStepMeta;
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
 * @author haihu.fhh jianke.ljk
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.QUIT_EVENT, desc = "优盟退出节点")
public class UMQuitLifeCyclePhase extends CommonLifeCyclePhase {

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

    public LifeCyclePhaseDSL createPhaseDSL() {
        LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
        dsl.then(this::createOrUpdateStation);
        dsl.then(this::createOrUpdatePartnerInstance);
        return dsl;
    }
}
