package com.taobao.cun.auge.asset.dto;

import java.io.Serializable;
import java.util.List;

import com.taobao.cun.auge.common.OperatorDto;

/**
 * 资产分发dto
 * @author quanzhu.wangqz
 *
 */

public class AssetDistributeDto extends OperatorDto implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 3674147716914075474L;
	/**
     * 分发到的村点id
     */
    private Long stationId;
    /**
     * 待分发的资产id列表
     */
    private List<Long> assetIdList;
    
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public List<Long> getAssetIdList() {
		return assetIdList;
	}
	public void setAssetIdList(List<Long> assetIdList) {
		this.assetIdList = assetIdList;
	}

   
}
