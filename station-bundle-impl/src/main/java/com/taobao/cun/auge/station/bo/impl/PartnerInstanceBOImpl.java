package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.alibaba.havana.oneid.api.HavanaResult;
import com.alibaba.havana.oneid.api.model.OuterInfo;
import com.alibaba.havana.oneid.client.api.AccountWriteServiceClient;
import com.taobao.sm.openshop.domain.OpenShopDO;
import com.taobao.sm.openshop.domain.Result;
import com.taobao.sm.openshop.service.OpenShopService;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taobao.cun.auge.alilang.UserProfile;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DateUtil;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.DiamondConfiguredProperties;
import com.taobao.cun.auge.dal.domain.CriusTaskExecute;
import com.taobao.cun.auge.dal.domain.CriusTaskExecuteExample;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerExample;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.*;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample.Criteria;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationExample;
import com.taobao.cun.auge.dal.mapper.CriusTaskExecuteMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.log.BizActionEnum;
import com.taobao.cun.auge.log.BizActionLogDto;
import com.taobao.cun.auge.log.bo.BizActionLogBo;
import com.taobao.cun.auge.lx.dto.LxPartnerDto;
import com.taobao.cun.auge.monitor.BusinessMonitorBO;
import com.taobao.cun.auge.qualification.service.CuntaoQualificationService;
import com.taobao.cun.auge.qualification.service.Qualification;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.*;
import com.taobao.cun.auge.station.enums.InstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceIsCurrentEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTransStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.cun.auge.station.service.PartnerAdzoneService;
import com.taobao.cun.auge.station.util.DateTimeUtil;
import com.taobao.cun.auge.store.bo.StoreReadBO;
import com.taobao.cun.auge.store.dto.StoreDto;
import com.taobao.cun.auge.store.service.StoreWriteService;
import com.taobao.cun.auge.tag.UserTag;
import com.taobao.cun.auge.tag.service.UserTagService;
import com.taobao.sellerservice.core.client.domain.ShopDO;
import com.taobao.sellerservice.core.client.shopmirror.MirrorBusiType;
import com.taobao.sellerservice.core.client.shopmirror.MirrorRights;
import com.taobao.sellerservice.core.client.shopmirror.MirrorSellerDO;
import com.taobao.sellerservice.core.client.shopmirror.ShopMirrorService;
import com.taobao.sellerservice.domain.ResultDO;
import com.taobao.tddl.client.sequence.impl.GroupSequence;
import com.tmall.usc.channel.client.UscChannelRelationService;
import com.tmall.usc.channel.client.dto.distributor.*;
import com.tmall.usc.support.common.dto.ResultDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.util.*;

@Component("partnerInstanceBO")
public class PartnerInstanceBOImpl implements PartnerInstanceBO {

    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceBO.class);

    @Autowired
    PartnerMapper partnerMapper;
    @Autowired
    StationMapper stationMapper;


    @Autowired
    PartnerStationRelMapper partnerStationRelMapper;

    @Autowired
    PartnerLifecycleBO partnerLifecycleBO;

    @Autowired
    PartnerBO partnerBO;

    @Autowired
    StationBO stationBO;
    @Autowired
    PartnerStationRelExtMapper partnerStationRelExtMapper;
    @Autowired
    CriusTaskExecuteMapper criusTaskExecuteMapper;
    @Autowired
    private ShopMirrorService shopMirrorService;
    @Autowired
    private UscChannelRelationService uscChannelRelationService;
    @Autowired
    private CuntaoQualificationService cuntaoQualificationService;
    @Autowired
    private DiamondConfiguredProperties diamondConfiguredProperties;
    @Autowired
    private StoreReadBO storeReadBO;
    
    @Autowired
    private StoreWriteService storeWriteService;
    @Autowired
    private UserTagService userTagService;
    @Autowired
    private BusinessMonitorBO businessMonitorBO;
    @Autowired
	@Qualifier("distributeChannelCodeSequence")
	private GroupSequence groupSequence;
    @Autowired
    private BizActionLogBo bizActionLogBo;
    @Autowired
	private PartnerAdzoneService partnerAdzoneService;

    @Autowired
    private AccountWriteServiceClient accountWriteServiceClient;

    @Autowired
    private OpenShopService openShopService;

    @Override
    public PartnerStationRel getPartnerInstanceByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState)
    {
        ValidateUtils.notNull(taobaoUserId);
        ValidateUtils.notNull(instanceState);
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsDeletedEqualTo("n");
        criteria.andStateEqualTo(instanceState.getCode());
        List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(instances)) {
            return null;
        }
        return instances.get(0);

    }

    @Override
    public Long getInstanceIdByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState) {
        PartnerStationRel rel = getPartnerInstanceByTaobaoUserId(taobaoUserId, instanceState);
        if (rel != null) {
            return rel.getId();
        }
        return null;
    }

    @Override
    public int findChildPartners(Long instanceId, PartnerInstanceStateEnum state) {
        PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
        Long parentStationId = curPartnerInstance.getStationId();

        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();

        criteria.andParentStationIdEqualTo(parentStationId);
        criteria.andIsDeletedEqualTo("n");
        criteria.andStateEqualTo(state.getCode());

        return partnerStationRelMapper.countByExample(example);
    }

    @Override
    public List<PartnerStationRel> findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums) {
        PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
        Long parentStationId = curPartnerInstance.getStationId();

        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();
        if (CollectionUtils.isNotEmpty(stateEnums)) {
            List<String> states = Lists.transform(stateEnums, new Function<PartnerInstanceStateEnum, String>() {
                @Override
                public String apply(PartnerInstanceStateEnum input) {
                    return input.getCode();
                }
            });
            criteria.andStateIn(states);
        }

        criteria.andParentStationIdEqualTo(parentStationId);
        criteria.andIsDeletedEqualTo("n");

        // 排除自己
        //criteria.andTypeNotEqualTo(curPartnerInstance.getType());
        criteria.andTypeNotIn(Lists.newArrayList(curPartnerInstance.getType(),"UM"));

        return partnerStationRelMapper.selectByExample(example);
    }

    @Override
    public List<PartnerStationRel> findPartnerInstances(Long stationId) {
        if (null == stationId) {
            return Collections.<PartnerStationRel> emptyList();
        }
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();

        criteria.andStationIdEqualTo(stationId);
        criteria.andIsDeletedEqualTo("n");

        //按照主键倒序
        example.setOrderByClause("id DESC");

        return partnerStationRelMapper.selectByExample(example);
    }

    @Override
    public PartnerStationRel findLastClosePartnerInstance(Long stationId) {
        if (null == stationId) {
            return null;
        }
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();

        criteria.andStationIdEqualTo(stationId);
        criteria.andIsDeletedEqualTo("n");

        //按照服务结束时间倒序
        example.setOrderByClause("service_end_time DESC");

        List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);

        return ResultUtils.selectOne(instances);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState, String operator)
    {
        PartnerStationRel partnerInstance = findPartnerInstanceById(instanceId);

        if (!preState.getCode().equals(partnerInstance.getState())) {
            logger.error("partner instance state is not " + preState.getDesc());
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"partner instance state is not " + preState.getDesc());
        }

        PartnerStationRel updateInstance = new PartnerStationRel();
        updateInstance.setId(instanceId);
        updateInstance.setState(postState.getCode());

        DomainUtils.beforeUpdate(updateInstance, operator);

        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);

    }

    @Override
    public PartnerStationRel findPartnerInstanceById(Long instanceId) {
        PartnerStationRel partnerInstance = partnerStationRelMapper.selectByPrimaryKey(instanceId);
        if (null == partnerInstance) {
            logger.error("partner instance is not exist.instance id " + instanceId);
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"partner instance is not exist.instance id " + instanceId);
        }
        return partnerInstance;
    }

    @Override
    public Long findStationIdByInstanceId(Long instanceId) {
        PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
        return curPartnerInstance.getStationId();
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updatePartnerStationRel(PartnerInstanceDto partnerInstanceDto) {
        ValidateUtils.validateParam(partnerInstanceDto);
        ValidateUtils.notNull(partnerInstanceDto.getId());
        // ValidateUtils.notNull(partnerInstanceDto.getVersion());
        PartnerStationRel rel = PartnerInstanceConverter.convert(partnerInstanceDto);
        DomainUtils.beforeUpdate(rel, partnerInstanceDto.getOperator());

        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();

        criteria.andIdEqualTo(partnerInstanceDto.getId());
        criteria.andIsDeletedEqualTo("n");
        if (partnerInstanceDto.getVersion() != null) {
            rel.setVersion(rel.getVersion() + 1L);
            criteria.andVersionEqualTo(partnerInstanceDto.getVersion());
        }

        int updateCount = partnerStationRelMapper.updateByExampleSelective(rel, example);

        if (updateCount < 1) {
            throw new AugeBusinessException(AugeErrorCodes.CONCURRENT_UPDATE_ERROR_CODE,"当前数据有更新，请刷新页面重试");
        }
    }

    @Override
    public PartnerInstanceDto getPartnerInstanceById(Long instanceId) {
        PartnerStationRel psRel = findPartnerInstanceById(instanceId);
        Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
        Station station = stationBO.getStationById(psRel.getStationId());

        return PartnerInstanceConverter.convert(psRel, station, partner);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void updateOpenDate(Long instanceId, Date openDate, String operator) {
        ValidateUtils.notNull(instanceId);
        ValidateUtils.notNull(operator);
        PartnerStationRel partnerStationRel = partnerStationRelMapper.selectByPrimaryKey(instanceId);
        if (partnerStationRel == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"PartnerInstanceId is null instanceId["+instanceId+"]");
        }
        partnerStationRel.setOpenDate(openDate);
        DomainUtils.beforeUpdate(partnerStationRel, operator);
        partnerStationRelMapper.updateByPrimaryKeySelective(partnerStationRel);
        
        //记录操作日志
        Station station = stationBO.getStationById(partnerStationRel.getStationId());
        BizActionLogDto bizActionLogAddDto = new BizActionLogDto();
        bizActionLogAddDto.setBizActionEnum(BizActionEnum.station_open);
        bizActionLogAddDto.setObjectId(partnerStationRel.getStationId());
        bizActionLogAddDto.setObjectType("station");
        bizActionLogAddDto.setOpOrgId(station.getApplyOrg());
        bizActionLogAddDto.setOpWorkId(operator);
        bizActionLogAddDto.setValue1(String.valueOf(partnerStationRel.getTaobaoUserId()));
        bizActionLogAddDto.setDept("opdept");
        bizActionLogBo.addLog(bizActionLogAddDto);
    }

    @Override
    public Long findPartnerInstanceIdByStationId(Long stationId) {
        PartnerStationRel rel = findPartnerInstanceByStationId(stationId);
        if (null == rel) {
            logger.info("partner instance is not exist.stationId " + stationId);
            throw new AugeBusinessException(AugeErrorCodes.STATION_BUSINESS_CHECK_ERROR_CODE,"partner instance is not exist.stationId " + stationId);
        }
        return rel.getId();
    }

    @Override
    public PartnerStationRel findPartnerInstanceByStationId(Long stationId) {
        ValidateUtils.notNull(stationId);
        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();

        criteria.andStationIdEqualTo(stationId);
        criteria.andIsCurrentEqualTo("y");
        criteria.andIsDeletedEqualTo("n");

        List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(instances)) {
            return null;
        }
        return instances.get(0);
    }

    @Override
    public List<PartnerStationRel> getPartnerStationRelByPartnerId(Long partnerId, String isCurrent) {
        ValidateUtils.notNull(partnerId);
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andPartnerIdEqualTo(partnerId);
        criteria.andIsCurrentEqualTo(isCurrent);
        criteria.andIsDeletedEqualTo("n");
        return partnerStationRelMapper.selectByExample(example);
    }

    @Override
    public List<PartnerStationRel> getPartnerStationRelByStationId(Long stationId, String isCurrent) {
        ValidateUtils.notNull(stationId);
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andStationIdEqualTo(stationId);
        criteria.andIsCurrentEqualTo(isCurrent);
        criteria.andIsDeletedEqualTo("n");
        return partnerStationRelMapper.selectByExample(example);
    }

    public List<PartnerStationRel> findPartnerInstanceByPartnerId(Long partnerId, List<String> states) {
        ValidateUtils.notNull(partnerId);
        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();

        criteria.andPartnerIdEqualTo(partnerId);
        criteria.andIsDeletedEqualTo("n");
        criteria.andStateIn(states);

        return partnerStationRelMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto){
        ValidateUtils.validateParam(partnerInstanceDto);
        PartnerStationRel partnerStationRel = PartnerInstanceConverter.convert(partnerInstanceDto);

        String operator = partnerInstanceDto.getOperator();
        //设置合伙人的历史服务站  is_current 为n
        setIsCurrentToNForParnter(partnerStationRel.getTaobaoUserId(),operator);

        //设置上一个合伙人 当前服务站所属关系为N
        setIsCurrentToN(partnerStationRel.getStationId(),operator);

        DomainUtils.beforeInsert(partnerStationRel, operator);
        partnerStationRelMapper.insert(partnerStationRel);
        return partnerStationRel.getId();
    }

    private void setIsCurrentToNForParnter(Long taobaoUserId,String operator) {

        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId).andIsCurrentEqualTo(PartnerInstanceIsCurrentEnum.Y.getCode());
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        if (resList != null && resList.size()>0) {
            for (PartnerStationRel rel: resList) {
                PartnerStationRel updateInstance = new PartnerStationRel();
                updateInstance.setIsCurrent(PartnerInstanceIsCurrentEnum.N.getCode());
                updateInstance.setId(rel.getId());
                DomainUtils.beforeUpdate(updateInstance, operator);
                partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
            }
        }
    }


    private void setIsCurrentToN(Long stationId,String operator) {

        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n").andStationIdEqualTo(stationId).andIsCurrentEqualTo(PartnerInstanceIsCurrentEnum.Y.getCode());
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        if (resList != null && resList.size()>0) {
            for (PartnerStationRel rel: resList) {
                PartnerStationRel updateInstance = new PartnerStationRel();
                updateInstance.setIsCurrent(PartnerInstanceIsCurrentEnum.N.getCode());
                updateInstance.setId(rel.getId());
                DomainUtils.beforeUpdate(updateInstance, operator);
                partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
            }
        }
    }

    @Override
    public boolean checkSettleQualification(Long taobaoUserId) {
        Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
        if (partner == null || partner.getId() == null) {
            return true;
        }

        List<PartnerStationRel> instatnceList = findPartnerInstanceByPartnerId(partner.getId(),
            PartnerInstanceStateEnum.unReSettlableStatusCodeList());

        if (CollectionUtils.isEmpty(instatnceList)) {
            return true;
        }
        for (PartnerStationRel rel : instatnceList) {
            if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(), rel.getState())) {
                return false;
            } else {
                PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.QUITING);
                if (null != item && PartnerLifecycleItemCheckResultEnum.EXECUTED.equals(PartnerLifecycleRuleParser
                    .parseExecutable(PartnerInstanceTypeEnum.valueof(rel.getType()), PartnerLifecycleItemCheckEnum.roleApprove, item))) {
                    continue;
                }

                //				if (StringUtils.equals(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode(), item.getCurrentStep())) {
                //					continue;
                //				}
                return false;
            }

        }
        return true;
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
    public void deletePartnerStationRel(Long instanceId, String operator) {
        PartnerStationRel rel = new PartnerStationRel();
        rel.setId(instanceId);
        DomainUtils.beforeDelete(rel, operator);
        partnerStationRelMapper.updateByPrimaryKeySelective(rel);
    }

    @Override
    public List<Long> getWaitOpenStationList(int fetchNum) {
        if (fetchNum < 0) {
            return null;
        }

        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();
        criteria.andIsDeletedEqualTo("n");
        criteria.andOpenDateLessThanOrEqualTo(new Date());
        criteria.andStateEqualTo(PartnerInstanceStateEnum.DECORATING.getCode());

        PageHelper.startPage(1, fetchNum);
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return null;
        }
        List<Long> instanceIdList = new ArrayList<Long>();
        for (PartnerStationRel rel : resList) {
            instanceIdList.add(rel.getId());
        }
        return instanceIdList;
    }

    @Override
    public List<Long> getWaitThawMoneyList(int fetchNum) {
        if (fetchNum < 0) {
            return null;
        }

        Map<String, Object> param = new HashMap<String, Object>();
        param.put("partnerState", PartnerInstanceStateEnum.QUITING.getCode());
        param.put("currentStep", PartnerLifecycleCurrentStepEnum.PROCESSING.getCode());
        param.put("businessType", PartnerLifecycleBusinessTypeEnum.QUITING.getCode());
        param.put("roleApprove", PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode());
        param.put("bond", PartnerLifecycleBondEnum.WAIT_THAW.getCode());

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 30);// 30天前的数据

        param.put("serviceEndTime", calendar.getTime());

        PageHelper.startPage(1, fetchNum);
        List<Long> instanceIdList = partnerStationRelExtMapper.getWaitThawMoney(param);
        if (CollectionUtils.isEmpty(instanceIdList)) {
            return instanceIdList;
        }
        Set<Long> idSet = Sets.newHashSet();
        for (Long id : instanceIdList) {
            CriusTaskExecuteExample example = new CriusTaskExecuteExample();
            example.createCriteria().andIsDeletedEqualTo("n").andBusinessTypeEqualTo(TaskBusinessTypeEnum.PARTNER_INSTANCE_QUIT.getCode())
                .andBusinessNoEqualTo(String.valueOf(id));
            List<CriusTaskExecute> existTaskList = criusTaskExecuteMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(existTaskList)) {
                idSet.add(id);
            }
        }
        List<Long> returnList = Lists.newArrayList(idSet);
        return returnList;
    }

    @Override
    public PartnerStationRel getActivePartnerInstance(Long taobaoUserId) {
        PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andTaobaoUserIdEqualTo(taobaoUserId)
            .andStateIn(PartnerInstanceStateEnum.unReSettlableStatusCodeList());
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return null;
        }
        for (PartnerStationRel rel : resList) {
            if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(), rel.getState())) {
                return rel;
            } else {
                PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.QUITING);
                if (null != item && PartnerLifecycleItemCheckResultEnum.EXECUTED.equals(PartnerLifecycleRuleParser
                    .parseExecutable(PartnerInstanceTypeEnum.valueof(rel.getType()), PartnerLifecycleItemCheckEnum.roleApprove, item))) {
                    continue;
                }
                return rel;
            }

        }
        return null;
    }

    @Override
    public int getActiveTpaByParentStationId(Long parentStationId) {
        int count = 0;
        PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andParentStationIdEqualTo(parentStationId)
            .andTypeEqualTo(PartnerInstanceTypeEnum.TPA.getCode()).andStateIn(PartnerInstanceStateEnum.getValidTpaStatusArray());
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(resList)) {
            return count;
        }
        for (PartnerStationRel rel : resList) {
            if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(), rel.getState())) {
                count++;
            } else {
                PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.QUITING);
                if (null != item && PartnerLifecycleItemCheckResultEnum.EXECUTED.equals(PartnerLifecycleRuleParser
                    .parseExecutable(PartnerInstanceTypeEnum.valueof(rel.getType()), PartnerLifecycleItemCheckEnum.roleApprove, item))) {
                    continue;
                }
                //				if (null != item && StringUtils.equals(PartnerLifecycleCurrentStepEnum.PROCESSING.getCode(), item.getCurrentStep())) {
                //					continue;
                //				}
                count++;
            }

        }
        return count;
    }

    @Override
    public void finishCourse(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRel rel = this.getActivePartnerInstance(taobaoUserId);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"getActivePartnerInstance is null:taobaoUserId["+taobaoUserId+"]");
        }
        partnerLifecycleBO.updateCourseState(rel.getId(), PartnerLifecycleCourseStatusEnum.Y, OperatorDto.defaultOperator());
    }

    @Override
    public boolean isAllPartnerQuit(Long stationId) 	{
        List<PartnerStationRel> instances = findPartnerInstances(stationId);

        return isAllPartnerQuit(instances);
    }

    private boolean isAllPartnerQuit(List<PartnerStationRel> instances) {
        for (PartnerStationRel instance : instances) {
            if (null == instance) {
                continue;
            }
            // 退出
            if (PartnerInstanceStateEnum.QUIT.getCode().equals(instance.getState())) {
                continue;
            }

            // 退出申请中
            if (PartnerInstanceStateEnum.QUITING.getCode().equals(instance.getState())) {
                PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(instance.getId(),
                    PartnerLifecycleBusinessTypeEnum.QUITING, PartnerLifecycleCurrentStepEnum.PROCESSING);

                // 退出待解冻
                if (PartnerLifecycleRoleApproveEnum.AUDIT_PASS.getCode().equals(item.getRoleApprove())
                    && PartnerLifecycleBondEnum.WAIT_THAW.getCode().equals(item.getBond())) {
                    continue;
                }
                return false;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isOtherPartnerQuit(Long instanceId) {
        PartnerStationRel partnerInstance = findPartnerInstanceById(instanceId);

        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();

        criteria.andStationIdEqualTo(partnerInstance.getStationId());
        criteria.andIsDeletedEqualTo("n");
        criteria.andIdNotEqualTo(instanceId);

        List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);

        return isAllPartnerQuit(instances);
    }

    @Override
    public PartnerInstanceDto getCurrentPartnerInstanceByPartnerId(Long partnerId) {
        List<PartnerStationRel> psRels = getPartnerStationRelByPartnerId(partnerId, PartnerInstanceIsCurrentEnum.Y.getCode());
        if (psRels.size() < 1){
            return null;
        }
        //原则上系统只允许存在一条这样的数据
        PartnerStationRel psRel = psRels.get(0);
        Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
        Station station = stationBO.getStationById(psRel.getStationId());
        return PartnerInstanceConverter.convert(psRel, station, partner);
    }

    @Override
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByPartnerId(Long partnerId) {
        List<PartnerStationRel> psRels = getPartnerStationRelByPartnerId(partnerId, PartnerInstanceIsCurrentEnum.N.getCode());
        if (psRels.size() < 1){
            return null;
        }
        List<PartnerInstanceDto> partnerInstanceDtos = new ArrayList<>(psRels.size());
        for (PartnerStationRel psRel : psRels){
            if(null == psRel){
                continue;
            }
            Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
            Station station = stationBO.getStationById(psRel.getStationId());
            partnerInstanceDtos.add(PartnerInstanceConverter.convert(psRel, station, partner));
        }
        return partnerInstanceDtos;
    }

    @Override
    public List<PartnerInstanceDto> getHistoryPartnerInstanceByStationId(
        Long stationId) {
        List<PartnerStationRel> psRels = getPartnerStationRelByStationId(stationId, PartnerInstanceIsCurrentEnum.N.getCode());
        if (psRels.size() < 1){
            return null;
        }
        List<PartnerInstanceDto> partnerInstanceDtos = new ArrayList<>();
        for (PartnerStationRel psRel : psRels){
            Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
            Station station = stationBO.getStationById(psRel.getStationId());

            PartnerInstanceDto insDto = PartnerInstanceConverter.convert(psRel, station, partner);

            // 获得生命周期数据
            PartnerLifecycleDto lifecycleDto = PartnerLifecycleConverter
                .toPartnerLifecycleDto(getLifecycleItemforHisPartner(psRel.getId(), psRel.getState()));
            insDto.setPartnerLifecycleDto(lifecycleDto);
            insDto.setStationApplyState(
                PartnerLifecycleRuleParser.parseStationApplyState(psRel.getType(), psRel.getState(), lifecycleDto));
            partnerInstanceDtos.add(insDto);
        }
        return partnerInstanceDtos;
    }

    private PartnerLifecycleItems getLifecycleItemforHisPartner(Long id, String state) {
        if (PartnerInstanceStateEnum.QUITING.getCode().equals(state)) {
            return partnerLifecycleBO.getLifecycleItems(id, PartnerLifecycleBusinessTypeEnum.QUITING);
        }
        return null;
    }

    @Override
    public void reService(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState, String operator) {
        changeState(instanceId, preState, postState, operator);
        PartnerStationRel updateInstance = new PartnerStationRel();
        updateInstance.setId(instanceId);
        updateInstance.setCloseType("");
        DomainUtils.beforeUpdate(updateInstance, operator);
        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
    }

    @Override
    public PartnerStationRel getCurrentPartnerInstanceByTaobaoUserId(
        Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria criteria = example.createCriteria();
        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsCurrentEqualTo(PartnerInstanceIsCurrentEnum.Y.getCode());
        criteria.andIsDeletedEqualTo("n");
        return ResultUtils.selectOne(partnerStationRelMapper.selectByExample(example));
    }

    @Override
    public void updateIsCurrentByInstanceId(Long instanceId,
                                            PartnerInstanceIsCurrentEnum isCurrentEnum)
    {
        PartnerStationRel updateInstance = new PartnerStationRel();
        updateInstance.setIsCurrent(isCurrentEnum.getCode());
        updateInstance.setId(instanceId);
        DomainUtils.beforeUpdate(updateInstance, OperatorDto.defaultOperator().getOperator());
        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);

    }

    @Override
    public PartnerInstanceDto getLastPartnerInstance(Long taobaoUserId) {
        ValidateUtils.notNull(taobaoUserId);

        PartnerStationRelExample example = new PartnerStationRelExample();
        // 按照服务结束时间倒序
        example.setOrderByClause("service_end_time DESC");

        Criteria criteria = example.createCriteria();

        criteria.andTaobaoUserIdEqualTo(taobaoUserId);
        criteria.andIsCurrentEqualTo(PartnerInstanceIsCurrentEnum.N.getCode());
        criteria.andIsDeletedEqualTo("n");

        return PartnerInstanceConverter
            .convert(ResultUtils.selectOne(partnerStationRelMapper.selectByExample(example)));
    }

    @Override
    public List<PartnerStationRel> getBatchActivePartnerInstance(
        List<Long> taobaoUserId,List<String> instanceType,List<String> statusList) {
        if(taobaoUserId.size()==0){
            return new ArrayList<PartnerStationRel>();
        }
        PartnerStationRelExample example = new PartnerStationRelExample();
        Criteria c=example.createCriteria();
        c.andIsDeletedEqualTo("n").andTaobaoUserIdIn(taobaoUserId).andIsCurrentEqualTo("y");
        if(instanceType!=null&&instanceType.size()>0){
            c.andTypeIn(instanceType);
        }
        if(statusList!=null&&statusList.size()>0){
            c.andStateIn(statusList);
        }
        List<PartnerStationRel> resList = partnerStationRelMapper.selectByExample(example);
        return resList;
    }

    @Override
    public List<UserProfile> queryUserProfileForAlilangMeeting(Long orgId,
                                                               String name) {
        Map<String,Object> param=new HashMap<String,Object>();
        param.put("orgId", orgId);
        param.put("name", name);
        return partnerStationRelMapper.queryUserProfileForAlilangMeeting(param);
    }

    @Override
    public Boolean judgeMobileUseble(Long taobaoUserId,Long partnerId, String mobile) {
        Assert.notNull(mobile);
        List<String> types = new ArrayList<String>();
        types.add(PartnerInstanceTypeEnum.TP.getCode());
        types.add(PartnerInstanceTypeEnum.TPA.getCode());
        types.add(PartnerInstanceTypeEnum.TPT.getCode());
        types.add(PartnerInstanceTypeEnum.UM.getCode());
        List<String> statuses = new ArrayList<String>();
        statuses.add(PartnerInstanceStateEnum.DECORATING.getCode());
        statuses.add(PartnerInstanceStateEnum.SERVICING.getCode());
        statuses.add(PartnerInstanceStateEnum.SETTLING.getCode());
        statuses.add(PartnerInstanceStateEnum.QUITING.getCode());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("mobile", mobile);
        param.put("instanceTypes", types);
        param.put("statusLists", statuses);
        List<PartnerStationRel> rels = partnerStationRelMapper
            .getInstanceForMobileJudge(param);
        if(taobaoUserId != null){
            for (PartnerStationRel p : rels) {
                if (p.getTaobaoUserId().compareTo(taobaoUserId) != 0) {
                    return false;
                }
            }
        }else if(partnerId !=null){
            for (PartnerStationRel p : rels) {
                if (p.getPartnerId().compareTo(partnerId) != 0) {
                    return false;
                }
            }
        }else{
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE,"user id is null");

        }
        return true;
    }


    @Override
    public List<PartnerStationRel> queryTpaPartnerInstances(Long parentStationId){
        PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andParentStationIdEqualTo(parentStationId).andTypeEqualTo(PartnerInstanceTypeEnum.TPA.getCode());
        return partnerStationRelMapper.selectByExample(example);
    }

    @Override
    public List<PartnerStationRel> queryTpaPartnerInstances(Long parentStationId,PartnerInstanceStateEnum state){
        PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andParentStationIdEqualTo(parentStationId).andTypeEqualTo(PartnerInstanceTypeEnum.TPA.getCode()).andStateEqualTo(state.getCode());
        return partnerStationRelMapper.selectByExample(example);
    }

    @Override
    public Partner getPartnerByStationId(Long stationId) {
        PartnerStationRel rel = findPartnerInstanceByStationId(stationId);
        if (rel == null) {
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "数据异常");
        }
        return partnerBO.getPartnerById(rel.getPartnerId());
    }

	@Override
	public void updateTransStatusByInstanceId(Long instanceId,
			PartnerInstanceTransStatusEnum transStatus, String operator) {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(transStatus);
		ValidateUtils.notNull(operator);
        PartnerStationRel updateInstance = new PartnerStationRel();
        updateInstance.setId(instanceId);
        updateInstance.setTransStatus(transStatus.getCode());

        DomainUtils.beforeUpdate(updateInstance, operator);

        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
		
	}

	@Override
	public void  createPartnerSellerInfo(Long taobaoUserId) {
		//
		createSellerAndShopId(taobaoUserId);
		
		createDistributionChannelId(taobaoUserId);
	}

	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
	public void createDistributionChannelId(Long taobaoUserId) {
		PartnerStationRel instance = getActivePartnerInstance(taobaoUserId);
		if(instance == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "村淘合伙人信息不存在");
		}
		PartnerStationRel update = new  PartnerStationRel();
		update.setId(instance.getId());
		if(instance.getDistributionChannelId() == null){
			Qualification qualification = cuntaoQualificationService.queryC2BQualification(taobaoUserId);
			if(qualification == null||qualification.getStatus() != 1){
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "用户营业执照未通过审核");
			}
			Station station = stationBO.getStationById(instance.getStationId());
			if(station == null){
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "服务站信息不存在");
			}
			Partner  partner = partnerBO.getPartnerById(instance.getPartnerId());
			if(partner == null){
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "合伙人信息不存在");
			}
			StoreDto store = storeReadBO.getStoreDtoByStationId(station.getId());
			if(store == null){
				storeWriteService.createSupplyStore(station.getId());
				store = storeReadBO.getStoreDtoByStationId(station.getId());
			}
			if(store == null){
				throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "门店信息不存在");
			}
			ChannelUserDTO channelUserDTO = createDistributeChannelRequest(qualification, station, partner, store);
			ResultDTO<Long> channelResult = createDistributionChannel(channelUserDTO);
			if(channelResult.isSuccess()){
				update.setDistributionChannelId(channelResult.getModule());
				update.setDistributorCode(channelUserDTO.getDistributorUser().getDistributorCode());
				businessMonitorBO.fixBusinessMonitor("createDistributionChannel", instance.getId());
				partnerStationRelMapper.updateByPrimaryKeySelective(update);
			}else{
				businessMonitorBO.addBusinessMonitor("createDistributionChannel", instance.getId(),JSON.toJSONString(channelUserDTO),channelResult.getErrorCode(),channelResult.getErrorMessage());
				logger.error("createDistributionChannel error! errorMessage:"+channelResult.getErrorMessage()+" errorCode:"+channelResult.getErrorCode());
				//throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, channelResult.getErrorMessage());
			}
		}
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
    @Override
	public void createSellerAndShopId(Long taobaoUserId) {
		Qualification qualification = cuntaoQualificationService.queryC2BQualification(taobaoUserId);
		if(qualification == null||qualification.getStatus() != 1){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "用户营业执照未通过审核");
		}
		PartnerStationRel instance = getActivePartnerInstance(taobaoUserId);
		if(instance == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "村淘合伙人信息不存在");
		}
		Station station = stationBO.getStationById(instance.getStationId());
		if(station == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "服务站信息不存在");
		}
		Partner  partner = partnerBO.getPartnerById(instance.getPartnerId());
		if(partner == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "合伙人信息不存在");
		}
		 PartnerStationRel update = new  PartnerStationRel();
		 update.setId(instance.getId());
		if(instance.getShopId() ==null || instance.getSellerId() == null){
			MirrorSellerDO mirrorSellerDO = createShopMirrorRequest(taobaoUserId, qualification, station);
			ResultDO<MirrorSellerDO> sellerResult = createShopMirror(mirrorSellerDO);
			if(sellerResult.isSuccess()){
				MirrorSellerDO sellerDO = sellerResult.getModule();
				update.setShopId(sellerDO.getShop().getShopId());
				update.setSellerId(sellerDO.getUserId());
				userTagService.addTag(taobaoUserId, UserTag.SELLER_HQZY_TAG.getTag());
				userTagService.addTagToUserData(sellerDO.getUserId(), 102209);
				userTagService.addTagToUserData(sellerDO.getUserId(), 76481);
				partnerStationRelMapper.updateByPrimaryKeySelective(update);
				businessMonitorBO.fixBusinessMonitor("createShopMirror", instance.getId());
			}else{
				businessMonitorBO.addBusinessMonitor("createShopMirror", instance.getId(),JSON.toJSONString(mirrorSellerDO),sellerResult.getErrorCode(),sellerResult.getErrMsg());
				logger.error("createShopMirror error! errorMessage:"+sellerResult.getErrMsg()+" errorCode:"+sellerResult.getErrorCode());
				//return false;
				//throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_EXT_RESULT_ERROR_CODE, sellerResult.getErrMsg());
			}
		}
	}

	private ResultDTO<Long> createDistributionChannel(ChannelUserDTO channelUserDTO) {
		ResultDTO<Long> channelResult = uscChannelRelationService.createChannelRelation(channelUserDTO);
		return channelResult;
	}

	private ChannelUserDTO createDistributeChannelRequest(Qualification qualification, Station station, Partner partner,
			StoreDto store) {
		ChannelUserDTO channelUserDTO = new ChannelUserDTO();
		BaseUserDTO baseUserDTO = new BaseUserDTO();
		baseUserDTO.setDistributorCode("CT_"+DateTimeUtil.getDate2Str("yyyyMMdd", new Date())+groupSequence.nextValue());
		baseUserDTO.setDistributorName(station.getName());
		baseUserDTO.setFullName(partner.getName());
		baseUserDTO.setPhone(partner.getMobile());
		baseUserDTO.setDistributorAccountType(5);
		baseUserDTO.setWareCode(store.getShareStoreId()+"");
		baseUserDTO.setSupplierTbId(diamondConfiguredProperties.getSupplierTbId());
		if(station.getTown() != null){
			baseUserDTO.setDivisionCode(Long.parseLong(station.getTown()));
		}
		baseUserDTO.setDetailAddress(station.getAddress());
		baseUserDTO.setIso("CN/中国");
		channelUserDTO.setDistributorUser(baseUserDTO);
		BizOrgRelationDTO bizOrgRelationDTO = new BizOrgRelationDTO();
		bizOrgRelationDTO.setChannel(100010);
		channelUserDTO.setBizOrgRelation(bizOrgRelationDTO);
		
		CompanyQualificationDTO companyQualificationDTO = new CompanyQualificationDTO();
		BillingInfoDTO billingInfoDTO = new BillingInfoDTO();
		companyQualificationDTO.setBillingInfo(billingInfoDTO);
		billingInfoDTO.setCompanyName(qualification.getCompanyName());
		billingInfoDTO.setCompanyPhone(partner.getMobile());
		billingInfoDTO.setLegalPerson(qualification.getLegalPerson());
		channelUserDTO.setCompanyQualification(companyQualificationDTO);
		return channelUserDTO;
	}

	private ResultDO<MirrorSellerDO> createShopMirror(MirrorSellerDO mirrorSellerDO) {
		ResultDO<MirrorSellerDO> sellerResult = shopMirrorService.createShopMirror(mirrorSellerDO);
		return sellerResult;
	}

	private MirrorSellerDO createShopMirrorRequest(Long taobaoUserId, Qualification qualification, Station station) {
		MirrorSellerDO mirrorSellerDO = new MirrorSellerDO();
		mirrorSellerDO.setMirrorBusiType(MirrorBusiType.tm_mirror_biz_cuntao);
		mirrorSellerDO.setMirrorRights(EnumSet.of(MirrorRights.tm_mirror_forbid_shop_manager, MirrorRights.tm_mirror_forbid_shop_search));
		mirrorSellerDO.setOpearatingLicense(qualification.getCompanyName());
		mirrorSellerDO.setOpearatingLicenseNumber(qualification.getQualiNo());
		mirrorSellerDO.setCreator(taobaoUserId);
		mirrorSellerDO.setLegalRepMan(qualification.getLegalPerson());
		ShopDO shop = new ShopDO();
		String name= "";
		if(StringUtils.isNotEmpty(station.getCountyDetail())){
			name = name+station.getCountyDetail();
		}
		if(StringUtils.isNotEmpty(station.getTownDetail())){
			name = name+station.getTownDetail();
		}
		name = name+groupSequence.nextValue()+"号店";
		shop.setName(name);
		shop.setDomain(null);
		mirrorSellerDO.setShop(shop);
		return mirrorSellerDO;
	}
	
	@Override
	public void cancelShopMirror(Long taobaoUserId){
		PartnerStationRel instance = getActivePartnerInstance(taobaoUserId);
		if(instance == null){
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "村淘合伙人信息不存在");
		}
		if(instance.getShopId() == null || instance.getSellerId() == null){
			return;
		}
		ResultDO<Boolean> cancelResult = shopMirrorService.cancelShopMirror(MirrorBusiType.tm_mirror_biz_cuntao, instance.getSellerId(), instance.getShopId(), "255.255.255.255", "sys", "服务站停业撤销影子店铺");
		if(cancelResult.isSuccess()){
			instance.setShopId(null);
			instance.setSellerId(null);
			partnerStationRelMapper.updateByPrimaryKey(instance);
			return;
		}else{
			logger.error("cancelShopMirror error! errorMessage:"+cancelResult.getErrMsg()+" errorCode:"+cancelResult.getErrorCode());
			throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, cancelResult.getErrMsg());
		}
	}

	@Override
	public void updateIncomeMode(Long instanceId, String incomeMode, String operator) {
		Date a = new Date();
		if(DateUtil.parseDateTime("2019-01-01 00:00:00").before(a)) {
			ValidateUtils.notNull(instanceId);
			ValidateUtils.notNull(incomeMode);
			ValidateUtils.notNull(operator);
	        PartnerStationRel updateInstance = new PartnerStationRel();
	        updateInstance.setId(instanceId);
	        updateInstance.setIncomeMode(incomeMode);
	        updateInstance.setIncomeModeBeginTime(a);
	        DomainUtils.beforeUpdate(updateInstance, operator);
	        partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
		}
		
	}

	@Override
	public Integer getActiveLxPartnerByParentStationId(Long taobaoUserId) {
		PartnerStationRel  r = this.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
		if (r == null) {
			return 0;
		}
		PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andParentStationIdEqualTo(r.getStationId())
            .andTypeEqualTo(PartnerInstanceTypeEnum.LX.getCode()).andStateIn(PartnerInstanceStateEnum.getValidLxStatusArray()).andIsCurrentEqualTo("y");
        return partnerStationRelMapper.countByExample(example);
	}

	@Override
	public List<LxPartnerDto> getActiveLxListrByParentStationId(Long taobaoUserId) {
		PartnerStationRel  r = this.getCurrentPartnerInstanceByTaobaoUserId(taobaoUserId);
		if (r == null) {
			return new ArrayList<LxPartnerDto>();
		}
		//查关系
		PartnerStationRelExample example = new PartnerStationRelExample();
        example.createCriteria().andIsDeletedEqualTo("n").andParentStationIdEqualTo(r.getStationId())
            .andTypeEqualTo(InstanceTypeEnum.LX.getCode()).andStateIn(PartnerInstanceStateEnum.getValidLxStatusArray()).andIsCurrentEqualTo("y");

        List<PartnerStationRel> rList = partnerStationRelMapper.selectByExample(example);

        if (rList == null || rList.size()<=0) {
        	return new ArrayList<LxPartnerDto>();
        }

        List<Long> partnerIds =rList.stream().map(PartnerStationRel::getPartnerId).collect(Collectors.toList());
        List<Long> stationIds = rList.stream().map(PartnerStationRel::getStationId).collect(Collectors.toList());

        //查station
        StationExample example2 = new StationExample();
      	example2.createCriteria().andIsDeletedEqualTo("n").andIdIn(stationIds);
        List<Station> sList =  stationMapper.selectByExample(example2);

        //查partner
        PartnerExample example1 = new PartnerExample();
        example1.createCriteria().andIsDeletedEqualTo("n").andIdIn(partnerIds);
        List<Partner> pList =  partnerMapper.selectByExample(example1);

        Map<Long, Partner> pmap = pList.stream().collect(Collectors.toMap(Partner::getId,java.util.function.Function.identity(),(key1, key2) -> key1));
        Map<Long, Station> smap = sList.stream().collect(Collectors.toMap(Station::getId,java.util.function.Function.identity(),(key1, key2) -> key1));
		return rList.stream().map(p -> bulidLx(p,pmap,smap)).collect(Collectors.toList());
	}

	/**
	 * 构建返回对象

	 */
	private LxPartnerDto bulidLx(PartnerStationRel p, Map<Long, Partner> pmap,Map<Long, Station> smap){
		LxPartnerDto pDto = new LxPartnerDto();
		pDto.setTaobaoNick(pmap.get(p.getPartnerId()).getTaobaoNick());
		pDto.setMobile(pmap.get(p.getPartnerId()).getMobile());
		pDto.setName(pmap.get(p.getPartnerId()).getName());
		pDto.setTaobaoUserId(p.getTaobaoUserId());
		pDto.setState(p.getState());
		pDto.setPid(partnerAdzoneService.getUnionPid(p.getTaobaoUserId(), p.getStationId()));
		pDto.setStationName(smap.get(p.getStationId()).getName());

		return pDto;
	}



    @Override
    public void updateIncomeModeNextMonth(Long instanceId, String incomeMode, String operator) {

        Date newMonth = new Date();
        DateFormat format=DateFormat.getDateInstance();
       // format.p
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(newMonth);
        calendar.add(Calendar.MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        newMonth=calendar.getTime();
        if(DateUtil.parseDateTime("2019-01-01 00:00:00").before(newMonth)) {
            ValidateUtils.notNull(instanceId);
            ValidateUtils.notNull(incomeMode);
            ValidateUtils.notNull(operator);
            PartnerStationRel updateInstance = new PartnerStationRel();
            updateInstance.setId(instanceId);
            updateInstance.setIncomeMode(incomeMode);
            updateInstance.setIncomeModeBeginTime(newMonth);
            DomainUtils.beforeUpdate(updateInstance, operator);
            partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);
        }
    }

    @Override
    public void createTaobaoSellerAndShopId(Long taobaoUserId) {
        Qualification qualification = cuntaoQualificationService.queryC2BQualification(taobaoUserId);
        if(qualification == null||qualification.getStatus() != 1){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "用户营业执照未通过审核");
        }
        PartnerStationRel instance = getActivePartnerInstance(taobaoUserId);
        if(instance == null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "村淘合伙人信息不存在");
        }
        Station station = stationBO.getStationById(instance.getStationId());
        if(station == null){
            throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, "服务站信息不存在");
        }

        PartnerStationRel update = new  PartnerStationRel();
        update.setId(instance.getId());
        if(instance.getShopId() ==null || instance.getSellerId() == null){
            OuterInfo outerInfo = new OuterInfo();
            outerInfo.setOuterId(String.valueOf(taobaoUserId));
            outerInfo.setBizScene(diamondConfiguredProperties.getCreateSellerBizScene());

            HavanaResult<Long> havanaResult = accountWriteServiceClient.queryOrCreateAccount(outerInfo);
            if (!havanaResult.isSuccess()){
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, taobaoUserId+" 创建卖家账号失败:"+havanaResult.getCode()+havanaResult.getMessage());
            }
            Long sellerId = havanaResult.getReturnValue();
            OpenShopDO shopParam = new OpenShopDO();
            shopParam.setUserId(sellerId);
            shopParam.setShopAddress(station.getAddress());
            Result<com.taobao.shopservice.core.client.domain.ShopDO> shopDOResult = openShopService.openShop(shopParam, "village_taobao", "auge");
            if(!shopDOResult.isSuccess()) {
                throw new AugeBusinessException(AugeErrorCodes.ILLEGAL_RESULT_ERROR_CODE, taobaoUserId+" 创建虚拟店铺失败:"+shopDOResult.getErrorCode()+shopDOResult.getErrorMessage());
            }
            Integer shopId = shopDOResult.getModule().getShopID();
            update.setShopId(shopId.longValue());
            update.setSellerId(sellerId);
            userTagService.addTag(taobaoUserId, UserTag.SELLER_HQZY_TAG.getTag());
            userTagService.addTagToUserData(sellerId, 102209);
            userTagService.addTagToUserData(sellerId, 76481);
            partnerStationRelMapper.updateByPrimaryKeySelective(update);

        }
    }
}
