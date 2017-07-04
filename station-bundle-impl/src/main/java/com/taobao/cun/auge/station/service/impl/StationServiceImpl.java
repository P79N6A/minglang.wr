package com.taobao.cun.auge.station.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.EventDispatcherUtil;
import com.taobao.cun.auge.event.StationStatusChangeEvent;
import com.taobao.cun.auge.event.enums.StationStatusChangeEnum;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.check.StationChecker;
import com.taobao.cun.auge.station.convert.StationEventConverter;
import com.taobao.cun.auge.station.dto.ApproveProcessTask;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.StationService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("stationService")
@HSFProvider(serviceInterface = StationService.class)
public class StationServiceImpl implements StationService {

	private static final Logger logger = LoggerFactory.getLogger(StationService.class);

	@Autowired
	StationBO stationBO;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	ShutDownStationApplyBO shutDownStationApplyBO;

	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;
	
	@Autowired
	StationChecker stationChecker;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void auditQuitStation(Long stationId, ProcessApproveResultEnum approveResult) throws AugeBusinessException,AugeSystemException{
		if (null == stationId) {
			logger.error("stationId is null");
			throw new IllegalArgumentException("stationId is null");
		}

		// 使用默认的系统操作人
		OperatorDto operatorDto = OperatorDto.defaultOperator();
		String operator = operatorDto.getOperator();

		// 审批结果
		if (ProcessApproveResultEnum.APPROVE_PASS.equals(approveResult)) {
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.QUIT, operator);
			generalTaskSubmitService.submitShutdownApprovedTask(stationId);
			
			// 发出同意撤点事件
			dispatchStationStatusChangeEvent(stationId, StationStatusChangeEnum.QUIT, operatorDto);
		} else {
			// 删除撤点申请单
			shutDownStationApplyBO.deleteShutDownStationApply(stationId, operator);
			// 村点状态变更为已停业
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
			
			// 发出拒绝撤点事件
			dispatchStationStatusChangeEvent(stationId, StationStatusChangeEnum.QUITTING_REFUSED, operatorDto);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyShutDownStationByManager(ShutDownStationApplyDto shutDownDto) throws AugeBusinessException,AugeSystemException{
		BeanValidator.validateWithThrowable(shutDownDto);

		Long stationId = shutDownDto.getStationId();
		
		Station station = stationBO.getStationById(stationId);
		if (null == station) {
			logger.warn("村点不存在。stationId=" + stationId);
			throw new AugeServiceException("村点不存在");
		}
	    
		// 校验村点撤点的前提提交
	    stationChecker.checkShutdownApply(stationId);

		// 撤点申请中
		stationBO.changeState(stationId, StationStatusEnum.CLOSED, StationStatusEnum.QUITING,
				shutDownDto.getOperator());

		// 保存申请单
		Long applyId = shutDownStationApplyBO.saveShutDownStationApply(shutDownDto);

		// 插入启动撤点流程的任务
		ApproveProcessTask processTask = new ApproveProcessTask();
		processTask.setBusiness(ProcessBusinessEnum.SHUT_DOWN_STATION);
		processTask.setBusinessId(stationId);
		processTask.setBusinessName(station.getName());
		processTask.setBusinessOrgId(station.getApplyOrg());
		processTask.copyOperatorDto(shutDownDto);
		Map<String, String> params = new HashMap<String, String>();
		params.put("applyId", String.valueOf(applyId));
		processTask.setParams(params);
		generalTaskSubmitService.submitApproveProcessTask(processTask);
		
		 //发出撤点申请事件
	    dispatchStationStatusChangeEvent(stationId,StationStatusChangeEnum.START_QUITTING,shutDownDto);
	}
	
	private void dispatchStationStatusChangeEvent(Long stationId, StationStatusChangeEnum statusChangeEnum,	OperatorDto operator) {
		Station station = stationBO.getStationById(stationId);
		StationStatusChangeEvent event = StationEventConverter.convert(statusChangeEnum, station, operator);
		EventDispatcherUtil.dispatch(EventConstant.CUNTAO_STATION_STATUS_CHANGED_EVENT, event);
	}
}
