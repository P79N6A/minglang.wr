package com.taobao.cun.auge.station.bo.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ali.com.google.common.base.Function;
import com.ali.com.google.common.collect.Lists;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRelExample.Criteria;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;
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
	
	@Override
	public PartnerStationRel findPartnerInstance(Long taobaoUserId, PartnerInstanceStateEnum instanceState) {
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);

		PartnerStationRelExample example = new PartnerStationRelExample();
		
		Criteria criteria=	example.createCriteria();
		
		criteria.andPartnerIdEqualTo(partner.getId());
		criteria.andIsCurrentEqualTo("y");
		criteria.andIsDeletedEqualTo("n");
		if (null != instanceState) {
			criteria.andStateEqualTo(instanceState.getCode());
		}
		List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(instances)){
			return null;
		}
		return instances.get(0);
		
	}
	
	@Override
	public Long findPartnerInstanceId(Long taobaoUserId, PartnerInstanceStateEnum state) {
		PartnerStationRel rel = findPartnerInstance(taobaoUserId,state);
		if (rel != null){
			return rel.getId();
		}
		return null;
	}
	
	@Override
	public Long findPartnerInstanceId(Long stationApplyId) {
		PartnerStationRelExample example = new PartnerStationRelExample();

		Criteria criteria = example.createCriteria();

		criteria.andStationApplyIdEqualTo(stationApplyId);
		criteria.andIsDeletedEqualTo("n");

		List<PartnerStationRel> instances = partnerStationRelMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(instances)) {
			return null;
		}
		return instances.get(0).getId();

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
	public int findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums) throws AugeServiceException {
		PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
		Long parentStationId = curPartnerInstance.getStationId();

		PartnerStationRelExample example = new PartnerStationRelExample();
		
		Criteria criteria = example.createCriteria();
		if(!CollectionUtils.isEmpty(stateEnums)){
			List<String> states =  Lists.transform(stateEnums, new Function<PartnerInstanceStateEnum,String>(){
				@Override
				public String apply(PartnerInstanceStateEnum input) {
					return input.getCode();
				}
			});
			criteria.andStateIn(states);
		}

		criteria.andParentStationIdEqualTo(parentStationId);
		criteria.andIsDeletedEqualTo("n");

		//排除自己
		criteria.andTypeNotEqualTo(curPartnerInstance.getType());
		
		return partnerStationRelMapper.countByExample(example);
	}

	@Override
	public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState,
			String operator) throws AugeServiceException {
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

	@Override
	public void updatePartnerStationRel(PartnerInstanceDto partnerInstanceDto) throws AugeServiceException {
		PartnerStationRel rel =convertToDomain(partnerInstanceDto);
		DomainUtils.beforeUpdate(rel, partnerInstanceDto.getOperator());
		partnerStationRelMapper.updateByPrimaryKeySelective(rel);
		
	}
	
	private PartnerStationRel convertToDomain (PartnerInstanceDto partnerInstanceDto) {
		PartnerStationRel rel =new PartnerStationRel();
		return rel;
	}

	@Override
	public Long findStationApplyId(Long instanceId) {
		PartnerStationRel partnerInstance = partnerStationRelMapper.selectByPrimaryKey(instanceId);
		return partnerInstance.getStationApplyId();
	}

	@Override
	public PartnerInstanceDto getPartnerInstanceById(Long instanceId)throws AugeServiceException {
		PartnerStationRel psRel = findPartnerInstanceById(instanceId);
		Partner partner = partnerBO.getPartnerById(psRel.getPartnerId());
		Station station = stationBO.getStationById(psRel.getStationId());
		
		return PartnerInstanceConverter.convert(psRel, station, partner);
	}

	@Override
	public void updateOpenDate(Long instanceId, Date openDate, String operator)
			throws AugeServiceException {
		if (null == instanceId|| operator==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerStationRel partnerStationRel = partnerStationRelMapper.selectByPrimaryKey(instanceId);
		if (partnerStationRel ==null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		
		partnerStationRel.setOpenDate(openDate);
		DomainUtils.beforeUpdate(partnerStationRel, operator);
		partnerStationRelMapper.updateByPrimaryKey(partnerStationRel);
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
	public PartnerStationRel findPartnerInstanceByStationId(Long stationId) throws AugeServiceException {
		if (null == stationId) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
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
	
	public List<PartnerStationRel> findPartnerInstanceByPartnerId(Long partnerId,List<String> states) throws AugeServiceException {
		if (null == partnerId) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		PartnerStationRelExample example = new PartnerStationRelExample();

		Criteria criteria = example.createCriteria();

		criteria.andPartnerIdEqualTo(partnerId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andStateIn(states);
		
		return partnerStationRelMapper.selectByExample(example);
	}
	

	@Override
	public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		return null;
	}

	@Override
	public boolean checkSettleQualification(Long taobaoUserId)
			throws AugeServiceException {
		Partner partner =  partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
		if (partner == null || partner.getId() ==null) {
			return true;
		}
		
		List<PartnerStationRel> instatnceList  = findPartnerInstanceByPartnerId(partner.getId(),PartnerInstanceStateEnum.unReSettlableStatusCodeList());
		
		if (CollectionUtils.isEmpty(instatnceList)) {
			return true;
		}
		for (PartnerStationRel rel: instatnceList) {
			if (!StringUtils.equals(PartnerInstanceStateEnum.QUITING.getCode(),rel.getState())) {
				return false;
			}else {
				PartnerLifecycleItems item = partnerLifecycleBO.getLifecycleItems(rel.getId(), PartnerLifecycleBusinessTypeEnum.QUITING);
				if (StringUtils.equals(PartnerLifecycleCurrentStepEnum.BOND.getCode(),item.getCurrentStep())) {
					continue;
				}
				return false;
			}
			
		}
		return true;
	}

	@Override
	public void deletePartnerStationRel(Long instanceId,String operator)
			throws AugeServiceException {
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
}
