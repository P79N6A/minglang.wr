package com.taobao.cun.auge.qualification.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.cun.auge.testuser.TestUserService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("settlingService")
@HSFProvider(serviceInterface= C2BSettlingService.class)
@EnableConfigurationProperties({SettlingStepsProperties.class})
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
	
	@Autowired
	private SettlingStepsProperties settlingStepsProperties;
	
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	
	@Override
	public C2BSettlingResponse settlingStep(C2BSettlingRequest settlingStepRequest) {
		C2BSettlingResponse response = new C2BSettlingResponse();
		try {
			Assert.notNull(settlingStepRequest);
			Assert.notNull(settlingStepRequest.getTaobaoUserId());
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(settlingStepRequest.getTaobaoUserId());
			boolean testUser = isTestUser(parnterInstance.getTaobaoUserId());
			boolean isSignProcotol = this.hasC2BSignProcotol(parnterInstance.getId());
			
			boolean isFrozenMoney = this.hasFrozenMoney(parnterInstance.getId());
			
			//没有冻结保证金就是要走新流程的用户
			response.setNewSettleUser(!isFrozenMoney);
			response.setTestUser(testUser);
			if(settlingStepRequest.getVersion() == null){
				setStep(settlingStepRequest.getTaobaoUserId(),isSignProcotol,isFrozenMoney,response);
				response.setSuccessful(true);
				return response;
			}else{
				String stepNames = settlingStepsProperties.getVersion().get(settlingStepRequest.getVersion());
				Assert.notNull(stepNames);
				response.setStepNames(stepNames.split(","));
				setStepName(settlingStepRequest.getTaobaoUserId(),parnterInstance.getId(),isSignProcotol,isFrozenMoney,response);
				return response;
			}
			
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
	
	private void  setStepName(Long taobaoUserId,Long parnterInstanceId,boolean isSignProtocol,boolean isFrozenMoeny,C2BSettlingResponse response){
		
		CuntaoQualification  qualification = cuntaoQulificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId);
		if(qualification == null){
			response.setStepName("SUBMIT_AUTH_METERAIL");
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
			response.setStepName("SIGN_PROTOCOL");
			return;
		}
		PartnerLifecycleItems items = partnerLifecycleBO.getLifecycleItems(parnterInstanceId, PartnerLifecycleBusinessTypeEnum.SETTLING);
		if(items.getConfirmPosition() ==null ||"N".equals(items.getConfirmPosition())){
			response.setStepName("CONFIRM_POSITION");
			return;
		}
		
		if(!isFrozenMoeny){
			response.setStepName("FRZONE_MONEY");
			return;
		}
		response.setStepName("ALL_DONE");
	}



	/**
	 * 是否签约新入住协议
	 * @param parnterInstanceId
	 * @return
	 */
	private boolean hasC2BSignProcotol(Long parnterInstanceId){
		PartnerProtocolRelDto settleC2BProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.C2B_SETTLE_PRO, parnterInstanceId, PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
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
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(c2bSignSettleProtocolRequest.getTaobaoUserId());
			boolean isSignC2BProcotol = this.hasC2BSignProcotol(parnterInstance.getId());
			if(isSignC2BProcotol){
				response.setSuccessful(true);
				return response;
			}
			
			CuntaoQualification  qualification = cuntaoQulificationBO.getCuntaoQualificationByTaobaoUserId(c2bSignSettleProtocolRequest.getTaobaoUserId());
			if(qualification == null){
				response.setSuccessful(false);
				response.setErrorMessage("未提交认证资料");
				return response;
			}
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

	public SettlingStepsProperties getSettlingStepsProperties() {
		return settlingStepsProperties;
	}

	public void setSettlingStepsProperties(SettlingStepsProperties settlingStepsProperties) {
		this.settlingStepsProperties = settlingStepsProperties;
	}

}
