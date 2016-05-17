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
		StationStatusEnum newStatus = (StationStatusEnum) map.get("newStatus");
		StationStatusEnum oldStatus = (StationStatusEnum) map.get("oldStatus");
		String operatorId = (String) map.get("operatorId");
		Long taobaoUserId = (Long) map.get("taobaoUserId");
		PartnerInstanceTypeEnum partnerType = (PartnerInstanceTypeEnum) map.get("partnerType");
		
		//由停业中，变更为已停业，去标,发短信
		if(StationStatusEnum.CLOSED.equals(newStatus) && StationStatusEnum.CLOSING.equals(oldStatus)){
			submitRemoveUserTagTasks( taobaoUserId,  partnerType, operatorId);
			sms();
		}else if(StationStatusEnum.QUIT.equals(newStatus) && StationStatusEnum.QUITING.equals(oldStatus)){
//			submitRemoveAlipayTagTask(taskLists, businessNo);
		}
	}
	
	private void sms(){
		
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
	
//	private void submitRemoveLogisticsTask(String stationId,String operatorId){
//		List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();
//		
//		
//		String businessNo = stationApplyId.toString();// getBusinessNo();
//		//取消物流站点
//		GeneralTaskDto cainiaoTaskVo = new GeneralTaskDto();
//		cainiaoTaskVo.setBusinessNo(businessNo);
//		cainiaoTaskVo.setBeanName("caiNiaoBo");
//		cainiaoTaskVo.setMethodName("closeStationInfo");
//		cainiaoTaskVo.setBusinessStepNo(1l);
//		cainiaoTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
//		cainiaoTaskVo.setBusinessStepDesc("closeStationInfo");
//		cainiaoTaskVo.setOperator(operatorId);
//		cainiaoTaskVo.setParameter(stationId);
//		taskLists.add(cainiaoTaskVo);
//
//		// 提交任务
//		taskExecuteService.submitTask(cainiaoTaskVo);
//	}
//
//	private void submitRemoveAlipayTagTask(List<GeneralTaskDto> taskLists, String businessNo) {
//		//取消支付宝标示
//		GeneralTaskDto dealStationTagTaskVo = new GeneralTaskDto();
//		dealStationTagTaskVo.setBusinessNo(businessNo);
//		dealStationTagTaskVo.setBeanName("alipayAccountTagService");
//		dealStationTagTaskVo.setMethodName("dealTag");
//		dealStationTagTaskVo.setBusinessStepNo(1l);
//		dealStationTagTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
//		dealStationTagTaskVo.setBusinessStepDesc("dealTag");
//		dealStationTagTaskVo.setOperator(context.getAppId());
//
//		AlipayAccountTagDto alipayAccountTagDto = new AlipayAccountTagDto();
//		alipayAccountTagDto.setTagName(AlipayAccountTagDto.ALIPAY_CUNTAO_TAG_NAME);
//		alipayAccountTagDto.setBelongTo(AlipayAccountTagDto.ALIPAY_CUNTAO_BELONG_TO);
//		alipayAccountTagDto.setTagValue(AlipayAccountTagDto.ALIPAY_TAG_VALUE_F);
//		String accountNo = getAlipayAccountNo(stationApplyDO.getTaobaoUserId(), context.getAppId());
//		if (StringUtils.isNotEmpty(accountNo)) {
//			alipayAccountTagDto.setUserId(accountNo.substring(0,accountNo.length()-4));
//		}
//		dealStationTagTaskVo.setParameter(alipayAccountTagDto);
//		taskLists.add(dealStationTagTaskVo);
//	}

}
