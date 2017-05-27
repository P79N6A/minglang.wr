package com.taobao.cun.auge.asset.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetRolloutDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutReceiverAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutTypeEnum;
import com.taobao.cun.auge.dal.domain.AssetRollout;

/**
 * 出库单对象转换
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutConverter {

	public static AssetRolloutDto toAssetRolloutDto(AssetRollout assetRollout) {
		if (assetRollout == null) {
			return null;
		}

		AssetRolloutDto assetRolloutDto = new AssetRolloutDto();
		
		assetRolloutDto.setId(assetRollout.getId());
		assetRolloutDto.setApplierName(assetRollout.getApplierName());
		assetRolloutDto.setApplierOrgId(assetRollout.getApplierOrgId());
		assetRolloutDto.setApplierOrgName(assetRollout.getApplierOrgName());
		assetRolloutDto.setApplierWorkno(assetRollout.getApplierWorkno());
		assetRolloutDto.setIsDeductible(assetRollout.getIsDeductible());
		assetRolloutDto.setLogisticsCost(assetRollout.getLogisticsCost());
		assetRolloutDto.setLogisticsDistance(assetRollout.getLogisticsDistance());
		assetRolloutDto.setReason(assetRollout.getReason());
		assetRolloutDto.setReceiverAreaId(assetRollout.getReceiverAreaId());
		assetRolloutDto.setReceiverAreaName(assetRollout.getReceiverAreaName());
		assetRolloutDto.setReceiverAreaType(AssetRolloutReceiverAreaTypeEnum.valueof(assetRollout.getReceiverAreaType()));
		assetRolloutDto.setReceiverId(assetRollout.getReceiverId());
		assetRolloutDto.setReceiverName(assetRollout.getReceiverName());
		assetRolloutDto.setRemark(assetRollout.getRemark());
		assetRolloutDto.setTotalPayment(assetRollout.getTotalPayment());
		assetRolloutDto.setType(AssetRolloutTypeEnum.valueof(assetRollout.getType()));
		assetRolloutDto.setStatus(AssetRolloutStatusEnum.valueof(assetRollout.getStatus()));
		assetRolloutDto.setAttachId(assetRollout.getAttachId());
		return assetRolloutDto;
	}

	public static AssetRollout toAssetRollout(AssetRolloutDto assetRolloutDto) {
		if (assetRolloutDto == null) {
			return null;
		}
		AssetRollout assetRollout = new AssetRollout();

		assetRollout.setId(assetRolloutDto.getId());
		assetRollout.setApplierName(assetRolloutDto.getApplierName());
		assetRollout.setApplierOrgId(assetRolloutDto.getApplierOrgId());
		assetRollout.setApplierOrgName(assetRolloutDto.getApplierOrgName());
		assetRollout.setApplierWorkno(assetRolloutDto.getApplierWorkno());
		assetRollout.setIsDeductible(assetRolloutDto.getIsDeductible());
		assetRollout.setLogisticsCost(assetRolloutDto.getLogisticsCost());
		assetRollout.setLogisticsDistance(assetRolloutDto.getLogisticsDistance());
		assetRollout.setReason(assetRolloutDto.getReason());
		assetRollout.setReceiverAreaId(assetRolloutDto.getReceiverAreaId());
		assetRollout.setReceiverAreaName(assetRolloutDto.getReceiverAreaName());
		assetRollout.setReceiverAreaType(assetRolloutDto.getReceiverAreaType()== null? null:assetRolloutDto.getReceiverAreaType().getCode());
		assetRollout.setReceiverId(assetRolloutDto.getReceiverId());
		assetRollout.setReceiverName(assetRolloutDto.getReceiverName());
		assetRollout.setRemark(assetRolloutDto.getRemark());
		assetRollout.setTotalPayment(assetRolloutDto.getTotalPayment());
		assetRollout.setType(assetRolloutDto.getType()== null? null:assetRolloutDto.getType().getCode());
		assetRollout.setStatus(assetRolloutDto.getStatus()== null? null:assetRolloutDto.getStatus().getCode());
		assetRollout.setAttachId(assetRolloutDto.getAttachId());
		return assetRollout;
	}

	public static List<AssetRolloutDto> toAssetRolloutDtos(List<AssetRollout> assetRollout) {
		if (assetRollout == null) {
			return null;
		}

		List<AssetRolloutDto> list = new ArrayList<AssetRolloutDto>();
		for (AssetRollout assetRollout_ : assetRollout) {
			list.add(toAssetRolloutDto(assetRollout_));
		}

		return list;
	}

	public static List<AssetRollout> toAssetRollouts(List<AssetRolloutDto> assetRollout) {
		if (assetRollout == null) {
			return null;
		}

		List<AssetRollout> list = new ArrayList<AssetRollout>();
		for (AssetRolloutDto assetRolloutDto : assetRollout) {
			list.add(toAssetRollout(assetRolloutDto));
		}

		return list;
	}
}
