package com.taobao.cun.auge.station.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.ShutDownStationApplyBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.ShutDownStationApplyDto;
import com.taobao.cun.auge.station.enums.ProcessApproveResultEnum;
import com.taobao.cun.auge.station.enums.ProcessBusinessEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.CaiNiaoService;
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
	CaiNiaoService caiNiaoService;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void auditQuitStation(Long stationId, ProcessApproveResultEnum approveResult) throws AugeServiceException {
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
			caiNiaoService.deleteNotUserdCainiaoStation(stationId);
		} else {
			// 删除撤点申请单
			shutDownStationApplyBO.deleteShutDownStationApply(stationId, operator);
			// 村点状态变更为已停业
			stationBO.changeState(stationId, StationStatusEnum.QUITING, StationStatusEnum.CLOSED, operator);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void applyShutDownStationByManager(ShutDownStationApplyDto shutDownDto) throws AugeServiceException {
		BeanValidator.validateWithThrowable(shutDownDto);

		Long stationId = shutDownDto.getStationId();
		// 校验村点上所有人是否都是退出待解冻、已退出的状态
		boolean isAllPartnerQuit = partnerInstanceBO.isAllPartnerQuit(stationId);
		if (!isAllPartnerQuit) {
			logger.warn("存在非退出，或者退出待解冻的合伙人，不可以撤点");
			throw new AugeServiceException("存在非退出，或者退出待解冻的合伙人，不可以撤点");
		}

		// 保存申请单
		shutDownStationApplyBO.saveShutDownStationApply(shutDownDto);
		// 撤点申请中
		stationBO.changeState(stationId, StationStatusEnum.CLOSED, StationStatusEnum.QUITING,
				shutDownDto.getOperator());

		// 插入启动撤点流程的任务
		generalTaskSubmitService.submitApproveProcessTask(ProcessBusinessEnum.SHUT_DOWN_STATION, stationId, shutDownDto,
				shutDownDto.getReason());
	}
}
