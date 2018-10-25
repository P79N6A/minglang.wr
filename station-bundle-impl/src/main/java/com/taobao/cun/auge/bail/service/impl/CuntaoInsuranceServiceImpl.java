package com.taobao.cun.auge.bail.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.alipay.baoxian.scene.facade.common.AliSceneResult;
import com.alipay.baoxian.scene.facade.common.policy.dto.InsPolicyDTO;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.insurance.CuntaoInsuranceService;
import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cuntaoInsuranceService")
@HSFProvider(serviceInterface = CuntaoInsuranceService.class)
public class CuntaoInsuranceServiceImpl implements CuntaoInsuranceService{

    /**
     * 合伙人保险编号
     */
    private final static String SP_NO = "6696";

    /**
     * 合伙人保险类型
     */
    private final static String SP_TYPE = "2";

    @Autowired
    private PartnerTagService partnerTagService;
    @Autowired
    private PolicyQueryService policyQueryService;
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    @Autowired
    private PartnerBO partnerBO;
    @Autowired
    private CuntaoQualificationBO qualificationBO;
    @Autowired
    private UicPaymentAccountReadServiceClient uicPaymentAccountReadServiceClient;

    private static final Logger logger = LoggerFactory.getLogger(CuntaoInsuranceServiceImpl.class);

    @Override
    public PersonInfoDto queryPersonInfo(Long taobaoUserId, String cpCode) {
        validateCpCode(cpCode);
        PersonInfoDto infoDto = new PersonInfoDto();
        ResultDO<BasePaymentAccountDO> resultDO = uicPaymentAccountReadServiceClient.getAccountByUserId(taobaoUserId);
        if (resultDO.isSuccess()) {
            infoDto.setAlipayAccount(resultDO.getModule().getOutUser());
        }
        return infoDto;
    }

    @Override
    public BusinessInfoDto queryBusinessInfo(Long taobaoUserId, String cpCode) {
        validateCpCode(cpCode);
        return Optional.ofNullable(qualificationBO.getCuntaoQualificationByTaobaoUserId(taobaoUserId))
                        .map(this::buildBusinessInfo)
                        .orElse(null);
    }

    private BusinessInfoDto buildBusinessInfo(CuntaoQualification qualification) {
        BusinessInfoDto infoDto = new BusinessInfoDto();
        infoDto.setName(qualification.getCompanyName());
        infoDto.setType(String.valueOf(qualification.getEnterpriceType()));
        infoDto.setIdenNum(qualification.getQualiNo());
        infoDto.setAddress(qualification.getRegsiterAddress());
        infoDto.setLegalPerson(qualification.getLegalPerson());
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(qualification.getTaobaoUserId());
        if (partner != null) {
            infoDto.setMobile(partner.getMobile());
        }
        return infoDto;
    }

    private void validateCpCode(String cpCode) {
        if (!diamondConfiguredProperties.getInsureCpCodes().contains(cpCode)) {
            throw new AugeBusinessException(AugeErrorCodes.CP_NOT_EXISTS_ERROR_CODE, "forbidden to access");
        }
    }

    @Override
     public Boolean hasInsurance(Long taobaoUserId){
        if (diamondConfiguredProperties.getInSureSwitch().equals("false") || isInFactoryAlipayList(taobaoUserId)) {
           return true;
        }
        try {
            Integer identy = partnerTagService.getPartnerType(taobaoUserId);
            // 如果是合伙人要判断是否买过保险，淘帮手是不用强制买保险
            if (identy != null && identy.intValue() == 1) {
                if(idenNumDupliInsure(taobaoUserId)){
                    return true;
                }
                AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService
                        .queryPolicyByInsured(String.valueOf(taobaoUserId),
                                SP_TYPE, SP_NO);
                if (insure.isSuccess() && insure.getModel() != null
                        && insure.getModel().size() > 0) {
                    Date nowTime = DateUtils.addDays(new Date(), 1);
                    for (InsPolicyDTO policy : insure.getModel()) {
                        if (nowTime.after(policy.getEffectStartTime())
                                && nowTime.before(policy.getEffectEndTime())) {// 投保中的保险
                           return true;
                        }
                    }
                }
            } else {
            	return true;
            }
        } catch (Exception e) {
            logger.error("isInsure get error :" + e.getCause());
            return false;
        }
        return false;
    }
    
    /*村小二保险续签*/
    @Override
    public  Integer hasReInsurance(Long taobaoUserId) {
        if (diamondConfiguredProperties.getInSureSwitch().equals("false") || isInFactoryAlipayList(taobaoUserId)) {
            return 0;
        }
        try {
            Integer identy = partnerTagService.getPartnerType(taobaoUserId);
            // 如果是合伙人要判断是否买过保险，淘帮手是不用强制买保险
            if (identy != null && identy.intValue() == 1) {
                // TODO支付宝接口判断是否买过保险
                AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService
                        .queryPolicyByInsured(String.valueOf(taobaoUserId),
                                SP_TYPE, SP_NO);
                if (insure.isSuccess() && insure.getModel() != null
                        && insure.getModel().size() > 0) {
                    Date nowTime = new Date();
                    for (InsPolicyDTO policy : insure.getModel()) {
                        int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                        if (nowTime.after(policy.getEffectStartTime())
                                && nowTime.before(policy.getEffectEndTime())
                                && !DateUtil.addDays(nowTime, 30).before(policy.getEffectEndTime())
                                ){
                            //已经续保了不用提醒
                            for(InsPolicyDTO py : insure.getModel()){
                                if(py.getEffectStartTime().after(policy.getEffectEndTime())){
                                   return 0;
                                }
                            }
                         // 续保30天内提醒
                            return durDate;
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("isInsure get error :" + e.getCause());
            return 0;
        }
		return 0;
    }

    
    //企业支付宝白名单
    private boolean isInFactoryAlipayList(Long taobaoId) {
        return diamondConfiguredProperties.getInsureWhiteListConfig().contains(taobaoId);
    }

    private boolean idenNumDupliInsure(Long taobaoUserId){
        Partner partner =  partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
        if(partner != null){
            List<Partner> partners =  partnerBO.getPartnerByIdnum(partner.getIdenNum());
            if(partners != null && partners.size() > 1){
                for(Partner p : partners){
                    AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService
                            .queryPolicyByInsured(String.valueOf(p.getTaobaoUserId()),
                                    SP_TYPE, SP_NO);
                    if (insure.isSuccess() && insure.getModel() != null
                            && insure.getModel().size() > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }



}
