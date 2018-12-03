package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.AssetCheckTask;
import com.taobao.cun.auge.dal.domain.AssetCheckTaskExample;
import com.taobao.cun.auge.dal.domain.AssetCheckTaskExample.Criteria;
import com.taobao.cun.auge.dal.mapper.AssetCheckTaskMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.exception.AugeBusinessException;

@Component
public class AssetCheckTaskBOImpl implements AssetCheckTaskBO {
	
	@Autowired
	private AssetCheckTaskMapper assetCheckTaskMapper;
	@Override
	public void initTaskForStation(String taskType, String taskCode, Long taobaoUserId) {
		// TODO Auto-generated method stub

	}
	

	@Override
	public Boolean finishTaskForStation(Long taobaoUserId) {
		
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerIdEqualTo(String.valueOf(taobaoUserId));
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.STATION.name());
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		AssetCheckTask at = ResultUtils.selectOne(assetCheckTaskMapper.selectByExample(example));
		if (at == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"未查询到当前服务站盘点任务");
		}
		String status = at.getTaskStatus();
		at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		DomainUtils.beforeUpdate(at, String.valueOf(taobaoUserId));
		assetCheckTaskMapper.updateByPrimaryKeySelective(at);
		if(!AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(status)) {
			//TODO:结束运营任务
		}
		return Boolean.TRUE;

	}

	@Override
	public void initTaskForCounty(List<Long> orgId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean finishTaskForCounty(FinishTaskForCountyDto param) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerIdEqualTo(param.getOperator());
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.name());
		criteria.andTaskTypeEqualTo(param.getTaskType());
		AssetCheckTask at = ResultUtils.selectOne(assetCheckTaskMapper.selectByExample(example));
		
		if (at == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"未查询到当前县点盘点任务");
		}
		String status = at.getTaskStatus();
		at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		at.setLostAsset(JSONObject.toJSONString(param.getLostAsset()));
		at.setWaitBackAsset(JSONObject.toJSONString(param.getWaitBackAsset()));
		at.setOtherReason(param.getOtherReason());
		DomainUtils.beforeUpdate(at, param.getOperator());
		assetCheckTaskMapper.updateByPrimaryKeySelective(at);
		if(!AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(status)) {
			//TODO:结束流程任务
		}
		return  Boolean.TRUE;
	}

	@Override
	public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition param) {
		// TODO Auto-generated method stub
		return null;
	}

}
