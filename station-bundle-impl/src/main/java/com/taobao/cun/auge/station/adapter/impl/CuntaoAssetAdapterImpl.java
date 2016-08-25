package com.taobao.cun.auge.station.adapter.impl;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.station.adapter.CuntaoAssetAdapter;
import com.taobao.cun.common.PageConditionDto;
import com.taobao.cun.common.resultmodel.PagedResultModel;
import com.taobao.cun.dto.ContextDto;
import com.taobao.cun.dto.SystemTypeEnum;
import com.taobao.cun.dto.asset.CuntaoAssetDto;
import com.taobao.cun.dto.asset.CuntaoAssetQueryCondition;
import com.taobao.cun.service.asset.CuntaoAssetService;

@Component("cuntaoAssetAdapter")
public class CuntaoAssetAdapterImpl implements CuntaoAssetAdapter {

	public static final Logger logger = LoggerFactory.getLogger(CuntaoAssetAdapter.class);

	@Resource
	CuntaoAssetService cuntaoAssetService;

	@Override
	public boolean isBackAssets(Long stationApplyId) {
		CuntaoAssetQueryCondition cuntaoAssetQueryCondition = new CuntaoAssetQueryCondition();
		cuntaoAssetQueryCondition.setStationId(String.valueOf(stationApplyId));
		
		ContextDto contextDto = buildSysContext();

		PagedResultModel<List<CuntaoAssetDto>> assetResult = cuntaoAssetService.queryByPage(cuntaoAssetQueryCondition,
				contextDto);

		if (null != assetResult && assetResult.isSuccess()) {
			long assetNum = assetResult.getTotalResultSize();
			if (assetNum < 1) {
				return true;
			}
		}
		return false;
	}

	private ContextDto buildSysContext() {
		ContextDto contextDto = new ContextDto();
		contextDto.setAppId("sys");
		contextDto.setSystemType(SystemTypeEnum.CUNTAO_ADMIN);
		return contextDto;
	}

}
