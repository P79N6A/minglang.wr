package com.taobao.cun.auge.dal.mapper;

import java.util.List;
import java.util.Map;

import com.taobao.cun.auge.dal.domain.CuntaoAssetFlow;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowDetailForExcel;
import com.taobao.cun.auge.dal.domain.CuntaoAssetFlowExtExample;

public interface CuntaoAssetFlowExtMapper {

	 List<CuntaoAssetFlow> selectByExample(CuntaoAssetFlowExtExample example);
	 
	 List<Map<String,Object>> getApplyAssetCount(Map<String,Object> param);
	 
	 List<CuntaoAssetFlowDetailForExcel> selectForExcel(CuntaoAssetFlowDetailForExcel detail);
	 
	 Integer selectCountForExcel(CuntaoAssetFlowDetailForExcel detail);
}
