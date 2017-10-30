package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;

public class AssetIncomeDetailDto implements Serializable{

	private static final long serialVersionUID = -4939163358121761029L;

	/**
     * 
     * 入库单dto
     */
    private AssetIncomeDto assetIncomeDto;;

    /**
     *是否有撤销的数据
     */
    private Boolean hasCanceldata = Boolean.FALSE;

	public AssetIncomeDto getAssetIncomeDto() {
		return assetIncomeDto;
	}

	public void setAssetIncomeDto(AssetIncomeDto assetIncomeDto) {
		this.assetIncomeDto = assetIncomeDto;
	}

	public Boolean getHasCanceldata() {
		return hasCanceldata;
	}

	public void setHasCanceldata(Boolean hasCanceldata) {
		this.hasCanceldata = hasCanceldata;
	}
}
