package com.taobao.cun.auge.fence.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.cun.auge.fence.constant.FenceConstants;
import com.taobao.cun.auge.fence.dto.FenceInstanceJobUpdateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.instance.FencenInstanceBuilder;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.StationStatusEnum;

/**
 * 抽象实现，对围栏实例的操作，与菜鸟的交互都在这里了
 * 
 * @author chengyu.zhoucy
 *
 * @param <F>
 */
public abstract class AbstractFenceInstanceJobExecutor<F extends FenceInstanceJob> implements FenceInstanceJobExecutor<F> {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Resource
	protected FenceEntityBO fenceEntityBO;
	@Resource
	private FenceTemplateBO fenceTemplateBO;
	@Resource
	private FenceInstanceJobBo fenceInstanceJobBo;
	@Resource
	private FencenInstanceBuilder fencenInstanceBuilder;
	@Resource
	protected StationBO stationBo;
	@Resource
	private RailServiceAdapter railServiceAdapter;
	
	private static final ThreadLocal<List<ExecuteError>> threadLocal = new ThreadLocal<List<ExecuteError>>();
	
	private static final ThreadLocal<FenceInstanceJob> fenceInstanceJobThreadLocal = new ThreadLocal<FenceInstanceJob>();
	
	public void execute(F fenceInstanceJob) {
		FenceInstanceJobUpdateDto fenceInstanceJobUpdateDto = new FenceInstanceJobUpdateDto();
		fenceInstanceJobUpdateDto.setId(fenceInstanceJob.getId());
		fenceInstanceJobUpdateDto.setGmtStartTime(new Date());
		fenceInstanceJobUpdateDto.setState("PENDING");
		fenceInstanceJobUpdateDto.setInstanceNum(0);
		fenceInstanceJobBo.updateJob(fenceInstanceJobUpdateDto);
		Integer instanceNum = null;
		threadLocal.set(Lists.newArrayList());
		fenceInstanceJobThreadLocal.set(fenceInstanceJob);
		try {
			instanceNum = doExecute(fenceInstanceJob);
		}finally {
			List<ExecuteError> list = threadLocal.get();
			if(list.isEmpty()) {
				fenceInstanceJobUpdateDto.setState("SUCCESS");
			}else {
				fenceInstanceJobUpdateDto.setState("ERROR");
				fenceInstanceJobUpdateDto.setErrorMsg(JSON.toJSONString(list));
			}
			fenceInstanceJobUpdateDto.setInstanceNum(instanceNum);
			fenceInstanceJobUpdateDto.setGmtEndTime(new Date());
			fenceInstanceJobBo.updateJob(fenceInstanceJobUpdateDto);
			threadLocal.remove();
			fenceInstanceJobThreadLocal.remove();
		}
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
	
	/**
	 * 构建围栏实例
	 * @param stationId
	 * @param templateId
	 */
	private void buildFenceEntity(Long stationId, Long templateId) {
		FenceTemplateDto fenceTemplateDto = getFenceTemplate(templateId);
		Preconditions.checkNotNull(fenceTemplateDto, "template id=" + templateId);
		Station station = stationBo.getStationById(stationId);
		if(!checkStation(station)) {
			return;
		}
		
		FenceEntity fenceEntity = fencenInstanceBuilder.build(station, fenceTemplateDto);
		if(fenceEntity != null) {
			FenceInstanceJob fenceInstanceJob = fenceInstanceJobThreadLocal.get();
			//检查是否存在：如果已经存在该站点跟该模板构建的实例，那么就做修改
			FenceEntity old = fenceEntityBO.getStationFenceEntityByTemplateId(stationId, templateId);
			if(old != null) {
				fenceEntity.setId(old.getId());
				fenceEntity.setCainiaoFenceId(old.getCainiaoFenceId());
				fenceEntity.setGmtCreate(old.getGmtCreate());
				fenceEntity.setVersion(old.getVersion() + 1);
				fenceEntity.setCreator(old.getCreator());
				fenceEntity.setModifier(fenceInstanceJob.getCreator());
				updateCainiaoFence(fenceEntity);
				fenceEntityBO.updateFenceEntity(fenceEntity);
			}else {
				fenceEntity.setCreator(fenceInstanceJob.getCreator());
				fenceEntity.setModifier(fenceInstanceJob.getCreator());
				Long cainiaoFenceId = addCainiaoFence(fenceEntity);
				fenceEntity.setCainiaoFenceId(cainiaoFenceId);
				fenceEntityBO.addFenceEntity(fenceEntity);
			}
		}
	}
	
	/**
	 * 检查站点状态
	 * @param station
	 * @return
	 */
	private boolean checkStation(Station station) {
		if(station == null) {
			return false;
		}
		if(station.getStatus().equals(StationStatusEnum.DECORATING.getCode()) || 
				station.getStatus().equals(StationStatusEnum.SERVICING.getCode()) ||
				station.getStatus().equals(StationStatusEnum.QUITING.getCode()) ||
				station.getStatus().equals(StationStatusEnum.CLOSING.getCode())) {
			return true;
		}
		return false;
	}

	/**
	 * 删除围栏实例
	 * @param stationId
	 * @param templateId
	 */
	protected void deleteFenceEntity(Long stationId, Long templateId) {
		deleteFenceEntity(fenceEntityBO.getStationFenceEntityByTemplateId(stationId, templateId));
	}
	
	/**
	 * 删除围栏实例
	 * @param fenceEntity
	 */
	protected void deleteFenceEntity(FenceEntity fenceEntity) {
		FenceInstanceJob fenceInstanceJob = fenceInstanceJobThreadLocal.get();
		try {
			if(fenceEntity != null) {
				deleteCainiaoFence(fenceEntity);
				fenceEntityBO.deleteById(fenceEntity.getId(), fenceInstanceJob.getCreator());
			}
		}catch(Exception e) {
			addExecuteError("delete", fenceEntity.getStationId(), fenceEntity.getTemplateId(), e);
		}
	}
	
	/**
	 * 覆盖围栏
	 * 1. 找出相同类型的围栏，将其全部删除(通知菜鸟删除，围栏实例也要删除)
	 * 2. 重新生成一个围栏实例，并且通知到菜鸟
	 * 为了简单起见，在删除的时候不考虑相同已存在相同模板实例的情况
	 * @param stationId
	 * @param templateId
	 */
	protected void overrideFenceEntity(Long stationId, Long templateId) {
		try {
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
		}catch(Exception e) {
			addExecuteError("create:override", stationId, templateId, e);
		}
	}
	
	protected void newFenceEntity(Long stationId, Long templateId) {
		try {
			buildFenceEntity(stationId, templateId);
		}catch(Exception e) {
			addExecuteError("create:new", stationId, templateId, e);
		}
	}
	
	protected void updateFenceEntity(Long stationId, Long templateId) {
		try {
			buildFenceEntity(stationId, templateId);
		}catch(Exception e) {
			addExecuteError("update", stationId, templateId, e);
		}
	}
	
	protected void updateCainiaoFenceState(FenceEntity fenceEntity, String state) {
		fenceEntity.setState(state);
		try {
			updateCainiaoFence(fenceEntity);
		}catch(Exception e) {
			addExecuteError("updatestate", fenceEntity.getStationId(), fenceEntity.getTemplateId(), e);
		}
		
	}
	
	protected int updateFenceState(Long templateId, String state) {
		FenceInstanceJob fenceInstanceJob = fenceInstanceJobThreadLocal.get();
		if(FenceConstants.ENABLE.equals(state)) {
			fenceEntityBO.enableEntityListByTemplateId(templateId, fenceInstanceJob.getCreator());
		}else {
			fenceEntityBO.disableEntityListByTemplateId(templateId, fenceInstanceJob.getCreator());
		}
		List<FenceEntity> fenceEntities = getFenceEntityList(templateId);
		if(fenceEntities != null) {
			for(FenceEntity fenceEntity : fenceEntities) {
				//更新菜鸟的状态
				updateCainiaoFenceState(fenceEntity, state);
			}
			return fenceEntities.size();
		}
		return 0;
	}
	
	/**
	 * 添加菜鸟围栏
	 * @param fenceEntity
	 */
	private Long addCainiaoFence(FenceEntity fenceEntity) {
		//调用菜鸟的新增接口
		return railServiceAdapter.addCainiaoFence(fenceEntity);
	}
	
	/**
	 * 更新菜鸟围栏
	 * @param fenceEntity
	 */
	private void updateCainiaoFence(FenceEntity fenceEntity) {
		railServiceAdapter.updateCainiaoFence(fenceEntity);
	}
	
	/**
	 * 删除菜鸟围栏
	 * @param fenceEntity
	 */
	private void deleteCainiaoFence(FenceEntity fenceEntity) {
		railServiceAdapter.deleteCainiaoFence(fenceEntity.getCainiaoFenceId());
	}
	
	private void addExecuteError(String action, Long stationId, Long templateId, Throwable error) {
		logger.error("action={}, stationId={}, templateId={}", action, stationId, templateId, error);
		threadLocal.get().add(new ExecuteError(action, stationId, templateId, error.getMessage()));
	}
	
	static class ExecuteError{
		private String action;
		
		private Long stationId;
		
		private Long templateId;
		
		private String error;
		ExecuteError(String action, Long stationId, Long templateId, String error){
			this.action = action;
			this.stationId = stationId;
			this.templateId = templateId;
			this.error = error;
		}
		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public Long getStationId() {
			return stationId;
		}

		public void setStationId(Long stationId) {
			this.stationId = stationId;
		}

		public Long getTemplateId() {
			return templateId;
		}

		public void setTemplateId(Long templateId) {
			this.templateId = templateId;
		}

		public String getError() {
			return error;
		}

		public void setError(String error) {
			this.error = error;
		}
	}
}
