package com.taobao.cun.auge.asset.dto;

import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetRolloutIncomeDetailTypeEnum;
import com.taobao.cun.auge.common.OperatorDto;

public class AssetRolloutIncomeDetailDto  extends OperatorDto {

	private static final long serialVersionUID = 5713720691774976062L;

	/**
     * 主键id
     */
    private Long id;

    /**
     * 资产类型
     */
    private String category;

    /**
     * 资产表主键
     */
    private Long assetId;

    /**
     * 入库单id
     */
    private Long incomeId;

    /**
     * 出库单id
     */
    private Long rolloutId;

    /**
     * 资产净值
     */
    private String price;

    /**
     * 状态
     */
    private AssetRolloutIncomeDetailStatusEnum status;

    /**
     * 类型
     */
    private AssetRolloutIncomeDetailTypeEnum type;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId(Long assetId) {
		this.assetId = assetId;
	}

	public Long getIncomeId() {
		return incomeId;
	}

	public void setIncomeId(Long incomeId) {
		this.incomeId = incomeId;
	}

	public Long getRolloutId() {
		return rolloutId;
	}

	public void setRolloutId(Long rolloutId) {
		this.rolloutId = rolloutId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public AssetRolloutIncomeDetailStatusEnum getStatus() {
		return status;
	}

	public void setStatus(AssetRolloutIncomeDetailStatusEnum status) {
		this.status = status;
	}

	public AssetRolloutIncomeDetailTypeEnum getType() {
		return type;
	}

	public void setType(AssetRolloutIncomeDetailTypeEnum type) {
		this.type = type;
	}
    
}
