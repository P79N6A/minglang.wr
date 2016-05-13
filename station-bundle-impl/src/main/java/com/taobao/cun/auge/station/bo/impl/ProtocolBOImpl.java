package com.taobao.cun.auge.station.bo.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.enums.ProtocolStateEnum;
import com.taobao.cun.auge.station.enums.ProtocolTargetBizTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;

@Service("protocolBO")
public class ProtocolBOImpl implements ProtocolBO {

	@Autowired
	PartnerMapper partnerMapper;

	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;

	@Autowired
	ProtocolMapper protocolMapper;

	@Autowired
	PartnerProtocolRelMapper partnerProtocolRelMapper;

	@Override
	public void signProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType) {
		Date now = new Date();
		signProtocol(businessId, taobaoUserId, type, now, now, null, String.valueOf(taobaoUserId),
				ProtocolTargetBizTypeEnum.PARTNER_INSTANCE);
	}

	private void signProtocol(Long objectId, Long taobaoUserId, ProtocolTypeEnum type, Date confirmTime, Date startTime,
			Date endTime, String operator, ProtocolTargetBizTypeEnum targetType) {
		Long protocolId = findProtocolId(type);

		PartnerProtocolRel partnerProtocolRelDO = new PartnerProtocolRel();

		partnerProtocolRelDO.setTaobaoUserId(taobaoUserId);
		partnerProtocolRelDO.setProtocolId(protocolId);
		partnerProtocolRelDO.setObjectId(objectId);
		partnerProtocolRelDO.setTargetType(targetType.getCode());
		partnerProtocolRelDO.setConfirmTime(confirmTime);
		partnerProtocolRelDO.setStartTime(startTime);
		partnerProtocolRelDO.setEndTime(endTime);

		beforeInsert(partnerProtocolRelDO, operator);

		partnerProtocolRelMapper.insertSelective(partnerProtocolRelDO);
	}

	/**
	 * 根据协议类型，查询协议id
	 * 
	 * @param type
	 * @return
	 */
	private Long findProtocolId(ProtocolTypeEnum type) {
		Protocol protocolDO = new Protocol();
		protocolDO.setIsDeleted("n");
		protocolDO.setType(type.getCode());
		protocolDO.setState(ProtocolStateEnum.VALID.getCode());

		Protocol protocol = protocolMapper.selectOne(protocolDO);
		return protocol.getId();
	}

	/**
	 * 根据taobaouserid查询合伙人实例id
	 * 
	 * @param taobaoUserId
	 * @return
	 */
	private Long findInstanceIdByTaobaouserId(Long taobaoUserId) {
		Partner partnerCondition = new Partner();
		partnerCondition.setTaobaoUserId(taobaoUserId);
		partnerCondition.setIsDeleted("n");
		Partner partner = partnerMapper.selectOne(partnerCondition);

		PartnerStationRel relCondition = new PartnerStationRel();
		relCondition.setPartnerId(partner.getId());
		relCondition.setIsCurrent("y");
		relCondition.setIsDeleted("n");

		PartnerStationRel rel = partnerStationRelMapper.selectOne(relCondition);

		return rel.getId();
	}

	private static void beforeInsert(PartnerProtocolRel partnerProtocolRel, String operator) {
		Date now = new Date();

		partnerProtocolRel.setGmtCreate(now);
		partnerProtocolRel.setGmtModified(now);

		partnerProtocolRel.setCreator(operator);
		partnerProtocolRel.setModifier(operator);
		partnerProtocolRel.setIsDeleted("n");
	}

	private static void beforeUpdate(PartnerProtocolRel partnerProtocolRel, String operator) {
		Date now = new Date();
		partnerProtocolRel.setGmtModified(now);
		partnerProtocolRel.setModifier(operator);
	}

	public static void beforeDelete(PartnerProtocolRel partnerProtocolRel, String operator) {
		Date now = new Date();
		partnerProtocolRel.setGmtModified(now);
		partnerProtocolRel.setModifier(operator);
		partnerProtocolRel.setIsDeleted("y");
	}

	@Override
	public void cancelProtocol(Long taobaoUserId, ProtocolTypeEnum type, Long businessId,
			ProtocolTargetBizTypeEnum bizType, String operator) {
		PartnerProtocolRel partnerProtocolRelDO = new PartnerProtocolRel();
		partnerProtocolRelDO.setTaobaoUserId(taobaoUserId);
		partnerProtocolRelDO.setTargetType(bizType.getCode());
		partnerProtocolRelDO.setObjectId(businessId);

		Long protocolId = findProtocolId(type);
		partnerProtocolRelDO.setProtocolId(protocolId);

		beforeDelete(partnerProtocolRelDO, operator);
		partnerProtocolRelMapper.delete(partnerProtocolRelDO);
	}

}
