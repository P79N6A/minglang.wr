package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.enums.PartnerInstanceStateChangeEnum;
import com.taobao.cun.auge.station.adapter.AlipayStandardBailAdapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceEventConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
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
	AccountMoneyBO accountMoneyBO;
	@Autowired
	PaymentAccountQueryAdapter paymentAccountQueryAdapter;
	@Autowired
	AlipayStandardBailAdapter alipayStandardBailAdapter;
	
	@Autowired
	GeneralTaskSubmitService generalTaskSubmitService;

	
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
		
		//获得冻结的金额
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
		String frozenMoney = accountMoney.getMoney().toString();
		
		generalTaskSubmitService.submitThawAndQuitTask(instanceId,accountDto,frozenMoney,operatorDto);
		
		return Boolean.TRUE;
	}
}
