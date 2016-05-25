package com.taobao.cun.auge.station.notify.listener;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.QuitStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.convert.StationStatusChangedEventConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.event.client.EventDispatcher;

@Component
public class ProcessApproveResultProcessor {

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	StationBO stationBO;

	@Autowired
	QuitStationApplyBO quitStationApplyBO;

	public void monitorCloseApprove	(Long stationApplyId,ProcessApproveResultEnum approveResult) throws Exception {
		Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);
		String operator = "sys";
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING, PartnerInstanceStateEnum.CLOSED,
					operator);
			// 更新服务结束时间
			PartnerInstanceDto instance = new PartnerInstanceDto();
			instance.setServiceEndTime(new Date());
			instance.setId(instanceId);
			instance.setOperator(operator);
			partnerInstanceBO.updatePartnerStationRel(instance);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.CLOSED, operator);

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateEnum.CLOSING,
							PartnerInstanceStateEnum.CLOSED, partnerInstanceBO.getPartnerInstanceById(instanceId)));

			// 去标，通过事件实现
			// 短信推送
			// 通知admin，合伙人退出。让他们监听村点状态变更事件
			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT,
					StationStatusChangedEventConverter.convert(StationStatusEnum.CLOSING, StationStatusEnum.CLOSED,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));
		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.CLOSING,
					PartnerInstanceStateEnum.SERVICING, operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.CLOSING, StationStatusEnum.SERVICING, operator);

			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateEnum.CLOSING,
							PartnerInstanceStateEnum.SERVICING, partnerInstanceBO.getPartnerInstanceById(instanceId)));

			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT,
					StationStatusChangedEventConverter.convert(StationStatusEnum.CLOSING, StationStatusEnum.SERVICING,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));
		}
	}

	public void monitorQuitApprove(Long stationApplyId,ProcessApproveResultEnum approveResult) throws Exception {
		String operator = "sys";
		
		Long instanceId = partnerInstanceBO.findPartnerInstanceId(stationApplyId);
		Long stationId = partnerInstanceBO.findStationIdByInstanceId(instanceId);

		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			// 合伙人实例已退出
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.QUIT,
					operator);

			// 村点已撤点
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);

			// 取消物流站点，取消支付宝标示，
			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT,
					StationStatusChangedEventConverter.convert(StationStatusEnum.QUITING, StationStatusEnum.QUIT,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));

		} else {
			// 合伙人实例已停业
			partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.QUITING, PartnerInstanceStateEnum.CLOSED,
					operator);

			// 村点已停业
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);

			// 删除退出申请单
			quitStationApplyBO.deleteQuitStationApply(instanceId, operator);
			// 记录村点状态变化
			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
					PartnerInstanceEventConverter.convert(PartnerInstanceStateEnum.QUITING,
							PartnerInstanceStateEnum.CLOSED, partnerInstanceBO.getPartnerInstanceById(instanceId)));

			EventDispatcher.getInstance().dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT,
					StationStatusChangedEventConverter.convert(StationStatusEnum.QUITING, StationStatusEnum.CLOSED,
							partnerInstanceBO.getPartnerInstanceById(instanceId), operator));
		}
		// tair清空缓存
	}
}
