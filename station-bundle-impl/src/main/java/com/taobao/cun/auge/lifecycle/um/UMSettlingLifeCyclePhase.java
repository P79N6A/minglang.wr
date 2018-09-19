package com.taobao.cun.auge.lifecycle.um;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.lifecycle.validator.UmLifeCycleValidator;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.validate.StationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 优盟入驻中阶段组件
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.SETTLING_EVENT, desc = "优盟入驻中服务节点")
public class UMSettlingLifeCyclePhase extends AbstractLifeCyclePhase {

    @Autowired
    private StationNumConfigBO stationNumConfigBO;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Autowired
    private UmLifeCycleValidator umLifeCycleValidator;

    @Override
    @PhaseStepMeta(descr = "创建或更新优盟站点")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        //前置校验
        umLifeCycleValidator.validateSettling(partnerInstanceDto);
        Long stationId = partnerInstanceDto.getStationId();
        if (stationId == null) {
            //优盟门店名称校验
            StationValidator.nameFormatCheck(partnerInstanceDto.getStationDto().getName());

            //创建优盟编号
            StationNumConfigTypeEnum typeEnum = StationNumConfigTypeEnum.UM;
            String stationNum = stationNumConfigBO.createStationNum(
                partnerInstanceDto.getStationDto().getAddress().getProvince(), typeEnum, 0);
            partnerInstanceDto.getStationDto().setStationNum(stationNum);

            //创建优盟门店
            stationId = addStation(partnerInstanceDto, StationType.STATION.getType());
            stationNumConfigBO.updateSeqNumByStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(),
                typeEnum, stationNum);
            partnerInstanceDto.setStationId(stationId);
        } else {
            StationDto stationDto = partnerInstanceDto.getStationDto();
            stationDto.setState(StationStateEnum.INVALID);
            stationDto.setStatus(StationStatusEnum.NEW);
            PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
            if (partnerDto != null) {
                stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
                stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
                stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
            }
            updateStation(stationId, partnerInstanceDto);
        }
    }

    @Override
    @PhaseStepMeta(descr = "创建优盟")
    public void createOrUpdatePartner(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        addPartner(partnerInstanceDto);
    }

    @Override
    @PhaseStepMeta(descr = "创建人村关系")
    public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        addPartnerInstanceRel(partnerInstanceDto);
    }

    @Override
    @PhaseStepMeta(descr = "创建lifeCycleItems")
    public void createOrUpdateLifeCycleItems(LifeCyclePhaseContext context) {
    }

    @Override
    @PhaseStepMeta(descr = "创建培训装修记录")
    public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {

    }

    @Override
    @PhaseStepMeta(descr = "触发入驻中事件")
    public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long partnerInstanceDtoId = partnerInstanceDto.getId();
        sendPartnerInstanceStateChangeEvent(partnerInstanceDtoId,
            PartnerInstanceStateChangeEnum.START_SETTLING, partnerInstanceDto);
    }
}
