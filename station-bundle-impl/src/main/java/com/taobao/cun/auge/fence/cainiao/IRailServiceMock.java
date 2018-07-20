package com.taobao.cun.auge.fence.cainiao;

import com.cainiao.dms.sorting.api.IRailService;
import com.cainiao.dms.sorting.api.hsf.model.BaseResult;
import com.cainiao.dms.sorting.api.model.RailInfoResult;
import com.cainiao.dms.sorting.common.dataobject.rail.RailInfoRequest;
import com.cainiao.dms.sorting.common.dataobject.rail.RailQueryRequest;
import com.cainiao.dms.sorting.common.dataobject.vo.RailSortingResult;

public class IRailServiceMock implements IRailService {

	@Override
	public BaseResult<Long> addRail(RailInfoRequest request) {
		return BaseResult.ofSuccess(0L);
	}

	@Override
	public BaseResult<Boolean> updateRailById(RailInfoRequest request) {
		return BaseResult.ofSuccess(true);
	}

	@Override
	public BaseResult<Boolean> deleteRailById(RailInfoRequest request) {
		return BaseResult.ofSuccess(true);
	}

	@Override
	public BaseResult<RailSortingResult> sorting(RailQueryRequest railQueryRequest) {
		return BaseResult.ofSuccess(null);
	}

	@Override
	public BaseResult<RailInfoResult> getRailInfoById(Long arg0) {
		return null;
	}

}
