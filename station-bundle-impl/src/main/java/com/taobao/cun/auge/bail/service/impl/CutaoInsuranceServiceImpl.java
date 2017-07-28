package com.taobao.cun.auge.bail.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alipay.baoxian.scene.facade.common.AliSceneResult;
import com.alipay.baoxian.scene.facade.common.policy.dto.InsPolicyDTO;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.insurance.CutaoInsuranceService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("icuntaoAlipayRelService")
@HSFProvider(serviceInterface = CutaoInsuranceService.class)
public class CutaoInsuranceServiceImpl implements CutaoInsuranceService{
    
    public static final String SP_NO = "6696";//合伙人保险编号
    
    public static final String SP_TYPE = "2";//合伙人保险类型
    @Autowired
    private PartnerTagService partnerTagService;
    @Autowired
    private PolicyQueryService policyQueryService;
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    private static final Logger logger = LoggerFactory
            .getLogger(CutaoInsuranceServiceImpl.class);

     public Boolean hasInsurance(Long taobaoUserId){
        if (diamondConfiguredProperties.getInSureSwitch().equals("false") || isInFactoryAlipayList(taobaoUserId)) {
           return true;
        }
        try {
            Integer identy = partnerTagService.getPartnerType(taobaoUserId);
            // 如果是合伙人要判断是否买过保险，淘帮手是不用强制买保险
            if (identy != null && identy.intValue() == 1) {
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


}
