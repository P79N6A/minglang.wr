package com.taobao.cun.auge.station.service.impl;

import java.math.BigDecimal;
import java.util.List;

import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.bail.BailService;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample.Criteria;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
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
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;
import com.taobao.cun.auge.station.service.GeneralTaskSubmitService;
import com.taobao.cun.auge.station.service.PartnerInstanceScheduleService;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.common.model.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	GeneralTaskSubmitService generalTaskSubmitService;
	
	@Autowired
	AccountMoneyMapper accountMoneyMapper;

	@Autowired
	BailService bailService;

	@Override
	public List<Long> getWaitOpenStationList(int fetchNum){
		return partnerInstanceBO.getWaitOpenStationList(fetchNum);
	}

	@Override
	public Boolean openStation(Long instanceId){
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
	public List<Long> getWaitThawMoneyList(int fetchNum){
		return partnerInstanceBO.getWaitThawMoneyList(fetchNum);
	}

	@Override
	public Boolean thawMoney(Long instanceId){
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceById(instanceId);
		if (rel == null) {
			return Boolean.TRUE;
		}
		// 获得冻结的金额
		String balanceFrozenMoney = getfreezedMoneyOfCNY(rel.getTaobaoUserId());
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
		String accountNo = getAccountNo(accountMoney.getAccountNo(), rel);
		String unfrozeMoney = getUnfrozenMoney(balanceFrozenMoney, accountMoney.getMoney());
		OperatorDto operatorDto = new OperatorDto(DomainUtils.DEFAULT_OPERATOR, OperatorTypeEnum.SYSTEM, 0L);
		generalTaskSubmitService.submitQuitTask(instanceId, accountNo, unfrozeMoney, operatorDto);
		return Boolean.TRUE;
	}

	/**
	 * 淘帮手转村小二时存在漏洞.这个逻辑兼容这种情形:
	 * 如果剩余冻结保证金大于初始保证金则解冻初始这笔
	 * 否则解冻剩余保证金
	 * @param balanceFrozedMoney 剩余冻结的保证金
	 * @param initFrozedMoney 最初冻结的保证金
	 * @return
	 */
	private String getUnfrozenMoney(String balanceFrozedMoney,  BigDecimal initFrozedMoney) {
		if (new BigDecimal(balanceFrozedMoney).compareTo(initFrozedMoney) > 0) {
			logger.warn("unfroze balance money:{}, great than init frozen monery:{}", balanceFrozedMoney, initFrozedMoney);
			return initFrozedMoney.toString();
		}
		return balanceFrozedMoney;
	}

	private String getAccountNo(String accountNo, PartnerStationRel rel) {
		if (accountNo!= null) {
			return accountNo;
		}
		OperatorDto operatorDto = new OperatorDto(DomainUtils.DEFAULT_OPERATOR, OperatorTypeEnum.SYSTEM, 0L);
		PaymentAccountDto accountDto = paymentAccountQueryAdapter.queryPaymentAccountByTaobaoUserId(rel.getTaobaoUserId(), operatorDto);
		if (accountDto == null){
			logger.error("PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:"+rel.getId());
			throw new  AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"PartnerInstanceScheduleService queryPaymentAccountByTaobaoUserId accountDto is null param:"+rel.getId());
		}
		return accountDto.getAccountNo();
	}

	private String getfreezedMoneyOfCNY(Long taobaoUserId) {
		ResultModel<String> resultModel = bailService.queryUserFreezeAmount(taobaoUserId, UserTypeEnum.PARTNER);
		if (resultModel!=null && resultModel.isSuccess()) {
			return resultModel.getResult();
		}
		throw new AugeSystemException("BailService|query freezed monery error");
	}


	@Override
	public List<AccountMoneyDto> getWaitInitAccountNoList(int fetchNum)
			{
		
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
	public Boolean initAccountNo(AccountMoneyDto accountMoneyDto){
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
			throw new  AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"PartnerInstanceScheduleService.initAccountNo rel is null param:"+accountMoneyDto.getObjectId());
		}

		PaymentAccountDto accountDto;
		try {
			accountDto = paymentAccountQueryAdapter
					.queryPaymentAccountByTaobaoUserId(rel.getTaobaoUserId(), operatorDto);
			if (accountDto == null){
				logger.error("PartnerInstanceScheduleService.initAccountNo accountDto is null param:"+accountMoneyDto.getObjectId());
				throw new  AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"PartnerInstanceScheduleService.initAccountNo accountDto is null param:"+accountMoneyDto.getObjectId());
			}
		} catch (Exception e) {
			logger.error("PartnerInstanceScheduleService.initAccountNo service erro param:"+accountMoneyDto.getObjectId());
			return Boolean.TRUE;
		}
		
		Partner partner = partnerBO.getPartnerById(rel.getPartnerId());
		if (partner == null) {
			return Boolean.TRUE;
		}
		
		if (!accountDto.getAlipayId().equals(partner.getAlipayAccount())) {
			logger.error("PartnerInstanceScheduleService.initAccountNo getAlipayAccount is change:"+accountMoneyDto.getObjectId());
			return Boolean.TRUE;
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
