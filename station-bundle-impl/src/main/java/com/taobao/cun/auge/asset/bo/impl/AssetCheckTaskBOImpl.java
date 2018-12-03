package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.common.PageDto;

@Component
public class AssetCheckTaskBOImpl implements AssetCheckTaskBO {

	@Override
	public void initTaskForStation(String taskType, String taskCode, Long taobaoUserId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishTaskForStation(Long taobaoUserId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTaskForCounty(List<Long> orgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void finishTaskForCounty(FinishTaskForCountyDto param) {
		// TODO Auto-generated method stub

	}

	@Override
	public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

}
