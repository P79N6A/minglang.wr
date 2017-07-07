package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.PartnerTpg;
import com.taobao.cun.auge.event.enums.PartnerInstanceTypeChangeEnum;
import com.taobao.cun.auge.msg.dto.MailSendDto;
import com.taobao.cun.auge.msg.dto.SmsSendDto;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerTpgBO;
import com.taobao.cun.auge.station.bo.PartnerTypeChangeApplyBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.AlipayStandardBailDto;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.BatchMailDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelProcessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceThrawSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerTypeChangeApplyDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncUpgradeToTPForTpaDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.dto.GeneralTaskRetryConfigDto;
import com.taobao.cun.chronus.enums.TaskPriority;
import com.taobao.cun.chronus.service.TaskSubmitService;

@Service("generalTaskSubmitService")
public class GeneralTaskSubmitServiceImpl implements GeneralTaskSubmitService {

	private static final Logger logger = LoggerFactory.getLogger(GeneralTaskSubmitService.class);
	private static final String TASK_SUBMIT_ERROR_MSG = "TASK_SUBMIT_ERROR";
	public static final String OUT_ORDER_NO_PRE = "CT";

	@Autowired
	TaskSubmitService taskSubmitService;
	@Autowired
	UicReadAdapter uicReadAdapter;
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;

	@Value("${cuntao.alipay.standerBailTypeCode}")
	String standerBailTypeCode;

	@Autowired
	PartnerBO partnerBO;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	AccountMoneyBO accountMoneyBO;
	
	@Autowired
	PartnerTpgBO partnerTpgBO;
	
	@Autowired
	PartnerInstanceService partnerInstanceService;
	
	@Autowired
	PartnerTypeChangeApplyBO partnerTypeChangeApplyBO;
	
	public void submitSettlingSysProcessTasks(PartnerInstanceDto instance, String operator) {
		try {
			// 异构系统交互提交后台任务
			List<GeneralTaskDto> taskDtos = Lists.newArrayList();

			// 菜鸟驿站同步 begin
			GeneralTaskDto cainiaoTaskDto;
			
			//是否是升级的合伙人
			if (partnerTypeChangeApplyBO.isUpgradePartnerInstance(instance.getId(),PartnerInstanceTypeChangeEnum.TPA_UPGRADE_2_TP)) {
				PartnerTypeChangeApplyDto typeChangeApplyDto = partnerTypeChangeApplyBO.getPartnerTypeChangeApply(instance.getId());
				// 解冻淘帮手保证金
				partnerInstanceService.thawMoney(typeChangeApplyDto.getPartnerInstanceId());
				cainiaoTaskDto = buildUpgradeCainiaoTask(typeChangeApplyDto.getPartnerInstanceId(),instance.getId(), operator);
			} else {
				cainiaoTaskDto = buildAddCainiaoTask(instance.getId(), operator);
			}
			
			cainiaoTaskDto.setBusinessStepNo(1l);
			taskDtos.add(cainiaoTaskDto);
			// TODO 菜鸟驿站同步 end

			// TODO uic打标 begin
			GeneralTaskDto uicGeneralTaskDto = buildAddUicTagTask(instance, operator);
			uicGeneralTaskDto.setBusinessStepNo(2l);
			taskDtos.add(uicGeneralTaskDto);
			// TODO uic打标 end

			// TODO 申请单更新为服务中 begin
			GeneralTaskDto stationConfirmGeneralTaskDto = buildInstanceSettleSuccessTask(instance.getId(), operator);
			stationConfirmGeneralTaskDto.setBusinessStepNo(3l);
			taskDtos.add(stationConfirmGeneralTaskDto);
			// TODO 申请单更新为服务中 end

			// TODO 旺旺打标 begin
			GeneralTaskDto wangwangGeneralTaskDto = buildAddWangwangTagTask(instance, operator);
			wangwangGeneralTaskDto.setBusinessStepNo(4l);
			taskDtos.add(wangwangGeneralTaskDto);
			// TODO 旺旺打标 end

			// TODO 提交任务
			taskSubmitService.submitTasks(taskDtos);
			logger.info("submitSettlingSysProcessTasks : {}", JSON.toJSONString(taskDtos));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[submitSettlingSysProcessTasks] instanceId = {}, {}", instance.getId(), e);
			throw new AugeServiceException("submitSettlingSysProcessTasks error: " + e.getMessage());
		}
	}
	
	private GeneralTaskDto buildUpgradeCainiaoTask(Long oldInsId,Long newInsId,String operator) {
		String businessNo = String.valueOf(newInsId);
		
		GeneralTaskDto cainiaoTaskDto = new GeneralTaskDto();
		cainiaoTaskDto.setBusinessNo(businessNo);
		cainiaoTaskDto.setBeanName("caiNiaoService");
		cainiaoTaskDto.setMethodName("upgradeToTPForTpa");

		cainiaoTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
		cainiaoTaskDto.setBusinessStepDesc("upgradeToTPForTpa");
		cainiaoTaskDto.setOperator(operator);
		cainiaoTaskDto.setPriority(TaskPriority.HIGH);

		SyncUpgradeToTPForTpaDto syncUpgradeToTPForTpaDto = new SyncUpgradeToTPForTpaDto();
		syncUpgradeToTPForTpaDto.setOldPartnerInstanceId(oldInsId);
		syncUpgradeToTPForTpaDto.setPartnerInstanceId(newInsId);
		cainiaoTaskDto.setParameterType(SyncUpgradeToTPForTpaDto.class.getName());
		cainiaoTaskDto.setParameter(JSON.toJSONString(syncUpgradeToTPForTpaDto));
		return cainiaoTaskDto;
	}

	private GeneralTaskDto buildAddCainiaoTask(Long instanceId, String operator) {
		String businessNo = String.valueOf(instanceId);
		
		GeneralTaskDto cainiaoTaskDto = new GeneralTaskDto();
		cainiaoTaskDto.setBusinessNo(businessNo);
		cainiaoTaskDto.setBeanName("caiNiaoService");
		cainiaoTaskDto.setMethodName("addCainiaoStation");
	
		cainiaoTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
		cainiaoTaskDto.setBusinessStepDesc("addCainiaoStation");
		cainiaoTaskDto.setOperator(operator);
		cainiaoTaskDto.setPriority(TaskPriority.HIGH);

		SyncAddCainiaoStationDto syncAddCainiaoStationDto = new SyncAddCainiaoStationDto();
		syncAddCainiaoStationDto.setPartnerInstanceId(instanceId);
		cainiaoTaskDto.setParameterType(SyncAddCainiaoStationDto.class.getName());
		cainiaoTaskDto.setParameter(JSON.toJSONString(syncAddCainiaoStationDto));
		return cainiaoTaskDto;
	}
	
	private GeneralTaskDto buildReServiceTask(Long instanceId, String operator) {
        String businessNo = String.valueOf(instanceId);
        
        GeneralTaskDto cainiaoTaskDto = new GeneralTaskDto();
        cainiaoTaskDto.setBusinessNo(businessNo);
        cainiaoTaskDto.setBeanName("caiNiaoService");
        cainiaoTaskDto.setMethodName("updateAdmin");
    
        cainiaoTaskDto.setBusinessType(TaskBusinessTypeEnum.CLOSE_TO_SERVICE.getCode());
        cainiaoTaskDto.setBusinessStepDesc("updateAdmin");
        cainiaoTaskDto.setOperator(operator);
        cainiaoTaskDto.setPriority(TaskPriority.HIGH);

        SyncAddCainiaoStationDto syncAddCainiaoStationDto = new SyncAddCainiaoStationDto();
        syncAddCainiaoStationDto.setPartnerInstanceId(instanceId);
        cainiaoTaskDto.setParameterType(SyncAddCainiaoStationDto.class.getName());
        cainiaoTaskDto.setParameter(JSON.toJSONString(syncAddCainiaoStationDto));
        return cainiaoTaskDto;
    }

	private GeneralTaskDto buildAddUicTagTask(PartnerInstanceDto instance, String operator) {
		String businessNo = String.valueOf(instance.getId());
		
		GeneralTaskDto uicGeneralTaskDto = new GeneralTaskDto();
		uicGeneralTaskDto.setBusinessNo(businessNo);
		uicGeneralTaskDto.setBeanName("uicTagService");
		uicGeneralTaskDto.setMethodName("addUserTag");
	
		uicGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
		uicGeneralTaskDto.setBusinessStepDesc("addUserTag");
		uicGeneralTaskDto.setOperator(operator);
		UserTagDto userTagDto = new UserTagDto();
		userTagDto.setPartnerType(instance.getType());
		userTagDto.setTaobaoUserId(instance.getTaobaoUserId());
		uicGeneralTaskDto.setParameterType(UserTagDto.class.getName());
		uicGeneralTaskDto.setParameter(JSON.toJSONString(userTagDto));
		uicGeneralTaskDto.setPriority(TaskPriority.HIGH);
		return uicGeneralTaskDto;
	}

	private GeneralTaskDto buildInstanceSettleSuccessTask(Long instanceId, String operator) {
		String businessNo = String.valueOf(instanceId);
		
		GeneralTaskDto stationConfirmGeneralTaskDto = new GeneralTaskDto();
		stationConfirmGeneralTaskDto.setBusinessNo(businessNo);
		stationConfirmGeneralTaskDto.setBeanName("partnerInstanceService");
		stationConfirmGeneralTaskDto.setMethodName("applySettleSuccess");
		stationConfirmGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
		
		stationConfirmGeneralTaskDto.setBusinessStepDesc("applySettleSuccess");
		stationConfirmGeneralTaskDto.setOperator(operator);

		PartnerInstanceSettleSuccessDto settleSuccessDto = new PartnerInstanceSettleSuccessDto();
		settleSuccessDto.setInstanceId(instanceId);
		settleSuccessDto.setOperator(operator);
		settleSuccessDto.setOperatorType(OperatorTypeEnum.HAVANA);
		stationConfirmGeneralTaskDto.setParameterType(PartnerInstanceSettleSuccessDto.class.getName());
		stationConfirmGeneralTaskDto.setParameter(JSON.toJSONString(settleSuccessDto));
		stationConfirmGeneralTaskDto.setPriority(TaskPriority.HIGH);
		return stationConfirmGeneralTaskDto;
	}

	private GeneralTaskDto buildAddWangwangTagTask(PartnerInstanceDto instance, String operator) {
		String businessNo = String.valueOf(instance.getId());
		
		GeneralTaskDto wangwangGeneralTaskDto = new GeneralTaskDto();
		wangwangGeneralTaskDto.setBusinessNo(businessNo);
		wangwangGeneralTaskDto.setBeanName("wangWangTagService");
		wangwangGeneralTaskDto.setMethodName("addWangWangTagByNick");
		
		wangwangGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
		wangwangGeneralTaskDto.setBusinessStepDesc("addWangWangTagByNick");
		wangwangGeneralTaskDto.setOperator(operator);
		wangwangGeneralTaskDto.setParameterType(String.class.getName());
		wangwangGeneralTaskDto.setParameter(instance.getPartnerDto().getTaobaoNick());
		GeneralTaskRetryConfigDto retry = new GeneralTaskRetryConfigDto();
		retry.setIntervalTime(6 * 3600);// 失败5小时重试
		retry.setMaxRetryTimes(120);// 失败一天执行4次,一个月120次，超过业务人工介入
		retry.setIntervalIncrement(false);
		wangwangGeneralTaskDto.setRetryTaskConfig(retry);
		wangwangGeneralTaskDto.setPriority(TaskPriority.HIGH);
		return wangwangGeneralTaskDto;
	}

	@Override
	public void submitUpdateCainiaoStation(Long instanceId, String operatorId) throws AugeServiceException {
		try {
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(instanceId));
			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setMethodName("updateCainiaoStation");
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.UPDATE_SERVICING_CAINIAO.getCode());
			cainiaoTaskVo.setBusinessStepDesc("修改物流站点");
			cainiaoTaskVo.setOperator(operatorId);

			SyncModifyCainiaoStationDto syncModifyCainiaoStationDto = new SyncModifyCainiaoStationDto();
			syncModifyCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));
			cainiaoTaskVo.setParameterType(SyncModifyCainiaoStationDto.class.getName());
			cainiaoTaskVo.setParameter(JSON.toJSONString(syncModifyCainiaoStationDto));

			// 提交任务
			taskSubmitService.submitTask(cainiaoTaskVo);
			logger.info("submitUpdateCainiaoStation : {}", JSON.toJSONString(cainiaoTaskVo));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[submitUpdateCainiaoStation] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitUpdateCainiaoStation error: " + e.getMessage());
		}

	}

	@Override
	public void submitDegradePartner(PartnerInstanceDto instanceDto, PartnerInstanceDto parentInstanceDto, OperatorDto operatorDto)
			throws AugeServiceException {
		try {
			// 异构系统交互提交后台任务
			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

			// 合伙人降级菜鸟同步
			GeneralTaskDto cainiaoTask = new GeneralTaskDto();
			cainiaoTask.setBusinessNo(String.valueOf(instanceDto.getId()));
			cainiaoTask.setBeanName("caiNiaoService");
			cainiaoTask.setMethodName("updateCainiaoStationFeatureForTPDegree");
			cainiaoTask.setBusinessStepNo(1l);
			cainiaoTask.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
			cainiaoTask.setBusinessStepDesc("updateCainiaoStationFeatureForTPDegree");
			cainiaoTask.setOperator(operatorDto.getOperator());
			SyncTPDegreeCainiaoStationDto syncTPDegreeCainiaoStationDto = new SyncTPDegreeCainiaoStationDto();
			syncTPDegreeCainiaoStationDto.setStationId(instanceDto.getStationId());
			syncTPDegreeCainiaoStationDto.setParentStationId(parentInstanceDto.getStationId());
			syncTPDegreeCainiaoStationDto.setTaobaoUserId(instanceDto.getTaobaoUserId());
			syncTPDegreeCainiaoStationDto.setParentTaobaoUserId(parentInstanceDto.getTaobaoUserId());
			cainiaoTask.setParameterType(SyncTPDegreeCainiaoStationDto.class.getName());
			cainiaoTask.setParameter(JSON.toJSONString(syncTPDegreeCainiaoStationDto));
			taskLists.add(cainiaoTask);

			// UIC打标
			UserTagDto userTagDto = new UserTagDto();
			userTagDto.setTaobaoUserId(instanceDto.getTaobaoUserId());
			userTagDto.setPartnerType(PartnerInstanceTypeEnum.TPA);

			GeneralTaskDto task = new GeneralTaskDto();
			task.setBusinessNo(String.valueOf(instanceDto.getId()));
			task.setBeanName("uicTagService");
			task.setMethodName("addUserTag");
			task.setBusinessStepNo(2l);
			task.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
			task.setBusinessStepDesc("addUserTag");
			task.setOperator(operatorDto.getOperator());
			task.setParameterType(UserTagDto.class.getName());
			task.setParameter(JSON.toJSONString(userTagDto));
			taskLists.add(task);

			GeneralTaskDto succTask = new GeneralTaskDto();
			succTask.setBusinessNo(String.valueOf(instanceDto.getId()));
			succTask.setBeanName("partnerInstanceService");
			succTask.setMethodName("degradePartnerInstanceSuccess");
			succTask.setBusinessStepNo(3l);
			succTask.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
			succTask.setBusinessStepDesc("degradePartnerInstanceSuccess");
			succTask.setOperator(operatorDto.getOperator());
			DegradePartnerInstanceSuccessDto degradePartnerInstanceSuccessDto = new DegradePartnerInstanceSuccessDto();
			degradePartnerInstanceSuccessDto.setInstanceId(instanceDto.getId());
			degradePartnerInstanceSuccessDto.setParentInstanceId(parentInstanceDto.getId());
			degradePartnerInstanceSuccessDto.copyOperatorDto(operatorDto);
			succTask.setParameterType(DegradePartnerInstanceSuccessDto.class.getName());
			succTask.setParameter(JSON.toJSONString(degradePartnerInstanceSuccessDto));
			taskLists.add(succTask);

			// 旺旺打标 begin
			if (PartnerInstanceStateEnum.CLOSED.getCode().equals(instanceDto.getState().getCode())) {
				String taobaoNick = uicReadAdapter.getTaobaoNickByTaobaoUserId(instanceDto.getTaobaoUserId());
				GeneralTaskDto wwTask = new GeneralTaskDto();

				wwTask.setBusinessNo(String.valueOf(instanceDto.getId()));
				wwTask.setBeanName("wangWangTagService");
				wwTask.setMethodName("addWangWangTagByNick");
				wwTask.setBusinessStepNo(4l);
				wwTask.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
				wwTask.setBusinessStepDesc("addWangWangTag");
				wwTask.setOperator(operatorDto.getOperator());
				wwTask.setParameterType(String.class.getName());
				wwTask.setParameter(taobaoNick);
				taskLists.add(wwTask);
			}

			taskSubmitService.submitTasks(taskLists);
			logger.info("submitDegradePartner : {}", JSON.toJSONString(taskLists));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[submitDegradePartner] instanceId = {}, {}", instanceDto.getId(), e);
			throw new AugeServiceException("submitDegradePartner error: " + e.getMessage());
		}
	}

	/**
	 * 启动停业、退出流程审批流程
	 * 
	 * @param processTask
	 */
	public void submitApproveProcessTask(ApproveProcessTask processTask) {
		BeanValidator.validateWithThrowable(processTask);
		try {
			StartProcessDto startProcessDto = new StartProcessDto();

			startProcessDto.setBusiness(processTask.getBusiness());
			startProcessDto.setBusinessId(processTask.getBusinessId());
			startProcessDto.setBusinessName(processTask.getBusinessName());
			startProcessDto.setBusinessOrgId(processTask.getBusinessOrgId());
			startProcessDto.copyOperatorDto(processTask);
			startProcessDto.setJsonParams(FeatureUtil.toStringUnencode(processTask.getParams()));
			// 启动流程
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(processTask.getBusinessId()));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(processTask.getBusiness().getCode());
			startProcessTask.setBusinessStepDesc(processTask.getBusiness().getDesc());
			startProcessTask.setBeanName("processService");
			startProcessTask.setMethodName("startApproveProcess");
			startProcessTask.setOperator(processTask.getOperator());
			startProcessTask.setParameterType(StartProcessDto.class.getName());
			startProcessTask.setParameter(JSON.toJSONString(startProcessDto));

			GeneralTaskRetryConfigDto config = new GeneralTaskRetryConfigDto();
			// 20s执行一次
			config.setIntervalTime(20000);
			// 提交任务
			taskSubmitService.submitTask(startProcessTask, config);
			logger.info("submitApproveProcessTask : {}", JSON.toJSONString(startProcessTask));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitApproveProcessTask] ApproveProcessTask = " + JSON.toJSONString(processTask), e);
			throw new AugeServiceException(
					TASK_SUBMIT_ERROR_MSG + " [submitApproveProcessTask] ApproveProcessTask = " + JSON.toJSONString(processTask), e);
		}
	}

	/**
	 * 发短信
	 * 
	 * @param taobaoUserId
	 * @param mobile
	 * @param operatorId
	 */
	public void submitSmsTask(Long taobaoUserId, String mobile, String operatorId, String content) {
		if (StringUtil.isEmpty(mobile)) {
			logger.error("合伙人手机号码为空。taobaoUserId=" + taobaoUserId);
			return;
		}

		try {
			SmsSendDto smsDto = new SmsSendDto();

			smsDto.setContent(content);
			smsDto.setMobilelist(new String[] { mobile });
			smsDto.setOperator(operatorId);

			GeneralTaskDto task = new GeneralTaskDto();

			task.setBusinessNo(String.valueOf(taobaoUserId));
			task.setBeanName("messageService");
			task.setMethodName("sendSmsMsg");
			task.setBusinessStepNo(1l);
			task.setBusinessType(TaskBusinessTypeEnum.SMS.getCode());
			task.setBusinessStepDesc("发短信");
			task.setOperator(operatorId);
			task.setParameterType(SmsSendDto.class.getName());
			task.setParameter(JSON.toJSONString(smsDto));
			taskSubmitService.submitTask(task);
			logger.info("submitSmsTask : {}", JSON.toJSONString(task));
		} catch (Exception e) {
			String msg = TASK_SUBMIT_ERROR_MSG + " [submitSmsTask] mobile=" + mobile + " taobaouserid = " + taobaoUserId + " content = "
					+ content;
			logger.error(msg, e);
			throw new AugeServiceException("submitSmsTask error: " + e.getMessage());
		}
	}
	
	@Override
	public void submitMailTask(BatchMailDto batchMailDto) {
		if (CollectionUtils.isEmpty(batchMailDto.getMailAddresses())) {
			logger.error("邮件地址为空");
			return;
		}
		try {
			MailSendDto mailDto = new MailSendDto();

			mailDto.setContent(batchMailDto.getTemplateId());
			mailDto.setSourceId(batchMailDto.getSourceId());
			mailDto.setMessageType(batchMailDto.getMessageTypeId());
			mailDto.setTemplateId(batchMailDto.getTemplateId());
			mailDto.setOperator(batchMailDto.getOperator());
			mailDto.setMailAddress(batchMailDto.getMailAddresses());
			mailDto.setContentMap(batchMailDto.getContentMap());
			
			GeneralTaskDto task = new GeneralTaskDto();
			Map<String, Object> contentMap = batchMailDto.getContentMap();
			if (contentMap != null) {
				task.setBusinessNo(String.valueOf(contentMap.get("station_id")));
			} else {
				task.setBusinessNo(String.valueOf(batchMailDto.getOperator()));
			}
			task.setBeanName("messageService");
			task.setMethodName("sendMail");
			task.setBusinessStepNo(1l);
			task.setBusinessType(TaskBusinessTypeEnum.MAIL.getCode());
			task.setBusinessStepDesc("发邮件");
			task.setOperator(batchMailDto.getOperator());
			task.setParameterType(MailSendDto.class.getName());
			task.setParameter(JSON.toJSONString(mailDto));
			taskSubmitService.submitTask(task);
			logger.info("submitSmsTask : {}", JSON.toJSONString(task));
		} catch (Exception e) {
			String msg = TASK_SUBMIT_ERROR_MSG + " [submitMailTask] address=" + String.join(",", batchMailDto.getMailAddresses()) + " operator = " + batchMailDto.getOperator() + " templateId = "
					+ batchMailDto.getTemplateId();
			logger.error(msg, e);
			throw new AugeServiceException("submitSmsTask error: " + e.getMessage());
		}
	}

	@Override
	public void submitAddUserTagTasks(Long instanceId,String operator) {
		try {
			PartnerInstanceDto instance =partnerInstanceBO.getPartnerInstanceById(instanceId);
			
			// 异构系统交互提交后台任务
			List<GeneralTaskDto> taskDtos = Lists.newArrayList();

			// TODO uic打标 begin
			GeneralTaskDto uicGeneralTaskDto = buildAddUicTagTask(instance, operator);
			uicGeneralTaskDto.setBusinessStepNo(1l);
			taskDtos.add(uicGeneralTaskDto);
			// TODO uic打标 end

			// TODO 旺旺打标 begin
			GeneralTaskDto wangwangGeneralTaskDto = buildAddWangwangTagTask(instance, operator);
			wangwangGeneralTaskDto.setBusinessStepNo(2l);
			taskDtos.add(wangwangGeneralTaskDto);
			// TODO 旺旺打标 end

			// TODO 提交任务
			taskSubmitService.submitTasks(taskDtos);
			logger.info("submitAddUserTagTasks : {}", JSON.toJSONString(taskDtos));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[submitAddUserTagTasks] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitAddUserTagTasks error: " + e.getMessage());
		}
		
	}
	
	@Override
	public void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType, String operatorId,Long instanceId) {
		try {
			UserTagDto userTagDto = new UserTagDto();

			userTagDto.setTaobaoUserId(taobaoUserId);
			userTagDto.setPartnerType(partnerType);

			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

			// uic去标
			GeneralTaskDto task = new GeneralTaskDto();
			task.setBusinessNo(String.valueOf(taobaoUserId));
			task.setBeanName("uicTagService");
			task.setMethodName("removeUserTag");
			task.setBusinessStepNo(1l);
			task.setBusinessType(TaskBusinessTypeEnum.REMOVE_USER_TAG.getCode());
			task.setBusinessStepDesc("去uic标");
			task.setOperator(operatorId);
			task.setParameterType(UserTagDto.class.getName());
			task.setParameter(JSON.toJSONString(userTagDto));
			taskLists.add(task);

			// 旺旺去标
			GeneralTaskDto wangwangTaskVo = new GeneralTaskDto();
			wangwangTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
			wangwangTaskVo.setBeanName("wangWangTagService");
			wangwangTaskVo.setMethodName("removeWangWangTagByNick");
			wangwangTaskVo.setBusinessStepNo(2l);
			wangwangTaskVo.setBusinessType(TaskBusinessTypeEnum.REMOVE_USER_TAG.getCode());
			wangwangTaskVo.setBusinessStepDesc("去旺旺标");
			wangwangTaskVo.setOperator(operatorId);
			wangwangTaskVo.setParameterType(String.class.getName());
			wangwangTaskVo.setParameter(taobaoNick);
			taskLists.add(wangwangTaskVo);

			Optional<PartnerTpg> tpgResult = partnerTpgBO.queryByParnterInstanceId(instanceId);
			if(tpgResult.isPresent()){
				GeneralTaskDto degradeTpgTask = new GeneralTaskDto();
				degradeTpgTask.setBusinessNo(String.valueOf(taobaoUserId));
				degradeTpgTask.setBeanName("partnerTpgService");
				degradeTpgTask.setMethodName("degradeTpg");
				degradeTpgTask.setBusinessStepNo(3l);
				degradeTpgTask.setBusinessType(TaskBusinessTypeEnum.REMOVE_USER_TAG.getCode());
				degradeTpgTask.setBusinessStepDesc("降级供赢通会员");
				degradeTpgTask.setOperator(OperatorDto.defaultOperator().getOperator());
				degradeTpgTask.setParameter(instanceId.toString());
				degradeTpgTask.setParameterType(Long.class.getName());
				taskLists.add(degradeTpgTask);
			}
			
			// 提交任务
			taskSubmitService.submitTasks(taskLists);
			logger.info("submitRemoveUserTagTasks : {}", JSON.toJSONString(taskLists));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitRemoveUserTagTasks] taobaoUserId=" + taobaoUserId + " operatorId = " + operatorId,
					e);
			throw new AugeServiceException("submitRemoveUserTagTasks error: " + e.getMessage());
		}
	}

	@Override
	public void submitQuitTask(Long instanceId, String accountNo, String frozenMoney, OperatorDto operatorDto) {
		try {
			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

			// 解除保证金
			GeneralTaskDto dealStanderBailTaskVo = buildDealStanderBailTaskVo(instanceId, accountNo, frozenMoney,
					operatorDto);
			taskLists.add(dealStanderBailTaskVo);

			// 正式撤点
			GeneralTaskDto quitTaskVo = new GeneralTaskDto();
			quitTaskVo.setBusinessNo(String.valueOf(instanceId));
			quitTaskVo.setBeanName("partnerInstanceService");
			quitTaskVo.setMethodName("quitPartnerInstance");
			quitTaskVo.setBusinessStepNo(2l);
			quitTaskVo.setBusinessType(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode());
			quitTaskVo.setBusinessStepDesc("quitPartnerInstance");
			quitTaskVo.setOperator(operatorDto.getOperator());

			PartnerInstanceQuitDto partnerInstanceQuitDto = new PartnerInstanceQuitDto();
			partnerInstanceQuitDto.copyOperatorDto(operatorDto);
			partnerInstanceQuitDto.setInstanceId(instanceId);
			quitTaskVo.setParameterType(PartnerInstanceQuitDto.class.getName());
			quitTaskVo.setParameter(JSON.toJSONString(partnerInstanceQuitDto));
			taskLists.add(quitTaskVo);

			taskSubmitService.submitTasks(taskLists);
			logger.info("submitQuitTask : {}", JSON.toJSONString(taskLists));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitQuitTask] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitQuitTask error: " + e.getMessage());
		}
	}

	private GeneralTaskDto buildDealStanderBailTaskVo(Long instanceId, String accountNo, String frozenMoney,
			OperatorDto operatorDto) {
		GeneralTaskDto dealStanderBailTaskVo = new GeneralTaskDto();
		dealStanderBailTaskVo.setBusinessNo(String.valueOf(instanceId));
		dealStanderBailTaskVo.setBeanName("alipayStandardBailAdapter");
		dealStanderBailTaskVo.setMethodName("dealStandardBail");
		dealStanderBailTaskVo.setBusinessStepNo(1l);
		dealStanderBailTaskVo.setBusinessType(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode());
		dealStanderBailTaskVo.setBusinessStepDesc("dealStandardBail");
		dealStanderBailTaskVo.setOperator(operatorDto.getOperator());

		AlipayStandardBailDto alipayStandardBailDto = new AlipayStandardBailDto();
		alipayStandardBailDto.setAmount(frozenMoney);
		alipayStandardBailDto.setOpType(AlipayStandardBailDto.ALIPAY_OP_TYPE_UNFREEZE);
		alipayStandardBailDto.setOutOrderNo(OUT_ORDER_NO_PRE + instanceId);
		alipayStandardBailDto.setTransferMemo("村淘保证金解冻");
		alipayStandardBailDto.setTypeCode(standerBailTypeCode);
		alipayStandardBailDto.setUserAccount(accountNo);
		dealStanderBailTaskVo.setParameterType(AlipayStandardBailDto.class.getName());
		dealStanderBailTaskVo.setParameter(JSON.toJSONString(alipayStandardBailDto));
		return dealStanderBailTaskVo;
	}
	
	@Override
	public void submitThawMoneyTask(Long instanceId, String accountNo, String frozenMoney, OperatorDto operatorDto) {
		try {
			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

			GeneralTaskDto dealStanderBailTaskVo = buildDealStanderBailTaskVo(instanceId, accountNo, frozenMoney,
					operatorDto);
			taskLists.add(dealStanderBailTaskVo);

			// 调用解冻成功
			GeneralTaskDto thawMoneyTaskVo = new GeneralTaskDto();
			thawMoneyTaskVo.setBusinessNo(String.valueOf(instanceId));
			thawMoneyTaskVo.setBeanName("partnerInstanceService");
			thawMoneyTaskVo.setMethodName("thawMoneySuccess");
			thawMoneyTaskVo.setBusinessStepNo(2l);
			thawMoneyTaskVo.setBusinessType(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode());
			thawMoneyTaskVo.setBusinessStepDesc("thawMoneySuccess");
			thawMoneyTaskVo.setOperator(operatorDto.getOperator());

			PartnerInstanceThrawSuccessDto thrawSuccessDto = new PartnerInstanceThrawSuccessDto();
			thrawSuccessDto.copyOperatorDto(operatorDto);
			thrawSuccessDto.setInstanceId(instanceId);
			thawMoneyTaskVo.setParameterType(PartnerInstanceThrawSuccessDto.class.getName());
			thawMoneyTaskVo.setParameter(JSON.toJSONString(thrawSuccessDto));
			taskLists.add(thawMoneyTaskVo);

			taskSubmitService.submitTasks(taskLists);
			logger.info("submitThawMoneyTask : {}", JSON.toJSONString(taskLists));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitThawMoneyTask] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitThawMoneyTask error: " + e.getMessage());
		}
	}

	@Override
	public void submitQuitApprovedTask(Long instanceId, Long stationId, Long taobaoUserId, String isQuitStation) {
		try {
			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();
			// 取消物流站点
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(instanceId));
			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT_APPROVED.getCode());
			cainiaoTaskVo.setBusinessStepDesc("关闭物流站点");
			cainiaoTaskVo.setOperator(OperatorDto.defaultOperator().getOperator());

			// 不撤点
			if ("n".equals(isQuitStation)) {
				if (partnerInstanceBO.isOtherPartnerQuit(instanceId)) {
					cainiaoTaskVo.setMethodName("unBindAdmin");
					cainiaoTaskVo.setParameterType(Long.class.getName());
					cainiaoTaskVo.setParameter(String.valueOf(stationId));
					taskLists.add(cainiaoTaskVo);
				}
			} else {
				cainiaoTaskVo.setMethodName("deleteCainiaoStation");
				SyncDeleteCainiaoStationDto syncDeleteCainiaoStationDto = new SyncDeleteCainiaoStationDto();
				syncDeleteCainiaoStationDto.copyOperatorDto(OperatorDto.defaultOperator());
				syncDeleteCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));
				cainiaoTaskVo.setParameterType(SyncDeleteCainiaoStationDto.class.getName());
				cainiaoTaskVo.setParameter(JSON.toJSONString(syncDeleteCainiaoStationDto));
				taskLists.add(cainiaoTaskVo);
			}

			// 取消支付宝标示
			GeneralTaskDto dealStationTagTaskVo = new GeneralTaskDto();
			dealStationTagTaskVo.setBusinessNo(String.valueOf(instanceId));
			dealStationTagTaskVo.setBeanName("alipayTagService");
			dealStationTagTaskVo.setMethodName("dealTag");
			dealStationTagTaskVo.setBusinessStepNo(2l);
			dealStationTagTaskVo.setBusinessType(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT_APPROVED.getCode());
			dealStationTagTaskVo.setBusinessStepDesc("取消支付宝标示");
			dealStationTagTaskVo.setOperator(OperatorDto.defaultOperator().getOperator());

			AlipayTagDto alipayTagDto = new AlipayTagDto();
			alipayTagDto.setTagName(AlipayTagDto.ALIPAY_CUNTAO_TAG_NAME);
			alipayTagDto.setBelongTo(AlipayTagDto.ALIPAY_CUNTAO_BELONG_TO);
			alipayTagDto.setTagValue(AlipayTagDto.ALIPAY_TAG_VALUE_F);

			AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
					AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
			
			String accountNo ;
			//村拍档没有保证金，所以accountMoney=null
			if (null == accountMoney || StringUtils.isEmpty(accountMoney.getAccountNo())) {
				OperatorDto operatorDto = new OperatorDto();
				operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
				operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
				operatorDto.setOperatorOrgId(0L);
				PaymentAccountDto accountDto = paymentAccountQueryAdapter.queryPaymentAccountByTaobaoUserId(taobaoUserId, operatorDto);
				accountNo = accountDto.getAccountNo();
			}else{
				accountNo = accountMoney.getAccountNo();
			}

			/**
			 * 注意:accountNo有两种形式:alipayId(16位)还有老的accountNo(20位且2088开头 0156结尾)
			 */
			if(StringUtils.length(accountNo) == 20) {
				accountNo = accountNo.substring(0, accountNo.length() - 4);
			}
			alipayTagDto.setUserId(accountNo);
			dealStationTagTaskVo.setParameterType(AlipayTagDto.class.getName());
			dealStationTagTaskVo.setParameter(JSON.toJSONString(alipayTagDto));
			taskLists.add(dealStationTagTaskVo);

			
			// 提交任务
			taskSubmitService.submitTasks(taskLists);
			logger.info("submitQuitApprovedTask : {}", JSON.toJSONString(taskLists));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitQuitApprovedTask] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitQuitApprovedTask error: " + e.getMessage());
		}
	}

	@Override
	public void submitShutdownApprovedTask(Long stationId) {
		try {
			// 关闭物流站点
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(stationId));
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_SHUTDOWN_APPROVED.getCode());
			cainiaoTaskVo.setBusinessStepDesc("关闭物流站点");
			cainiaoTaskVo.setOperator(OperatorDto.defaultOperator().getOperator());

			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setMethodName("deleteNotUsedCainiaoStation");
			cainiaoTaskVo.setParameterType(Long.class.getName());
			cainiaoTaskVo.setParameter(String.valueOf(stationId));

			taskSubmitService.submitTask(cainiaoTaskVo);
			logger.info("submitShutdownApprovedTask : {}", JSON.toJSONString(cainiaoTaskVo));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitShutdownApprovedTask] stationId = {}, {}", stationId, e);
			throw new AugeServiceException("submitShutdownApprovedTask error: " + e.getMessage());
		}
	}

	@Override
	public void submitCloseToServiceTask(Long instanceId, Long taobaoUserId, PartnerInstanceTypeEnum partnerType, String operator) {
		try {
			// 异构系统交互提交后台任务
			List<GeneralTaskDto> taskDtos = Lists.newArrayList();

			GeneralTaskDto cainiaoTaskDto = buildReServiceTask(instanceId, operator);
			taskDtos.add(cainiaoTaskDto);
			
			String businessNo = String.valueOf(instanceId);
			UserTagDto userTagDto = new UserTagDto();
			userTagDto.setPartnerType(partnerType);
			userTagDto.setTaobaoUserId(taobaoUserId);

			// uic打标 begin
			GeneralTaskDto uicGeneralTaskDto = new GeneralTaskDto();
			uicGeneralTaskDto.setBusinessNo(businessNo);
			uicGeneralTaskDto.setBeanName("uicTagService");
			uicGeneralTaskDto.setMethodName("addUserTag");
			uicGeneralTaskDto.setBusinessStepNo(1l);
			uicGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.CLOSE_TO_SERVICE.getCode());
			uicGeneralTaskDto.setBusinessStepDesc("addUserTag");
			uicGeneralTaskDto.setOperator(operator);
			uicGeneralTaskDto.setParameterType(UserTagDto.class.getName());
			uicGeneralTaskDto.setParameter(JSON.toJSONString(userTagDto));
			uicGeneralTaskDto.setPriority(TaskPriority.HIGH);
			taskDtos.add(uicGeneralTaskDto);
			// uic打标 end

			// 旺旺打标 begin
			GeneralTaskDto wangwangGeneralTaskDto = new GeneralTaskDto();
			wangwangGeneralTaskDto.setBusinessNo(businessNo);
			wangwangGeneralTaskDto.setBeanName("wangWangTagService");
			wangwangGeneralTaskDto.setMethodName("addWangWangTagByNick");
			wangwangGeneralTaskDto.setBusinessStepNo(2l);
			wangwangGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.CLOSE_TO_SERVICE.getCode());
			wangwangGeneralTaskDto.setBusinessStepDesc("addWangWangTagByNick");
			wangwangGeneralTaskDto.setOperator(operator);
			wangwangGeneralTaskDto.setParameterType(String.class.getName());
			String taobaoNick = uicReadAdapter.getTaobaoNickByTaobaoUserId(taobaoUserId);
			wangwangGeneralTaskDto.setParameter(taobaoNick);
			GeneralTaskRetryConfigDto retry = new GeneralTaskRetryConfigDto();
			retry.setIntervalTime(6 * 3600);// 失败5小时重试
			retry.setMaxRetryTimes(120);// 失败一天执行4次,一个月120次，超过业务人工介入
			retry.setIntervalIncrement(false);
			wangwangGeneralTaskDto.setRetryTaskConfig(retry);
			wangwangGeneralTaskDto.setPriority(TaskPriority.HIGH);
			taskDtos.add(wangwangGeneralTaskDto);
			// 旺旺打标 end

			taskSubmitService.submitTasks(taskDtos);
			logger.info("closeToServiceTasks : {}", JSON.toJSONString(taskDtos));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[closeToServiceTasks] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("closeToServiceTasks error: " + e.getMessage());
		}
	}

	@Override
	public void submitLevelApproveProcessTask(ProcessBusinessEnum business, PartnerInstanceLevelProcessDto levelProcessDto) {
		try {
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(levelProcessDto.getBusinessId()));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(business.getCode());
			startProcessTask.setBusinessStepDesc(business.getDesc());
			startProcessTask.setBeanName("processService");
			startProcessTask.setMethodName("startLevelApproveProcess");
			startProcessTask.setOperator(OperatorDto.DEFAULT_OPERATOR);

			startProcessTask.setParameterType(PartnerInstanceLevelProcessDto.class.getName());
			startProcessTask.setParameter(JSON.toJSONString(levelProcessDto));

			GeneralTaskRetryConfigDto config = new GeneralTaskRetryConfigDto();
			// 20s执行一次
			config.setIntervalTime(20000);
			// 提交任务
			taskSubmitService.submitTask(startProcessTask, config);
			logger.info("submitLevelApproveProcessTask : {}", JSON.toJSONString(startProcessTask));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitLevelApproveProcessTask] param = " + JSON.toJSONString(levelProcessDto), e);
			throw new AugeServiceException("submitLevelApproveProcessTask error: " + e.getMessage());
		}
	}

	/**
	 * 启动激励方案审批流程
	 * @param processTask
	 */
	@Override
	public void submitIncentiveProgramAuditTask(ApproveProcessTask processTask) {
		try{

			GeneralTaskDto startProcessTask = buildGeneralTaskDto(processTask, "processService", "startIncentiveProgramAuditProcess");
			GeneralTaskRetryConfigDto config = new GeneralTaskRetryConfigDto();
			config.setIntervalTime(20000);
			taskSubmitService.submitTask(startProcessTask, config);
			logger.info("submitIncentiveProgramAuditTask : {}", JSON.toJSONString(startProcessTask));
		}catch (Exception e) {
			logger.error("submitIncentiveProgramAuditTask error: " + JSON.toJSONString(processTask), e);
		}
	}

	private static GeneralTaskDto buildGeneralTaskDto(ApproveProcessTask processTask, String beanName, String methodName) {
		StartProcessDto startProcessDto = new StartProcessDto();
		startProcessDto.setBusiness(processTask.getBusiness());
		startProcessDto.setBusinessId(processTask.getBusinessId());
		startProcessDto.setBusinessName(processTask.getBusinessName());
		startProcessDto.setBusinessOrgId(processTask.getBusinessOrgId());
		startProcessDto.copyOperatorDto(processTask);
		startProcessDto.setJsonParams(FeatureUtil.toStringUnencode(processTask.getParams()));
		// 启动流程
		GeneralTaskDto startProcessTask = new GeneralTaskDto();
		startProcessTask.setBusinessNo(String.valueOf(processTask.getBusinessId()));
		startProcessTask.setBusinessStepNo(1L);
		startProcessTask.setBusinessType(processTask.getBusiness().getCode());
		startProcessTask.setBusinessStepDesc(processTask.getBusiness().getDesc());
		startProcessTask.setBeanName(beanName);
		startProcessTask.setMethodName(methodName);
		startProcessTask.setOperator(processTask.getOperator());
		startProcessTask.setParameterType(StartProcessDto.class.getName());
		startProcessTask.setParameter(JSON.toJSONString(startProcessDto));
		return startProcessTask;
	}

	/**
	 * 停业同步菜鸟
	 * 
	 * @param instanceId
	 * @param operatorId
	 * @throws AugeServiceException
	 */
	public void submitClosedCainiaoStation(Long instanceId, String operatorId) throws AugeServiceException {
		try {
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(instanceId));
			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setMethodName("closeCainiaoStation");
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.CLOSED_TO_CAINIAO.getCode());
			cainiaoTaskVo.setBusinessStepDesc("停业同步菜鸟");
			cainiaoTaskVo.setOperator(operatorId);

			SyncModifyCainiaoStationDto syncModifyCainiaoStationDto = new SyncModifyCainiaoStationDto();
			syncModifyCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));
			syncModifyCainiaoStationDto.setOperator(operatorId);
			syncModifyCainiaoStationDto.setOperatorType(OperatorTypeEnum.BUC);
			cainiaoTaskVo.setParameterType(SyncModifyCainiaoStationDto.class.getName());
			cainiaoTaskVo.setParameter(JSON.toJSONString(syncModifyCainiaoStationDto));

			// 提交任务
			taskSubmitService.submitTask(cainiaoTaskVo);
			logger.info("submitClosedCainiaoStation : {}", JSON.toJSONString(cainiaoTaskVo));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + "[submitClosedCainiaoStation] instanceId = {}, {}", instanceId, e);
			throw new AugeServiceException("submitClosedCainiaoStation error: " + e.getMessage());
		}
		
	}
}
