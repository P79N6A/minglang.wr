package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceSettleSuccessDto;
import com.taobao.cun.auge.station.dto.SyncAddCainiaoStationDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.dto.GeneralTaskRetryConfigDto;
import com.taobao.cun.chronus.enums.TaskPriority;
import com.taobao.cun.chronus.service.TaskExecuteService;

@Service("generalTaskSubmitService")
public class GeneralTaskSubmitServiceImpl implements GeneralTaskSubmitService {

	@Autowired
	TaskExecuteService taskExecuteService;

	public void submitFreezeBondTasks(PartnerInstanceDto instance) {
		// 异构系统交互提交后台任务
		List<GeneralTaskDto> taskDtos = Lists.newArrayList();

		String businessNo = String.valueOf(instance.getId());
		String operator = String.valueOf(instance.getTaobaoUserId());
		//  菜鸟驿站同步 begin
		GeneralTaskDto cainiaoTaskDto = new GeneralTaskDto();
		cainiaoTaskDto.setBusinessNo(businessNo);
		cainiaoTaskDto.setBeanName("caiNiaoService");
		cainiaoTaskDto.setMethodName("addCainiaoStation");
		cainiaoTaskDto.setBusinessStepNo(1l);
		cainiaoTaskDto.setBusinessType(TaskBusinessTypeEnum.STATION_APPLY_CONFIRM.getCode());
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
		uicGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.STATION_APPLY_CONFIRM.getCode());
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
		stationConfirmGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.STATION_APPLY_CONFIRM.getCode());
		stationConfirmGeneralTaskDto.setBusinessStepNo(3l);
		stationConfirmGeneralTaskDto.setBusinessStepDesc("applySettleSuccess");
		stationConfirmGeneralTaskDto.setOperator(operator);

		PartnerInstanceSettleSuccessDto settleSuccessDto = new PartnerInstanceSettleSuccessDto();
		settleSuccessDto.setInstanceId(instance.getId());
		settleSuccessDto.setOperator(operator);
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
		wangwangGeneralTaskDto.setBusinessType(TaskBusinessTypeEnum.STATION_APPLY_CONFIRM.getCode());
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

}