package com.taobao.cun.auge.asset.bo;

import java.util.List;

import com.taobao.cun.auge.station.dto.StationDto;

public interface AssetSynBO {

	public Boolean syncAsset(List<Long> cuntaoAssetIds);
	
	
	public Boolean changeOwner(Long orgId,String ownerWorkNo,String ownerName,List<Long> assetId);
	
	
	public void checkAssetInfo(List<Long> cuntaoAssetIds,String status);
	
	
	public void tempUpdateStation(StationDto sDto);
}
