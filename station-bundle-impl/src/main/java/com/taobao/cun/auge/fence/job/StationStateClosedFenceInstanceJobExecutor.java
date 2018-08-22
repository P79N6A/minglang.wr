package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.StationStateClosedFenceInstanceJob;

/**
 * 站点进入停业状态,删除围栏
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationStateClosedFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationStateClosedFenceInstanceJob> {
	@Override
	protected int doExecute(StationStateClosedFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntitiesByStationId(fenceInstanceJob.getStationId());
		for(FenceEntity fenceEntity : fenceEntities) {
			deleteFenceEntity(fenceEntity);
		}
		return fenceEntities.size();
	}
}
