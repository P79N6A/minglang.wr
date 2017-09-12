package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

public class AssetRolloutDetailDto implements Serializable{

	private static final long serialVersionUID = -4939163358121761029L;

	/**
     * 
     * 入库单dto
     */
    private AssetRolloutDto assetRolloutDto;;

    /**
     *是否有撤销的数据
     */
    private Boolean hasCanceldata = Boolean.FALSE;

	public AssetRolloutDto getAssetRolloutDto() {
		return assetRolloutDto;
	}

	public void setAssetRolloutDto(AssetRolloutDto assetRolloutDto) {
		this.assetRolloutDto = assetRolloutDto;
	}

	public Boolean getHasCanceldata() {
		return hasCanceldata;
	}

	public void setHasCanceldata(Boolean hasCanceldata) {
		this.hasCanceldata = hasCanceldata;
	}
}
