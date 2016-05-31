package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.domain.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = PartnerInstanceScheduleService.class)
public class PartnerInstanceScheduleServiceImpl implements PartnerInstanceScheduleService {
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	PartnerInstanceService partnerInstanceService;
	@Autowired
	PartnerBO partnerBO;
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;
	
	@Override
	public List<Long> getWaitOpenStationList(int fetchNum)
			throws AugeServiceException {
		return partnerInstanceBO.getWaitOpenStationList(fetchNum);
	}

	@Override
	public Boolean openStation(Long instanceId) throws AugeServiceException {
		OperatorDto operatorDto= new OperatorDto();
		operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		operatorDto.setOperatorOrgId(0L);
		// TODO:检查开业包
		if (!partnerInstanceService.checkKyPackage()) {
			//开业时间置为空
			partnerInstanceBO.updateOpenDate(instanceId, null,
					operatorDto.getOperator());
			//TODO：发短信
			return Boolean.TRUE;
		}
		partnerInstanceBO.changeState(instanceId, PartnerInstanceStateEnum.DECORATING,
				PartnerInstanceStateEnum.SERVICING, DomainUtils.DEFAULT_OPERATOR);
	
		// 记录村点状态变化
		EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_INSTANCE_STATE_CHANGE_EVENT,
				PartnerInstanceEventConverter.convert(PartnerInstanceStateChangeEnum.START_SERVICING,
						partnerInstanceBO.getPartnerInstanceById(instanceId),operatorDto));
		return Boolean.TRUE;
	}

	@Override
	public List<Long> getWaitThawMoneyList(int fetchNum)
			throws AugeServiceException {
		return partnerInstanceBO.getWaitOpenStationList(fetchNum);
	}

	@Override
	public Boolean thawMoney(Long instanceId)
			throws AugeServiceException {
		
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null)  {
			return Boolean.TRUE;
		}
		Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
		if (partner == null) {
			return Boolean.TRUE;
		}
		OperatorDto operatorDto= new OperatorDto();
		operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		operatorDto.setOperatorOrgId(0L);
		PaymentAccountDto accountDto = paymentAccountQueryAdapter.queryPaymentAccountByTaobaoUserId(partner.getTaobaoUserId(), operatorDto);
		accountDto.getAccountNo();
		
		List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();
		return null;
		//解除保证金
		/*GeneralTaskDto dealStanderBailTaskVo = new GeneralTaskDto();
		dealStanderBailTaskVo.setBusinessNo(String.valueOf(instanceId));
		dealStanderBailTaskVo.setBeanName("alipayStandardBailService");
		dealStanderBailTaskVo.setMethodName("dealStandardBail");
		dealStanderBailTaskVo.setBusinessStepNo(1l);
		dealStanderBailTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		dealStanderBailTaskVo.setBusinessStepDesc("dealStandardBail");
		dealStanderBailTaskVo.setOperator(context.getAppId());

		AlipayStandardBailDto alipayStandardBailDto = new AlipayStandardBailDto();
		alipayStandardBailDto.setAmount(stationApplyDO.getFrozenMoney());
		alipayStandardBailDto.setOpType(AlipayStandardBailDto.ALIPAY_OP_TYPE_UNFREEZE);
		alipayStandardBailDto.setOutOrderNo(stationApplyDO.getId().toString());
		alipayStandardBailDto.setTransferMemo("村淘保证金解冻");
		alipayStandardBailDto.setTypeCode(alipayConfig.getStanderBailTypeCode());
		alipayStandardBailDto.setUserAccount(getAlipayAccountNo(stationApplyDO.getTaobaoUserId(),context.getAppId()));

		dealStanderBailTaskVo.setParameter(alipayStandardBailDto);
		taskVos.add(dealStanderBailTaskVo);*/
		//正式撤点
		/*GeneralTaskDto changeStateTaskVo = new GeneralTaskDto();
		changeStateTaskVo.setBusinessNo(String.valueOf(instanceId));
		changeStateTaskVo.setBeanName("partnerInstanceBO");
		changeStateTaskVo.setMethodName("changeState");
		changeStateTaskVo.setBusinessStepNo(1l);
		changeStateTaskVo.setBusinessType(BusinessTypeEnum.STATION_QUITE_CONFIRM);
		changeStateTaskVo.setBusinessStepDesc("changeState");
		changeStateTaskVo.setOperator(context.getAppId());

		ChangeStationDto changeStationDto = new ChangeStationDto();
		changeStationDto.setState(StationApplyStateEnum.QUIT.getCode());
		changeStationDto.setStationApplyId(stationApplyDO.getId());
		changeStationDto.setLoginId(context.getLoginId());
		changeStationDto.setNow(new Date());
		changeStateTaskVo.setParameter(changeStationDto);
		taskVos.add(changeStateTaskVo);
		taskExecuteService.submitTasks(taskVos, true);
				
		

		// uic去标
		GeneralTaskDto task = new GeneralTaskDto();
		task.setBusinessNo(String.valueOf(taobaoUserId));
		task.setBeanName("uicTagService");
		task.setMethodName("removeUserTag");
		task.setBusinessStepNo(1l);
		task.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
		task.setBusinessStepDesc("去uic标");
		task.setOperator(operatorId);
		task.setParameter(userTagDto);
		taskLists.add(task);

		// 旺旺去标
		GeneralTaskDto wangwangTaskVo = new GeneralTaskDto();
		wangwangTaskVo.setBusinessNo(String.valueOf(taobaoUserId));
		wangwangTaskVo.setBeanName("wangWangTagService");
		wangwangTaskVo.setMethodName("removeWangWangTagByNick");
		wangwangTaskVo.setBusinessStepNo(2l);
		wangwangTaskVo.setBusinessType(TaskBusinessTypeEnum.STATION_QUITE_CONFIRM.getCode());
		wangwangTaskVo.setBusinessStepDesc("去旺旺标");
		wangwangTaskVo.setOperator(operatorId);
		wangwangTaskVo.setParameter(taobaoNick);
		taskLists.add(wangwangTaskVo);

		// 提交任务
		taskExecuteService.submitTasks(taskLists);*/
	}
	
	

}
