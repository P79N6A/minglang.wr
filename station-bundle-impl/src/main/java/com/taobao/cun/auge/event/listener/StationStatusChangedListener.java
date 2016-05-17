package com.taobao.cun.auge.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.station.bo.Emp360BO;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub("station-status-changed-event")
public class StationStatusChangedListener implements EventListener {
	
	private static final Logger logger = LoggerFactory.getLogger(StationStatusChangedListener.class);

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
	 * 启动停业、退出流程审批流程
	 */
	private void startApproveProcess(String businessCode,String businessId,String applier,String parentOrgId,String remarks) {
		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", parentOrgId);
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode, businessId,
				applier, initData);
		if (rm.isSuccess()) {
			String procId = rm.getResult().getProcessInstanceId();
		}
		// FIXME FHH 邮件通知审批人，应该监听流程中心事件
		// noticeNextOperator(PermissionNameEnum.BOPS_FORCE_QUIT_PROVINCE_AUDIT,
		// applyId, context);
	}
}
