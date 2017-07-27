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
import com.taobao.cun.admin.service.IcuntaoAlipayRelService;
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.common.exception.ServiceException;
import com.taobao.cun.common.resultmodel.ResultModel;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("icuntaoAlipayRelService")
@HSFProvider(serviceInterface = IcuntaoAlipayRelService.class)
public class IcuntaoAlipayRelServiceImpl implements IcuntaoAlipayRelService{
    
    public static final String SP_NO = "6696";//合伙人保险编号
    
    public static final String SP_TYPE = "2";//合伙人保险类型
    @Autowired
    private PartnerTagService partnerTagService;
    @Autowired
    private PolicyQueryService policyQueryService;
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;

    private static final Logger logger = LoggerFactory
            .getLogger(IcuntaoAlipayRelServiceImpl.class);

    public ResultModel<Boolean> hasAlipayInsure(Long taobaoUserId) {
        ResultModel<Boolean> rm = new ResultModel<Boolean>();
        rm.setSuccess(true);
        if (diamondConfiguredProperties.getInSureSwitch().equals("false") || isInFactoryAlipayList(taobaoUserId)) {
            rm.setResult(true);
            return rm;
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
                            rm.setResult(true);
                            return rm;
                        }
                    }
                }
            } else {
                rm.setResult(true);
                return rm;
            }
        } catch (Exception e) {
            logger.error("isInsure get error :" + e.getCause());
            rm.setSuccess(false);
            rm.setException(new ServiceException(e.getCause()));
            return rm;
        }
        rm.setResult(false);
        return rm;
    }
    
    /*村小二保险续签*/
    public ResultModel<Integer> hasReAlipayInsure(Long taobaoUserId) {
        ResultModel<Integer> rm = new ResultModel<Integer>();
        rm.setSuccess(true);
        if (diamondConfiguredProperties.getInSureSwitch().equals("false") || isInFactoryAlipayList(taobaoUserId)) {
            rm.setResult(0);
            return rm;
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
                                ){// 续保30天内提醒
                            rm.setResult(durDate);
                            //已经续保了不用提醒
                            for(InsPolicyDTO py : insure.getModel()){
                                if(py.getEffectStartTime().after(policy.getEffectEndTime())){
                                    rm.setResult(0);
                                }
                            }
                            return rm;
                        }
                    }
                }
            } else {
                rm.setResult(0);
                return rm;
            }
        } catch (Exception e) {
            logger.error("isInsure get error :" + e.getCause());
            rm.setSuccess(false);
            rm.setException(new ServiceException(e.getCause()));
            return rm;
        }
        rm.setResult(0);
        return rm;
    }

    public ResultModel<Boolean> hasActualVerify(Long taobaoUserId) {
        return null;
    }
    
    //企业支付宝白名单
    private boolean isInFactoryAlipayList(Long taobaoId) {
        return diamondConfiguredProperties.getInsureWhiteListConfig().contains(taobaoId);
    }
}
