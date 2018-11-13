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
import com.taobao.cun.ar.scene.station.service.PartnerTagService;
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
import org.springframework.stereotype.Service;

@Service("cuntaoInsuranceService")
@HSFProvider(serviceInterface = CuntaoInsuranceService.class)
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
            if (partnerInstance != null && PartnerInstanceTypeEnum.TP.getCode().equals(partnerInstance.getType())) {
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
        //证件类型，线下10:身份证 线上100:身份证
        insQueryPerson.setCertType("10");
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
        if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
            return 0;
        }
        try {
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (partnerInstance != null && PartnerInstanceTypeEnum.TP.getCode().equals(partnerInstance.getType())) {
                Date nowTime = new Date();
                // 查询新平台的保险数据
                Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
                InsPolicySearchResult searchResult = null;
                if (partner != null) {
                    //查询未生效或在保障中的保单
                    searchResult = queryInsuranceFromAlipay(partner.getIdenNum(),
                        Lists.newArrayList("INEFFECTIVE_OR_GUARANTEE"));
                }
                //1.新平台数据判断
                if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                    for (InsPolicy policy : searchResult.getPolicys()) {
                        int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                        if (nowTime.after(policy.getEffectStartTime())
                            && nowTime.before(policy.getEffectEndTime())
                            && !DateUtil.addDays(nowTime, 30).before(policy.getEffectEndTime())
                            ) {
                            // 已经续保了不用提醒
                            for (InsPolicy py : searchResult.getPolicys()) {
                                if (py.getEffectStartTime().after(policy.getEffectEndTime())) {
                                    return 0;
                                }
                            }
                            // 续保30天内提醒
                            return durDate;
                        }
                    }
                    return 0;
                }

                // 2.查询老平台保险数据
                AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                    String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
                if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
                    for (InsPolicyDTO policy : insure.getModel()) {
                        int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                        if (nowTime.after(policy.getEffectStartTime())
                            && nowTime.before(policy.getEffectEndTime())
                            && !DateUtil.addDays(nowTime, 30).before(policy.getEffectEndTime())
                            ) {
                            //1.老平台数据单独判断 已经续保了不用提醒
                            for (InsPolicyDTO oldPy : insure.getModel()) {
                                if (oldPy.getEffectStartTime().after(policy.getEffectEndTime())) {
                                    return 0;
                                }
                            }
                            //3.老、新台数据结合判断 已经续保了不用提醒
                            if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                                for (InsPolicy newPy : searchResult.getPolicys()) {
                                    if (newPy.getEffectStartTime().after(policy.getEffectEndTime())) {
                                        return 0;
                                    }
                                }
                            }
                            // 续保30天内提醒
                            return durDate;
                        }
                    }
                    return 0;
                }

            }
        } catch (Exception e) {
            logger.error("isInsure get error,taobaoUserId = {}", taobaoUserId, e);
            //异常按有效期大于30天处理
            return 0;
        }
        return 0;
    }

    @Override
    public Map<String, Object> hasInsuranceForMobile(Long taobaoUserId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
            resultMap.put("hasInsurance", true);
            resultMap.put("effectiveDay", 0);
            return resultMap;
        }
        Integer identity = partnerTagService.getPartnerType(taobaoUserId);
        if (null == identity || identity != 1) {
            //只有合伙人才强制买保险
            resultMap.put("hasInsurance", true);
            resultMap.put("effectiveDay", 0);
            return resultMap;

        }
        // 查询新平台的保险数据
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
        if (partner == null || StringUtils.isBlank(partner.getIdenNum())) {
            resultMap.put("hasInsurance", false);
            return resultMap;

        }
        //查询未生效或在保障中的保单
        InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(),
            Lists.newArrayList("INEFFECTIVE_OR_GUARANTEE"));

        resultMap.put("hasInsurance", false);
        if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
            for (InsPolicy insPolicy : searchResult.getPolicys()) {
                if ("GUARANTEE".equals(insPolicy.getPolicyStatus())) {
                    resultMap.put("hasInsurance", true);
                }
            }
        }
        if (queryInsuranceFromOldPlatform(partner.getIdenNum())) {
            resultMap.put("hasInsurance", true);
        }
        Date nowTime = new Date();
        //1.新平台数据判断
        if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
            for (InsPolicy policy : searchResult.getPolicys()) {
                int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime());
                if (nowTime.after(policy.getEffectStartTime())
                    && nowTime.before(policy.getEffectEndTime())
                    && !DateUtil.addDays(nowTime, 30).before(policy.getEffectEndTime())
                    ) {
                    // 已经续保了不用提醒
                    for (InsPolicy py : searchResult.getPolicys()) {
                        if (py.getEffectStartTime().after(policy.getEffectEndTime())) {
                            resultMap.put("effectiveDay", 0);
                            return resultMap;
                        }
                    }
                    // 续保30天内提醒
                    resultMap.put("effectiveDay", durDate);
                    return resultMap;
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
                    && !DateUtil.addDays(nowTime, 30).before(policy.getEffectEndTime())
                    ) {
                    //1.老平台数据单独判断 已经续保了不用提醒
                    for (InsPolicyDTO oldPy : insure.getModel()) {
                        if (oldPy.getEffectStartTime().after(policy.getEffectEndTime())) {
                            resultMap.put("effectiveDay", 0);
                            return resultMap;
                        }
                    }
                    //3.老、新台数据结合判断 已经续保了不用提醒
                    if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                        for (InsPolicy newPy : searchResult.getPolicys()) {
                            if (newPy.getEffectStartTime().after(policy.getEffectEndTime())) {
                                resultMap.put("effectiveDay", 0);
                                return resultMap;
                            }
                        }
                    }
                    // 续保30天内提醒
                    resultMap.put("effectiveDay", durDate);
                    return resultMap;
                }
            }
        }
        resultMap.put("effectiveDay", 0);
        return resultMap;
    }

}
