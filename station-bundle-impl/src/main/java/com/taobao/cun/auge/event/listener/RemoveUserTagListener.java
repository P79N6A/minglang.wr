package com.taobao.cun.auge.event.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.dto.UserTagDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.enums.BusinessTypeEnum;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub("station-status-changed-event")
public class RemoveUserTagListener implements EventListener {
	
	@Autowired
	TaskExecuteService taskExecuteService;

	@Override
	public void onMessage(Event event) {
		System.out.println("a:" + event.getContent().toString() + event.getEventMeta().toString());
		Map<String, Object> map = event.getContent();
		StationStatusEnum status = (StationStatusEnum) map.get("status");
		String operatorId = (String) map.get("operatorId");
		Long taobaoUserId = (Long) map.get("taobaoUserId");
		PartnerInstanceTypeEnum partnerType = (PartnerInstanceTypeEnum) map.get("partnerType");
		
		//监听已停业后，去标
		if(StationStatusEnum.CLOSED.equals(status)){
			submitRemoveUserTagTasks( taobaoUserId,  partnerType, operatorId);
		}
	}

	private void submitRemoveUserTagTasks(Long taobaoUserId, PartnerInstanceTypeEnum partnerType, String operatorId) {
		UserTagDto userTagDto = new UserTagDto();

		userTagDto.setTaobaoUserId(taobaoUserId);
		userTagDto.setPartnerType(partnerType);

		List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();

		//FIXME FHH 去标的服务未写，businessType未定义
		// uic去标
		GeneralTaskDto task = new GeneralTaskDto();
		task.setBusinessNo(String.valueOf(taobaoUserId));
		task.setBeanName("uicBo");
		task.setMethodName("removeUserTag");
		task.setBusinessStepNo(1l);
		task.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		task.setBusinessStepDesc("removeUserTag");
		task.setOperator(operatorId);
		task.setParameter(userTagDto);
		taskLists.add(task);

		// 旺旺去标
		GeneralTaskDto wangwangTaskVo = new GeneralTaskDto();
		wangwangTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
		wangwangTaskVo.setBeanName("aliWangWangBO");
		wangwangTaskVo.setMethodName("removeWangWangTag");
		wangwangTaskVo.setBusinessStepNo(2l);
		wangwangTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		wangwangTaskVo.setBusinessStepDesc("removeWangWangTag");
		wangwangTaskVo.setOperator(operatorId);
		wangwangTaskVo.setParameter(userTagDto);
		taskLists.add(wangwangTaskVo);

		// 提交任务
		taskExecuteService.submitTasks(taskLists);
	}

}
