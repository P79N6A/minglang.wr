package com.taobao.cun.auge.asset.bo;

import java.util.List;

public interface AssetSynBO {

	public Boolean syncAsset(List<Long> cuntaoAssetIds);
	
	
	public Boolean changeOwner(Long orgId,String ownerWorkNo,String ownerName,List<String> aliNos);
	
	
	public void checkAssetInfo(List<Long> cuntaoAssetIds);
	
	public boolean scrapAssetByOrg(List<String> aliNoList);
	
	
	
	public void checkAssetToAmpForBcp(Long assetId);
	
	public Boolean changeOwner(List<Long> assetId);
	
	
	 public Boolean disAsset(String aliNo,Long userAreaId,String userName,String userId);
	 
	//618优品售卖范围扩大项目
	 public boolean initStationFeatureToCainiao(String key,String value);
	 
	 
	 public Boolean batchDisRouter();
	
}
