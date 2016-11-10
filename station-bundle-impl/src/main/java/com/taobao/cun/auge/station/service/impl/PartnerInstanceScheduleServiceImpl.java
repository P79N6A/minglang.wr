package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.PageQuery;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample.Criteria;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.station.adapter.AlipayStandardBailAdapter;
import com.taobao.cun.auge.station.adapter.PaymentAccountQueryAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.AccountMoneyConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.OpenStationDto;
import com.taobao.cun.auge.station.dto.PaymentAccountDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.validator.BeanValidator;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceScheduleService")
@HSFProvider(serviceInterface = PartnerInstanceScheduleService.class)
public class PartnerInstanceScheduleServiceImpl implements PartnerInstanceScheduleService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceScheduleService.class);
	
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
	
	@Autowired
	AccountMoneyMapper accountMoneyMapper;

	@Override
	public List<Long> getWaitOpenStationList(int fetchNum) throws AugeServiceException {
		return partnerInstanceBO.getWaitOpenStationList(fetchNum);
	}

	@Override
	public Boolean openStation(Long instanceId) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null) {
			logger.error("PartnerInstanceScheduleService error record is null param:"+instanceId);
			return Boolean.TRUE;
		}
		OpenStationDto openStationDto =new OpenStationDto();
		openStationDto.setImme(Boolean.TRUE);
		openStationDto.setOpenDate(rel.getOpenDate());
		openStationDto.copyOperatorDto(OperatorDto.defaultOperator());
		openStationDto.setPartnerInstanceId(instanceId);
		partnerInstanceService.openStation(openStationDto);
		return Boolean.TRUE;
	}

	@Override
	public List<Long> getWaitThawMoneyList(int fetchNum) throws AugeServiceException {
		return partnerInstanceBO.getWaitThawMoneyList(fetchNum);
	}

	@Override
	public Boolean thawMoney(Long instanceId) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null) {
			return Boolean.TRUE;
		}
//		Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
//		if (partner == null) {
//			return Boolean.TRUE;
//		}
		
		// 获得冻结的金额
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
		String frozenMoney = accountMoney.getMoney().toString();
		
		String accountNo = accountMoney.getAccountNo();
		
		OperatorDto operatorDto = new OperatorDto();
		operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		operatorDto.setOperatorOrgId(0L);
		if (accountNo== null) {
			PaymentAccountDto accountDto = paymentAccountQueryAdapter
					.queryPaymentAccountByTaobaoUserId(rel.getTaobaoUserId(), operatorDto);
			if (accountDto == null){
				logger.error("PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:"+instanceId);
				throw new  AugeServiceException("PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:"+instanceId);
			}
			accountNo = accountDto.getAccountNo();
		}

		generalTaskSubmitService.submitQuitTask(instanceId, accountNo, frozenMoney, operatorDto);

		return Boolean.TRUE;
	}

	@Override
	public List<AccountMoneyDto> getWaitInitAccountNoList(int fetchNum)
			throws AugeServiceException {
		
		AccountMoneyExample example = new AccountMoneyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andStateEqualTo(AccountMoneyStateEnum.HAS_FROZEN.getCode());
		criteria.andAccountNoIsNull();
		criteria.andTargetTypeEqualTo(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE.getCode());
		criteria.andTypeEqualTo(AccountMoneyTypeEnum.PARTNER_BOND.getCode());
		
		PageHelper.startPage(1, fetchNum);
		List<AccountMoney> page = accountMoneyMapper.selectByExample(example);
		
		return AccountMoneyConverter.toAccountMoneyDtos(page);
		
	}

	@Override
	public Boolean initAccountNo(AccountMoneyDto accountMoneyDto) throws AugeServiceException {
		if (accountMoneyDto == null || StringUtils.isNotEmpty(accountMoneyDto.getAccountNo())) {
			return Boolean.TRUE;
		}
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(accountMoneyDto.getObjectId());
		
		OperatorDto operatorDto = new OperatorDto();
		operatorDto.setOperator(DomainUtils.DEFAULT_OPERATOR);
		operatorDto.setOperatorType(OperatorTypeEnum.SYSTEM);
		operatorDto.setOperatorOrgId(0L);
		
		if (rel == null || rel.getTaobaoUserId() == null) {
			logger.error("PartnerInstanceScheduleService.initAccountNo rel is null param:"+accountMoneyDto.getObjectId());
			throw new  AugeServiceException("PartnerInstanceScheduleService.initAccountNo rel is null param:"+accountMoneyDto.getObjectId());
		}

		PaymentAccountDto accountDto = paymentAccountQueryAdapter
				.queryPaymentAccountByTaobaoUserId(rel.getTaobaoUserId(), operatorDto);
		if (accountDto == null){
			logger.error("PartnerInstanceScheduleService.initAccountNo accountDto is null param:"+accountMoneyDto.getObjectId());
			throw new  AugeServiceException("PartnerInstanceScheduleService.initAccountNo accountDto is null param:"+accountMoneyDto.getObjectId());
		}
		AccountMoney   record = new AccountMoney();
		record.setId(accountMoneyDto.getId());
		record.setAccountNo(accountDto.getAccountNo());
		record.setAlipayAccount(accountDto.getAlipayId());
		DomainUtils.beforeUpdate(record, DomainUtils.DEFAULT_OPERATOR);
		accountMoneyMapper.updateByPrimaryKeySelective(record);
		return Boolean.TRUE;
	}
}
