package com.taobao.cun.auge.fence.job;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Strings;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.job.StationUpdateFenceInstanceJob;
import com.taobao.cun.auge.fence.job.init.FenceInitTemplateConfig;

/**
 * 执行站点（行政地址、地标、领用地址、行政区划、经纬度、是否在镇上）变化的任务
 * 
 * 找出该站点的围栏，将其重新生成一遍
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class StationUpdateFenceInstanceJobExecutor extends AbstractFenceInstanceJobExecutor<StationUpdateFenceInstanceJob> {
	@Resource
	private FenceInitTemplateConfig fenceInitTemplateConfig;
	
	@Override
	protected int doExecute(StationUpdateFenceInstanceJob fenceInstanceJob) {
		List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntitiesByStationId(fenceInstanceJob.getStationId());
		if(fenceEntities != null) {
			Station station = stationBo.getStationById(fenceInstanceJob.getStationId());
			for(FenceEntity fenceEntity : fenceEntities) {
				if(isOnTown(station) && fenceInitTemplateConfig.isVillageTemplate(fenceEntity.getTemplateId())) {
					deleteFenceEntity(fenceEntity);
				}else if(!isOnTown(station) && fenceInitTemplateConfig.isTownTemplate(fenceEntity.getTemplateId())) {
					deleteFenceEntity(fenceEntity);
				}else {
					updateFenceEntity(fenceEntity.getStationId(), fenceEntity.getTemplateId());
				}
			}
			return fenceEntities.size();
		}
		return 0;
	}
	
	private boolean isOnTown(Station station) {
		return !Strings.isNullOrEmpty(station.getIsOnTown()) && station.getIsOnTown().equals("y");
	}
}
