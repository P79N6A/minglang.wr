package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.ConditionCreateFenceInstanceJob;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;

/**
 * 执行条件筛选站点关联模板的任务
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class ConditionCreateFenceInstanceJobExecutor extends AbstractConditionFenceInstanceJobExecutor<ConditionCreateFenceInstanceJob> {

	@Override
	protected int doExecute(ConditionCreateFenceInstanceJob fenceInstanceJob) {
		List<Station> stations = getFenceStations(fenceInstanceJob.getCondition());
		if(stations != null) {
			for(Station station : stations) {
				for(Long templateId : fenceInstanceJob.getTemplateIds()) {
					if(fenceInstanceJob.getCreateRule().equals(FenceInstanceJob.CREATE_RULE_OVERRIDE)){
						overrideFenceEntity(station.getId(), templateId, fenceInstanceJob.getId());
					}else {
						newFenceEntity(station.getId(), templateId, fenceInstanceJob.getId());
					}
				}
			}
			
			return stations.size() * fenceInstanceJob.getTemplateIds().size();
		}
		return 0;
	}
}
