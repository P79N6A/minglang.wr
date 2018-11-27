package com.taobao.cun.auge.bail.service.impl;

import java.util.*;

import com.ali.unit.rule.util.StringUtils;
import com.alipay.baoxian.scene.facade.common.AliSceneResult;
import com.alipay.baoxian.scene.facade.common.policy.dto.InsPolicyDTO;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.alipay.insopenprod.common.service.facade.api.InsPolicyApiFacade;
import com.alipay.insopenprod.common.service.facade.model.common.InsPolicy;
import com.alipay.insopenprod.common.service.facade.model.common.InsQueryPerson;
import com.alipay.insopenprod.common.service.facade.model.request.scene.InsPolicySearchRequest;
import com.alipay.insopenprod.common.service.facade.model.result.scene.InsPolicySearchResult;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.DiamondHelper;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.insurance.CuntaoInsuranceService;
import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.uic.common.domain.BasePaymentAccountDO;
import com.taobao.uic.common.domain.ResultDO;
import com.taobao.uic.common.service.userinfo.client.UicPaymentAccountReadServiceClient;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

@Service("cuntaoInsuranceService")
@HSFProvider(serviceInterface = CuntaoInsuranceService.class)
@RefreshScope
public class CuntaoInsuranceServiceImpl implements CuntaoInsuranceService {

    /**
     * 合伙人保险编号
     */
    private final static String SP_NO = "6696";

    /**
     * 合伙人保险类型
     */
    private final static String SP_TYPE = "2";

    /**
     * 村淘人身意外险编号
     */
    private final static String PAI_NO = "4025";

    /**
     * 保险白名单Group
     */
    private final static String WHITELIST_GROUP = "whiteList";

    /**
     * 保险白名单DataId
     */
    private final static String WHITELIST_DATAID = "insurance";

    private final static String SWITCH_OFF = "false";

    @Value("${insurance.expired.day}")
    private Integer insuranceExpiredDay;

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

    @Autowired
    private InsPolicyApiFacade insPolicyApiFacade;
    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

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
            infoDto.setIdenCard(partner.getIdenNum());
        }
        return infoDto;
    }

    private void validateCpCode(String cpCode) {
        if (!diamondConfiguredProperties.getInsureCpCodes().contains(cpCode)) {
            throw new AugeBusinessException(AugeErrorCodes.CP_NOT_EXISTS_ERROR_CODE, "forbidden to access");
        }
    }

    /**
     * 企业支付宝白名单
     *
     * @param taobaoId
     * @return
     */
    private boolean isInFactoryAlipayList(Long taobaoId) {
        return DiamondHelper.getConfig(WHITELIST_GROUP, WHITELIST_DATAID).contains(String.valueOf(taobaoId));
    }

    /**
     * 查询是否购买过保险
     *
     * @param taobaoUserId
     * @return
     */
    @Override
    public Boolean hasInsurance(Long taobaoUserId) {
        if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
            return true;
        }
        try {
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (partnerInstance != null && PartnerInstanceTypeEnum.isTpOrTps(partnerInstance.getType())) {
                Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
                if (null == partner || StringUtils.isEmpty(partner.getIdenNum())) {
                    return false;
                }
                //一.先从老平台查询原数据,判断合伙人是否买过保险
                if (queryInsuranceFromOldPlatform(partner.getIdenNum())) {
                    return true;
                }
                //二.原始数据没有查询到则调用蚂蚁接口，判断是否买过保险
                InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(),
                    Lists.newArrayList("GUARANTEE"));
                if (searchResult.isSuccess() && searchResult.getTotal() > 0) {
                    return true;
                }
            } else {
                return true;
            }
        } catch (Exception e) {
            logger.error("hasInsurance method execute error ,{taobaoUserId} = {}", taobaoUserId, e);
            //异常按购买保险处理
            return true;
        }
        return false;
    }

    private boolean queryInsuranceFromOldPlatform(String idenNum) {
        Date nowTime = DateUtils.addDays(new Date(), 1);
        List<Partner> partners = partnerBO.getPartnerByIdnum(idenNum);
        if (CollectionUtils.isNotEmpty(partners)) {
            for (Partner p : partners) {
                AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService
                    .queryPolicyByInsured(String.valueOf(p.getTaobaoUserId()),
                        SP_TYPE, SP_NO);
                if (insure.isSuccess() && CollectionUtils.isNotEmpty(insure.getModel())) {
                    for (InsPolicyDTO policy : insure.getModel()) {
                        if (nowTime.after(policy.getEffectStartTime()) && nowTime.before(policy.getEffectEndTime())) {
                            // 投保中的保险
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * 从蚂蚁保险查询是否购买保险
     *
     * @param idenNum
     * @return
     */
    private InsPolicySearchResult queryInsuranceFromAlipay(String idenNum, List<String> statusList) {
        InsPolicySearchRequest insRequest = new InsPolicySearchRequest();
        InsQueryPerson insQueryPerson = new InsQueryPerson();
        //1投保人，2被保人，村淘默认按照投保人维度查询
        insQueryPerson.setType("1");
        //证件号码
        insQueryPerson.setCertNo(idenNum);
        //证件类型，100:身份证
        insQueryPerson.setCertType("100");
        insRequest.setPerson(insQueryPerson);
        //产品列表集合村淘合伙人意外险:4025
        insRequest.setProductList(Lists.newArrayList(PAI_NO));
        //保单状态列表
        insRequest.setStatusList(statusList);
        insRequest.setPageNo(1);
        insRequest.setPageSize(20);
        //投放渠道，必填，村淘默认cuntao
        insRequest.setChannel("cuntao");
        return insPolicyApiFacade.search(insRequest);
    }

    /**
     * 村小二保险续签（新老平台查询兼容）
     *
     * @param taobaoUserId
     * @return
     */
    @Override
    public Integer hasReInsurance(Long taobaoUserId) {
        {
            try {
                if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
                    return 0;
                }
                PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
                if (partnerInstance == null && !PartnerInstanceTypeEnum.isTpOrTps(partnerInstance.getType())) {
                    //只有合伙人和淘帮手才强制买保险
                    return 0;
                }
                // 查询新平台的保险数据
                Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
                //查询未生效或在保障中的保单
                InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(),
                        Lists.newArrayList("INEFFECTIVE_OR_GUARANTEE"));
                Date nowTime = new Date();
                //1.新平台数据判断
                if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                    for (InsPolicy policy : searchResult.getPolicys()) {
                        int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                        if(nowTime.after(policy.getEffectStartTime()) && nowTime.before(policy.getEffectEndTime())&&DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())){
                            //1.1保险有效期超过限定天数，返回有效天数
                            return 0;
                        }
                        if (nowTime.after(policy.getEffectStartTime())
                                && nowTime.before(policy.getEffectEndTime())
                                && !DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())
                        ) {

                            for (InsPolicy py : searchResult.getPolicys()) {
                                if ( DateUtil.daysBetween(nowTime, py.getEffectStartTime()) <= durDate) {
                                    // 1.2保单有效天数小于限定天数，但是已经续保,返回有效天数
                                    return 0;
                                }
                            }
                            //1.3 保单有效天数小于限定天数，且没有续保
                            return durDate;
                        }
                    }
                }

                // 2.查询老平台保险数据
                AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                        String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
                if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
                    for (InsPolicyDTO policy : insure.getModel()) {
                        int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                        if (nowTime.after(policy.getEffectStartTime())
                                && nowTime.before(policy.getEffectEndTime())
                                && DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())){
                            //2.1保险有效期超过限定天数，返回有效天数
                            return 0;
                        }
                        if (nowTime.after(policy.getEffectStartTime())
                                && nowTime.before(policy.getEffectEndTime())
                                && !DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())) {
                            for (InsPolicyDTO oldPy : insure.getModel()) {
                                //2.2老平台数据单独判断:保单有效天数小于限定天数，但是老平台已经续保,返回有效天数
                                if (DateUtil.daysBetween(nowTime,oldPy.getEffectStartTime()) <= durDate) {
                                    return 0;
                                }
                            }
                            if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                                for (InsPolicy newPy : searchResult.getPolicys()) {
                                    //2.3老、新台数据结合判断：保单有效天数小于限定天数，但是新平台已经续保,返回有效天数
                                    if (DateUtil.daysBetween(nowTime,newPy.getEffectStartTime()) <= durDate) {
                                        return 0;
                                    }
                                }
                            }
                            // 2.4保险有效期小于限定期，且没有续保
                            return durDate;
                        }
                    }
                }
            }catch (Exception ex){
                logger.error("query insurance effective day error,{taobaoUserId}",taobaoUserId,ex);
                //异常当作买过保险处理
                return 0;
            }
            //续保
            return 0;
        }
    }

    /**
     * 保单起期 ：投保完成之日次日零时，精确到秒
     * 保险止期：保险止期+1年-1秒，精确到秒
     * 保险期间：1年整
     * 保险生效时间：同保险起期
     *
     * @param taobaoUserId
     * @return
     */
    @Override
    public Integer hasInsuranceForMobile(Long taobaoUserId) {
        try {
            if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
                return 365;
            }
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (partnerInstance == null && !PartnerInstanceTypeEnum.isTpOrTps(partnerInstance.getType())) {
                //只有合伙人和淘帮手才强制买保险
                return 365;
            }
            // 查询新平台的保险数据
            Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
            if (partner == null || StringUtils.isBlank(partner.getIdenNum())) {
                //设置为没买保险
                return 0;
            }
            //查询未生效或在保障中的保单
            InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(),
                    Lists.newArrayList("INEFFECTIVE_OR_GUARANTEE"));
            Date nowTime = new Date();
            //1.新平台数据判断
            if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                for (InsPolicy policy : searchResult.getPolicys()) {
                    int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                    if(nowTime.after(policy.getEffectStartTime()) && nowTime.before(policy.getEffectEndTime())&&DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())){
                        //1.1保险有效期超过限定天数，返回有效天数
                        return durDate;
                    }
                    if (nowTime.after(policy.getEffectStartTime())
                            && nowTime.before(policy.getEffectEndTime())
                            && !DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())
                    ) {

                        for (InsPolicy py : searchResult.getPolicys()) {
                            if ( DateUtil.daysBetween(nowTime, py.getEffectStartTime()) <= durDate) {
                                // 1.2保单有效天数小于限定天数，但是已经续保,返回有效天数
                                return DateUtil.daysBetween(nowTime, py.getEffectEndTime());
                            }
                        }
                        //1.3 保单有效天数小于限定天数，且没有续保
                        return durDate;
                    }
                }
            }

            // 2.查询老平台保险数据
            AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                    String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
            if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
                for (InsPolicyDTO policy : insure.getModel()) {
                    int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                    if (nowTime.after(policy.getEffectStartTime())
                            && nowTime.before(policy.getEffectEndTime())
                            && DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())){
                        //2.1保险有效期超过限定天数，返回有效天数
                        return durDate;
                    }
                    if (nowTime.after(policy.getEffectStartTime())
                            && nowTime.before(policy.getEffectEndTime())
                            && !DateUtil.addDays(nowTime, insuranceExpiredDay).before(policy.getEffectEndTime())) {
                        for (InsPolicyDTO oldPy : insure.getModel()) {
                            //2.2老平台数据单独判断:保单有效天数小于限定天数，但是老平台已经续保,返回有效天数
                            if (DateUtil.daysBetween(nowTime,oldPy.getEffectStartTime()) <= durDate) {
                                return DateUtil.daysBetween(nowTime,oldPy.getEffectEndTime());
                            }
                        }
                        if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                            for (InsPolicy newPy : searchResult.getPolicys()) {
                                //2.3老、新台数据结合判断：保单有效天数小于限定天数，但是新平台已经续保,返回有效天数
                                if (DateUtil.daysBetween(nowTime,newPy.getEffectStartTime()) <= durDate) {
                                    return DateUtil.daysBetween(nowTime,newPy.getEffectEndTime());
                                }
                            }
                        }
                        // 2.4保险有效期小于限定期，且没有续保
                        return durDate;
                    }
                }
            }
        }catch (Exception ex){
            logger.error("query insurance effective day error,{taobaoUserId}",taobaoUserId,ex);
            //异常当作买过保险处理
            return 365;
        }
        //没买过保险
        return 0;
    }

}
