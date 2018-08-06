package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.job.StationStateClosedFenceInstanceJob;

/**
 * 站点进入停业状态
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationStateClosedFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationStateClosedFenceInstanceJob> {
	@Override
	protected int doExecute(StationStateClosedFenceInstanceJob fenceInstanceJob) {
		return updateFenceStateByStation(fenceInstanceJob.getStationId(), FenceConstants.DISABLE);
	}
}
