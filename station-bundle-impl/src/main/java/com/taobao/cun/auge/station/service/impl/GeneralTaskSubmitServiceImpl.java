package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.msg.dto.SmsSendDto;
import com.taobao.cun.auge.station.adapter.UicReadAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.SyncModifyCainiaoStationDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.DingtalkTemplateEnum;
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
import com.taobao.cun.chronus.service.TaskExecuteService;

@Service("generalTaskSubmitService")
public class GeneralTaskSubmitServiceImpl implements GeneralTaskSubmitService {

	private static final Logger logger = LoggerFactory.getLogger(GeneralTaskSubmitService.class);

	@Autowired
	TaskExecuteService taskExecuteService;

	@Autowired
	UicReadAdapter uicReadAdapter;
	
	@Autowired
	PartnerBO partnerBO;

	public void submitSettlingSysProcessTasks(PartnerInstanceDto instance, String operator) {
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
		cainiaoTaskDto.setParameter(syncAddCainiaoStationDto);
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
		uicGeneralTaskDto.setParameter(userTagDto);
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
		stationConfirmGeneralTaskDto.setParameter(settleSuccessDto);
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
		taskExecuteService.submitTasks(taskDtos);
	}

	@Override
	public void submitUpdateCainiaoStation(Long instanceId, String operatorId) throws AugeServiceException {
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

		cainiaoTaskVo.setParameter(syncModifyCainiaoStationDto);

		// 提交任务
		taskExecuteService.submitTask(cainiaoTaskVo);

	}

	@Override
	public void submitDegradePartner(PartnerInstanceDto instanceDto, String operatorId) throws AugeServiceException {
		// 异构系统交互提交后台任务
		List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

		// UIC打标
		UserTagDto userTagDto = new UserTagDto();
		userTagDto.setTaobaoUserId(instanceDto.getTaobaoUserId());
		userTagDto.setPartnerType(PartnerInstanceTypeEnum.TPA);

		GeneralTaskDto task = new GeneralTaskDto();
		task.setBusinessNo(String.valueOf(instanceDto.getId()));
		task.setBeanName("uicTagService");
		task.setMethodName("addUserTag");
		task.setBusinessStepNo(1l);
		task.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
		task.setBusinessStepDesc("addUserTag");
		task.setOperator(operatorId);
		task.setParameter(userTagDto);
		taskLists.add(task);

		// 旺旺打标 begin
		if (PartnerInstanceStateEnum.CLOSED.getCode().equals(instanceDto.getState().getCode())) {
			String taobaoNick = uicReadAdapter.getTaobaoNickByTaobaoUserId(instanceDto.getTaobaoUserId());
			GeneralTaskDto wwTask = new GeneralTaskDto();

			wwTask.setBusinessNo(String.valueOf(instanceDto.getId()));
			wwTask.setBeanName("wangWangTagService");
			wwTask.setMethodName("addWangWangTagByNick");
			wwTask.setBusinessStepNo(2l);
			wwTask.setBusinessType(TaskBusinessTypeEnum.TP_DEGRADE.getCode());
			wwTask.setBusinessStepDesc("addWangWangTag");
			wwTask.setOperator(operatorId);
			wwTask.setParameter(taobaoNick);
			taskLists.add(wwTask);
		}
		taskExecuteService.submitTasks(taskLists);
	}

	/**
	 * 启动停业、退出流程审批流程
	 * 
	 * @param business
	 *            业务类型
	 * @param stationApplyId
	 *            业务主键
	 * @param applierId
	 *            申请人
	 * @param applierOrgId
	 *            申请人orgid
	 * @param remarks
	 *            备注
	 */
	public void submitApproveProcessTask(ProcessBusinessEnum business, Long stationApplyId,
			PartnerInstanceStateChangeEvent stateChangeEvent) {
		try {

			StartProcessDto startProcessDto = new StartProcessDto();

			startProcessDto.setRemarks(stateChangeEvent.getRemark());
			startProcessDto.setBusinessId(stationApplyId);
			startProcessDto.setBusinessCode(business.getCode());
			startProcessDto.copyOperatorDto(stateChangeEvent);
			// 启动流程
			GeneralTaskDto startProcessTask = new GeneralTaskDto();
			startProcessTask.setBusinessNo(String.valueOf(stationApplyId));
			startProcessTask.setBusinessStepNo(1l);
			startProcessTask.setBusinessType(business.getCode());
			startProcessTask.setBusinessStepDesc(business.getDesc());
			startProcessTask.setBeanName("processService");
			startProcessTask.setMethodName("startApproveProcess");
			startProcessTask.setOperator(stateChangeEvent.getOperator());
			startProcessTask.setParameter(startProcessDto);

			// 提交任务
			taskExecuteService.submitTask(startProcessTask);
		} catch (Exception e) {
			logger.error("创建启动流程任务失败。stationApplyId = " + stationApplyId + " business=" + business.getCode()
					+ " applierId=" + stateChangeEvent.getOperator() + " operatorType="
					+ stateChangeEvent.getOperatorType().getCode(), e);
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
			task.setBusinessType(TaskBusinessTypeEnum.PARTNER_SMS.getCode());
			task.setBusinessStepDesc("发短信");
			task.setOperator(operatorId);
			task.setParameter(smsDto);
			taskExecuteService.submitTask(task);
		} catch (Exception e) {
			String msg = "Failed to send sms. mobile=" + mobile + " taobaouserid = " + taobaoUserId + " content = "
					+ content;
			logger.error(msg);
			logger.error(msg, e);
		}
	}
	
	public void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType,
			String operatorId) {
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
			task.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			task.setBusinessStepDesc("去uic标");
			task.setOperator(operatorId);
			task.setParameter(userTagDto);
			taskLists.add(task);

			// 旺旺去标
			GeneralTaskDto wangwangTaskVo = new GeneralTaskDto();
			wangwangTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
			wangwangTaskVo.setBeanName("wangWangTagService");
			wangwangTaskVo.setMethodName("removeWangWangTagByNick");
			wangwangTaskVo.setBusinessStepNo(2l);
			wangwangTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			wangwangTaskVo.setBusinessStepDesc("去旺旺标");
			wangwangTaskVo.setOperator(operatorId);
			wangwangTaskVo.setParameter(taobaoNick);
			taskLists.add(wangwangTaskVo);

			// 提交任务
			taskExecuteService.submitTasks(taskLists);
		} catch (Exception e) {
			logger.error("Failed to submit remove user tag task. taobaoUserId=" + taobaoUserId + " operatorId = "
					+ operatorId, e);
		}
	}

	public void submitRemoveLogisticsTask(Long instanceId, String operatorId) {
		try {
			// 取消物流站点
			// FIXME FHH 待完成
			GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
			cainiaoTaskVo.setBusinessNo(String.valueOf(instanceId));
			cainiaoTaskVo.setBeanName("caiNiaoService");
			cainiaoTaskVo.setMethodName("deleteCainiaoStation");
			cainiaoTaskVo.setBusinessStepNo(1l);
			cainiaoTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			cainiaoTaskVo.setBusinessStepDesc("关闭物流站点");
			cainiaoTaskVo.setOperator(operatorId);

			SyncDeleteCainiaoStationDto syncDeleteCainiaoStationDto = new SyncDeleteCainiaoStationDto();
			syncDeleteCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));

			cainiaoTaskVo.setParameter(syncDeleteCainiaoStationDto);

			// 提交任务
			taskExecuteService.submitTask(cainiaoTaskVo);
		} catch (Exception e) {
			logger.error("Failed to submit remove logistics station task. instanceId=" + instanceId + " operatorId = "
					+ operatorId, e);
		}
	}

	public void submitRemoveAlipayTagTask(Long taobaoUserId, String operatorId) {
		try {
			// 取消支付宝标示
			GeneralTaskDto dealStationTagTaskVo = new GeneralTaskDto();
			dealStationTagTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
			dealStationTagTaskVo.setBeanName("alipayTagService");
			dealStationTagTaskVo.setMethodName("dealTag");
			dealStationTagTaskVo.setBusinessStepNo(1l);
			dealStationTagTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
			dealStationTagTaskVo.setBusinessStepDesc("dealTag");
			dealStationTagTaskVo.setOperator(operatorId);

			AlipayTagDto alipayTagDto = new AlipayTagDto();
			alipayTagDto.setTagName(AlipayTagDto.ALIPAY_CUNTAO_TAG_NAME);
			alipayTagDto.setBelongTo(AlipayTagDto.ALIPAY_CUNTAO_BELONG_TO);
			alipayTagDto.setTagValue(AlipayTagDto.ALIPAY_TAG_VALUE_F);

			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);

			String accountNo = partner.getAlipayAccount();
			if (StringUtils.isNotEmpty(accountNo)) {
				alipayTagDto.setUserId(accountNo.substring(0, accountNo.length() - 4));
			}
			dealStationTagTaskVo.setParameter(alipayTagDto);

			// 提交任务
			taskExecuteService.submitTask(dealStationTagTaskVo);
		} catch (AugeServiceException e) {
			logger.error("提交取消支付宝标示任务失败。taobaoUserId=" + taobaoUserId + " operatorId = " + operatorId, e);
		}
	}

}
