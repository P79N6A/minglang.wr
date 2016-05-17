package com.taobao.cun.auge.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;
import com.taobao.cun.dto.flow.enums.CuntaoFlowOperateOpinionEnum;
import com.taobao.cun.dto.flow.enums.CuntaoFlowTargetTypeEnum;
import com.taobao.cun.dto.permission.enums.PermissionNameEnum;

@EventSub("station-status-changed-event")
public class StationStatusChangedListener implements EventListener {

	@Autowired
	private CuntaoWorkFlowService cuntaoWorkFlowService;

	@Autowired
	private Emp360BO emp360BO;

	@Override
	public void onMessage(Event event) {
		Map<String, Object> map = event.getContent();
		String operatorType = (String) map.get("operatorType");

	}

	/**
	 * 启动停业审批流程
	 */
	private void startClosingProcess() {

		String businessCode = "station-closing";
		String stationApplyId ="";
		String operatorId="";

		String orgId="";
		String remarks="";

		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", orgId);
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode, stationApplyId,
				operatorId, initData);
		if (rm.isSuccess()) {
			String procId = rm.getResult().getProcessInstanceId();
			//FIXME FHH 邮件通知审批人，应该监听流程中心事件
//			noticeNextOperator(PermissionNameEnum.BOPS_FORCE_QUIT_PROVINCE_AUDIT, applyId, context);
		}
	}

	/**
	 * 启动退出审批流程
	 */
	private void startQuitingProcess() {

		String businessCode = "station-quit";
		String stationApplyId="";
		String operatorId="";

		String orgId="";
		String remarks="";

		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", orgId);
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode, stationApplyId,
				operatorId, initData);
		if (rm.isSuccess()) {
			String procId = rm.getResult().getProcessInstanceId();
			//FIXME FHH 邮件通知审批人，应该监听流程中心事件
//			noticeNextOperator(PermissionNameEnum.BOPS_STATION_PROVINCE_AUDIT, applyId, context);
		}
	}
	
	

}
