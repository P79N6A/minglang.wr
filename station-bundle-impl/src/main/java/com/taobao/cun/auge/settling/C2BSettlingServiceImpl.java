package com.taobao.cun.auge.settling;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.pm.sc.api.quali.constants.UserQualiRecordStatus;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.taobao.cun.auge.common.TestUserContext;
import com.taobao.cun.auge.common.TestUserSupport;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.station.adapter.SellerQualiServiceAdapter;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("settlingService")
@RefreshScope
@HSFProvider(serviceInterface= C2BSettlingService.class)
public class C2BSettlingServiceImpl implements C2BSettlingService {

	private static final Logger logger = LoggerFactory.getLogger(C2BSettlingServiceImpl.class);
	@Autowired
	private PartnerInstanceBO partnerInstanceBO;
	@Autowired
	private StationBO stationBO;
	
	@Autowired
	private AccountMoneyBO accountMoneyBO;
	
	@Autowired
	private PartnerProtocolRelBO partnerProtocolRelBO;
	
	@Value("${c2bSettleProcotolId}")
	//B类用户新的入住协议ID
	private Long c2bSettleProcotolId;
	
	@Autowired
	private C2BTestUserConfig c2bTestUserConfig;
	
	@Autowired
	private TestUserSupport testUserSupport;
	
	@Autowired
	private SellerQualiServiceAdapter sellerQualiServiceAdapter;
	
	@Autowired
	private PartnerInstanceService partnerInstanceService;
	
	
	
	@Override
	public C2BSettlingResponse settlingStep(C2BSettlingRequest settlingStepRequest) {
		C2BSettlingResponse response = new C2BSettlingResponse();
		try {
			Assert.notNull(settlingStepRequest);
			Assert.notNull(settlingStepRequest.getTaobaoUserId());
			
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(settlingStepRequest.getTaobaoUserId());
			Station station = stationBO.getStationById(parnterInstance.getStationId());
			boolean testUser = isTestUser(parnterInstance, station);
			response.setTestUser(testUser);
			
			boolean isSignProcotol = this.hasC2BSignProcotol(parnterInstance.getId());
			
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

	private boolean isTestUser(PartnerStationRel parnterInstance, Station station) {
		TestUserContext context = new TestUserContext();
		context.setTaobaoUserId(parnterInstance.getTaobaoUserId());
		context.setOrgId(station.getApplyOrg());
		context.setUserType(parnterInstance.getType());
		context.setTestUserConfig(c2bTestUserConfig);
		
		boolean testUser = testUserSupport.setCurrentContext(context).isTestUserOrg(true).and().isTestTaobaoUser(false).and().isTestUserType(true).getResult();
		return testUser;
	}

	//???qualiInfoId是否是1，可能需要修改成村淘专用
	private void  setStep(Long taobaoUserId,boolean isSignProtocol,boolean isFrozenMoeny,C2BSettlingResponse response){
		boolean hasValidQuali = sellerQualiServiceAdapter.hasValidQuali(taobaoUserId);
		//没有有效资质，并且没有提交或审核记录或者最新一条审核记录是审核失败，跳提交资质页面
		UserQualiRecord lastAuditRecord = sellerQualiServiceAdapter.lastAuditQualiStatus(taobaoUserId);
		int lastAuditRecodStatus = lastAuditRecord.getStatus();
		if(!hasValidQuali && (lastAuditRecodStatus == SellerQualiServiceAdapter.NO_AUDIT_RECORD||lastAuditRecodStatus == UserQualiRecordStatus.AUDIT_PASS)){
			response.setStep(C2BSettlingService.SUBMIT_AUTH_METERAIL);
			return;
		}
		if(!hasValidQuali && (lastAuditRecodStatus == UserQualiRecordStatus.TO_BE_AUDITED||lastAuditRecodStatus == UserQualiRecordStatus.AUDIT_FAIL)){
			response.setStep(C2BSettlingService.AUDIT_AUTH_DETAIL);
			return;
		}
		if(hasValidQuali && !isSignProtocol){
			response.setStep(C2BSettlingService.SIGN_PROTOCOL);
			return;
		}
		
		if(hasValidQuali && isSignProtocol && !isFrozenMoeny){
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
	private boolean hasC2BSignProcotol(Long parnterInstanceId){
		PartnerProtocolRelDto partnerProtocolDto = partnerProtocolRelBO.getPartnerProtocolRelDto(parnterInstanceId, PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE,c2bSettleProcotolId);
		return Optional.ofNullable(partnerProtocolDto).isPresent();
	}
	
	/**
	 * 是否冻结保证金
	 * @param parnterInstanceId
	 * @return
	 */
	private boolean hasFrozenMoney(Long parnterInstanceId){
		AccountMoneyDto accountMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, parnterInstanceId);
		return accountMoney != null && AccountMoneyStateEnum.HAS_FROZEN.getCode().equals(accountMoney.getState());
	}
	
	
	

	@Override
	public C2BSignSettleProtocolResponse signC2BSettleProtocol(C2BSignSettleProtocolRequest newSettleProtocolRequest) {
		C2BSignSettleProtocolResponse response = new C2BSignSettleProtocolResponse();
		try {
			PartnerStationRel parnterInstance = partnerInstanceBO.getActivePartnerInstance(newSettleProtocolRequest.getTaobaoUserId());

			boolean isSignC2BProcotol = this.hasC2BSignProcotol(parnterInstance.getId());
			
			boolean isFrozenMoney = this.hasFrozenMoney(parnterInstance.getId());
			
			this.partnerInstanceService.signC2BSettledProtocol(newSettleProtocolRequest.getTaobaoUserId(), isSignC2BProcotol, isFrozenMoney);
			response.setSuccessful(true);
		} catch (Exception e) {
			logger.error("signNewSettleProtocol error!taobaoUserId["+newSettleProtocolRequest.getTaobaoUserId()+"]",e);
			response.setErrorMessage("系统异常");
			response.setSuccessful(false);
		}
			return response;
	}

}
