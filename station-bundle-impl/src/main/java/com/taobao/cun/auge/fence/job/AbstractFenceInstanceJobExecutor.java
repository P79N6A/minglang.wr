package com.taobao.cun.auge.fence.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.dto.FenceInstanceJobUpdateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.instance.FencenInstanceBuilder;
import com.taobao.cun.auge.station.bo.StationBO;

public abstract class AbstractFenceInstanceJobExecutor<F extends FenceInstanceJob> implements FenceInstanceJobExecutor<F> {
	@Resource
	protected FenceEntityBO fenceEntityBO;
	@Resource
	private FenceTemplateBO fenceTemplateBO;
	@Resource
	private FenceInstanceJobBo fenceInstanceJobBo;
	@Resource
	private FencenInstanceBuilder fencenInstanceBuilder;
	@Resource
	private StationBO stationBo;
	
	public void execute(F fenceInstanceJob) {
		FenceInstanceJobUpdateDto fenceInstanceJobUpdateDto = new FenceInstanceJobUpdateDto();
		fenceInstanceJobUpdateDto.setId(fenceInstanceJob.getId());
		fenceInstanceJobUpdateDto.setGmtStartTime(new Date());
		Integer instanceNum = null;
		try {
			instanceNum = doExecute(fenceInstanceJob);
			fenceInstanceJobUpdateDto.setState("SUCCESS");
			fenceInstanceJobUpdateDto.setInstanceNum(instanceNum);
		}catch(Exception e) {
			fenceInstanceJobUpdateDto.setState("ERROR");
			fenceInstanceJobUpdateDto.setErrorMsg(e.getMessage());
		}
		
		fenceInstanceJobUpdateDto.setGmtStartTime(new Date());
		fenceInstanceJobBo.updateJob(fenceInstanceJobUpdateDto);
	}
	
	/**
	 * 
	 * @param fenceInstanceJob
	 * @return 返回变更的实例数量
	 */
	protected abstract int doExecute(F fenceInstanceJob);

	protected FenceTemplateDto getFenceTemplate(Long templateId) {
		return fenceTemplateBO.getFenceTemplateById(templateId);
	}
	
	protected List<FenceEntity> getFenceEntityList(Long templateId) {
		return fenceEntityBO.getFenceEntityByTemplateId(templateId);
	}
	
	protected void updateFenceInstanceJob(FenceInstanceJobUpdateDto fenceInstanceJobUpdateDto) {
		fenceInstanceJobBo.updateJob(fenceInstanceJobUpdateDto);
	}
	
	protected void rebuildFenceEntity(FenceEntity fenceEntity) {
		FenceTemplateDto fenceTemplateDto = fenceTemplateBO.getFenceTemplateById(fenceEntity.getTemplateId());
		Station station = stationBo.getStationById(fenceEntity.getStationId());
		FenceEntity newFenceEntity = fencenInstanceBuilder.build(station, fenceTemplateDto);
		newFenceEntity.setId(fenceEntity.getId());
		newFenceEntity.setGmtCreate(fenceEntity.getGmtCreate());
		fenceEntityBO.updateFenceEntity(fenceEntity);
		//通知菜鸟
	}
	
	protected void buildFenceEntity(Long stationId, Long templateId) {
		FenceTemplateDto fenceTemplateDto = fenceTemplateBO.getFenceTemplateById(templateId);
		Station station = stationBo.getStationById(stationId);
		FenceEntity fenceEntity = fencenInstanceBuilder.build(station, fenceTemplateDto);
		if(fenceEntity != null) {
			fenceEntityBO.addFenceEntity(fenceEntity);
		}
		//通知菜鸟
	}
}
