package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.BatchStationQuitFenceInstanceJob;

/**
 * 批量删除退出的站点的围栏实例
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class BatchStationQuitFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<BatchStationQuitFenceInstanceJob> {
	
	@Override
	protected int doExecute(BatchStationQuitFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = fenceEntityBO.getStationQuitedFenceEntities();
		for(FenceEntity fenceEntity : fenceEntities) {
			deleteFenceEntity(fenceEntity);
		}
		return fenceEntities.size();
	}
}
