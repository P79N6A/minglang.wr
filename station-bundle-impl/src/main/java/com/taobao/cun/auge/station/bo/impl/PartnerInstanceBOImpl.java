package com.taobao.cun.auge.station.bo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.StationExceptionEnum;

@Component
public class PartnerInstanceBOImpl implements PartnerInstanceBO {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceBO.class);

	@Autowired
	PartnerMapper partnerMapper;

	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;

	@Override
	public Long findPartnerInstanceId(Long taobaoUserId, PartnerInstanceStateEnum state) {
		Partner partnerCondition = new Partner();
		partnerCondition.setTaobaoUserId(taobaoUserId);
		partnerCondition.setIsDeleted("n");
		partnerCondition.setState(PartnerStateEnum.NORMAL.getCode());
		Partner partner = partnerMapper.selectOne(partnerCondition);

		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setPartnerId(partner.getId());
		relCondition.setIsCurrent("y");
		relCondition.setIsDeleted("n");
		if (null != state) {
			relCondition.setState(state.getCode());
		}

		PartnerStationRel rel = partnerStationRelMapper.selectOne(relCondition);

		return rel.getId();
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
	public void changeState(Long instanceId, PartnerInstanceStateEnum preState, PartnerInstanceStateEnum postState,
			String operator) throws Exception {
		PartnerStationRel partnerInstance = findPartnerInstanceById(instanceId);

		if (!preState.getCode().equals(partnerInstance.getState())) {
			logger.error("partner instance state is not " + preState.getDesc());
			throw new Exception("partner instance state is not " + preState.getDesc());
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

}
