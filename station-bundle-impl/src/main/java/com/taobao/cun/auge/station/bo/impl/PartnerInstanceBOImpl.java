package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.pagehelper.PageHelper;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.taobao.cun.auge.alilang.UserProfile;
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.CriusTaskExecute;
import com.taobao.cun.auge.dal.domain.CriusTaskExecuteExample;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample.Criteria;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.CriusTaskExecuteMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.failure.AugeErrorCodes;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
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

@Component("partnerInstanceBO")
public class PartnerInstanceBOImpl implements PartnerInstanceBO {

    private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceBO.class);

    @Autowired
    PartnerMapper partnerMapper;

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
    public Long getInstanceIdByStationApplyId(Long stationApplyId) {
        PartnerStationRel rel = getPartnerStationRelByStationApplyId(stationApplyId);
        if (rel == null) {
            return null;
        }
        return rel.getId();

    }

    @Override
    public PartnerStationRel getPartnerStationRelByStationApplyId(Long stationApplyId) {
        ValidateUtils.notNull(stationApplyId);
        PartnerStationRelExample example = new PartnerStationRelExample();

        Criteria criteria = example.createCriteria();

        criteria.andStationApplyIdEqualTo(stationApplyId);
        criteria.andIsDeletedEqualTo("n");

        List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);

        return ResultUtils.selectOne(instances);
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
        criteria.andTypeNotEqualTo(curPartnerInstance.getType());

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
    public Long findStationApplyId(Long instanceId) {
        PartnerStationRel partnerInstance = partnerStationRelMapper.selectByPrimaryKey(instanceId);
        return partnerInstance.getStationApplyId();
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
    }

    @Override
    public Long findStationApplyIdByStationId(Long stationId) {
        PartnerStationRel partnerStationRel = findPartnerInstanceByStationId(stationId);
        if (partnerStationRel != null) {
            return partnerStationRel.getStationApplyId();
        }
        return null;
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
    public Long findStationIdByStationApplyId(Long stationApplyId) {
        PartnerStationRel rel = getPartnerStationRelByStationApplyId(stationApplyId);
        return null != rel ? rel.getStationId() : null;
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
}
