package com.taobao.cun.auge.bail.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.bail.BailService;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.OperatorTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleReplenishMoneyEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.settle.bail.dto.CuntaoBailBaseQueryDto;
import com.taobao.cun.settle.bail.dto.CuntaoBailSignDto;
import com.taobao.cun.settle.bail.dto.CuntaoFreezeBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoTransferBailDto;
import com.taobao.cun.settle.bail.dto.CuntaoTransferBailForPunishDto;
import com.taobao.cun.settle.bail.dto.CuntaoUnFreezeBailDto;
import com.taobao.cun.settle.bail.enums.BailBizSceneEnum;
import com.taobao.cun.settle.bail.enums.BailChannelEnum;
import com.taobao.cun.settle.bail.enums.BailOperateTypeEnum;
import com.taobao.cun.settle.bail.enums.UserTypeEnum;
import com.taobao.cun.settle.bail.service.CuntaoNewBailService;
import com.taobao.cun.settle.common.model.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * Created by xujianhui on 16/12/27.
 */
@Service("bailService")
@HSFProvider(serviceInterface = BailService.class, clientTimeout = 15000)
public class BailServiceImpl implements BailService {
	

    private static final Logger logger = LoggerFactory.getLogger(BailServiceImpl.class);

    @Autowired
    private CuntaoNewBailService cuntaoNewBailService;

    @Autowired
    private AccountMoneyBO accountMoneyBO;
    
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;
    
    @Autowired
    private PartnerLifecycleBO partnerLifecycleBO;
    @Override
    public ResultModel<Boolean> isUserSignBail(Long taobaoUserId, String alipayId, UserTypeEnum userTypeEnum) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        ResultModel<Boolean> result = cuntaoNewBailService.isUserSignBail(taobaoUserId, alipayId, userTypeEnum);
        if (result != null && !result.isSuccess()) {
            logExceptionInfo("isUserSignBail", taobaoUserId, alipayId, result.getMessage());
        }
        return result;
    }

    @Override
    @Deprecated
    public ResultModel<String> buildSignBailUrl(Long taobaoUserId, UserTypeEnum userTypeEnum, String returnUrl,
                                                BailChannelEnum channel) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        Assert.notNull(channel);
        Assert.notNull(returnUrl);
        CuntaoBailSignDto signDto = new CuntaoBailSignDto();
        signDto.setTaobaoUserId(taobaoUserId);
        signDto.setUserTypeEnum(userTypeEnum);
        signDto.setReturnUrl(returnUrl);
        signDto.setBailChannelEnum(channel);
        ResultModel<String> result = cuntaoNewBailService.buildSignBailUrl(signDto);
        if (result != null && !result.isSuccess()) {
            logExceptionInfo("buildSignBailUrl0", taobaoUserId, "", result.getMessage());
        }
        return result;
    }

    @Override
    public ResultModel<String> buildSignBailUrl(Long taobaoUserId, String outerOrderNo, UserTypeEnum userTypeEnum,
                                                String returnUrl, BailChannelEnum channel) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(outerOrderNo);
        Assert.notNull(userTypeEnum);
        Assert.notNull(channel);
        Assert.notNull(returnUrl);
        CuntaoBailSignDto signDto = new CuntaoBailSignDto();
        signDto.setTaobaoUserId(taobaoUserId);
        signDto.setUserTypeEnum(userTypeEnum);
        signDto.setReturnUrl(returnUrl);
        signDto.setBailChannelEnum(channel);
        signDto.setOutRequestNo(outerOrderNo);
        ResultModel<String> result = cuntaoNewBailService.buildSignBailUrl(signDto);
        if (result != null && !result.isSuccess()) {
            logExceptionInfo("buildSignBailUrl", taobaoUserId, "", result.getMessage());
        }
        return result;
    }

    /**
     * 解冻0元 直接返回成功
     *
     * @param cuntaoUnFreezeBailDto 可以使用BaiDtoBuilder.generateUnfreezeBailDto构建
     * @return
     */
    @Override
    public ResultModel<Boolean> unfreezeUserBail(CuntaoUnFreezeBailDto cuntaoUnFreezeBailDto) {
        logger.warn("unfreezeUserBail:{}", ToStringBuilder.reflectionToString(cuntaoUnFreezeBailDto));
        if (Long.valueOf(0L).equals(cuntaoUnFreezeBailDto.getAmount())) {
            ResultModel<Boolean> resultModel = new ResultModel<>();
            resultModel.setSuccess(true);
            resultModel.setResult(Boolean.TRUE);
            return resultModel;
        }
        String outOrderId = cuntaoUnFreezeBailDto.getOutOrderId() + "UNFREEZE";
        cuntaoUnFreezeBailDto.setOutOrderId(outOrderId);
        ResultModel<Boolean> result = cuntaoNewBailService.unfreezeUserBail(cuntaoUnFreezeBailDto);
        if (result != null && !result.isSuccess()) {
            logExceptionInfo("unfreezeUserBail", cuntaoUnFreezeBailDto.getTaobaoUserId(),
                cuntaoUnFreezeBailDto.getAlipayId(), result.getMessage());
        }
        return result;
    }

    @Override
    public ResultModel<Boolean> freezeUserBail(CuntaoFreezeBailDto cuntaoFreezeBailDto) {
        logger.warn("unfreezeUserBail:{}", ToStringBuilder.reflectionToString(cuntaoFreezeBailDto));
        Assert.notNull(cuntaoFreezeBailDto);
        Assert.notNull(cuntaoFreezeBailDto.getTaobaoUserId());
        Assert.notNull(cuntaoFreezeBailDto.getAmount());
        ResultModel<Boolean> result = cuntaoNewBailService.freezeUserBail(cuntaoFreezeBailDto);
        if (result != null && !result.isSuccess()) {
            logExceptionInfo("freezeUserBail", cuntaoFreezeBailDto.getTaobaoUserId(), cuntaoFreezeBailDto.getAlipayId(),
                result.getMessage());
        }
        return result;
    }

    @Override
    public ResultModel<Boolean> transferUserBail(CuntaoTransferBailDto cuntaoTransferBailDto) {
        logger.warn("transferUserBail:{}", ToStringBuilder.reflectionToString(cuntaoTransferBailDto));
        return cuntaoNewBailService.transferUserBail(cuntaoTransferBailDto);
    }

    @Override
    public ResultModel<Boolean> transferUserBailForPunish(
        CuntaoTransferBailForPunishDto cuntaoTransferBailForPunishDto) {
        logger.warn("transferUserBailForPunish:{}", ToStringBuilder.reflectionToString(cuntaoTransferBailForPunishDto));
        return cuntaoNewBailService.transferUserBailForPunish(cuntaoTransferBailForPunishDto);
    }

    @Override
    public ResultModel<Boolean> checkAlipaySign(Map<String, String> params) {
        Assert.notEmpty(params);
        return cuntaoNewBailService.checkAlipaySign(params);
    }

    @Override
    public ResultModel<Boolean> fillSignInfo(Map<String, String> params) {
        Assert.notEmpty(params);
        logger.warn("fillSignInfo:{}", params);
        return cuntaoNewBailService.fillSignInfo(params);
    }

    @Override
    public ResultModel<String> queryUserFreezeAmount(Long taobaoUserId, UserTypeEnum userTypeEnum) {
        Assert.notNull(taobaoUserId);
        Assert.notNull(userTypeEnum);
        return cuntaoNewBailService.queryUserFreezeAmount(taobaoUserId, userTypeEnum);
    }

    private void logExceptionInfo(String operation, Long taobaoUserId, String alipayId, String msg) {
        if (StringUtils.contains(msg, "MONEY_NOT_ENOUGH") || StringUtils.contains(msg, "NOT_SIGN")) {
            logger.warn("operation:{}, taobaoUserId:{}, alipayId{}, errorMsg:{}",
                new Object[] {operation, taobaoUserId, alipayId, msg});
        } else {
            logger.error("operation:{}, taobaoUserId:{}, alipayId{}, errorMsg:{}",
                new Object[] {operation, taobaoUserId, alipayId, msg});
        }
    }

	@Override
	public ResultModel<Boolean> unfreezeUserReplenishBail(Long partnerInstanceId) {
		ResultModel<Boolean> resultModel = new ResultModel<>();
		PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceId);
		if (instance == null || accountMoney == null
				|| AccountMoneyStateEnum.HAS_THAW.getCode().equals(accountMoney.getState())) {
			logger.warn("unfreezeUserReplenishBail instanceId:{}", new Object[] { partnerInstanceId });
			resultModel.setSuccess(true);
			resultModel.setResult(Boolean.TRUE);
			resultModel.setMessage("铺货保证金已解冻或铺货金未冻结");
			return resultModel;
		} else {
			Long taobaoUserId = instance.getTaobaoUserId();
			try {
				ResultModel<Long> freezeAmount = queryUserFreezeAmountNew(taobaoUserId, UserTypeEnum.STORE);
				if (freezeAmount != null && freezeAmount.isSuccess() && freezeAmount.getResult() != null) {
					Long amount = getReplenishAmount(partnerInstanceId, resultModel, freezeAmount);
					if (amount > 0L) {
						CuntaoUnFreezeBailDto cuntaoUnFreezeBailDto = new CuntaoUnFreezeBailDto();
						cuntaoUnFreezeBailDto.setTaobaoUserId(taobaoUserId);
						cuntaoUnFreezeBailDto.setAmount(amount);
						cuntaoUnFreezeBailDto.setBailBizSceneEnum(BailBizSceneEnum.PARTNER_KAIYEBAO);
						cuntaoUnFreezeBailDto.setReason("村点退出解冻铺货金");
						cuntaoUnFreezeBailDto.setBailOperateTypeEnum(BailOperateTypeEnum.QUIT_UNFREEZE);
						cuntaoUnFreezeBailDto.setSource("auge");
						cuntaoUnFreezeBailDto.setOutOrderId("CT_REPLENISH_" + partnerInstanceId + "_UNFREEZE");
						cuntaoUnFreezeBailDto.setUserTypeEnum(UserTypeEnum.STORE);
						return cuntaoNewBailService.unfreezeUserBail(cuntaoUnFreezeBailDto);
					}
				}else{
					logger.warn("unfreezeUserReplenishBail warn instanceId:{}, amountResult:{}",
							new Object[] { partnerInstanceId ,freezeAmount.getResult()});
				}
			} catch (Exception e) {
				logger.error("unfreezeUserReplenishBail error instanceId:{}",
						new Object[] { partnerInstanceId }, e);
			}
		}
		return resultModel;
	}

	    public ResultModel<Long> queryUserFreezeAmountNew(Long taobaoUserId, UserTypeEnum userTypeEnum) {
	        Assert.notNull(taobaoUserId);
	        Assert.notNull(userTypeEnum);
	        CuntaoBailBaseQueryDto queryDto =new CuntaoBailBaseQueryDto();
	        queryDto.setTaobaoUserId(taobaoUserId);
	        queryDto.setUserTypeEnum(userTypeEnum);
	        return cuntaoNewBailService.queryUserFreezeAmountNew(queryDto);
	    }
	 
	private Long getReplenishAmount(Long partnerInstanceId, ResultModel<Boolean> resultModel,
			ResultModel<Long> freezeAmount) {
		Long amount = 0L;
		try {
			amount = freezeAmount.getResult();
		} catch (Exception e) {
			logger.error("unfreezeUserReplenishBail getReplenishAmount error instanceId:{}",
					new Object[] { partnerInstanceId }, e);
			return amount;
		}
		return amount;
	}

	@Override
	public Boolean freezeUserReplenishBail(Long partnerInstanceId) {
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceId);
		if (accountMoney != null && AccountMoneyStateEnum.HAS_FROZEN.getCode().equals(accountMoney.getState().getCode())) {
			return Boolean.TRUE;
		}
		PartnerInstanceDto instance = partnerInstanceBO.getPartnerInstanceById(partnerInstanceId);
		ResultModel<Long> freezeAmount = queryUserFreezeAmountNew(instance.getTaobaoUserId(), UserTypeEnum.STORE);
		if (freezeAmount != null && freezeAmount.isSuccess() && freezeAmount.getResult() != null) {
			if (freezeAmount.getResult()>=300000) {
				if (instance == null || !PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState().getCode())) {
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"当前服务站必须装修中");
				}
				
				AccountMoneyDto baseMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
						AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, partnerInstanceId);
				if (baseMoney == null && ! AccountMoneyStateEnum.HAS_FROZEN.getCode().equals(accountMoney.getState().getCode())) {
					throw new AugeBusinessException(AugeErrorCodes.PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE,"基础保证金未冻结");
				}
				PartnerLifecycleItems decoItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
				        PartnerLifecycleBusinessTypeEnum.DECORATING, PartnerLifecycleCurrentStepEnum.PROCESSING);
				// 修改生命周期表
				if (null != decoItems){
					updateRelenishMoneyIsHasFrozen(instance.getTaobaoUserId().toString(), decoItems);
				}
				addHasFrozenReplienishMoney(instance.getId(),instance.getTaobaoUserId(),baseMoney.getAlipayAccount(),baseMoney.getAccountNo());
				return Boolean.TRUE;
			}
		}
		return Boolean.FALSE;
	}
	
	private void updateRelenishMoneyIsHasFrozen(String operator,
			PartnerLifecycleItems decoItems) {
		PartnerLifecycleDto lifecycleUpdateDto = new PartnerLifecycleDto();
		lifecycleUpdateDto.setLifecycleId(decoItems.getId());
		lifecycleUpdateDto.setReplenishMoney(PartnerLifecycleReplenishMoneyEnum.HAS_FROZEN);
		lifecycleUpdateDto.setOperator(operator);
		lifecycleUpdateDto.setOperatorType(OperatorTypeEnum.HAVANA);
		partnerLifecycleBO.updateLifecycle(lifecycleUpdateDto);
	}
	
	 private void addHasFrozenReplienishMoney(Long instanceId, Long taobaoUserId, String alipayAccount,String accountNo) {
	        ValidateUtils.notNull(instanceId);
	        AccountMoneyDto accountMoneyDto = new AccountMoneyDto();
	        accountMoneyDto.setMoney(new BigDecimal("3000.00"));
	        accountMoneyDto.setOperator(String.valueOf(taobaoUserId));
	        accountMoneyDto.setOperatorType(OperatorTypeEnum.HAVANA);
	        accountMoneyDto.setObjectId(instanceId);
	        accountMoneyDto.setState(AccountMoneyStateEnum.HAS_FROZEN);
	        accountMoneyDto.setTaobaoUserId(String.valueOf(taobaoUserId));
	        accountMoneyDto.setTargetType(AccountMoneyTargetTypeEnum.PARTNER_INSTANCE);
	        accountMoneyDto.setType(AccountMoneyTypeEnum.REPLENISH_MONEY);
	        accountMoneyDto.setFrozenTime(new Date());
	        accountMoneyDto.setAlipayAccount(alipayAccount);
	        accountMoneyDto.setAccountNo(accountNo);
	        
	        AccountMoneyDto dupRecord = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.REPLENISH_MONEY,
	                AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instanceId);
	        if (dupRecord != null) {
	            accountMoneyBO.updateAccountMoneyByObjectId(accountMoneyDto);
	        } else {
	            accountMoneyBO.addAccountMoney(accountMoneyDto);
	        }
	    }
}