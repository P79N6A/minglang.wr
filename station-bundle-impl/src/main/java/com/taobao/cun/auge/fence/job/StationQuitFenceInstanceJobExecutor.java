package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.StationQuitFenceInstanceJob;

/**
 * 执行站点退出后，删除围栏实例的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationQuitFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationQuitFenceInstanceJob> {
	
	@Override
	protected int doExecute(StationQuitFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = fenceEntityBO.getStationQuitedFenceEntities();
		for(FenceEntity fenceEntity : fenceEntities) {
			deleteFenceEntity(fenceEntity);
		}
		return fenceEntities.size();
	}
}
