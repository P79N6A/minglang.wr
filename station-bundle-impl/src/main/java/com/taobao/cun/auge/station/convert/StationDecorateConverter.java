package com.taobao.cun.auge.station.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.dal.domain.DecorationInfoDecision;
import com.taobao.cun.auge.dal.domain.StationDecorate;
import com.taobao.cun.auge.station.dto.DecorationInfoDecisionDto;
import com.taobao.cun.auge.station.dto.StationDecorateDto;
import com.taobao.cun.auge.station.enums.DecorationInfoDecisionStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecoratePaymentTypeEnum;
import com.taobao.cun.auge.station.enums.StationDecorateStatusEnum;
import com.taobao.cun.auge.station.enums.StationDecorateTypeEnum;
import org.apache.commons.collections.CollectionUtils;

/**
 * 服务站装修记录转换
 * @author quanzhu.wangqz
 *
 */
public class StationDecorateConverter {
	
	public static StationDecorateDto toStationDecorateDto(StationDecorate stationDecorate) {
		if (null == stationDecorate) {
			return null;
		}
		StationDecorateDto stationDecorateDto = new StationDecorateDto();

		//stationDecorateDto.setAttachements(attachements);
		stationDecorateDto.setAuditOpinion(stationDecorate.getAuditOpinion());
		stationDecorateDto.setDoorArea(stationDecorate.getDoorArea());
		stationDecorateDto.setGlassArea(stationDecorate.getGlassArea());
		stationDecorateDto.setId(stationDecorate.getId());
		stationDecorateDto.setInsideArea(stationDecorate.getInsideArea());
		stationDecorateDto.setPartnerUserId(stationDecorate.getPartnerUserId());
		stationDecorateDto.setReflectNick(stationDecorate.getReflectNick());
		stationDecorateDto.setReflectOrderNum(stationDecorate.getReflectOrderNum());
		stationDecorateDto.setReflectUserId(stationDecorate.getReflectUserId());
		stationDecorateDto.setSellerTaobaoUserId(stationDecorate.getSellerTaobaoUserId());
		stationDecorateDto.setStationId(stationDecorate.getStationId());
		stationDecorateDto.setStatus(StationDecorateStatusEnum.valueof(stationDecorate.getStatus()));
		stationDecorateDto.setTaobaoOrderNum(stationDecorate.getTaobaoOrderNum());
		stationDecorateDto.setWallArea(stationDecorate.getWallArea());
		stationDecorateDto.setCarpetArea(stationDecorate.getCarpetArea());
		stationDecorateDto.setReflectSatisfySolid(stationDecorate.getReflectSatisfySolid());
		stationDecorateDto.setPaymentType(StationDecoratePaymentTypeEnum.valueof(stationDecorate.getPaymentType()));
		stationDecorateDto.setDecorateType(StationDecorateTypeEnum.valueof(stationDecorate.getDecorateType()));
		return stationDecorateDto;
	}

	public static StationDecorate toStationDecorate(StationDecorateDto stationDecorateDto) {
		StationDecorate stationDecorate = new StationDecorate();

		//stationDecorate.setAttachment(attachment);
		stationDecorate.setAuditOpinion(stationDecorateDto.getAuditOpinion());
		stationDecorate.setDoorArea(stationDecorateDto.getDoorArea());
		stationDecorate.setGlassArea(stationDecorateDto.getGlassArea());
		stationDecorate.setId(stationDecorateDto.getId());
		stationDecorate.setInsideArea(stationDecorateDto.getInsideArea());
		stationDecorate.setPartnerUserId(stationDecorateDto.getPartnerUserId());
		stationDecorate.setReflectNick(stationDecorateDto.getReflectNick());
		stationDecorate.setReflectOrderNum(stationDecorateDto.getReflectOrderNum());
		stationDecorate.setReflectUserId(stationDecorateDto.getReflectUserId());
		stationDecorate.setSellerTaobaoUserId(stationDecorateDto.getSellerTaobaoUserId());
		stationDecorate.setStationId(stationDecorateDto.getStationId());
		stationDecorate.setStatus(stationDecorateDto.getStatus() ==null? null:stationDecorateDto.getStatus().getCode());
		stationDecorate.setTaobaoOrderNum(stationDecorateDto.getTaobaoOrderNum());
		stationDecorate.setWallArea(stationDecorateDto.getWallArea());
		stationDecorate.setCarpetArea(stationDecorateDto.getCarpetArea());
		stationDecorate.setReflectSatisfySolid(stationDecorateDto.getReflectSatisfySolid());
	    stationDecorate.setPaymentType(stationDecorateDto.getPaymentType()==null?null:stationDecorateDto.getPaymentType().getCode());
	    stationDecorate.setDecorateType(stationDecorateDto.getDecorateType()==null?null:stationDecorateDto.getDecorateType().getCode());
		return stationDecorate;
	}

	public static List<StationDecorateDto> toStationDecorateDtos(List<StationDecorate> stationDecorates) {
		if (CollectionUtils.isEmpty(stationDecorates)) {
			return Collections.<StationDecorateDto>emptyList();
		}

		List<StationDecorateDto> list = new ArrayList<StationDecorateDto>(stationDecorates.size());
		for (StationDecorate stationDecorate : stationDecorates) {
			if(null == stationDecorate){
				continue;
			}
			list.add(toStationDecorateDto(stationDecorate));
		}

		return list;
	}

	public static List<StationDecorate> toStationDecorates(List<StationDecorateDto> stationDecorateDtos) {
		if (CollectionUtils.isEmpty(stationDecorateDtos)) {
			return Collections.<StationDecorate>emptyList();
		}

		List<StationDecorate> list = new ArrayList<StationDecorate>();
		for (StationDecorateDto stationDecorateDto : stationDecorateDtos) {
			if(null == stationDecorateDto){
				continue;
			}
			list.add(toStationDecorate(stationDecorateDto));
		}

		return list;
	}
	
	public static DecorationInfoDecisionDto toDecorationInfoDecisionDto(DecorationInfoDecision decorationInfo) {
        if (null == decorationInfo) {
            return null;
        }
        DecorationInfoDecisionDto decorationInfoDto = new DecorationInfoDecisionDto();
        decorationInfoDto.setId(decorationInfo.getId());
        decorationInfoDto.setStationId(decorationInfo.getStationId());
        decorationInfoDto.setOrgId(decorationInfo.getOrgId());
        decorationInfoDto.setDecorateMoney(decorationInfo.getDecorateMoney());
        decorationInfoDto.setShopArea(decorationInfo.getShopArea());
        decorationInfoDto.setStatus(DecorationInfoDecisionStatusEnum.valueof(decorationInfo.getStatus()));
        decorationInfoDto.setWarehouseArea(decorationInfo.getWarehouseArea());
        decorationInfoDto.setAuditOpinion(decorationInfo.getAuditOpinion());
        return decorationInfoDto;
    }

    public static DecorationInfoDecision toDecorationInfoDecision(DecorationInfoDecisionDto decorationInfoDto) {
        DecorationInfoDecision decorationInfo = new DecorationInfoDecision();

        decorationInfo.setId(decorationInfoDto.getId());
        decorationInfo.setStationId(decorationInfoDto.getStationId());
        decorationInfo.setOrgId(decorationInfoDto.getOrgId());
        decorationInfo.setShopArea(decorationInfoDto.getShopArea());
        decorationInfo.setDecorateMoney(decorationInfoDto.getDecorateMoney());
        decorationInfo.setStatus(decorationInfoDto.getStatus()==null?null:decorationInfoDto.getStatus().getCode());
        decorationInfo.setWarehouseArea(decorationInfoDto.getWarehouseArea());
        decorationInfo.setAuditOpinion(decorationInfoDto.getAuditOpinion());
        return decorationInfo;
    }
}
