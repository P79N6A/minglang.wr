package com.taobao.cun.auge.station.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.adapter.AlipayStandardBailAdapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AlipayStandardBailDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceQuitDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.enums.GeneralTaskBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.chronus.dto.GeneralTaskDto;
import com.taobao.cun.chronus.service.TaskExecuteService;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceScheduleService")
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
	@Autowired
	AlipayStandardBailAdapter alipayStandardBailAdapter;
	
	@Value("${cuntao.alipay.standerBailTypeCode}")
	String standerBailTypeCode;
	
	@Autowired
	TaskExecuteService taskExecuteService;

	
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
		
		//TODO:获得冻结的金额
		String frozenMoney ="";
		
		List<GeneralTaskDto> taskLists = new LinkedList<GeneralTaskDto>();
		
		//解除保证金
		GeneralTaskDto dealStanderBailTaskVo = new GeneralTaskDto();
		dealStanderBailTaskVo.setBusinessNo(String.valueOf(instanceId));
		dealStanderBailTaskVo.setBeanName("alipayStandardBailAdapter");
		dealStanderBailTaskVo.setMethodName("dealStandardBail");
		dealStanderBailTaskVo.setBusinessStepNo(1l);
		dealStanderBailTaskVo.setBusinessType(GeneralTaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode());
		dealStanderBailTaskVo.setBusinessStepDesc("dealStandardBail");
		dealStanderBailTaskVo.setOperator(operatorDto.getOperator());

		AlipayStandardBailDto alipayStandardBailDto = new AlipayStandardBailDto();
		alipayStandardBailDto.setAmount(frozenMoney);
		alipayStandardBailDto.setOpType(AlipayStandardBailDto.ALIPAY_OP_TYPE_UNFREEZE);
		alipayStandardBailDto.setOutOrderNo(String.valueOf(instanceId));
		alipayStandardBailDto.setTransferMemo("村淘保证金解冻");
		alipayStandardBailDto.setTypeCode(standerBailTypeCode);
		alipayStandardBailDto.setUserAccount(accountDto.getAccountNo());

		dealStanderBailTaskVo.setParameter(alipayStandardBailDto);
		taskLists.add(dealStanderBailTaskVo);
		
		//正式撤点
		GeneralTaskDto quitTaskVo = new GeneralTaskDto();
		quitTaskVo.setBusinessNo(String.valueOf(instanceId));
		quitTaskVo.setBeanName("partnerInstanceService");
		quitTaskVo.setMethodName("quitPartnerInstance");
		quitTaskVo.setBusinessStepNo(2l);
		quitTaskVo.setBusinessType(GeneralTaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode());
		quitTaskVo.setBusinessStepDesc("quitPartnerInstance");
		quitTaskVo.setOperator(operatorDto.getOperator());

		PartnerInstanceQuitDto partnerInstanceQuitDto = new PartnerInstanceQuitDto();
		partnerInstanceQuitDto.copyOperatorDto(operatorDto);
		partnerInstanceQuitDto.setInstanceId(instanceId);
		
		quitTaskVo.setParameter(partnerInstanceQuitDto);
		taskLists.add(quitTaskVo);
		
		taskExecuteService.submitTasks(taskLists);
		return Boolean.TRUE;
	}
}
