package com.taobao.cun.auge.asset.bo.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.buc.api.EnhancedUserQueryService;
import com.alibaba.buc.api.exception.BucException;
import com.alibaba.buc.api.model.enhanced.EnhancedUser;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.asset.bo.AssetBO;
import com.taobao.cun.auge.asset.bo.AssetCheckInfoBO;
import com.taobao.cun.auge.asset.bo.AssetCheckTaskBO;
import com.taobao.cun.auge.asset.dto.AssetCheckStationExtInfo;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskCondition;
import com.taobao.cun.auge.asset.dto.AssetCheckTaskDto;
import com.taobao.cun.auge.asset.dto.FinishTaskForCountyDto;
import com.taobao.cun.auge.asset.dto.QueryStationTaskCondition;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskStatusEnum;
import com.taobao.cun.auge.asset.enums.AssetCheckTaskTaskTypeEnum;
import com.taobao.cun.auge.asset.enums.AssetUseAreaTypeEnum;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.dal.domain.Asset;
import com.taobao.cun.auge.dal.domain.AssetCheckInfo;
import com.taobao.cun.auge.dal.domain.AssetCheckTask;
import com.taobao.cun.auge.dal.domain.AssetCheckTaskExample;
import com.taobao.cun.auge.dal.domain.AssetCheckTaskExample.Criteria;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.AssetCheckTaskMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.org.dto.CuntaoOrgDto;
import com.taobao.cun.auge.org.dto.CuntaoUserRole;
import com.taobao.cun.auge.org.service.CuntaoOrgServiceClient;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.service.ProcessService;
import com.taobao.cun.auge.task.dto.TaskInteractionExecuteDto;
import com.taobao.cun.auge.task.service.TaskElementService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.cun.crius.bpm.dto.CuntaoTaskExecuteDto;
import com.taobao.cun.crius.bpm.enums.NodeActionEnum;
import com.taobao.cun.crius.bpm.exception.CuntaoWorkFlowException;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;

import net.sf.cglib.beans.BeanCopier;

@Component
public class AssetCheckTaskBOImpl implements AssetCheckTaskBO {

	
	private static BeanCopier assetCheckTaskVo2DtoCopier = BeanCopier.create(AssetCheckTask.class, AssetCheckTaskDto.class, false);
	private static final Logger logger = LoggerFactory.getLogger(AssetCheckTaskBO.class);

	@Autowired
	private EnhancedUserQueryService enhancedUserQueryService;
	@Autowired
	private AssetCheckTaskMapper assetCheckTaskMapper;
	@Autowired
	private AssetCheckInfoBO assetCheckInfoBO;
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;
    @Autowired
    private StationBO stationBO;
    @Autowired
    private PartnerBO partnerBO;
    @Autowired
    private CuntaoOrgServiceClient cuntaoOrgServiceClient;
    @Autowired
    private TaskElementService taskElementService;
    
	@Autowired
	private ProcessService processService;
	
    @Autowired
    private CuntaoUserRoleService cuntaoUserRoleService;
    
    @Autowired
	private CuntaoWorkFlowService cuntaoWorkFlowService;
    
	@Autowired
	private AssetBO assetBO;
	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void initTaskForStation(String taskType, String taskCode, Long taobaoUserId) {
		AssetCheckTask at = getTaskForStation(String.valueOf(taobaoUserId));
		if (at != null && AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(at.getTaskStatus())) {
			at.setTaskCode(taskCode);
			at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DOING.getCode());
			DomainUtils.beforeUpdate(at, String.valueOf(taobaoUserId));
			assetCheckTaskMapper.updateByPrimaryKeySelective(at);
		}else {
			PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
			if (rel == null) {
				return;
			}
			Station s = stationBO.getStationById(rel.getStationId());
			Partner p = partnerBO.getPartnerById(rel.getPartnerId());
			CuntaoOrgDto o = cuntaoOrgServiceClient.getCuntaoOrg(s.getApplyOrg());
			AssetCheckTask r = new AssetCheckTask();
			r.setCheckerId(String.valueOf(taobaoUserId));
			r.setCheckerName(p.getName());
			r.setCheckerType(AssetUseAreaTypeEnum.STATION.name());
			r.setOrgId(s.getApplyOrg());
			r.setOrgName(o.getName());
			r.setStationExtInfo(bulideStationExtInfo(s,p,rel));
			r.setStationId(rel.getStationId());
			r.setStationName(s.getName());
			r.setTaskCode(taskCode);
			r.setTaskStatus(AssetCheckTaskTaskStatusEnum.TODO.getCode());
			r.setTaskType(taskType);
			DomainUtils.beforeInsert(r, "SYSTEM");
			assetCheckTaskMapper.insert(r);
		}
	}
	
	private String bulideStationExtInfo(Station s,Partner p,PartnerStationRel rel) {
		AssetCheckStationExtInfo e = new AssetCheckStationExtInfo();
		StringBuilder add = new StringBuilder();
		if (StringUtils.isNotEmpty(s.getTownDetail())) {
			add.append(s.getTownDetail());
		}
		if (StringUtils.isNotEmpty(s.getVillageDetail())) {
			add.append(s.getVillageDetail());
		}
		add.append(s.getAddress());
		e.setAddress(add.toString());
		e.setPartnerName(p.getName());
		e.setPhone(p.getMobile());
		e.setStateDesc(PartnerInstanceStateEnum.valueof(rel.getState()).getDesc());
		e.setStationName(s.getName());
		e.setStationNum(s.getStationNum());
		return JSONObject.toJSONString(e);
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean finishTaskForStation(Long taobaoUserId) {
		
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerIdEqualTo(String.valueOf(taobaoUserId));
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.STATION.name());
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		AssetCheckTask at = ResultUtils.selectOne(assetCheckTaskMapper.selectByExample(example));
		if (at == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"未查询到当前服务站盘点任务");
		}
		String status = at.getTaskStatus();
		at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		DomainUtils.beforeUpdate(at, String.valueOf(taobaoUserId));
		assetCheckTaskMapper.updateByPrimaryKeySelective(at);
		
		//系统确认
		assetCheckInfoBO.confrimCheckInfoForSystemToStation(at.getStationId(), String.valueOf(taobaoUserId),at.getCheckerName());
		if(!AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(status)) {
			TaskInteractionExecuteDto o = JSONObject.parseObject(at.getTaskCode(),TaskInteractionExecuteDto.class);
			taskElementService.executeInteraction(o);
		}
		return Boolean.TRUE;
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void initTaskForCounty(List<Long> orgIds) {
		orgIds.stream().distinct().forEach(item->initTaskForCounty(item));
	}
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    private void initTaskForCounty(Long orgId) {
    	try {
			CuntaoOrgDto o = cuntaoOrgServiceClient.getCuntaoOrg(orgId);
			if (o == null) {
				return;
			}
			 //县负责人
            List<CuntaoUserRole> userRoles = cuntaoUserRoleService.getCuntaoUserRoles(orgId, "COUNTY_LEADER_AUDIT");
            
			AssetCheckTask at = getTaskForCounty(orgId,AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
			if (at != null) {
				at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DOING.getCode());
				DomainUtils.beforeUpdate(at, "SYSTEM");
				
				assetCheckTaskMapper.updateByPrimaryKeySelective(at);
				StartProcessDto startProcessDto =new StartProcessDto();
		        startProcessDto.setBusiness(ProcessBusinessEnum.assetCheckCountyTask);
		        startProcessDto.setBusinessId(at.getId());
		        startProcessDto.setBusinessName(o.getName());
		        startProcessDto.setBusinessOrgId(orgId);
		        startProcessDto.setOperator("68694");
		        startProcessDto.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.BUC);
		        processService.startApproveProcess(startProcessDto);
			}else {
				AssetCheckTask r = new AssetCheckTask();
				if (CollectionUtils.isNotEmpty(userRoles)) {
					r.setCheckerId(userRoles.get(0).getUserId());
					r.setCheckerName(userRoles.get(0).getUserName());
				}
				
				r.setCheckerType(AssetUseAreaTypeEnum.COUNTY.name());
				r.setOrgId(orgId);
				r.setOrgName(o.getName());
				//r.setStationExtInfo(bulideStationExtInfo(s,p,rel));
				//r.setStationId(rel.getStationId());
				//r.setStationName(s.getName());
				//r.setTaskCode(taskCode);
				r.setTaskStatus(AssetCheckTaskTaskStatusEnum.TODO.getCode());
				r.setTaskType(AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
				DomainUtils.beforeInsert(r, "SYSTEM");
				assetCheckTaskMapper.insert(r);
				
				StartProcessDto startProcessDto =new StartProcessDto();
		        startProcessDto.setBusiness(ProcessBusinessEnum.assetCheckCountyTask);
		        startProcessDto.setBusinessId(r.getId());
		        startProcessDto.setBusinessName(o.getName());
		        startProcessDto.setBusinessOrgId(orgId);
		        startProcessDto.setOperator("68694");
		        startProcessDto.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.BUC);
		        processService.startApproveProcess(startProcessDto);
			}
			
			AssetCheckTask f = getTaskForCounty(orgId,AssetCheckTaskTaskTypeEnum.COUNTY_FOLLOW.getCode());
			if (f != null) {
				f.setTaskStatus(AssetCheckTaskTaskStatusEnum.DOING.getCode());
				DomainUtils.beforeUpdate(at, "SYSTEM");
				assetCheckTaskMapper.updateByPrimaryKeySelective(at);
				
				StartProcessDto startProcessDto =new StartProcessDto();
		        startProcessDto.setBusiness(ProcessBusinessEnum.assetCheckCountyFollowTask);
		        startProcessDto.setBusinessId(f.getId());
		        startProcessDto.setBusinessName(o.getName());
		        startProcessDto.setBusinessOrgId(orgId);
		        startProcessDto.setOperator("68694");
		        startProcessDto.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.BUC);
		        processService.startApproveProcess(startProcessDto);
			}else {
				AssetCheckTask r1 = new AssetCheckTask();
				if (CollectionUtils.isNotEmpty(userRoles)) {
					r1.setCheckerId(userRoles.get(0).getUserId());
					r1.setCheckerName(userRoles.get(0).getUserName());
				}
				r1.setCheckerType(AssetUseAreaTypeEnum.COUNTY.name());
				r1.setOrgId(orgId);
				r1.setOrgName(o.getName());
				//r.setStationExtInfo(bulideStationExtInfo(s,p,rel));
				//r.setStationId(rel.getStationId());
				//r.setStationName(s.getName());
				//r.setTaskCode(taskCode);
				r1.setTaskStatus(AssetCheckTaskTaskStatusEnum.TODO.getCode());
				r1.setTaskType(AssetCheckTaskTaskTypeEnum.COUNTY_FOLLOW.getCode());
				DomainUtils.beforeInsert(r1, "SYSTEM");
				assetCheckTaskMapper.insert(r1);
				
				StartProcessDto startProcessDto =new StartProcessDto();
		        startProcessDto.setBusiness(ProcessBusinessEnum.assetCheckCountyFollowTask);
		        startProcessDto.setBusinessId(r1.getId());
		        startProcessDto.setBusinessName(o.getName());
		        startProcessDto.setBusinessOrgId(orgId);
		        startProcessDto.setOperator("68694");
		        startProcessDto.setOperatorType(com.taobao.cun.auge.station.enums.OperatorTypeEnum.BUC);
		        processService.startApproveProcess(startProcessDto);
			}
		} catch (Exception e) {
			logger.error("initTaskForCounty error,param:"+orgId,e);
		}
    }
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public Boolean finishTaskForCounty(FinishTaskForCountyDto param) {
		AssetCheckTask at = getTaskForCounty(param.getOperatorOrgId(),param.getTaskType());
		if (at == null) {
			throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"未查询到当前县点盘点任务");
		}
		String status = at.getTaskStatus();
		at.setTaskStatus(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		at.setCheckerId(param.getOperator());
		try {
			EnhancedUser enhancedUser = enhancedUserQueryService.getUser(param.getOperator());
			at.setCheckerName(enhancedUser.getLastName());
		} catch (BucException e) {
		}
		
		if (AssetCheckTaskTaskTypeEnum.COUNTY_FOLLOW.getCode().equals(param.getTaskType())) {
			AssetCheckTask atflow = getTaskForCounty(param.getOperatorOrgId(),AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode());
			if (atflow != null && !AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(atflow.getTaskStatus())) {
				throw new AugeBusinessException(AugeErrorCodes.ASSET_BUSINESS_ERROR_CODE,"请先完成县点盘点任务");
			}
			at.setLostAsset(param.getLostAsset());
			at.setWaitBackAsset(param.getWaitBackAsset());
			at.setOtherReason(param.getOtherReason());
		}
		DomainUtils.beforeUpdate(at, param.getOperator());
		assetCheckTaskMapper.updateByPrimaryKeySelective(at);
		
		if (AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode().equals(param.getTaskType())) {
			assetCheckInfoBO.confrimCheckInfoForSystemToCounty(at.getOrgId(), param.getOperator());
		}
		if(!AssetCheckTaskTaskStatusEnum.DONE.getCode().equals(status)) {
			try {
			   CuntaoTaskExecuteDto executeDto = new CuntaoTaskExecuteDto();
	            executeDto.setLoginId(param.getOperator());
	            executeDto.setRemark("完成盘点任务");
	            executeDto.setObjectId(String.valueOf(at.getId()));
	            if (AssetCheckTaskTaskTypeEnum.COUNTY_CHECK.getCode().equals(at.getTaskType())) {
	            	 executeDto.setBusinessCode("assetCheckCountyTask");
	            }else if(AssetCheckTaskTaskTypeEnum.COUNTY_FOLLOW.getCode().equals(at.getTaskType())) {
	            	 executeDto.setBusinessCode("assetCheckCountyFollowTask");
	            }
	           
	            executeDto.setAction(NodeActionEnum.AGREE);
	            cuntaoWorkFlowService.executeTask(executeDto);
			}catch(Exception e) {}
		}
		return  Boolean.TRUE;
	}

	@Override
	public PageDto<AssetCheckTaskDto> listTasks(AssetCheckTaskCondition param) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		if(StringUtils.isNotEmpty(param.getTaskType())) {
			criteria.andTaskTypeEqualTo(param.getTaskType());
		}
		if(StringUtils.isNotEmpty(param.getOrgName())) {
			criteria.andOrgNameEqualTo(param.getOrgName());
		}
		if(StringUtils.isNotEmpty(param.getStationName())) {
			criteria.andStationNameEqualTo(param.getStationName());
		}
		if(StringUtils.isNotEmpty(param.getTaskStatus())) {
			criteria.andTaskStatusEqualTo(param.getTaskStatus());
		}
		example.setOrderByClause("id desc");
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		Page<AssetCheckTask> page = (Page<AssetCheckTask>)assetCheckTaskMapper.selectByExample(example);
		List<AssetCheckTaskDto> targetList = page.getResult().stream().map(assetCheckTask -> assetCheckTask2Dto(assetCheckTask)).collect(Collectors.toList());
		return PageDtoUtil.success(page, targetList);
	}

	private  AssetCheckTaskDto assetCheckTask2Dto(AssetCheckTask assetCheckTask){
		AssetCheckTaskDto assetCheckTaskDto = new AssetCheckTaskDto();
		assetCheckTaskVo2DtoCopier.copy(assetCheckTask,assetCheckTaskDto,null);
		if (StringUtils.isNotEmpty(assetCheckTask.getStationExtInfo())) {
			assetCheckTaskDto.setStationExtInfo(JSONObject.parseObject(assetCheckTask.getStationExtInfo(), AssetCheckStationExtInfo.class));
		}
		if(AssetCheckTaskTaskTypeEnum.COUNTY_FOLLOW.getCode().equals(assetCheckTask.getTaskType())) {
			List<Asset> a = assetBO.getCheckAsset(assetCheckTask.getOrgId());
			List<AssetCheckInfo> l = assetCheckInfoBO.getInfoByCountyOrgId(assetCheckTask.getOrgId());
			Long assetCount =0L;
			Long doneCount =0L;
			if (CollectionUtils.isNotEmpty(a)) {
				assetCount = new Long(a.size());
			}
			if (CollectionUtils.isNotEmpty(l)) {
				doneCount = new Long(l.size());
			}
			assetCheckTaskDto.setDoneCount(doneCount);
			assetCheckTaskDto.setAssetCount(assetCount);
		}
	
		return assetCheckTaskDto;
	}

	@Override
	public AssetCheckTask getTaskForCounty(Long orgId, String taskType) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andOrgIdEqualTo(orgId);
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.COUNTY.name());
		criteria.andTaskTypeEqualTo(taskType);
		return ResultUtils.selectOne(assetCheckTaskMapper.selectByExample(example));
	}


	@Override
	public AssetCheckTask getTaskForStation(String taobaoUserId) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andCheckerIdEqualTo(taobaoUserId);
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.STATION.name());
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		return ResultUtils.selectOne(assetCheckTaskMapper.selectByExample(example));
	}

	@Override
	public Integer getWaitCheckStationCount(Long countyOrgId) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andOrgIdEqualTo(countyOrgId);
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.STATION.name());
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		criteria.andTaskStatusNotEqualTo(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		return assetCheckTaskMapper.selectByExample(example).size();
	}

	@Override
	public Integer getDoneCheckStationCount(Long countyOrgId) {
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andOrgIdEqualTo(countyOrgId);
		criteria.andCheckerTypeEqualTo(AssetUseAreaTypeEnum.STATION.name());
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		criteria.andTaskStatusEqualTo(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		return assetCheckTaskMapper.selectByExample(example).size();
	}

	@Override
	public PageDto<AssetCheckTaskDto> listTasks(QueryStationTaskCondition param) {
		Objects.requireNonNull(param, "参数不能为空");
		Objects.requireNonNull(param.getCountyOrgId(), "参数不能为空");
		AssetCheckTaskExample example = new AssetCheckTaskExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andTaskTypeEqualTo(AssetCheckTaskTaskTypeEnum.STATION_CHECK.getCode());
		criteria.andOrgIdEqualTo(param.getCountyOrgId());
		if ("y".equals(param.getIsDone())) {
			criteria.andTaskStatusEqualTo(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		}else {
			criteria.andTaskStatusNotEqualTo(AssetCheckTaskTaskStatusEnum.DONE.getCode());
		}
		example.setOrderByClause("id desc");
		PageHelper.startPage(param.getPageNum(), param.getPageSize());
		Page<AssetCheckTask> page = (Page<AssetCheckTask>)assetCheckTaskMapper.selectByExample(example);
		List<AssetCheckTaskDto> targetList = page.getResult().stream().map(assetCheckTask -> assetCheckTask2Dto(assetCheckTask)).collect(Collectors.toList());
		return PageDtoUtil.success(page, targetList);
	}

	@Override
	public void doingTask(Long taskId) {
		AssetCheckTask t =new AssetCheckTask();
		t.setId(taskId);
		t.setTaskStatus(AssetCheckTaskTaskStatusEnum.DOING.getCode());
		DomainUtils.beforeUpdate(t, "SYSTEM");
		assetCheckTaskMapper.updateByPrimaryKeySelective(t);
	}

}
