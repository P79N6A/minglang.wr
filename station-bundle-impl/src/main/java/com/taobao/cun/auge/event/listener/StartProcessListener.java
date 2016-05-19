package com.taobao.cun.auge.event.listener;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.StartProcessDto;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.enums.BusinessTypeEnum;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.bpm.service.CuntaoWorkFlowService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("startProcessListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class StartProcessListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(StartProcessListener.class);

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	TaskExecuteService taskExecuteService;

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
				createStartApproveProcessTask(ProcessBusinessEnum.stationForcedClosure.getCode(), stationApplyId, employeeId,
						parentOrgId, remark);
			} else if (StationStatusEnum.QUITING.equals(newStatus) && StationStatusEnum.CLOSED.equals(oldStatus)) {
				// 启动退出流程
				createStartApproveProcessTask(ProcessBusinessEnum.stationQuitRecord.getCode(), stationApplyId, employeeId,
						parentOrgId, remark);
			}
		} catch (Exception e) {
			logger.error("启动审批流程失败。stationId=" + stationId + " employeeId =" + employeeId + "newStatus = "
					+ newStatus.getDesc() + " oldStatus = " + oldStatus.getDesc() + " remark = " + remark, e);
		}
	}

	/**
	 * 启动停业、退出流程审批流程
	 */
	private void createStartApproveProcessTask(String businessCode, Long businessId, String applierId, Long parentOrgId,
			String remarks) {

		StartProcessDto startProcessDto = new StartProcessDto();

		startProcessDto.setRemarks(remarks);
		startProcessDto.setParentOrgId(parentOrgId);
		startProcessDto.setBusinessId(businessId);
		startProcessDto.setBusinessCode(businessCode);
		startProcessDto.setApplierId(applierId);
		// 旺旺去标
		GeneralTaskDto startProcessTask = new GeneralTaskDto();
		startProcessTask.setBusinessNo(String.valueOf(businessId));
		startProcessTask.setBusinessStepNo(1l);
		startProcessTask.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		startProcessTask.setBusinessStepDesc("启动审批流程");
		startProcessTask.setBeanName("processService");
		startProcessTask.setMethodName("startApproveProcess");
		startProcessTask.setOperator(applierId);
		startProcessTask.setParameter(startProcessDto);

		// 提交任务
		taskExecuteService.submitTask(startProcessTask);
	}
}
