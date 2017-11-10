package com.taobao.cun.auge.payment.protocol.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.result.ErrorInfo;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.payment.protocol.AlipayAgreementService;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.cun.settle.cae.dto.AccountCaeSignDto;
import com.taobao.cun.settle.cae.service.SellerSignService;
import com.taobao.cun.settle.common.model.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.payment.account.dto.CreateAccountDTO;
import com.taobao.payment.account.dto.QueryAccountDTO;
import com.taobao.payment.account.result.AccountQueryResult;
import com.taobao.payment.account.result.CreateAccountResult;
import com.taobao.payment.account.service.AccountManageService;
import com.taobao.payment.account.service.query.AccountQueryService;
import com.taobao.payment.common.constants.AccountConstants;
import com.taobao.payment.common.domain.AccountBO;
import com.taobao.payment.common.enums.AccountStatusEnum;
import com.taobao.payment.common.enums.BizTypeEnum;
import com.taobao.payment.common.enums.ChannelEnum;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;

@Service("alipayAgreementService")
@HSFProvider(serviceInterface = AlipayAgreementService.class)
public class AlipayAgreementServiceImpl implements AlipayAgreementService {

	@Autowired
	private AccountManageService accountManageService;
	
	@Autowired
	private AccountQueryService accountQueryService;
	
	@Autowired
	private PartnerInstanceQueryService partnerInstanceQueryService;
	
	@Autowired
	private SellerSignService sellerSignService;
	
	@Autowired
	private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;
	
	@Autowired
	private AccountMoneyBO accountMoneyBO;
	
	private static Logger logger = LoggerFactory.getLogger(AlipayAgreementServiceImpl.class);
	
	@Autowired
	private DiamondConfiguredProperties diamondConfiguredProperties;
	@Override
	public Result<String> createAlipayAgreementSignUrl(Long taobaoUserId) {
		Result<String> result = null;
		PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
		if(partnerInstance == null){
			result = Result.of(ErrorInfo.of("PARTNER_INSTANCE_NOT_EXITS", null, "合伙人不存在"));
			return result;
		}
		CreateAccountDTO createAccountDTO = new CreateAccountDTO();
		try {
			createAccountDTO.setUserId(taobaoUserId);
			createAccountDTO.setNickName(partnerInstance.getPartnerDto().getTaobaoNick());
			createAccountDTO.setBizType(BizTypeEnum.CUNTAO);
			createAccountDTO.setChannel(ChannelEnum.ALIPAY_AGREEMENT_INSTANT);
			ResultDO<BasePaymentAccountDO> basePaymentAccountDOResult = uicPaymentAccountReadServiceClient.getAccountByUserId(taobaoUserId);
			if(basePaymentAccountDOResult.isSuccess() && basePaymentAccountDOResult.getModule()!=null){
				createAccountDTO.setChannelAccount(basePaymentAccountDOResult.getModule().getAccountNo());
			}
			if(!"none".equals(diamondConfiguredProperties.getAlipayProvideHostName())){
				createAccountDTO.setAttribute(AccountConstants.PROVIDER_HOSTNAME, diamondConfiguredProperties.getAlipayProvideHostName());
			}
			createAccountDTO.setAttribute(AccountConstants.CREATE_ACCOUNT_SUCCESS_REDIRECT_URL, diamondConfiguredProperties.getPaymentSignReturnUrl());
			CreateAccountResult createAccountResult = accountManageService.createPaymentAccount(taobaoUserId, createAccountDTO);
			if(createAccountResult.isSuccess()){
				result =  Result.of(createAccountResult.getSignUpURL());
				return result;
			}else{
				result = Result.of(ErrorInfo.of("CREATE_PAYMENT_ACCOUNT_ERROR", createAccountResult.getResultCode(), createAccountResult.getResultMessage()));
				return result;
			}
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("CREATE_PAYMENT_ACCOUNT_ERROR", null, "创建支付协议异常"));
			logger.error("CREATE_PAYMENT_ACCOUNT_ERROR",e);
			return result;
		} 
	}

	@Override
	public Result<Boolean> isAlipayAgreementSigned(Long taobaoUserId) {
		Result<Boolean> result = null;
		try {
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(partnerInstance == null){
				logger.info("alipayAgreementCallBack:PARTNER_INSTANCE_NOT_EXITS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("PARTNER_INSTANCE_NOT_EXITS", null, "合伙人不存在"));
				return result;
			}
			AccountQueryResult accountQueryResult = queryAccount(taobaoUserId);
			if(accountQueryResult.isSuccess()){
				List<AccountBO> accounts = accountQueryResult.getAccountBOList();
				if(accounts!=null && !accounts.isEmpty()){
					AccountBO accountBO = accounts.iterator().next();
					if(AccountStatusEnum.VALID.getCode().equals(accountBO.getAccountStatus().getCode())){
						addPaymentAgreementInfo(partnerInstance.getId(),taobaoUserId,queryAlipayAccountNo(accountBO.getChannelAccount()),queryAlipayAccount(accountBO.getChannelAccount()));
						return Result.of(Boolean.TRUE);
					}
				}
			}
		    return Result.of(Boolean.FALSE);
		} catch (Exception e) {
			logger.error("isSignedAlipayAgreement error["+taobaoUserId+"]",e);
			result = Result.of(ErrorInfo.of("CHECK_SIGN_PAYMENT_ACCOUNT_ERROR", null, "查询支付协议异常"));
			return result;
		}
	}

	public AccountQueryResult queryAccount(Long taobaoUserId) throws Exception{
		QueryAccountDTO queryAccountDTO = new  QueryAccountDTO();
		queryAccountDTO.setUserId(taobaoUserId);
		queryAccountDTO.setBizType(BizTypeEnum.CUNTAO);
		queryAccountDTO.setChannelEnum(ChannelEnum.ALIPAY_AGREEMENT_INSTANT);
		AccountQueryResult accountQueryResult = accountQueryService.queryUserAccount(taobaoUserId, queryAccountDTO);
		return accountQueryResult;
	}
	
	
	public Result<Boolean> alipayAgreementCallBack(Long taobaoUserId){
		Result<Boolean> result = null;
		try {
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(partnerInstance == null){
				logger.info("alipayAgreementCallBack:PARTNER_INSTANCE_NOT_EXITS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("PARTNER_INSTANCE_NOT_EXITS", null, "合伙人不存在"));
				return result;
			}
			
			AccountBO account = queryValidAccount(taobaoUserId);
			if(account == null){
				logger.info("alipayAgreementCallBack:VALID_PAYMENT_ACCOUNT_NOT_EXITS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("VALID_PAYMENT_ACCOUNT_NOT_EXITS", null, "协议支付签约账号不存在"));
				return result;
			}
			addPaymentAgreementInfo(partnerInstance.getId(),taobaoUserId,queryAlipayAccountNo(account.getChannelAccount()),queryAlipayAccount(account.getChannelAccount()));
			return Result.of(true);
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("SYSTEM_ERROR", null, "协议支付回调异常"));
			logger.error("ALIPAY_AGREEMENT_CALLBACK_ERROR",e);
			return result;
		}
	}

	private void addPaymentAgreementInfo(Long partnerInstanceId,Long taobaoUserId,String accountNo,String alipayAccount){
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PAYMENT_AGREEMENT, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceId);
		if(accountMoney == null){
			AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
			accountMoneyDto.setObjectId(partnerInstanceId);
			accountMoneyDto.setOperator("system");
			accountMoneyDto.setOperatorType(OperatorTypeEnum.SYSTEM);
			accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			accountMoneyDto.setTaobaoUserId(taobaoUserId+"");
			accountMoneyDto.setType(AccountMoneyTypeEnum.PAYMENT_AGREEMENT);
			accountMoneyDto.setAccountNo(accountNo);
			accountMoneyDto.setAlipayAccount(alipayAccount);
			accountMoneyDto.setState(AccountMoneyStateEnum.PAYMENT_AGREEMENT_SIGNED);
			accountMoneyDto.setFrozenTime(new Date());
			accountMoneyBO.addAccountMoney(accountMoneyDto);
		}
	
	}
	
	private void addCaeAgreementInfo(Long partnerInstanceId,Long taobaoUserId,String accountNo,String alipayAccount){
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.CAE_AGREEMENT, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceId);
		if(accountMoney == null){
			AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
			accountMoneyDto.setObjectId(partnerInstanceId);
			accountMoneyDto.setOperator("system");
			accountMoneyDto.setOperatorType(OperatorTypeEnum.SYSTEM);
			accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
			accountMoneyDto.setTaobaoUserId(taobaoUserId+"");
			accountMoneyDto.setType(AccountMoneyTypeEnum.CAE_AGREEMENT);
			accountMoneyDto.setAccountNo(accountNo);
			accountMoneyDto.setAlipayAccount(alipayAccount);
			accountMoneyDto.setState(AccountMoneyStateEnum.CAE_SIGNED);
			accountMoneyDto.setFrozenTime(new Date());
			accountMoneyBO.addAccountMoney(accountMoneyDto);
		}
	}
	
	
	
	private AccountBO queryValidAccount(Long taobaoUserId) throws Exception {
		AccountQueryResult accountQueryResult = queryAccount(taobaoUserId);
		if(accountQueryResult.isSuccess()){
			List<AccountBO> accounts = accountQueryResult.getAccountBOList();
			if(accounts!=null && !accounts.isEmpty()){
				AccountBO account = accounts.iterator().next();
				if(AccountStatusEnum.VALID.getCode().equals(account.getAccountStatus().getCode())){
					return account;
				}
			}
		}
		return null;
	}

	private String queryAlipayAccount(String accountNo){
		if(!accountNo.endsWith("0156")){
			accountNo = accountNo+"0156";
		}
		ResultDO<BasePaymentAccountDO> resultDO = queryAlipayAccountByAccountNo(accountNo);
		if(resultDO.isSuccess() && resultDO.getModule() != null){
			return resultDO.getModule().getOutUser();
		}
		return null;
	}
	
	private String queryAlipayAccountNo(String accountNo){
		if(!accountNo.endsWith("0156")){
			accountNo = accountNo+"0156";
		}
		ResultDO<BasePaymentAccountDO> resultDO = queryAlipayAccountByAccountNo(accountNo);
		if(resultDO.isSuccess() && resultDO.getModule() != null){
			return resultDO.getModule().getAccountNo();
		}
		return null;
	}
	
	
	private ResultDO<BasePaymentAccountDO> queryAlipayAccountByAccountNo(String accountNo){
		if(!accountNo.endsWith("0156")){
			accountNo = accountNo+"0156";
		}
		ResultDO<BasePaymentAccountDO> resultDO = uicPaymentAccountReadServiceClient.getAccountByAccountNo(accountNo);
		return resultDO;
	}
	
	
	@Override
	public Result<Boolean> caeSign(Long taobaoUserId) {
		Result<Boolean> result = null;
		AccountBO account;
		try {
			PartnerInstanceDto partnerInstance = partnerInstanceQueryService.getActivePartnerInstance(taobaoUserId);
			if(partnerInstance == null){
				logger.info("caeSign:PARTNER_INSTANCE_NOT_EXITS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("PARTNER_INSTANCE_NOT_EXITS", null, "合伙人不存在"));
				return result;
			}
			
			account = queryValidAccount(taobaoUserId);
			if(account == null){
				logger.info("caeSign:VAID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("VAID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS", null, "有效协议支付账户不存在"));
			}
			ResultDO<BasePaymentAccountDO> resultDO = queryAlipayAccountByAccountNo(account.getChannelAccount());
			if(resultDO.isSuccess() && resultDO.getModule()!= null){
				Long userId = resultDO.getModule().getUserId();
				AccountCaeSignDto signDto = new AccountCaeSignDto();
				signDto.setUserId(userId);
				ResultModel<Boolean>  resultModel = sellerSignService.alipayUserSign(signDto);
				if(resultModel.isSuccess()){
					result = Result.of(true);
					result.setModule(Boolean.TRUE);
					addCaeAgreementInfo(partnerInstance.getId(),taobaoUserId,resultDO.getModule().getAccountNo(),resultDO.getModule().getOutUser());
					return result;
				}else{
					result = Result.of(ErrorInfo.of("CAE_SIGN_FAIL", resultModel.getMsgCode(), "CAE代扣签约失败"));
					logger.info("CAE_SIGN_FAIL userID["+userId+"],aliAccountNo["+resultDO.getModule().getAccountNo()+"],aliOutUser["+resultDO.getModule().getOutUser()+"]"+resultModel.getMessage());
					return result;
				}
			}
			result = Result.of(ErrorInfo.of("QUERY_ALIPAYACCOUNT_FAIL",null, "CAE代扣签约失败"));
			return result;
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("SYSTEM_ERROR", null, "协议支付回调异常"));
			logger.error("caeSign_error",e);
			return result;
		}
		
	}

	@Override
	public Result<String> queryPaymentAgreementAccount(Long taobaoUserId) {
		Result<String> result = null;
		try {
			AccountBO accountBO = queryValidAccount(taobaoUserId);
			if(accountBO != null){
				return Result.of(accountBO.getChannelAccount());
			}
			result = Result.of(ErrorInfo.of("VALID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS", null, "签约协议不存在"));
			return result;
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("SYSTEM_ERROR", null, "查询支付协议异常"));
			logger.error("queryPaymentAgreementAccount",e);
			return result;
		}
	}

	@Override
	public Result<Boolean> isCaeSigned(Long taobaoUserId) {
		AccountCaeSignDto signQueryDto = new AccountCaeSignDto();
		Result<Boolean> result = null;
		AccountBO account;
		try {
			account = queryValidAccount(taobaoUserId);
			if(account == null){
				logger.info("caeSign:VAID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS ["+taobaoUserId+"]");
				result = Result.of(ErrorInfo.of("VAID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS", null, "有效协议支付账户不存在"));
			}
			ResultDO<BasePaymentAccountDO> resultDO = queryAlipayAccountByAccountNo(account.getChannelAccount());
			if(resultDO.isSuccess()&& resultDO.getModule() !=null){
				Long userId = resultDO.getModule().getUserId();
				signQueryDto.setUserId(userId);
				//signQueryDto.setAlipayEmail( resultDO.getModule().getOutUser());
				//signQueryDto.setAlipayId( resultDO.getModule().getAccountNo());
				ResultModel<Boolean> resultModel = sellerSignService.queryAlipayUserSignInfo(signQueryDto);
				if(resultModel.isSuccess()){
					result = Result.of(resultModel.getResult());
					return result;
				}
				result = Result.of(ErrorInfo.of("QUERY_ALIPAY_USE_ACCOUNT_ERROR", null, "查询支付宝账号异常"));
				return result;
			}
			result = Result.of(ErrorInfo.of("VALID_PAYMENT_AGREEMENT_ACCOUNT_NOT_EXISTS", null, "签约协议不存在"));
			return result;
			
		} catch (Exception e) {
			result = Result.of(ErrorInfo.of("SYSTEM_ERROR", null, "查询CAE协议异常"));
			logger.error("isCaeSignedERROR",e);
			return result;
		}
	}
	
	
	
}
