package com.taobao.cun.auge.asset.convert;

import java.util.ArrayList;
import java.util.List;

import com.taobao.cun.auge.asset.dto.AssetRolloutIncomeDetailDto;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.dal.domain.AssetRolloutIncomeDetail;

/**
 * 出入库单详情对象转换
 * @author quanzhu.wangqz
 *
 */
public class AssetRolloutIncomeDetailConverter {

	public static AssetRolloutIncomeDetailDto toAssetRolloutIncomeDetailDto(AssetRolloutIncomeDetail assetRolloutIncomeDetail) {
		if (assetRolloutIncomeDetail == null) {
			return null;
		}

		AssetRolloutIncomeDetailDto assetRolloutIncomeDetailDto = new AssetRolloutIncomeDetailDto();
		
		assetRolloutIncomeDetailDto.setId(assetRolloutIncomeDetail.getId());
		assetRolloutIncomeDetailDto.setAssetId(assetRolloutIncomeDetail.getAssetId());
		assetRolloutIncomeDetailDto.setCategory(assetRolloutIncomeDetail.getCategory());
		assetRolloutIncomeDetailDto.setIncomeId(assetRolloutIncomeDetail.getIncomeId());
		assetRolloutIncomeDetailDto.setPrice(assetRolloutIncomeDetail.getPrice());
		assetRolloutIncomeDetailDto.setRolloutId(assetRolloutIncomeDetail.getRolloutId());
		assetRolloutIncomeDetailDto.setStatus(AssetRolloutIncomeDetailStatusEnum.valueof(assetRolloutIncomeDetail.getStatus()));
		assetRolloutIncomeDetailDto.setType(AssetRolloutIncomeDetailTypeEnum.valueof(assetRolloutIncomeDetail.getType()));
		return assetRolloutIncomeDetailDto;
	}

	public static AssetRolloutIncomeDetail toAssetRolloutIncomeDetail(AssetRolloutIncomeDetailDto assetRolloutIncomeDetailDto) {
		if (assetRolloutIncomeDetailDto == null) {
			return null;
		}

		AssetRolloutIncomeDetail assetRolloutIncomeDetail = new AssetRolloutIncomeDetail();
		assetRolloutIncomeDetail.setId(assetRolloutIncomeDetailDto.getId());
		assetRolloutIncomeDetail.setAssetId(assetRolloutIncomeDetailDto.getAssetId());
		assetRolloutIncomeDetail.setCategory(assetRolloutIncomeDetailDto.getCategory());
		assetRolloutIncomeDetail.setIncomeId(assetRolloutIncomeDetailDto.getIncomeId());
		assetRolloutIncomeDetail.setPrice(assetRolloutIncomeDetailDto.getPrice());
		assetRolloutIncomeDetail.setRolloutId(assetRolloutIncomeDetailDto.getRolloutId());
		assetRolloutIncomeDetail.setStatus(assetRolloutIncomeDetailDto.getStatus()== null?null:assetRolloutIncomeDetailDto.getStatus().getCode());
		assetRolloutIncomeDetail.setType(assetRolloutIncomeDetailDto.getType()==null?null:assetRolloutIncomeDetailDto.getType().getCode());
		
		return assetRolloutIncomeDetail;
	}

	public static List<AssetRolloutIncomeDetailDto> toAssetRolloutIncomeDetailDtos(List<AssetRolloutIncomeDetail> assetRolloutIncomeDetail) {
		if (assetRolloutIncomeDetail == null) {
			return null;
		}

		List<AssetRolloutIncomeDetailDto> list = new ArrayList<AssetRolloutIncomeDetailDto>();
		for (AssetRolloutIncomeDetail assetRolloutIncomeDetail_ : assetRolloutIncomeDetail) {
			list.add(toAssetRolloutIncomeDetailDto(assetRolloutIncomeDetail_));
		}

		return list;
	}

	public static List<AssetRolloutIncomeDetail> toAssetRolloutIncomeDetails(List<AssetRolloutIncomeDetailDto> assetRolloutIncomeDetail) {
		if (assetRolloutIncomeDetail == null) {
			return null;
		}

		List<AssetRolloutIncomeDetail> list = new ArrayList<AssetRolloutIncomeDetail>();
		for (AssetRolloutIncomeDetailDto assetRolloutIncomeDetailDto : assetRolloutIncomeDetail) {
			list.add(toAssetRolloutIncomeDetail(assetRolloutIncomeDetailDto));
		}

		return list;
	}
}
