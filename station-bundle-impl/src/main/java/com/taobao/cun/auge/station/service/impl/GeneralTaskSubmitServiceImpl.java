package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.msg.dto.SmsSendDto;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.dto.AlipayStandardBailDto;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.dto.DegradePartnerInstanceSuccessDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncTPDegreeCainiaoStationDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
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

	@Value("${cuntao.alipay.standerBailTypeCode}")
	String standerBailTypeCode;
	
	@Autowired
	PartnerBO partnerBO;

	public void submitSettlingSysProcessTasks(PartnerInstanceDto instance, String operator) {
		try {
			// 异构系统交互提交后台任务
			List<GeneralTaskDto> taskDtos = Lists.newArrayList();

			String businessNo = String.valueOf(instance.getId());

			// 菜鸟驿站同步 begin
			GeneralTaskDto cainiaoTaskDto = new GeneralTaskDto();
			cainiaoTaskDto.setBusinessNo(businessNo);
			cainiaoTaskDto.setBeanName("caiNiaoService");
			cainiaoTaskDto.setMethodName("addCainiaoStation");
			cainiaoTaskDto.setBusinessStepNo(1l);
			cainiaoTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
			cainiaoTaskDto.setBusinessStepDesc("addCainiaoStation");
			cainiaoTaskDto.setOperator(operator);
			cainiaoTaskDto.setPriority(TaskPriority.HIGH);

			SyncAddCainiaoStationDto syncAddCainiaoStationDto = new SyncAddCainiaoStationDto();
			syncAddCainiaoStationDto.setPartnerInstanceId(instance.getId());
			cainiaoTaskDto.setParameterType(SyncAddCainiaoStationDto.class.getName());
			cainiaoTaskDto.setParameter(JSON.toJSONString(syncAddCainiaoStationDto));
			taskDtos.add(cainiaoTaskDto);
			// TODO 菜鸟驿站同步 end

			// TODO uic打标 begin
			GeneralTaskDto uicGeneralTaskDto = new GeneralTaskDto();
			uicGeneralTaskDto.setBusinessNo(businessNo);
			uicGeneralTaskDto.setBeanName("uicTagService");
			uicGeneralTaskDto.setMethodName("addUserTag");
			uicGeneralTaskDto.setBusinessStepNo(2l);
			uicGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
			uicGeneralTaskDto.setBusinessStepDesc("addUserTag");
			uicGeneralTaskDto.setOperator(operator);
			UserTagDto userTagDto = new UserTagDto();
			userTagDto.setPartnerType(instance.getType());
			userTagDto.setTaobaoUserId(instance.getTaobaoUserId());
			uicGeneralTaskDto.setParameterType(UserTagDto.class.getName());
			uicGeneralTaskDto.setParameter(JSON.toJSONString(userTagDto));
			uicGeneralTaskDto.setPriority(TaskPriority.HIGH);
			taskDtos.add(uicGeneralTaskDto);
			// TODO uic打标 end

			// TODO 申请单更新为服务中 begin
			GeneralTaskDto stationConfirmGeneralTaskDto = new GeneralTaskDto();
			stationConfirmGeneralTaskDto.setBusinessNo(businessNo);
			stationConfirmGeneralTaskDto.setBeanName("partnerInstanceService");
			stationConfirmGeneralTaskDto.setMethodName("applySettleSuccess");
			stationConfirmGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.SETTLING_SYS_PROCESS.getCode());
			stationConfirmGeneralTaskDto.setBusinessStepNo(3l);
			stationConfirmGeneralTaskDto.setBusinessStepDesc("applySettleSuccess");
			stationConfirmGeneralTaskDto.setOperator(operator);

			PartnerInstanceSettleSuccessDto settleSuccessDto = new PartnerInstanceSettleSuccessDto();
			settleSuccessDto.setInstanceId(instance.getId());
			settleSuccessDto.setOperator(operator);
			settleSuccessDto.setOperatorType(OperatorTypeEnum.HAVANA);
			stationConfirmGeneralTaskDto.setParameterType(PartnerInstanceSettleSuccessDto.class.getName());
			stationConfirmGeneralTaskDto.setParameter(JSON.toJSONString(settleSuccessDto));
			stationConfirmGeneralTaskDto.setPriority(TaskPriority.HIGH);
			taskDtos.add(stationConfirmGeneralTaskDto);
			// TODO 申请单更新为服务中 end

			// TODO 旺旺打标 begin
			GeneralTaskDto wangwangGeneralTaskDto = new GeneralTaskDto();
			wangwangGeneralTaskDto.setBusinessNo(businessNo);
			wangwangGeneralTaskDto.setBeanName("wangWangTagService");
			wangwangGeneralTaskDto.setMethodName("addWangWangTagByNick");
			wangwangGeneralTaskDto.setBusinessStepNo(4l);
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
	 * @param business
	 *            业务类型
	 * @param businessId
	 *            业务主键
	 * @param applierId
	 *            申请人
	 * @param applierOrgId
	 *            申请人orgid
	 * @param remarks
	 *            备注
	 */
	public void submitApproveProcessTask(ProcessBusinessEnum business, Long businessId,OperatorDto operatorDto, Long applyId) {
		try {

			StartProcessDto startProcessDto = new StartProcessDto();

			startProcessDto.setApplyId(applyId);
			startProcessDto.setBusinessId(businessId);
			startProcessDto.setBusinessCode(business.getCode());
			startProcessDto.copyOperatorDto(operatorDto);
			// 启动流程
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(businessId));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(business.getCode());
			startProcessTask.setBusinessStepDesc(business.getDesc());
			startProcessTask.setBeanName("processService");
			startProcessTask.setMethodName("startApproveProcess");
			startProcessTask.setOperator(operatorDto.getOperator());
			startProcessTask.setParameterType(StartProcessDto.class.getName());
			startProcessTask.setParameter(JSON.toJSONString(startProcessDto));

			GeneralTaskRetryConfigDto config = new GeneralTaskRetryConfigDto();
			//20s执行一次
			config.setIntervalTime(20000);
			// 提交任务
			taskSubmitService.submitTask(startProcessTask,config);
			logger.info("submitApproveProcessTask : {}", JSON.toJSONString(startProcessTask));
		} catch (Exception e) {
			logger.error(TASK_SUBMIT_ERROR_MSG + " [submitApproveProcessTask] businessId = " + businessId + " business="
					+ business.getCode() + " applierId=" + operatorDto.getOperator() + " operatorType="
					+ operatorDto.getOperatorType().getCode(), e);
			throw new AugeServiceException("submitApproveProcessTask error: " + e.getMessage());
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

	public void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType, String operatorId) {
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
	public void submitQuitTask(Long instanceId, PaymentAccountDto accountDto, String frozenMoney, OperatorDto operatorDto) {
		try {
			List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

			// 解除保证金
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
			alipayStandardBailDto.setUserAccount(accountDto.getAccountNo());
			dealStanderBailTaskVo.setParameterType(AlipayStandardBailDto.class.getName());
			dealStanderBailTaskVo.setParameter(JSON.toJSONString(alipayStandardBailDto));
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

	@Override
	public void submitQuitApprovedTask(Long instanceId, Long stationId,Long taobaoUserId, String isQuitStation) {
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
			
			//不撤点
			if ("n".equals(isQuitStation)) {
				cainiaoTaskVo.setMethodName("unBindAdmin");
				cainiaoTaskVo.setParameterType(Long.class.getName());
				cainiaoTaskVo.setParameter(String.valueOf(stationId));
			} else {
				cainiaoTaskVo.setMethodName("deleteCainiaoStation");
				SyncDeleteCainiaoStationDto syncDeleteCainiaoStationDto = new SyncDeleteCainiaoStationDto();
				syncDeleteCainiaoStationDto.copyOperatorDto(OperatorDto.defaultOperator());
				syncDeleteCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));
				cainiaoTaskVo.setParameterType(SyncDeleteCainiaoStationDto.class.getName());
				cainiaoTaskVo.setParameter(JSON.toJSONString(syncDeleteCainiaoStationDto));
			}

			taskLists.add(cainiaoTaskVo);

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

			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
			String accountNo = partner.getAlipayAccount();
			if (StringUtils.isNotEmpty(accountNo)) {
				alipayTagDto.setUserId(accountNo.substring(0, accountNo.length() - 4));
			}
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
	public void submitShutdownApprovedTask(Long stationId){
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
}
