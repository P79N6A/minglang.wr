package com.taobao.cun.auge.qualification.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.testuser.TestUserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("settlingService")
@HSFProvider(serviceInterface= C2BSettlingService.class)
public class C2BSettlingServiceImpl implements C2BSettlingService {

	private static final Logger logger = LoggerFactory.getLogger(C2BSettlingServiceImpl.class);
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	
	@Autowired
	private AccountMoneyBO accountMoneyBO;
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Autowired
	private TestUserService testUserService;
	
	@Autowired
	private PartnerInstanceService partnerInstanceService;
	
	@Autowired
	private CuntaoQualificationBO cuntaoQulificationBO;
	
	
	@Override
	public C2BSettlingResponse settlingStep(C2BSettlingRequest settlingStepRequest) {
		C2BSettlingResponse response = new C2BSettlingResponse();
		try {
			Assert.notNull(settlingStepRequest);
			Assert.notNull(settlingStepRequest.getTaobaoUserId());
			
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(settlingStepRequest.getTaobaoUserId());
			boolean testUser = isTestUser(parnterInstance.getTaobaoUserId());
			response.setTestUser(testUser);
			
			boolean isSignProcotol = this.hasC2BSignProcotol(settlingStepRequest.getTaobaoUserId());
			
			boolean isFrozenMoney = this.hasFrozenMoney(parnterInstance.getId());
			
			//没有冻结保证金就是要走新流程的用户
			response.setNewSettleUser(!isFrozenMoney);
			
			setStep(settlingStepRequest.getTaobaoUserId(),isSignProcotol,isFrozenMoney,response);
			response.setSuccessful(true);
			return response;
		} catch (Exception e) {
			logger.error("settlingStep error!",e);
			response.setErrorMessage("系统异常");
			response.setSuccessful(false);
			return response;
		}
	}

	private boolean isTestUser(Long taobaoUserId) {
		return this.testUserService.isTestUser(taobaoUserId, "c2b", true);
	}

	//???qualiInfoId是否是1，可能需要修改成村淘专用
	private void  setStep(Long taobaoUserId,boolean isSignProtocol,boolean isFrozenMoeny,C2BSettlingResponse response){
		
		CuntaoQualification  qualification = cuntaoQulificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
	//	boolean hasValidQuali = sellerQualiServiceAdapter.hasValidQuali(taobaoUserId);
		//没有有效资质，并且没有提交或审核记录或者最新一条审核记录是审核失败，跳提交资质页面
	//	UserQualiRecord lastAuditRecord = sellerQualiServiceAdapter.lastAuditQualiStatus(taobaoUserId);
	//	int lastAuditRecodStatus = lastAuditRecord.getStatus();
		if(qualification == null){
			response.setStep(C2BSettlingService.SUBMIT_AUTH_METERAIL);
			return;
		}
		if(qualification != null){
			response.setQualificationStatus(qualification.getStatus());
			response.setErrorCode(qualification.getErrorCode());
			if(qualification.getStatus() == QualificationStatus.AUDIT_FAIL){
				response.setErrorMessage(qualification.getErrorMessage());
			}
		}
		
		if(!isSignProtocol){
			response.setStep(C2BSettlingService.SIGN_PROTOCOL);
			return;
		}
		
		if(!isFrozenMoeny){
			response.setStep(C2BSettlingService.FRZONE_MONEY);
			return;
		}
		response.setStep(C2BSettlingService.ALL_DONE);
	}
	
	/**
	 * 是否签约新入住协议
	 * @param parnterInstanceId
	 * @return
	 */
	private boolean hasC2BSignProcotol(Long taobaoUserId){
		PartnerProtocolRelDto settleC2BProtocol = partnerProtocolRelBO.getLastPartnerProtocolRelDtoByTaobaoUserId(taobaoUserId,ProtocolTypeEnum.C2B_SETTLE_PRO,PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		return Optional.ofNullable(settleC2BProtocol).isPresent();
	}
	
	/**
	 * 是否冻结保证金
	 * @param parnterInstanceId
	 * @return
	 */
	private boolean hasFrozenMoney(Long parnterInstanceId){
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, parnterInstanceId);
		return accountMoney != null && AccountMoneyStateEnum.HAS_FROZEN.getCode().equals(accountMoney.getState().getCode());
	}
	
	
	

	@Override
	public C2BSignSettleProtocolResponse signC2BSettleProtocol(C2BSignSettleProtocolRequest c2bSignSettleProtocolRequest) {
		C2BSignSettleProtocolResponse response = new C2BSignSettleProtocolResponse();
		try {
			CuntaoQualification  qualification = cuntaoQulificationBO.getCuntaoQualificationByTaobaoUserId(c2bSignSettleProtocolRequest.getTaobaoUserId());
			if(qualification == null){
				response.setSuccessful(false);
				response.setErrorMessage("未提交认证资料");
				return response;
			}
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(c2bSignSettleProtocolRequest.getTaobaoUserId());

			boolean isSignC2BProcotol = this.hasC2BSignProcotol(parnterInstance.getTaobaoUserId());
			
			boolean isFrozenMoney = this.hasFrozenMoney(parnterInstance.getId());
			
			this.partnerInstanceService.signC2BSettledProtocol(c2bSignSettleProtocolRequest.getTaobaoUserId(), isSignC2BProcotol, isFrozenMoney);
			response.setSuccessful(true);
		} catch (Exception e) {
			logger.error("signNewSettleProtocol error!taobaoUserId["+c2bSignSettleProtocolRequest.getTaobaoUserId()+"]",e);
			response.setErrorMessage("系统异常");
			response.setSuccessful(false);
		}
			return response;
	}

}
