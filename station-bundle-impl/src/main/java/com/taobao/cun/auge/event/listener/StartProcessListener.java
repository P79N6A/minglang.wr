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
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.ProcessTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.handler.PartnerInstanceHandler;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.enums.BusinessTypeEnum;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.Event;
import com.taobao.cun.crius.event.annotation.EventSub;
import com.taobao.cun.crius.event.client.EventListener;

@Component("startProcessListener")
@EventSub(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT)
public class StartProcessListener implements EventListener {

	private static final Logger logger = LoggerFactory.getLogger(StartProcessListener.class);

	@Autowired
	PartnerInstanceHandler partnerInstanceHandler;

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
		// 可能是小二，也可能是TP商淘宝账号
		String operatorId = (String) map.get("operatorId");
		Long operatorOrgId = (Long) map.get("operatorOrgId");
		String intanceId = (String) map.get("intanceId");
		String remark = (String) map.get("remark");
		PartnerInstanceTypeEnum partnerType = (PartnerInstanceTypeEnum) map.get("partnerType");

		Long stationApplyId = partnerInstanceBO.findStationApplyId(Long.valueOf(intanceId));

		ProcessBusinessEnum business = findBusinessType(newStatus, oldStatus, partnerType);
		if (null != business) {
			createStartApproveProcessTask(business, stationApplyId, operatorId, operatorOrgId, remark);
		}
	}

	private ProcessBusinessEnum findBusinessType(StationStatusEnum newStatus, StationStatusEnum oldStatus,
			PartnerInstanceTypeEnum partnerType) {
		if (StationStatusEnum.CLOSING.equals(newStatus) && StationStatusEnum.SERVICING.equals(oldStatus)) {
			return partnerInstanceHandler.findProcessBusiness(partnerType, ProcessTypeEnum.CLOSING_PRO);
		} else if (StationStatusEnum.QUITING.equals(newStatus) && StationStatusEnum.CLOSED.equals(oldStatus)) {
			return partnerInstanceHandler.findProcessBusiness(partnerType, ProcessTypeEnum.QUIT_PRO);
		}

		return null;
	}

	/**
	 * 启动停业、退出流程审批流程
	 * 
	 * @param business
	 *            业务类型
	 * @param stationApplyId
	 *            业务主键
	 * @param applierId
	 *            申请人
	 * @param applierOrgId
	 *            申请人orgid
	 * @param remarks
	 *            备注
	 */
	private void createStartApproveProcessTask(ProcessBusinessEnum business, Long stationApplyId, String applierId,
			Long applierOrgId, String remarks) {
		StartProcessDto startProcessDto = new StartProcessDto();

		startProcessDto.setRemarks(remarks);
		startProcessDto.setParentOrgId(applierOrgId);
		startProcessDto.setBusinessId(stationApplyId);
		startProcessDto.setBusinessCode(business.getCode());
		startProcessDto.setApplierId(applierId);
		// 旺旺去标
		GeneralTaskDto startProcessTask = new GeneralTaskDto();
		startProcessTask.setBusinessNo(String.valueOf(stationApplyId));
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
