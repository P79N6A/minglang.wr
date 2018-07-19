package com.taobao.cun.auge.fence.job;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.job.StationStateReopenFenceInstanceJob;

/**
 * 站点停业状态到重新开业
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationStateReopenFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationStateReopenFenceInstanceJob> {
	@Override
	protected int doExecute(StationStateReopenFenceInstanceJob fenceInstanceJob) {
		return updateFenceStateByStation(fenceInstanceJob.getStationId(), FenceConstants.ENABLE);
	}
}
