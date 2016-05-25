package com.taobao.cun.auge.event.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.dto.AlipayTagDto;
import com.taobao.cun.auge.station.dto.SyncDeleteCainiaoStationDto;
import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.enums.BusinessTypeEnum;
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
		Map<String, Object> map = event.getContent();

		StationStatusEnum newStatus = (StationStatusEnum) map.get("newStatus");
		StationStatusEnum oldStatus = (StationStatusEnum) map.get("oldStatus");

		String operatorId = (String) map.get("operatorId");
		Long taobaoUserId = (Long) map.get("taobaoUserId");
		String stationId = (String) map.get("stationId");
		String taobaoNick = (String) map.get("taobaoNick");
		PartnerInstanceTypeEnum partnerType = (PartnerInstanceTypeEnum) map.get("partnerType");

		// 由停业中，变更为已停业，去标,发短信
		if (StationStatusEnum.CLOSED.equals(newStatus) && StationStatusEnum.CLOSING.equals(oldStatus)) {
			submitRemoveUserTagTasks(taobaoUserId, taobaoNick, partnerType, operatorId);
		} else if (StationStatusEnum.QUIT.equals(newStatus) && StationStatusEnum.QUITING.equals(oldStatus)) {
			submitRemoveAlipayTagTask(taobaoUserId, operatorId);
			submitRemoveLogisticsTask(stationId, operatorId);
		}
	}

	private void submitRemoveUserTagTasks(Long taobaoUserId, String taobaoNick, PartnerInstanceTypeEnum partnerType,
			String operatorId) {
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
		task.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
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
		wangwangTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		wangwangTaskVo.setBusinessStepDesc("去旺旺标");
		wangwangTaskVo.setOperator(operatorId);
		wangwangTaskVo.setParameter(taobaoNick);
		taskLists.add(wangwangTaskVo);

		// 提交任务
		taskExecuteService.submitTasks(taskLists);
	}

	private void submitRemoveLogisticsTask(String instanceId, String operatorId) {
		// 取消物流站点
		GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
		cainiaoTaskVo.setBusinessNo(instanceId);
		cainiaoTaskVo.setBeanName("caiNiaoService");
		cainiaoTaskVo.setMethodName("deleteCainiaoStation");
		cainiaoTaskVo.setBusinessStepNo(1l);
		cainiaoTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		cainiaoTaskVo.setBusinessStepDesc("关闭物流站点");
		cainiaoTaskVo.setOperator(operatorId);
		
		SyncDeleteCainiaoStationDto  syncDeleteCainiaoStationDto = new SyncDeleteCainiaoStationDto();
		syncDeleteCainiaoStationDto.setPartnerInstanceId(Long.valueOf(instanceId));
		
		cainiaoTaskVo.setParameter(syncDeleteCainiaoStationDto);

		// 提交任务
		taskExecuteService.submitTask(cainiaoTaskVo);
	}

	private void submitRemoveAlipayTagTask(Long taobaoUserId, String operator) {
		try {
			// 取消支付宝标示
			GeneralTaskDto dealStationTagTaskVo = new GeneralTaskDto();
			dealStationTagTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
			dealStationTagTaskVo.setBeanName("alipayTagService");
			dealStationTagTaskVo.setMethodName("dealTag");
			dealStationTagTaskVo.setBusinessStepNo(1l);
			dealStationTagTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
			dealStationTagTaskVo.setBusinessStepDesc("dealTag");
			dealStationTagTaskVo.setOperator(operator);

			AlipayTagDto AlipayTagDto = new AlipayTagDto();
			AlipayTagDto.setTagName(AlipayTagDto.ALIPAY_CUNTAO_TAG_NAME);
			AlipayTagDto.setBelongTo(AlipayTagDto.ALIPAY_CUNTAO_BELONG_TO);
			AlipayTagDto.setTagValue(AlipayTagDto.ALIPAY_TAG_VALUE_F);

			Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);

			String accountNo = partner.getAlipayAccount();
			if (StringUtils.isNotEmpty(accountNo)) {
				AlipayTagDto.setUserId(accountNo.substring(0, accountNo.length() - 4));
			}
			dealStationTagTaskVo.setParameter(AlipayTagDto);

			// 提交任务
			taskExecuteService.submitTask(dealStationTagTaskVo);
		} catch (AugeServiceException e) {
			logger.error("提交取消支付宝标示任务失败。taobaoUserId=" + taobaoUserId);
		}
	}
}
