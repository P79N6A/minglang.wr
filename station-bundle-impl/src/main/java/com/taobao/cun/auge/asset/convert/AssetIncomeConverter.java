package com.taobao.cun.auge.asset.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetIncomeDto;
import com.taobao.cun.auge.asset.enums.AssetIncomeApplierAreaTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetIncomeTypeEnum;
import com.taobao.cun.auge.dal.domain.AssetIncome;

/**
 * 入库单对象转换
 * @author quanzhu.wangqz
 *
 */
public class AssetIncomeConverter {

	public static AssetIncomeDto toAssetIncomeDto(AssetIncome assetIncome) {
		if (assetIncome == null) {
			return null;
		}

		AssetIncomeDto assetIncomeDto = new AssetIncomeDto();
		
		assetIncomeDto.setId(assetIncome.getId());
		assetIncomeDto.setApplierAreaId(assetIncome.getApplierAreaId());
		assetIncomeDto.setApplierAreaName(assetIncome.getApplierAreaName());
		assetIncomeDto.setApplierAreaType(AssetIncomeApplierAreaTypeEnum.valueof(assetIncome.getApplierAreaType()));
		assetIncomeDto.setApplierId(assetIncome.getApplierId());
		assetIncomeDto.setApplierName(assetIncome.getApplierName());
		assetIncomeDto.setPoNo(assetIncome.getPoNo());
		assetIncomeDto.setReceiverName(assetIncome.getReceiverName());
		assetIncomeDto.setReceiverOrgId(assetIncome.getReceiverOrgId());
		assetIncomeDto.setType(AssetIncomeTypeEnum.valueof(assetIncome.getType()));
		assetIncomeDto.setReceiverOrgName(assetIncome.getReceiverOrgName());
		assetIncomeDto.setReceiverWorkno(assetIncome.getReceiverWorkno());
		assetIncomeDto.setRemark(assetIncome.getRemark());
		assetIncomeDto.setStatus(AssetIncomeStatusEnum.valueof(assetIncome.getStatus()));
		return assetIncomeDto;
	}

	public static AssetIncome toAssetIncome(AssetIncomeDto assetIncomeDto) {
		if (assetIncomeDto == null) {
			return null;
		}

		AssetIncome assetIncome = new AssetIncome();
		assetIncome.setId(assetIncomeDto.getId());
		assetIncome.setApplierAreaId(assetIncomeDto.getApplierAreaId());
		assetIncome.setApplierAreaName(assetIncomeDto.getApplierAreaName());
		assetIncome.setApplierAreaType(assetIncomeDto.getApplierAreaType()==null?null:assetIncomeDto.getApplierAreaType().getCode());
		assetIncome.setApplierId(assetIncomeDto.getApplierId());
		assetIncome.setApplierName(assetIncomeDto.getApplierName());
		assetIncome.setPoNo(assetIncomeDto.getPoNo());
		assetIncome.setReceiverName(assetIncomeDto.getReceiverName());
		assetIncome.setReceiverOrgId(assetIncomeDto.getReceiverOrgId());
		assetIncome.setType(assetIncomeDto.getType()==null?null:assetIncomeDto.getType().getCode());
		assetIncome.setReceiverOrgName(assetIncomeDto.getReceiverOrgName());
		assetIncome.setReceiverWorkno(assetIncomeDto.getReceiverWorkno());
		assetIncome.setRemark(assetIncomeDto.getRemark());
		assetIncome.setStatus(assetIncomeDto.getStatus()==null?null:assetIncomeDto.getStatus().getCode());
		
		return assetIncome;
	}

	public static List<AssetIncomeDto> toAssetIncomeDtos(List<AssetIncome> assetIncome) {
		if (assetIncome == null) {
			return null;
		}

		List<AssetIncomeDto> list = new ArrayList<AssetIncomeDto>();
		for (AssetIncome assetIncome_ : assetIncome) {
			list.add(toAssetIncomeDto(assetIncome_));
		}

		return list;
	}

	public static List<AssetIncome> toAssetIncomes(List<AssetIncomeDto> assetIncome) {
		if (assetIncome == null) {
			return null;
		}

		List<AssetIncome> list = new ArrayList<AssetIncome>();
		for (AssetIncomeDto assetIncomeDto : assetIncome) {
			list.add(toAssetIncome(assetIncomeDto));
		}

		return list;
	}
}
