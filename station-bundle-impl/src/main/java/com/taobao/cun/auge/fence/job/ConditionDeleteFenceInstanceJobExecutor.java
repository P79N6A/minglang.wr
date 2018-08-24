package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.ConditionDeleteFenceInstanceJob;

/**
 * 执行条件筛选站点批量取消模板关联的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class ConditionDeleteFenceInstanceJobExecutor extends AbstractConditionFenceInstanceJobExecutor<ConditionDeleteFenceInstanceJob> {

	@Override
	protected int doExecute(ConditionDeleteFenceInstanceJob fenceInstanceJob) {
		List<Station> stations = getFenceStations(fenceInstanceJob.getCondition());
		if(stations != null) {
			for(Station station : stations) {
				for(Long templateId : fenceInstanceJob.getTemplateIds()) {
					deleteFenceEntity(station.getId(), templateId, fenceInstanceJob.getCreator());
				}
			}
			return stations.size() * fenceInstanceJob.getTemplateIds().size();
		}
		return 0;
	}
}
