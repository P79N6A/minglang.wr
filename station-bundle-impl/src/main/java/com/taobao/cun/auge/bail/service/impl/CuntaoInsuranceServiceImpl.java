package com.taobao.cun.auge.bail.service.impl;

import java.util.*;
import java.util.concurrent.*;

import com.alibaba.fastjson.JSON;

import com.ali.unit.rule.util.StringUtils;
import com.alipay.baoxian.scene.facade.common.AliSceneResult;
import com.alipay.baoxian.scene.facade.common.policy.dto.InsPolicyDTO;
import com.alipay.baoxian.scene.facade.common.policy.service.PolicyQueryService;
import com.alipay.insopenprod.common.service.facade.api.InsPolicyApiFacade;
import com.alipay.insopenprod.common.service.facade.api.InsSceneApiFacade;
import com.alipay.insopenprod.common.service.facade.model.common.InsPolicy;
import com.alipay.insopenprod.common.service.facade.model.common.InsQueryPerson;
import com.alipay.insopenprod.common.service.facade.model.enums.InsAgreementStatusEnum;
import com.alipay.insopenprod.common.service.facade.model.enums.InsSignTypeEnum;
import com.alipay.insopenprod.common.service.facade.model.enums.SignUserTypeEnum;
import com.alipay.insopenprod.common.service.facade.model.request.scene.InsPolicySearchRequest;
import com.alipay.insopenprod.common.service.facade.model.request.scene.InsProductAgreementQryRequest;
import com.alipay.insopenprod.common.service.facade.model.result.scene.InsPolicySearchResult;
import com.alipay.insopenprod.common.service.facade.model.result.scene.InsProductAgreementResult;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.configuration.DiamondFactory;
import com.taobao.cun.auge.dal.domain.*;
import com.taobao.cun.auge.dal.mapper.PartnerInsuranceDetailMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.insurance.CuntaoInsuranceService;
import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;
import com.taobao.cun.auge.insurance.enums.InsuranceStateEnum;
import com.taobao.cun.auge.station.bo.CuntaoQualificationBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.StationStatusEnum;
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
     * 雇主险编号
     */
    private final static String EMPLOYER_NO = "4027";

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
    private InsSceneApiFacade insSceneApiFacade;

    @Autowired
    private PartnerInstanceBO partnerInstanceBO;

    @Autowired
    private PartnerStationRelMapper partnerStationRelMapper;

    @Autowired
    private PartnerInsuranceDetailMapper partnerInsuranceDetailMapper;

    @Autowired
    private StationBO stationBo;

    private ExecutorService executorService = new ThreadPoolExecutor(5,30,60000, TimeUnit.MILLISECONDS,new ArrayBlockingQueue<>(200));

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
        return DiamondFactory.getInsuranceDiamondConfig().contains(String.valueOf(taobaoId));
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
                if (partnerInstance.getState().equals(StationStatusEnum.CLOSED.getCode())) {
                    //如果站点状态是已退出的，则返回买过保险
                    return true;
                }
                if (null == partner || StringUtils.isEmpty(partner.getIdenNum())) {
                    return false;
                }
                //一.先从老平台查询原数据,判断合伙人是否买过保险
                if (queryInsuranceFromOldPlatform(partner.getIdenNum())) {
                    return true;
                }
                //二.原始数据没有查询到则调用蚂蚁接口，判断是否买过保险
                InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(), "INEFFECTIVE_OR_GUARANTEE");
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
    private InsPolicySearchResult queryInsuranceFromAlipay(String idenNum, String status) {
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
        //保单状态
        if (status != null) {
            insRequest.setStatus(status);
        }
        insRequest.setPageNo(1);
        insRequest.setPageSize(20);
        //投放渠道，必填，村淘默认cuntao
        insRequest.setChannel("cuntao");
        return insPolicyApiFacade.search(insRequest);
    }

    /**
     * 村小二保险续签（新老平台查询兼容）
     * 0：表示购买过保险且日期大于限定日期
     * >0:保险有效期天数
     *
     * @param taobaoUserId
     * @return
     */
    @Override
    public Integer hasReInsurance(Long taobaoUserId) {
        try {
            if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(
                    taobaoUserId)) {
                return 0;
            }
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (partnerInstance == null || !PartnerInstanceTypeEnum.isTpOrTps(partnerInstance.getType())) {
                //只有合伙人和淘帮手才强制买保险
                return 0;
            }
            // 查询新平台的保险数据
            Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
            //查询未生效或在保障中的保单
            InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(), "INEFFECTIVE_OR_GUARANTEE");
            Date nowTime = new Date();

            //1.新平台数据判断
            if (searchResult != null && searchResult.isSuccess() && searchResult.getTotal() > 0) {
                //因为是已买保险之后才会调用该接口
                int maxDurDate = 0;
                for (InsPolicy policy : searchResult.getPolicys()) {
                    //找到距离保险止期最大的值
                    maxDurDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime()) > maxDurDate ? DateUtil
                            .daysBetween(nowTime, policy.getEffectEndTime()) : maxDurDate;
                }
                if (maxDurDate > insuranceExpiredDay) {
                    return 0;
                } else {
                    return maxDurDate;
                }
            }

            // 2.查询老平台保险数据
            AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                    String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
            if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
                //因为是已买保险之后才会调用该接口
                int maxDurDate = 0;
                for (InsPolicyDTO policyDto : insure.getModel()) {
                    //找到距离保险止期最大的值
                    maxDurDate = DateUtil.daysBetween(nowTime, policyDto.getEffectEndTime()) > maxDurDate ? DateUtil
                            .daysBetween(nowTime, policyDto.getEffectEndTime()) : maxDurDate;
                }
                if (maxDurDate > insuranceExpiredDay) {
                    return 0;
                } else {
                    return maxDurDate;
                }
            }
        } catch (Exception ex) {
            logger.error("query insurance effective day error,{taobaoUserId}", taobaoUserId, ex);
            //异常当作买过保险处理
            return 0;
        }
        //买过保险处理
        return 0;
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
            if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(
                    taobaoUserId)) {
                return 365;
            }
            PartnerStationRel partnerInstance = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
            if (partnerInstance == null || !PartnerInstanceTypeEnum.isTpOrTps(partnerInstance.getType())
                    || partnerInstance.getState().equals(StationStatusEnum.CLOSED.getCode())) {
                //只有合伙人和淘帮手才强制买保险,同时站点已经退出的也返回已买保险
                return 365;
            }
            // 查询新平台的保险数据
            Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
            if (partner == null || StringUtils.isBlank(partner.getIdenNum())) {
                //设置为没买保险
                return 0;
            }
            //查询未生效或在保障中的保单
            InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(), "INEFFECTIVE_OR_GUARANTEE");
            Date nowTime = new Date();
            //1.新平台数据判断
            if (searchResult.isSuccess() && CollectionUtils.isNotEmpty(searchResult.getPolicys())) {
                //约定给-10（事实上，此处可以给任意负整数）
                int maxDurDate = -10;
                for (InsPolicy policy : searchResult.getPolicys()) {
                    //找到距离保险止期最大的
                    maxDurDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime()) > maxDurDate ? DateUtil
                            .daysBetween(nowTime, policy.getEffectEndTime()) : maxDurDate;
                }
                //<0，新平台的保险都过期了
                return maxDurDate < 0 ? 0 : maxDurDate;

            }

            // 2.查询老平台保险数据
            AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                    String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
            if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
                //约定给-10（事实上，此处可以给任意负整数）
                int maxDurDate = -10;
                for (InsPolicyDTO policyDto : insure.getModel()) {
                    maxDurDate = DateUtil.daysBetween(nowTime, policyDto.getEffectEndTime()) > maxDurDate ? DateUtil
                            .daysBetween(nowTime, policyDto.getEffectEndTime()) : maxDurDate;
                }
                //如果保单过期了，即maxDurDate<0
                return maxDurDate < 0 ? 0 : maxDurDate;
            }
        } catch (Exception ex) {
            logger.error("query insurance effective day error,{taobaoUserId}", taobaoUserId, ex);
            //异常当作买过保险处理
            return 365;
        }
        //没买过保险
        return 0;
    }

    @Override
    public Boolean hasEmployerInsurance(Long taobaoUserId) {
        if (SWITCH_OFF.equals(diamondConfiguredProperties.getInSureSwitch()) || isInFactoryAlipayList(
                taobaoUserId)) {
            return true;
        }
        InsProductAgreementQryRequest request = new InsProductAgreementQryRequest();
        request.setItemId(EMPLOYER_NO);
        request.setChannel("cuntao");
        request.setSignType(InsSignTypeEnum.CONTRACT_WITHHOLD_FOR_ORDER.getKey());
        request.setSignUserType(SignUserTypeEnum.TAOBAO.getCode());
        request.setSignUserId(String.valueOf(taobaoUserId));
        ResultDO<BasePaymentAccountDO> resultDO = uicPaymentAccountReadServiceClient.getAccountByUserId(taobaoUserId);
        if (resultDO.isSuccess()) {
            request.setAlipayUserId(cut0156ForAlipayAccount(resultDO.getModule().getAccountNo()));
        } else {
            logger.error("{bizType} query {taobaoUserId} alipay user id error", "insurance", taobaoUserId, resultDO.getErrMsg());
            return false;
        }
        InsProductAgreementResult result = insSceneApiFacade.queryProductAgreement(request);
        if (result.isSuccess() && InsAgreementStatusEnum.EFFECTIVE.getCode().equals(result.getStatus())) {
            return true;
        } else if (!result.isSuccess()) {
            logger.info("{bizType} {parameter}", "insurance", JSON.toJSONString(request));
            logger.error("{bizType} query {taobaoUserId} employer insurance error", "insurance", taobaoUserId, JSON.toJSONString(result));
        } else {
            logger.info("{bizType} {parameter}", "insurance", JSON.toJSONString(request));
            logger.info("{bizType} query {taobaoUserId} employer status:" + result.getStatus(), "insurance", taobaoUserId);
        }
        return false;
    }

    private static String cut0156ForAlipayAccount(String partnerAlipayUserId) {
        if (partnerAlipayUserId.endsWith("0156")) {
            return partnerAlipayUserId.substring(0, partnerAlipayUserId.length() - 4);
        }
        return partnerAlipayUserId;
    }

    @Override
    public void countInsuranceData(int pageSize) {
        //物理删除之前的数据
        int deletedCount = partnerInsuranceDetailMapper.deleteByExample(null);
        logger.info("delete from partner_insurance_detail ,count = {}", deletedCount);
        int pageNum = 1;
        int handledCount = 0 ;
        PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andIsCurrentEqualTo("y")
                .andTypeIn(Lists.newArrayList("TP","TPS")).andStateIn(Lists.newArrayList("DECORATING","SERVICING","CLOSING"));
        long startTime = System.currentTimeMillis();
        while (true) {
            PageHelper.startPage(pageNum++, pageSize,"id asc");
            List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
            handledCount += resList.size();
            logger.info("query partner_station_rel data ,count = {} ，id = {}", handledCount,resList.get(resList.size()-1).getId());
            for (PartnerStationRel partnerStationRel : resList) {

               /* while(true){
                    try {
                        Future<?> future = executorService.submit(() -> {
                        });
                        //阻塞，改成单线程
                        //future.get();
                    } catch (RejectedExecutionException e) {
                        continue;
                    } catch (Exception e) {
                        logger.info("buildPartnerInsuranceDetail error  {}",e);
                        break;
                    }
                    break;
                }*/
                buildPartnerInsuranceDetailList(partnerStationRel.getTaobaoUserId(), partnerStationRel.getStationId(), null, partnerStationRel.getState());

            }

            if (resList.size()<pageSize) {
                break;
            }

        }
        logger.info("count insurance data costs = {} ms ,handledCount count = {} ",System.currentTimeMillis()-startTime,handledCount);
    }


    private void buildPartnerInsuranceDetailList(Long taobaoUserId, Long stationId, String stationName,String state) {

        // 查询新平台的保险数据
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
        if (partner == null || StringUtils.isBlank(partner.getIdenNum())) {
            return ;
        }
        boolean flag = false;

        List<PartnerInsuranceDetail> resultList = new ArrayList<PartnerInsuranceDetail>();
        Date nowTime = new Date();
        InsPolicySearchResult searchResult = queryInsuranceFromAlipay(partner.getIdenNum(), "GUARANTEE");

        //1.查询新平台数据
        if (searchResult.isSuccess() && CollectionUtils.isNotEmpty(searchResult.getPolicys())) {
            for (InsPolicy policy : searchResult.getPolicys()) {
                //找到距离保险止期最大的
                int durDate = DateUtil.daysBetween(nowTime, policy.getEffectEndTime()) ;
                PartnerInsuranceDetail insuranceDetail = buildPartnerInsuranceDetail(taobaoUserId, partner.getIdenNum(),
                        policy.getEffectStartTime(),policy.getEffectEndTime(),policy.getPolicyStatus(),Long.valueOf(durDate),partner.getName(),stationId,stationName,"n",state);
                resultList.add(insuranceDetail);
            }
            flag = true;
            //如果已经在新平台购买过保险了，那么就不再去查询老平台保险
            //return resultList;
        }
        // 2.查询老平台保险数据
        AliSceneResult<List<InsPolicyDTO>> insure = policyQueryService.queryPolicyByInsured(
                String.valueOf(taobaoUserId), SP_TYPE, SP_NO);
        if (insure.isSuccess() && insure.getModel() != null && insure.getModel().size() > 0) {
            InsPolicyDTO insPolicyDTO = null ;
            int durDate = -10000;
            for (InsPolicyDTO insPolicyDTONew : insure.getModel()) {
                int newDurDate = DateUtil.daysBetween(nowTime, insPolicyDTONew.getEffectEndTime());
                if(newDurDate > durDate ){
                    durDate = newDurDate;
                    insPolicyDTO = insPolicyDTONew;
                }

            }
            if(insPolicyDTO != null){
                PartnerInsuranceDetail insuranceDetail = buildPartnerInsuranceDetail(taobaoUserId, partner.getIdenNum(),
                        insPolicyDTO.getEffectStartTime(),insPolicyDTO.getEffectEndTime(), InsuranceStateEnum.valueof(Integer.valueOf(insPolicyDTO.getStatus())).getDesc(),Long.valueOf(durDate),partner.getName(),stationId,stationName,"y",state);
                resultList.add(insuranceDetail);
            }else{
                PartnerInsuranceDetail insuranceDetail = buildPartnerInsuranceDetail(taobaoUserId, partner.getIdenNum(),
                        null,null, null,Long.valueOf(durDate),partner.getName(),stationId,stationName,"y",state);
                resultList.add(insuranceDetail);
            }
            flag = true;

           // return resultList;
        }

        if(!flag){

            PartnerInsuranceDetail insuranceDetail = buildPartnerInsuranceDetail(taobaoUserId, partner.getIdenNum(),
                    null,null, null,-10000L,partner.getName(),stationId,stationName,"x",state);
            resultList.add(insuranceDetail);
        }

        //return resultList;

        resultList.forEach(insuranceDetail->{

            partnerInsuranceDetailMapper.insert(insuranceDetail);

        });


    }




    private PartnerInsuranceDetail buildPartnerInsuranceDetail(Long taobaoUserId, String idenNum,Date effectStartTime,Date effectEndTime,String status,Long expiredDay ,String parnterName,Long stationId,String stationName,String isOld,String state) {
        Station station = stationBo.getStationById(stationId);
        PartnerInsuranceDetail insuranceDetail = new PartnerInsuranceDetail();
        insuranceDetail.setTaobaoUserId(taobaoUserId);
        insuranceDetail.setIdenNum(idenNum);
        insuranceDetail.setEffectStartTime(effectStartTime);
        insuranceDetail.setEffectEndTime(effectEndTime);
        insuranceDetail.setCreator("system");
        insuranceDetail.setGmtCreate(new Date());
        insuranceDetail.setGmtModified(new Date());
        insuranceDetail.setExpiredDay(expiredDay);
        insuranceDetail.setIsOld(isOld);
        insuranceDetail.setStatus(status);
        insuranceDetail.setType(state);
        insuranceDetail.setIsDeleted("n");
        insuranceDetail.setName(parnterName);
        insuranceDetail.setStationId(stationId);
        insuranceDetail.setStationName(station.getName());
        insuranceDetail.setOrgId(station.getApplyOrg());
        insuranceDetail.setExtInfo(expiredDay<=0?"n":"y");
        return insuranceDetail;
    }

}