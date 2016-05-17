package com.taobao.cun.auge.event.listener;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
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
		
		if(StationStatusEnum.CLOSED.equals(status)){
			submitRemoveUserTagTasks( taobaoUserId,  partnerType, operatorId);
		}
	}

	private void submitRemoveUserTagTasks(Long taobaoUserId, PartnerInstanceTypeEnum partnerType,String operatorId) {
//		// TODO 异构系统交互提交后台任务
//		List<TaskVo> taskVos = new LinkedList<TaskVo>();
//
//		String businessNo = stationApplicationId.toString();// getBusinessNo();
//		// TODO uic打标 begin
//		TaskVo uicTaskVo = new TaskVo();
//		uicTaskVo.setBusinessNo(businessNo);
//		uicTaskVo.setBeanName("uicBo");
//		uicTaskVo.setMethodName("removeUserTag");
//		uicTaskVo.setBusinessStepNo(1l);
//		uicTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
//		uicTaskVo.setBusinessStepDesc("removeUserTag");
//		uicTaskVo.setOperator(appId);
//		uicTaskVo.setParameter(stationApplicationId);
//		taskVos.add(uicTaskVo);
//		// TODO uic打标 end
//
//		// TODO 旺旺打标 begin
//		TaskVo wangwangTaskVo = new TaskVo();
//		wangwangTaskVo.setBusinessNo(businessNo);
//		wangwangTaskVo.setBeanName("aliWangWangBO");
//		wangwangTaskVo.setMethodName("removeWangWangTag");
//		wangwangTaskVo.setBusinessStepNo(2l);
//		wangwangTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
//		wangwangTaskVo.setBusinessStepDesc("removeWangWangTag");
//		wangwangTaskVo.setOperator(appId);
//		wangwangTaskVo.setParameter(stationApplicationId);
//		taskVos.add(wangwangTaskVo);
//		// TODO 旺旺打标 end
//
//		// TODO 提交任务
//		taskExecuteService.submitTasks(taskVos, true);
	}

}
