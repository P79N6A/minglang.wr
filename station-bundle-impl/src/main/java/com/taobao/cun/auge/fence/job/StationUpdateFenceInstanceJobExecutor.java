package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;

/**
 * 执行站点（行政地址、地标、领用地址）变化的任务
 * 
 * 找出该站点的围栏，将其重新生成一遍
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationUpdateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationUpdateFenceInstanceJob> {

	@Override
	protected int doExecute(StationUpdateFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntitiesByStationId(fenceInstanceJob.getStationId());
		if(fenceEntities != null) {
			for(FenceEntity fenceEntity : fenceEntities) {
				updateFenceEntity(fenceEntity.getStationId(), fenceEntity.getTemplateId());
			}
			return fenceEntities.size();
		}
		return 0;
	}
}
