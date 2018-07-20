package com.taobao.cun.auge.fence.job;

import java.util.List;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
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
		int intanceNum = 0;
		List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntitiesByStationId(fenceInstanceJob.getStationId());
		for(FenceEntity fenceEntity : fenceEntities) {
			FenceTemplateDto template = getFenceTemplate(fenceEntity.getTemplateId());
			if(template.getState().equals(FenceConstants.ENABLE)) {
				updateFenceState(fenceEntity, FenceConstants.ENABLE, fenceInstanceJob.getCreator());
				intanceNum++;
			}
		}
		return intanceNum;
	}
}
