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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ali.com.google.common.base.Function;
import com.ali.com.google.common.collect.Lists;
import com.ali.com.google.common.collect.Sets;
import com.github.pagehelper.PageHelper;
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
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBondEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCourseStatusEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleRoleApproveEnum;
import com.taobao.cun.auge.station.enums.TaskBusinessTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
import com.taobao.cun.auge.station.rule.PartnerLifecycleRuleParser;
import com.taobao.pandora.util.StringUtils;

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
			throws AugeServiceException {
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
	public Long getInstanceIdByTaobaoUserId(Long taobaoUserId, PartnerInstanceStateEnum instanceState) throws AugeServiceException {
		PartnerStationRel rel = getPartnerInstanceByTaobaoUserId(taobaoUserId, instanceState);
		if (rel != null) {
			return rel.getId();
		}
		return null;
	}

	@Override
	public Long getInstanceIdByStationApplyId(Long stationApplyId) throws AugeServiceException {
		PartnerStationRel rel = getPartnerStationRelByStationApplyId(stationApplyId);
		if (rel == null) {
			return null;
		}
		return rel.getId();

	}

	@Override
	public PartnerStationRel getPartnerStationRelByStationApplyId(Long stationApplyId) throws AugeServiceException {
		ValidateUtils.notNull(stationApplyId);
		PartnerStationRelExample example = new PartnerStationRelExample();

		Criteria criteria = example.createCriteria();

		criteria.andStationApplyIdEqualTo(stationApplyId);
		criteria.andIsDeletedEqualTo("n");

		List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);

		return ResultUtils.selectOne(instances);
	}

	@Override
	public int findChildPartners(Long instanceId, PartnerInstanceStateEnum state) throws AugeServiceException {
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
	public List<PartnerStationRel> findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums) throws AugeServiceException {
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
	public List<PartnerStationRel> findPartnerInstances(Long stationId) throws AugeServiceException {
		if (null == stationId) {
			return Collections.<PartnerStationRel> emptyList();
		}
		PartnerStationRelExample example = new PartnerStationRelExample();
		Criteria criteria = example.createCriteria();

		criteria.andStationIdEqualTo(stationId);
		criteria.andIsDeletedEqualTo("n");

		return partnerStationRelMapper.selectByExample(example);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState, String operator)
			throws AugeServiceException {
		PartnerStationRel partnerInstance = findPartnerInstanceById(instanceId);

		if (!preState.getCode().equals(partnerInstance.getState())) {
			logger.error("partner instance state is not " + preState.getDesc());
			throw new AugeServiceException("partner instance state is not " + preState.getDesc());
		}

		PartnerStationRel updateInstance = new PartnerStationRel();
		updateInstance.setId(instanceId);
		updateInstance.setState(postState.getCode());

		DomainUtils.beforeUpdate(updateInstance, operator);

		partnerStationRelMapper.updateByPrimaryKeySelective(updateInstance);

	}

	@Override
	public PartnerStationRel findPartnerInstanceById(Long instanceId) throws AugeServiceException {
		PartnerStationRel partnerInstance = partnerStationRelMapper.selectByPrimaryKey(instanceId);
		if (null == partnerInstance) {
			logger.error("partner instance is not exist.instance id " + instanceId);
			throw new AugeServiceException(StationExceptionEnum.PARTNER_INSTANCE_NOT_EXIST);
		}
		return partnerInstance;
	}

	@Override
	public Long findStationIdByInstanceId(Long instanceId) throws AugeServiceException {
		PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
		return curPartnerInstance.getStationId();
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updatePartnerStationRel(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
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
			rel.setVersion(rel.getVersion() + 1l);
			criteria.andVersionEqualTo(partnerInstanceDto.getVersion());
		}

		int updateCount = partnerStationRelMapper.updateByExampleSelective(rel, example);

		if (updateCount < 1) {
			throw new AugeServiceException(CommonExceptionEnum.VERION_IS_INVALID);
		}
	}

	@Override
	public Long findStationApplyId(Long instanceId) {
		PartnerStationRel partnerInstance = partnerStationRelMapper.selectByPrimaryKey(instanceId);
		return partnerInstance.getStationApplyId();
	}

	@Override
	public PartnerInstanceDto getPartnerInstanceById(Long instanceId) throws AugeServiceException {
		PartnerStationRel psRel = findPartnerInstanceById(instanceId);
		Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
		Station station = stationBO.getStationById(psRel.getStationId());

		return PartnerInstanceConverter.convert(psRel, station, partner);
	}

	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	@Override
	public void updateOpenDate(Long instanceId, Date openDate, String operator) throws AugeServiceException {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(operator);
		PartnerStationRel partnerStationRel = partnerStationRelMapper.selectByPrimaryKey(instanceId);
		if (partnerStationRel == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}

		partnerStationRel.setOpenDate(openDate);
		DomainUtils.beforeUpdate(partnerStationRel, operator);
		partnerStationRelMapper.updateByPrimaryKeySelective(partnerStationRel);
	}

	@Override
	public Long findStationApplyIdByStationId(Long stationId) throws AugeServiceException {
		PartnerStationRel partnerStationRel = findPartnerInstanceByStationId(stationId);
		if (partnerStationRel != null) {
			return partnerStationRel.getStationApplyId();
		}
		return null;
	}
	
	@Override
	public Long findPartnerInstanceIdByStationId(Long stationId) throws AugeServiceException{
		PartnerStationRel rel = findPartnerInstanceByStationId(stationId);
		if (null == rel) {
			logger.error("partner instance is not exist.stationId " + stationId);
			throw new AugeServiceException(StationExceptionEnum.PARTNER_INSTANCE_NOT_EXIST);
		}
		return rel.getId();
	}

	@Override
	public PartnerStationRel findPartnerInstanceByStationId(Long stationId) throws AugeServiceException {
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

	public List<PartnerStationRel> findPartnerInstanceByPartnerId(Long partnerId, List<String> states) throws AugeServiceException {
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
	public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		ValidateUtils.validateParam(partnerInstanceDto);
		PartnerStationRel partnerStationRel = PartnerInstanceConverter.convert(partnerInstanceDto);
		DomainUtils.beforeInsert(partnerStationRel, partnerInstanceDto.getOperator());
		partnerStationRelMapper.insert(partnerStationRel);
		return partnerStationRel.getId();
	}

	@Override
	public boolean checkSettleQualification(Long taobaoUserId) throws AugeServiceException {
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
	public void deletePartnerStationRel(Long instanceId, String operator) throws AugeServiceException {
		PartnerStationRel rel = new PartnerStationRel();
		rel.setId(instanceId);
		DomainUtils.beforeDelete(rel, operator);
		partnerStationRelMapper.updateByPrimaryKeySelective(rel);
	}

	@Override
	public List<Long> getWaitOpenStationList(int fetchNum) throws AugeServiceException {
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
	public List<Long> getWaitThawMoneyList(int fetchNum) throws AugeServiceException {
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
	public int getActiveTpaByParentStationId(Long parentStationId) throws AugeServiceException {
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
	public void finishCourse(Long taobaoUserId) throws AugeServiceException {
		ValidateUtils.notNull(taobaoUserId);
		PartnerStationRel rel = this.getActivePartnerInstance(taobaoUserId);
		if (rel == null) {
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		partnerLifecycleBO.updateCourseState(rel.getId(), PartnerLifecycleCourseStatusEnum.Y, OperatorDto.defaultOperator());
	}

}
