package com.taobao.cun.auge.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.crius.bpm.dto.CuntaoProcessInstance;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.common.resultmodel.ResultModel;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class StartProcessListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(StartProcessListener.class);

	@Autowired
	private CuntaoWorkFlowService cuntaoWorkFlowService;

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public void onMessage(Event event) {
		Map<String, Object> map = event.getContent();
		
		StationStatusEnum newStatus = (StationStatusEnum) map.get("newStatus");
		StationStatusEnum oldStatus = (StationStatusEnum) map.get("oldStatus");
		String employeeId = (String) map.get("operatorId");
		String stationId = (String) map.get("stationId");
		String remark = (String) map.get("remark");
		try {
			Long parentOrgId = stationBO.getParentOrgId(Long.valueOf(stationId));
			Long stationApplyId = partnerInstanceBO.findStationApplyIdByStationId(Long.valueOf(stationId));
			if (StationStatusEnum.CLOSING.equals(newStatus) && StationStatusEnum.SERVICING.equals(oldStatus)) {
				// 启动停业流程
				startApproveProcess(ProcessBusinessEnum.stationForcedClosure.getCode(), stationApplyId, employeeId, parentOrgId, remark);
			} else if (StationStatusEnum.QUITING.equals(newStatus) && StationStatusEnum.CLOSED.equals(oldStatus)) {
				// 启动退出流程
				startApproveProcess(ProcessBusinessEnum.stationQuitRecord.getCode(), stationApplyId, employeeId, parentOrgId, remark);
			}
		} catch (Exception e) {
			logger.error("启动审批流程失败。stationId=" + stationId + " employeeId =" + employeeId + "newStatus = " + newStatus.getDesc()
					+ " oldStatus = " + oldStatus.getDesc() + " remark = " + remark,e);
		}
	}

	/**
	 * 启动停业、退出流程审批流程
	 */
	private void startApproveProcess(String businessCode, Long businessId, String applier, Long parentOrgId,
			String remarks) {
		// // 创建退出村点任务流程
		Map<String, String> initData = new HashMap<String, String>();
		initData.put("orgId", String.valueOf(parentOrgId));
		initData.put("remarks", remarks);
		ResultModel<CuntaoProcessInstance> rm = cuntaoWorkFlowService.startProcessInstance(businessCode,
				String.valueOf(businessId), applier, initData);
		if (!rm.isSuccess()) {
			logger.error("启动审批流程失败。businessCode=" + businessCode + " businessId =" + businessId + "applier = " + applier
					+ " parentOrgId = " + parentOrgId + " remarks = " + remarks,rm.getException());
		}
		// FIXME FHH 邮件通知审批人，应该监听流程中心事件
		// noticeNextOperator(PermissionNameEnum.BOPS_FORCE_QUIT_PROVINCE_AUDIT,
		// applyId, context);
	}
}
