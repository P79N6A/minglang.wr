package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ali.com.google.common.base.Function;
import com.ali.com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Component("partnerInstanceBO")
public class PartnerInstanceBOImpl implements PartnerInstanceBO {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceBO.class);

	@Autowired
	PartnerMapper partnerMapper;

	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	
	@Override
	public PartnerStationRel findPartnerInstance(Long taobaoUserId, PartnerInstanceStateEnum instanceState) {
		Partner partnerCondition = new Partner();
		partnerCondition.setTaobaoUserId(taobaoUserId);
		partnerCondition.setIsDeleted("n");
		partnerCondition.setState(PartnerStateEnum.NORMAL.getCode());
		Partner partner = partnerMapper.selectOne(partnerCondition);

		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setPartnerId(partner.getId());
		relCondition.setIsCurrent("y");
		relCondition.setIsDeleted("n");
		if (null != instanceState) {
			relCondition.setState(instanceState.getCode());
		}
		return partnerStationRelMapper.selectOne(relCondition);
		
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
		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setStationApplyId(stationApplyId);
		relCondition.setIsDeleted("n");
		PartnerStationRel rel = partnerStationRelMapper.selectOne(relCondition);
		if (rel != null){
			return rel.getId();
		}
		return null;
		
	}

	@Override
	public int findChildPartners(Long instanceId, PartnerInstanceStateEnum state) throws AugeServiceException {
		PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
		Long parentStationId = curPartnerInstance.getStationId();

		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setParentStationId(parentStationId);
		relCondition.setIsDeleted("n");
		relCondition.setState(state.getCode());
		return partnerStationRelMapper.selectCount(relCondition);
	}
	
	@Override
	public int findChildPartners(Long instanceId, List<PartnerInstanceStateEnum> stateEnums) throws AugeServiceException {
		PartnerStationRel curPartnerInstance = findPartnerInstanceById(instanceId);
		Long parentStationId = curPartnerInstance.getStationId();

		Example example = new Example(PartnerStationRel.class);
		
		Criteria criteria = example.createCriteria();
		if(!CollectionUtils.isEmpty(stateEnums)){
			List<String> states =  Lists.transform(stateEnums, new Function<PartnerInstanceStateEnum,String>(){
				@Override
				public String apply(PartnerInstanceStateEnum input) {
					return input.getCode();
				}
			});
			criteria.andIn("state", states);
		}
		criteria.andEqualTo("parentStationId", parentStationId);
		criteria.andEqualTo("isDeleted", "n");
		//排除自己
		criteria.andNotEqualTo("type", curPartnerInstance.getType());
		
		return partnerStationRelMapper.selectByExample(example).size();
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
	public PartnerInstanceDto getPartnerInstanceById(Long instanceId)
			throws AugeServiceException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOpenDate(Long instanceId, Date openDate, String operator)
			throws AugeServiceException {
		if (null == instanceId|| openDate ==null || operator==null) {
			throw new AugeServiceException(CommonExceptionEnum.PARAM_IS_NULL);
		}
		
		PartnerStationRel partnerStationRel = new PartnerStationRel();
		partnerStationRel.setId(instanceId);
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
		PartnerStationRel condition = new PartnerStationRel();
		condition.setIsDeleted("n");
		condition.setStationId(stationId);
		condition.setIsCurrent("y");
		return partnerStationRelMapper.selectOne(condition);
	}

	@Override
	public Long addPartnerStationRel(PartnerInstanceDto partnerInstanceDto)
			throws AugeServiceException {
		return null;
	}
}
