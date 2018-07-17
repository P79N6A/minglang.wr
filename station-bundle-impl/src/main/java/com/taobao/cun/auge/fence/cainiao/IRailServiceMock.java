package com.taobao.cun.auge.fence.cainiao;

import org.springframework.stereotype.Component;

import com.cainiao.dms.sorting.api.IRailService;
import com.cainiao.dms.sorting.api.hsf.model.BaseResult;
import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;
import com.cainiao.dms.sorting.common.dataobject.rail.RailQueryRequest;
import com.cainiao.dms.sorting.common.dataobject.vo.RailSortingResult;

//@Component
public class IRailServiceMock implements IRailService {

	@Override
	public BaseResult<Long> addRail(RailInfoRequest request) {
		return BaseResult.ofSuccess(1L);
	}

	@Override
	public BaseResult<Boolean> deleteRailById(RailInfoRequest request) {
		return BaseResult.ofSuccess(true);
	}

	@Override
	public BaseResult<RailSortingResult> sorting(RailQueryRequest arg0) {
		return null;
	}

	@Override
	public BaseResult<Boolean> updateRailById(RailInfoRequest request) {
		return BaseResult.ofSuccess(true);
	}

}
