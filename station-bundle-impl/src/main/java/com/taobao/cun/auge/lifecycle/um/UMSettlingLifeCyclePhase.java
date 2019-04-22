package com.taobao.cun.auge.lifecycle.um;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.lifecycle.annotation.Phase;
import com.taobao.cun.auge.lifecycle.annotation.PhaseMeta;
import com.taobao.cun.auge.lifecycle.common.BaseLifeCyclePhase;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseContext;
import com.taobao.cun.auge.lifecycle.common.LifeCyclePhaseDSL;
import com.taobao.cun.auge.lifecycle.statemachine.StateMachineEvent;
import com.taobao.cun.auge.lifecycle.validator.UmLifeCycleValidator;
import com.taobao.cun.auge.lock.ManualReleaseDistributeLock;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.bo.StationNumConfigBO;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.StationNumConfigTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;

/**
 * 优盟入驻中阶段组件
 */
@Component
@Phase(type = "UM", event = StateMachineEvent.SETTLING_EVENT, desc = "优盟入驻中节点")
public class UMSettlingLifeCyclePhase extends BaseLifeCyclePhase {

    private static final Logger logger = LoggerFactory.getLogger(UMSettlingLifeCyclePhase.class);

    @Autowired
    private StationNumConfigBO stationNumConfigBO;

    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    @Autowired
    private UmLifeCycleValidator umLifeCycleValidator;

    @Autowired
    private StationBO stationBO;

    @Autowired
    private GeneralTaskSubmitService generalTaskSubmitService;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private ManualReleaseDistributeLock distributeLock;

    @Override
    @PhaseMeta(descr = "创建或更新优盟站点")
    public void createOrUpdateStation(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        //前置校验
        umLifeCycleValidator.validateSettling(partnerInstanceDto);
        Long stationId = partnerInstanceDto.getStationId();
        if (stationId == null) {
            //创建优盟编号
            StationNumConfigTypeEnum typeEnum = StationNumConfigTypeEnum.UM;

            String province = partnerInstanceDto.getStationDto().getAddress().getProvince();
            String stationNum = stationNumConfigBO.createUmStationNum(province, typeEnum, 0);
            boolean lockResult= false;
            try {
                lockResult = distributeLock.lock("unionMember-station-num", stationNum, 5);
                if (!lockResult) {
                    logger.error("distributeLock failed: {}", stationNum);
                    throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "请稍后重试");
                }
                partnerInstanceDto.getStationDto().setStationNum(stationNum);

                //创建优盟门店
                stationId = addUmStation(partnerInstanceDto, StationType.STATION.getType());
//                stationNumConfigBO.updateSeqNumByStationNum(partnerInstanceDto.getStationDto().getAddress().getProvince(),
//                        typeEnum, stationNum);
                partnerInstanceDto.setStationId(stationId);
            } catch (Exception e) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "创建优盟失败，请稍后重试");
            } finally {
                if(lockResult){
                    distributeLock.unlock("unionMember-station-num", stationNum);
                }
            }
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
        stationDto.setState(StationStateEnum.NORMAL);
        stationDto.setStatus(StationStatusEnum.SERVICING);
        stationDto.copyOperatorDto(partnerInstanceDto);
        stationDto.setStationType(stationType);
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        if (partnerDto != null) {
            stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
            stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
            stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
        }

        stationDto.setOwnDept("opdept");
        stationDto.setTransferState("FINISHED");
        checkUmStationNumDuplicate(stationDto.getStationNum());
        Long stationId = stationBO.addStation(stationDto);
        partnerInstanceDto.setStationId(stationId);
        if (partnerInstanceDto.getParentStationId() == null) {
            partnerInstanceDto.setParentStationId(stationId);
        }
        return stationId;
    }

    public void checkUmStationNumDuplicate(String newStationNum) {
        int count = stationBO.getStationCountByStationNum(newStationNum);
        if (count > 0) {
            throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "系统自动生成优盟站点编号重复，请重试");
        }
    }

    @Override
    @PhaseMeta(descr = "创建优盟")
    public void createOrUpdatePartner(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        addPartner(partnerInstanceDto);
    }

    @Override
    @PhaseMeta(descr = "创建人村关系")
    public void createOrUpdatePartnerInstance(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        addUmPartnerInstanceRel(partnerInstanceDto);
    }

    public Long addUmPartnerInstanceRel(PartnerInstanceDto partnerInstanceDto) {
        partnerInstanceDto.setState(PartnerInstanceStateEnum.SERVICING);
        Date serviceBeginTime = new Date();
        partnerInstanceDto.setApplyTime(serviceBeginTime);
        partnerInstanceDto.setServiceBeginTime(serviceBeginTime);
        partnerInstanceDto.setOpenDate(serviceBeginTime);
        partnerInstanceDto.setApplierId(partnerInstanceDto.getOperator());
        partnerInstanceDto.setApplierType(partnerInstanceDto.getOperatorType().getCode());
        partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
        partnerInstanceDto.setVersion(0L);
        // 当前partner_station_rel.isCurrent = n, 并添加新的当前partner_station_rel
        Long partnerInstanceId = partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
        partnerInstanceDto.setId(partnerInstanceId);
        return partnerInstanceId;
    }


    @Override
    @PhaseMeta(descr = "创建培训装修记录")
    public void createOrUpdateExtensionBusiness(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        String operatorId = partnerInstanceDto.getOperator();
        generalTaskSubmitService.submitAddUserTagTasks(partnerInstanceDto.getId(), operatorId);
        generalTaskSubmitService.submitCreateUnionAdzoneTask(partnerInstanceDto, operatorId);
    }

    @Override
    @PhaseMeta(descr = "触发入驻中事件")
    public void triggerStateChangeEvent(LifeCyclePhaseContext context) {
        PartnerInstanceDto partnerInstanceDto = context.getPartnerInstance();
        Long partnerInstanceDtoId = partnerInstanceDto.getId();
        sendPartnerInstanceStateChangeEvent(partnerInstanceDtoId,
            PartnerInstanceStateChangeEnum.START_SERVICING, partnerInstanceDto);
    }

    public LifeCyclePhaseDSL createPhaseDSL() {
        LifeCyclePhaseDSL dsl = new LifeCyclePhaseDSL();
        dsl.then(this::createOrUpdateStation);
        dsl.then(this::createOrUpdatePartner);
        dsl.then(this::createOrUpdatePartnerInstance);
        dsl.then(this::createOrUpdateExtensionBusiness);
        dsl.then(this::triggerStateChangeEvent);
        return dsl;
    }
}
