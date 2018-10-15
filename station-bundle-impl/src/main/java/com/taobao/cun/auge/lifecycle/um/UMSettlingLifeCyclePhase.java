package com.taobao.cun.auge.lifecycle.um;

import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.lifecycle.AbstractLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.Phase;
import com.taobao.cun.auge.lifecycle.PhaseStepMeta;
import com.taobao.cun.auge.lifecycle.validator.UmLifeCycleValidator;
import com.taobao.cun.auge.statemachine.StateMachineEvent;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.org.dto.OrgDeptType;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.transfer.dto.TransferState;
import com.taobao.cun.auge.station.transfer.state.CountyTransferStateMgrBo;
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

    @Autowired
    private CountyTransferStateMgrBo countyTransferStateMgrBo;

    @Autowired
    private StationBO stationBO;

    @Override
    @PhaseStepMeta(descr = "创建或更新优盟站点")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        //前置校验
        umLifeCycleValidator.validateSettling(partnerInstanceDto);
        Long stationId = partnerInstanceDto.getStationId();
        if (stationId == null) {
            //创建优盟编号
            StationNumConfigTypeEnum typeEnum = StationNumConfigTypeEnum.UM;
            String stationNum = stationNumConfigBO.createStationNum(
                partnerInstanceDto.getStationDto().getAddress().getProvince(), typeEnum, 0);
            partnerInstanceDto.getStationDto().setStationNum(stationNum);

            //创建优盟门店
            stationId = addUmStation(partnerInstanceDto, StationType.STATION.getType());
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

    public Long addUmStation(PartnerInstanceDto partnerInstanceDto, int stationType) {
        StationDto stationDto = partnerInstanceDto.getStationDto();
        stationDto.setState(StationStateEnum.INVALID);
        stationDto.setStatus(StationStatusEnum.NEW);
        stationDto.copyOperatorDto(partnerInstanceDto);
        stationDto.setStationType(stationType);
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        if (partnerDto != null) {
            stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
            stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
            stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
        }

        stationDto.setOwnDept(
            countyTransferStateMgrBo.getCountyDeptByOrgId(partnerInstanceDto.getStationDto().getApplyOrg()));
        if (stationDto.getOwnDept().equals(OrgDeptType.extdept.name())) {
            stationDto.setTransferState(TransferState.WAITING.name());
        } else {
            stationDto.setTransferState(TransferState.FINISHED.name());
        }
        Long stationId = stationBO.addStation(stationDto);
        partnerInstanceDto.setStationId(stationId);
        if (partnerInstanceDto.getParentStationId() == null) {
            partnerInstanceDto.setParentStationId(stationId);
        }
        return stationId;
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
