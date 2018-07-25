package com.taobao.cun.auge.fence.job;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.bo.FenceEntityBO;
import com.taobao.cun.auge.fence.bo.FenceInstanceJobBo;
import com.taobao.cun.auge.fence.bo.FenceTemplateBO;
import com.taobao.cun.auge.fence.cainiao.RailServiceAdapter;
import com.taobao.cun.auge.fence.dto.FenceInstanceJobUpdateDto;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.dto.job.FenceInstanceJob;
import com.taobao.cun.auge.fence.instance.FencenInstanceBuilder;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.oss.client.FileStoreService;

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
	@Resource
	private FileStoreService fileStoreService;
	
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
				String fileName = "fence_job_error_" + fenceInstanceJob.getId() + ".json";
				fileStoreService.saveFile(fileName, fileName, JSON.toJSONString(list).getBytes(),"application/json");
				fenceInstanceJobUpdateDto.setErrorMsg("http://crius.cn-hangzhou.oss-cdn.aliyun-inc.com/" + fileName);
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
	private void buildFenceEntity(Long stationId, Long templateId, Long jobId) {
		FenceTemplateDto fenceTemplateDto = getFenceTemplate(templateId);
		Preconditions.checkNotNull(fenceTemplateDto, "template id=" + templateId);
		Station station = stationBo.getStationById(stationId);
		if(!isServicing(station)) {
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
				fenceEntity.setJobId(old.getJobId());
				fenceEntity.setCreator(old.getCreator());
				fenceEntity.setModifier(fenceInstanceJob.getCreator());
				updateCainiaoFence(fenceEntity);
				fenceEntityBO.updateFenceEntity(fenceEntity);
			}else {
				fenceEntity.setCreator(fenceInstanceJob.getCreator());
				fenceEntity.setModifier(fenceInstanceJob.getCreator());
				fenceEntity.setJobId(jobId);
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
	private boolean isServicing(Station station) {
		if(station == null) {
			return false;
		}
		if(station.getStatus().equals(StationStatusEnum.DECORATING.getCode()) || 
				station.getStatus().equals(StationStatusEnum.SERVICING.getCode()) ||
				station.getStatus().equals(StationStatusEnum.CLOSING.getCode())) {
			return true;
		}
		return false;
	}
	
	/**
	 * 检查站点状态
	 * @param station
	 * @return
	 */
	private boolean isClosed(Station station) {
		if(station == null) {
			return false;
		}
		if(station.getStatus().equals(StationStatusEnum.CLOSED.getCode()) || 
				station.getStatus().equals(StationStatusEnum.QUITING.getCode())||
				station.getStatus().equals(StationStatusEnum.QUIT.getCode())){
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
	protected void overrideFenceEntity(Long stationId, Long templateId, Long jobId) {
		try {
			FenceTemplateDto fenceTemplateDto = getFenceTemplate(templateId);
			Preconditions.checkNotNull(fenceTemplateDto, "template id=" + templateId);
			//删除菜鸟的围栏
			List<FenceEntity> fenceEntities = fenceEntityBO.getStationFenceEntitiesByFenceType(stationId, fenceTemplateDto.getTypeEnum().getCode());
			for(FenceEntity fenceEntity : fenceEntities) {
				if(!fenceEntity.getJobId().equals(jobId)) {
					deleteCainiaoFence(fenceEntity);
					//删除围栏实例
					fenceEntityBO.deleteById(fenceEntity.getId(), fenceTemplateDto.getCreator());
				}
			}
			//新建围栏
			buildFenceEntity(stationId, templateId, jobId);
		}catch(Exception e) {
			addExecuteError("create:override", stationId, templateId, e);
		}
	}
	
	protected void newFenceEntity(Long stationId, Long templateId, Long jobId) {
		try {
			buildFenceEntity(stationId, templateId, jobId);
		}catch(Exception e) {
			addExecuteError("create:new", stationId, templateId, e);
		}
	}
	
	protected void updateFenceEntity(Long stationId, Long templateId) {
		FenceEntity fenceEntity = fenceEntityBO.getStationFenceEntityByTemplateId(stationId, templateId);
		if(fenceEntity == null) {
			return;
		}
		try {
			buildFenceEntity(stationId, templateId, 0L);
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
	
	protected int updateFenceStateByTemplate(Long templateId, String state) {
		FenceInstanceJob fenceInstanceJob = fenceInstanceJobThreadLocal.get();
		List<FenceEntity> fenceEntities = getFenceEntityList(templateId);
		if(fenceEntities != null) {
			for(FenceEntity fenceEntity : fenceEntities) {
				Station station = stationBo.getStationById(fenceEntity.getStationId());
				if(station != null) {
					try {
						if(!isClosed(station)) {//如果站点是CLOSED、QUITING则不处理
							doUpdateFenceState(fenceEntity, state, fenceInstanceJob.getCreator());
						}
					}catch(Exception e) {
						addExecuteError("update-template:state", station.getId(), templateId, e);
					}
				}
			}
			return fenceEntities.size();
		}
		return 0;
	}
	
	private void doUpdateFenceState(FenceEntity fenceEntity, String state, String operator) {
		//更新菜鸟的状态
		updateCainiaoFenceState(fenceEntity, state);
		//更新实例状态
		fenceEntityBO.updateEntityState(fenceEntity.getId(), state, operator);
	}
	
	protected void updateFenceState(FenceEntity fenceEntity, String state, String operator) {
		try {
			doUpdateFenceState(fenceEntity, state, operator);
		}catch(Exception e) {
			addExecuteError("update:state", fenceEntity.getStationId(), fenceEntity.getTemplateId(), e);
		}
	}
	
	
	protected int updateFenceStateByStation(Long stationId, String state) {
		FenceInstanceJob fenceInstanceJob = fenceInstanceJobThreadLocal.get();
		List<FenceEntity> fenceEntities = fenceEntityBO.getFenceEntitiesByStationId(stationId);
		if(fenceEntities != null) {
			for(FenceEntity fenceEntity : fenceEntities) {
				try {
					doUpdateFenceState(fenceEntity, state, fenceInstanceJob.getCreator());
				}catch(Exception e) {
					addExecuteError("update-station:state", stationId, 0L, e);
				}
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
		threadLocal.get().add(new ExecuteError(action, stationId, templateId, error.getMessage(), ExceptionUtils.getStackFrames(error)));
	}
	
	static class ExecuteError{
		@JSONField(ordinal=1)
		private String action;
		@JSONField(ordinal=2)
		private Long stationId;
		@JSONField(ordinal=3)
		private Long templateId;
		@JSONField(ordinal=4)
		private String errorMsg;
		@JSONField(ordinal=100)
		private List<String> errors;
		ExecuteError(String action, Long stationId, Long templateId, String errorMsg, String[] errors){
			this.action = action;
			this.stationId = stationId;
			this.templateId = templateId;
			this.errorMsg = errorMsg;
			
			List<String> errorList = Lists.newArrayList();
			for(String error : errors) {
				errorList.add(error.replaceAll("\t", " - "));
			}
			this.errors = errorList;
		}
		public String getErrorMsg() {
			return errorMsg;
		}
		public void setErrorMsg(String errorMsg) {
			this.errorMsg = errorMsg;
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

		public List<String> getErrors() {
			return errors;
		}

		public void setError(List<String> errors) {
			this.errors = errors;
		}
	}
}
