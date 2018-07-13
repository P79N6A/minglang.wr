package com.taobao.cun.auge.fence.cainiao;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.cainiao.dms.sorting.api.IRailService;
import com.cainiao.dms.sorting.api.hsf.model.BaseResult;
import com.cainiao.dms.sorting.api.model.rail.RailInfoRequest;
import com.taobao.cun.auge.dal.domain.FenceEntity;

/**
 * 调用菜鸟围栏接口
 * 
 * @author chengyu.zhoucy
 *
 */
//@Component
public class RailServiceAdapterImpl implements RailServiceAdapter {
	//@Resource
	private IRailService railService;
	
	@Override
	public Long addCainiaoFence(FenceEntity fenceEntity) {
		BaseResult<Long> result = railService.addRail(toCainiaoFence(fenceEntity));
		return null;
	}

	@Override
	public void updateCainiaoFence(FenceEntity fenceEntity) {
		BaseResult<Boolean> result = railService.updateRailById(toCainiaoFence(fenceEntity));
	}

	@Override
	public void deleteCainiaoFence(Long cainiaoFenceId) {
		RailInfoRequest request = new RailInfoRequest();
		request.setId(cainiaoFenceId);
		BaseResult<Boolean> result = railService.deleteRailById(request);
	}

	private RailInfoRequest toCainiaoFence(FenceEntity fenceEntity) {
		return null;
	}
}
