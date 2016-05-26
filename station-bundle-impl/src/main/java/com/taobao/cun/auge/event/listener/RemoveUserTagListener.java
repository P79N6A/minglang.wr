package com.taobao.cun.auge.event.listener;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.event.PartnerInstanceStateChangeEvent;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("removeUserTagListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class RemoveUserTagListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(RemoveUserTagListener.class);

	@Autowired
	TaskExecuteService taskExecuteService;

	@Autowired
	PartnerBO partnerBO;

	@Override
	public void onMessage(Event event) {
		PartnerInstanceStateChangeEvent stateChangeEvent = (PartnerInstanceStateChangeEvent) event.getValue();

		PartnerInstanceStateChangeEnum stateChangeEnum = stateChangeEvent.getStateChangeEnum();
		String operatorId = stateChangeEvent.getOperator();
		Long taobaoUserId = stateChangeEvent.getTaobaoUserId();
		Long instanceId = stateChangeEvent.getPartnerInstanceId();
		String taobaoNick = stateChangeEvent.getTaobaoNick();
		PartnerInstanceTypeEnum partnerType = stateChangeEvent.getPartnerType();

		// 已停业，去标
		if (PartnerInstanceStateChangeEnum.CLOSED.equals(stateChangeEnum)) {
			submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId);
			// 已退出
		} else if (PartnerInstanceStateChangeEnum.QUIT.equals(stateChangeEnum)) {
			//FIXME FHH是否要保持事务
			submitRemoveAlipayTagTask(taobaoUserId, operatorId);
			submitRemoveLogisticsTask(instanceId, operatorId);
		}
	}

	private void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType,
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

	private void submitRemoveLogisticsTask(Long instanceId, String operatorId) {
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

	private void submitRemoveAlipayTagTask(Long taobaoUserId, String operatorId) {
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
