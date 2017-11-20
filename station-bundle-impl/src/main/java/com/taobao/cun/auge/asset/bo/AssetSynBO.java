package com.taobao.cun.auge.asset.bo;

import java.util.List;

public interface AssetSynBO {

	public Boolean syncAsset(List<Long> cuntaoAssetIds);
	
	
	public Boolean changeOwner(Long orgId,String ownerWorkNo,String ownerName,List<Long> assetId);
	
	
	public void checkAssetInfo(List<Long> cuntaoAssetIds,String status);
}
