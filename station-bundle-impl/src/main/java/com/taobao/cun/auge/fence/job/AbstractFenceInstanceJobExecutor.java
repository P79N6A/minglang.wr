package com.taobao.cun.auge.fence.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import com.google.common.base.Preconditions;
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
		
		fenceInstanceJobUpdateDto.setGmtEndTime(new Date());
		fenceInstanceJobBo.updateJob(fenceInstanceJobUpdateDto);
	}
	
	/**
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
	
	protected void buildFenceEntity(Long stationId, Long templateId) {
		FenceTemplateDto fenceTemplateDto = getFenceTemplate(templateId);
		Preconditions.checkNotNull(fenceTemplateDto, "template id=" + templateId);
		Station station = stationBo.getStationById(stationId);
		Preconditions.checkNotNull(station, "station id=" + stationId);
		
		FenceEntity fenceEntity = fencenInstanceBuilder.build(station, fenceTemplateDto);
		if(fenceEntity != null) {
			//检查是否存在
			FenceEntity old = fenceEntityBO.getStationFenceEntityByTemplateId(stationId, templateId);
			if(old != null) {
				fenceEntity.setId(old.getId());
				fenceEntity.setCainiaoFenceId(old.getCainiaoFenceId());
				fenceEntity.setGmtCreate(old.getGmtCreate());
				fenceEntityBO.updateFenceEntity(fenceEntity);
				updateCainiaoFence(fenceEntity);
			}else {
				fenceEntityBO.addFenceEntity(fenceEntity);
				addCainiaoFence(fenceEntity);
			}
		}
	}
	
	protected void deleteFenceEntity(Long stationId, Long templateId) {
		FenceEntity fenceEntity = fenceEntityBO.getStationFenceEntityByTemplateId(stationId, templateId);
		deleteFenceEntity(fenceEntity);
	}
	
	protected void deleteFenceEntity(FenceEntity fenceEntity) {
		if(fenceEntity != null) {
			deleteCainiaoFence(fenceEntity);
			fenceEntityBO.deleteById(fenceEntity.getId());
		}
	}
	
	protected void overrideFenceEntity(Long stationId, Long templateId) {
		FenceTemplateDto fenceTemplateDto = getFenceTemplate(templateId);
		Preconditions.checkNotNull(fenceTemplateDto, "template id=" + templateId);
		//删除菜鸟的围栏
		List<FenceEntity> fenceEntities = fenceEntityBO.getStationFenceEntitiesByFenceType(stationId, fenceTemplateDto.getTypeEnum().getCode());
		for(FenceEntity fenceEntity : fenceEntities) {
			deleteCainiaoFence(fenceEntity);
		}
		//删除围栏实例
		fenceEntityBO.deleteFences(stationId, fenceTemplateDto.getTypeEnum().getCode());
		//新建围栏
		buildFenceEntity(stationId, templateId);
	}
	
	protected void addCainiaoFence(FenceEntity fenceEntity) {
		toCainiaoFence(fenceEntity);
		//TODO 调用菜鸟的新增接口
		//TODO 将菜鸟的围栏ID写回到围栏实例上
	}
	
	protected void updateCainiaoFence(FenceEntity fenceEntity) {
		toCainiaoFence(fenceEntity);
		//TODO 调用菜鸟的更新接口
	}
	
	protected void deleteCainiaoFence(FenceEntity fenceEntity) {
		toCainiaoFence(fenceEntity);
		//TODO 调用菜鸟的删除接口
	}
	
	/**
	 * 转成菜鸟的围栏对象
	 * @param fenceEntity
	 */
	private void toCainiaoFence(FenceEntity fenceEntity) {
		
	}
}
