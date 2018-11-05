package com.taobao.cun.auge.bail.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.ali.unit.rule.util.StringUtils;
import com.alipay.baoxian.scene.facade.common.AliSceneResult;
import com.alipay.baoxian.scene.facade.common.policy.dto.InsPolicyDTO;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.alipay.insopenprod.common.service.facade.api.InsPolicyApiFacade;
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
import org.apache.commons.collections4.CollectionUtils;
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

    /**
     * 村淘人身意外险编号
     */
    private final static String  PAI_NO = "4025";

    /**
     * 保险白名单Group
     */
    private final static String WHITELIST_GROUP = "whiteList";

    /**
     * 保险白名单DataId
     */
    private final static String WHITELIST_DATAID = "insurance";

    private final static String SWHITCH_OFF = "false";

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
     * @param taobaoId
     * @return
     */
    private boolean isInFactoryAlipayList(Long taobaoId) {
        return DiamondHelper.getConfig(WHITELIST_GROUP, WHITELIST_DATAID).contains(String.valueOf(taobaoId));
    }

    /**
     * 查询是否购买过保险
     * @param taobaoUserId
     * @return
     */
     @Override
     public Boolean hasInsurance(Long taobaoUserId){
        if (SWHITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
            return true;
        }
        try {
            Integer identity = partnerTagService.getPartnerType(taobaoUserId);
            if(null != identity && identity != 1){
                //如果是合伙人要判断是否买过保险，淘帮手是不用强制买保险
                return true;
            }
            Partner partner =  partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
            if (null == partner || StringUtils.isEmpty(partner.getIdenNum())) {
                return false;
            }
            //一.先从老平台查询原数据,判断合伙人是否买过保险
            if(queryInsuranceFromOldPlatform(partner.getIdenNum())){
                return true;
            }
            
            //二.原始数据没有查询到则调用蚂蚁接口，判断是否买过保险
            if (queryInsuranceFromAlipay(partner.getIdenNum())) {
                return true;
            }
        } catch (Exception e) {
            logger.error("hasInsurance method execute error ,{taobaoUserId} = {}" ,taobaoUserId,e);
            return false;
        }
        return false;
    }

    private boolean queryInsuranceFromOldPlatform( String idenNum){
        Date nowTime = DateUtils.addDays(new Date(), 1);
        List<Partner> partners =  partnerBO.getPartnerByIdnum(idenNum);
        if(CollectionUtils.isNotEmpty(partners)){
            for(Partner p : partners){
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
     * @param idenNum
     * @return
     */
    private Boolean queryInsuranceFromAlipay(String idenNum) {
        InsPolicySearchRequest insRequest = new InsPolicySearchRequest();
        InsQueryPerson insQueryPerson = new InsQueryPerson();
        //1投保人，2被保人，村淘默认按照投保人维度查询
        insQueryPerson.setType("1");
        //证件号码
        insQueryPerson.setCertNo(idenNum);
        //证件类型，10身份证
        insQueryPerson.setCertType("10");
        insRequest.setPerson(insQueryPerson);
        //产品列表集合村淘合伙人意外险:4025 村淘雇主责任险 4027
        insRequest.setProductList( Lists.newArrayList(PAI_NO));
        //保单状态列表
        insRequest.setStatusList(Lists.newArrayList("GUARANTEE"));
        insRequest.setPageNo(1);
        insRequest.setPageSize(1);
        //投放渠道，必填，村淘默认cuntao
        insRequest.setChannel("cuntao");
        InsPolicySearchResult searchResult = insPolicyApiFacade.search(insRequest);
        //查询到有效保单
        return "Success".equals(searchResult.getErrorMessage())&&searchResult.getTotal()>0;
    }

    /**
     * 村小二保险续签
     * @param taobaoUserId
     * @return
     */
    @Override
    public  Integer hasReInsurance(Long taobaoUserId) {
        if (SWHITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(taobaoUserId)) {
            return 0;
        }
        try {
            Integer identity = partnerTagService.getPartnerType(taobaoUserId);
            // 如果是合伙人要判断是否买过保险，淘帮手是不用强制买保险
            if (identity != null && identity == 1) {
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

}
