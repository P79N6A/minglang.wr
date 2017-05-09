package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.CuntaoAsset;
import com.taobao.cun.auge.dal.domain.CuntaoAssetExtExample;

public interface CuntaoAssetExtMapper {

	 List<CuntaoAsset> selectByExample(CuntaoAssetExtExample example);
	 
	 
	 List<String> selectBoNoByExample(CuntaoAssetExtExample example);
	 
	 List<Map<String,Object>> getAssetSituation(Long orgId);
}
