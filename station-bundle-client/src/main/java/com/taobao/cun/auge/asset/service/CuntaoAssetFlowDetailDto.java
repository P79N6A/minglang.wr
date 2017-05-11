package com.taobao.cun.auge.asset.service;

import java.io.Serializable;


/**
 * @author quanzhu.wangqz
 *
 */
public class CuntaoAssetFlowDetailDto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5571429686278588027L;

	/**
	 * 资产明细主键id
	 */
	private Long AssetFlowDetailId;
	
	/**
	 * 资产申请表主键
	 */
	private Long applyId;
	/**
	 * 	申请数量
	 */
	private Integer applyNum;
	/**
	 * 资产类目名称
	 */
	private String name;
	/**
	 * 资产类型
	 */
	private AssetFlowDetailAssetTypeEnum type;
	/**
	 * sku属性
	 */
	private String sku;
	
	/**
	 * 资产编号(采集系统编号)
	 */
	private String poNo;
	
	public Long getAssetFlowDetailId() {
		return AssetFlowDetailId;
	}
	public void setAssetFlowDetailId(Long assetFlowDetailId) {
		AssetFlowDetailId = assetFlowDetailId;
	}
	public Long getApplyId() {
		return applyId;
	}
	public void setApplyId(Long applyId) {
		this.applyId = applyId;
	}
	public Integer getApplyNum() {
		return applyNum;
	}
	public void setApplyNum(Integer applyNum) {
		this.applyNum = applyNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public AssetFlowDetailAssetTypeEnum getType() {
		return type;
	}
	public void setType(AssetFlowDetailAssetTypeEnum type) {
		this.type = type;
	}
	public String getPoNo() {
		return poNo;
	}
	public void setPoNo(String poNo) {
		this.poNo = poNo;
	}
	
}
