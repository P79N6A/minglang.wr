package com.taobao.cun.auge.lifecycle;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationBundleEventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.OperatorConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDeleteDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationAreaTypeEnum;
import com.taobao.cun.auge.station.enums.StationStateEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.enums.StationType;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.sync.StationApplySyncBO;

/**
 * 
 * @author zhenhuan.zhangzh
 *
 */
public abstract class AbstractLifeCyclePhase extends LifeCyclePhaseAdapter {
	@Autowired
	private StationBO stationBO;

	@Autowired
	private AttachmentService criusAttachmentService;

	 @Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private PartnerBO partnerBO;
	
	@Autowired
    private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private StationApplySyncBO syncStationApplyBO;
	public Long addStation(PartnerInstanceDto partnerInstanceDto) {
		StationDto stationDto = partnerInstanceDto.getStationDto();
		stationDto.setState(StationStateEnum.INVALID);
		stationDto.setStatus(StationStatusEnum.NEW);
		stationDto.copyOperatorDto(partnerInstanceDto);
		stationDto.setStationType(StationType.STATION.getType());
		PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
		if (partnerDto != null) {
			stationDto.setTaobaoNick(partnerDto.getTaobaoNick());
			stationDto.setAlipayAccount(partnerDto.getAlipayAccount());
			stationDto.setTaobaoUserId(partnerDto.getTaobaoUserId());
		}
     
		// 判断服务站编号是否使用中
		Long stationId = partnerInstanceDto.getStationId();
		checkStationNumDuplicate(stationId, stationDto.getStationNum());
		stationId = stationBO.addStation(stationDto);
		partnerInstanceDto.setStationId(stationId);
		if (partnerInstanceDto.getParentStationId() == null) {
			partnerInstanceDto.setParentStationId(stationId);
		}
		criusAttachmentService.addAttachmentBatch(stationDto.getAttachments(), stationId,
				AttachmentBizTypeEnum.CRIUS_STATION, OperatorConverter.convert(partnerInstanceDto));
		saveStationFixProtocol(stationDto, stationId);
		return stationId;
	}

	public void checkStationNumDuplicate(Long stationId, String newStationNum) {
		// 判断服务站编号是否使用中
		String oldStationNum = null;
		if (stationId != null) {
			Station oldStation = stationBO.getStationById(stationId);
			oldStationNum = oldStation.getStationNum();
		}
		if (!StringUtils.equals(oldStationNum, newStationNum)) {
			int count = stationBO.getStationCountByStationNum(newStationNum);
			if (count > 0) {
				throw new AugeBusinessException(AugeErrorCodes.DATA_EXISTS_ERROR_CODE, "服务站编号重复");
			}
		}
	}
	
	/**
     * 更新固点协议
     *
     * @param stationDto
     * @param stationId
     */
    private void saveStationFixProtocol(StationDto stationDto, Long stationId) {
        if (stationDto.getAreaType() != null) {
            if (StringUtils.equals(StationAreaTypeEnum.FIX_NEW.getCode(), stationDto.getAreaType().getCode())) {
                PartnerProtocolRelDeleteDto deleteDto = new PartnerProtocolRelDeleteDto();
                deleteDto.setObjectId(stationId);
                deleteDto.setTargetType(PartnerProtocolRelTargetTypeEnum.CRIUS_STATION);
                List<ProtocolTypeEnum> fixProList = new ArrayList<ProtocolTypeEnum>();
                fixProList.add(ProtocolTypeEnum.GOV_FIXED);
                fixProList.add(ProtocolTypeEnum.TRIPARTITE_FIXED);
                deleteDto.copyOperatorDto(stationDto);
                deleteDto.setProtocolTypeList(fixProList);

                partnerProtocolRelBO.deletePartnerProtocolRel(deleteDto);
                PartnerProtocolRelDto fixPro = stationDto.getFixedProtocols();

                if (fixPro != null) {
                    fixPro.copyOperatorDto(stationDto);
                    fixPro.setObjectId(stationId);
                    partnerProtocolRelBO.addPartnerProtocolRel(fixPro);
                }
            }
        }
    }
    
    public Long addPartner(PartnerInstanceDto partnerInstanceDto) {
        //确保taobaouserId在partner表唯一
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(partnerInstanceDto.getTaobaoUserId());
        PartnerDto partnerDto = partnerInstanceDto.getPartnerDto();
        if (partner == null) {
            partnerDto.copyOperatorDto(partnerInstanceDto);
            partnerDto.setState(PartnerStateEnum.NORMAL);
            Long partnerId = partnerBO.addPartner(partnerDto);
            criusAttachmentService.addAttachmentBatch(partnerDto.getAttachments(), partnerId, AttachmentBizTypeEnum.PARTNER, OperatorConverter.convert(partnerInstanceDto));
            partnerInstanceDto.setPartnerId(partnerId);
            return partnerId;
        } else {
        	partnerInstanceDto.setPartnerId(partner.getId());
            partnerDto.setId(partner.getId());
            partnerDto.setAliLangUserId(partner.getAlilangUserId());
            partnerDto.setState(PartnerStateEnum.NORMAL);
            partnerBO.updatePartner(partnerDto);
            criusAttachmentService.modifyAttachementBatch(partnerDto.getAttachments(), partner.getId(), AttachmentBizTypeEnum.PARTNER, OperatorConverter.convert(partnerInstanceDto));
            return partner.getId();
        }

    }
    
    
    public void updateStation(Long stationId, PartnerInstanceDto partnerInstanceDto) {
        StationDto stationDto = partnerInstanceDto.getStationDto();
        stationDto.copyOperatorDto(partnerInstanceDto);
        stationDto.setId(stationId);
        updateStation(stationDto);
    }

    public void updateStation(StationDto stationDto) {
        Long stationId = stationDto.getId();
        // 判断服务站编号是否使用中
        checkStationNumDuplicate(stationId, stationDto.getStationNum());

        stationBO.updateStation(stationDto);
        saveStationFixProtocol(stationDto, stationId);
        criusAttachmentService.modifyAttachementBatch(stationDto.getAttachments(), stationId, AttachmentBizTypeEnum.CRIUS_STATION, OperatorConverter.convert(stationDto));
    }
    
    public Long addPartnerInstanceRel(PartnerInstanceDto partnerInstanceDto) {
        partnerInstanceDto.setState(PartnerInstanceStateEnum.SETTLING);
        partnerInstanceDto.setApplyTime(new Date());
        partnerInstanceDto.setApplierId(partnerInstanceDto.getOperator());
        partnerInstanceDto.setApplierType(partnerInstanceDto.getOperatorType().getCode());
        partnerInstanceDto.setIsCurrent(PartnerInstanceIsCurrentEnum.Y);
        partnerInstanceDto.setVersion(0L);
        // 当前partner_station_rel.isCurrent = n, 并添加新的当前partner_station_rel
        Long partnerInstanceId = partnerInstanceBO.addPartnerStationRel(partnerInstanceDto);
        partnerInstanceDto.setId(partnerInstanceId);
        return partnerInstanceId;
    }

    /**
     * 发送状态变更事件
     * @param instanceId
     * @param stateChangeEnum
     * @param operator
     */
    public void sendPartnerInstanceStateChangeEvent(Long instanceId, PartnerInstanceStateChangeEnum stateChangeEnum,
            OperatorDto operator) {
		PartnerInstanceDto piDto = partnerInstanceBO.getPartnerInstanceById(instanceId);
		EventDispatcherUtil.dispatch(StationBundleEventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
		PartnerInstanceEventConverter.convertStateChangeEvent(stateChangeEnum, piDto, operator));
    }
    
    /**
     * 同步老模型
     * @param type
     * @param instanceId
     */
    public void syncStationApply(SyncStationApplyEnum type, Long instanceId) {
        switch (type) {
            case ADD:
                syncStationApplyBO.addStationApply(instanceId);
                break;
            case DELETE:
                syncStationApplyBO.deleteStationApply(instanceId);
            default:
                syncStationApplyBO.updateStationApply(instanceId, type);
                break;
        }

    }
}
