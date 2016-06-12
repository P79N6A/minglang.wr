package com.taobao.cun.auge.station.sync;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.FeatureUtil;
import com.taobao.cun.auge.dal.domain.AccountMoney;
import com.taobao.cun.auge.dal.domain.AccountMoneyExample;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItemsExample;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRel;
import com.taobao.cun.auge.dal.domain.PartnerProtocolRelExample;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Protocol;
import com.taobao.cun.auge.dal.domain.ProtocolExample;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.domain.StationApply;
import com.taobao.cun.auge.dal.mapper.AccountMoneyMapper;
import com.taobao.cun.auge.dal.mapper.PartnerLifecycleItemsMapper;
import com.taobao.cun.auge.dal.mapper.PartnerMapper;
import com.taobao.cun.auge.dal.mapper.PartnerProtocolRelMapper;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelMapper;
import com.taobao.cun.auge.dal.mapper.ProtocolMapper;
import com.taobao.cun.auge.dal.mapper.StationApplyMapper;
import com.taobao.cun.auge.dal.mapper.StationMapper;
import com.taobao.cun.auge.event.enums.SyncStationApplyEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleCurrentStepEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.enums.StationCategoryEnum;
import com.taobao.cun.auge.station.enums.TargetTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.dto.station.enums.StationApplyStateEnum;
import com.taobao.util.CollectionUtil;
import com.taobao.vipserver.client.utils.CollectionUtils;

@Component("syncStationApplyBO")
public class SyncStationApplyBOImpl implements SyncStationApplyBO {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private static final String ERROR_MSG = "SYNC_BACK_TO_STATIONAPPLY_ERROR";

	@Autowired
	StationApplyMapper stationApplyMapper;
	@Autowired
	PartnerMapper partnerMapper;
	@Autowired
	PartnerStationRelMapper partnerStationRelMapper;
	@Autowired
	PartnerLifecycleItemsMapper partnerLifecycleItemsMapper;
	@Autowired
	StationMapper stationMapper;
	@Autowired
	AccountMoneyMapper accountMoneyMapper;
	@Autowired
	PartnerProtocolRelMapper partnerProtocolRelMapper;
	@Autowired
	ProtocolMapper protocolMapper;

	@Override
	public StationApply addStationApply(Long partnerInstanceId) throws AugeServiceException {
		try {
			StationApply stationApply = buildStationApply(partnerInstanceId, SyncStationApplyEnum.ADD);
			DomainUtils.beforeInsert(stationApply, DomainUtils.DEFAULT_OPERATOR);
			stationApply.setVersion(0l);
			// stationApply.setCustomerLevel(customerLevel);
			logger.info("sync add to station_apply : {}", JSON.toJSONString(stationApply));
			stationApplyMapper.insert(stationApply);

			// 更新instance的station_apply_id字段
			PartnerStationRel instance = new PartnerStationRel();
			instance.setId(partnerInstanceId);
			instance.setStationApplyId(stationApply.getId());
			instance.setGmtModified(new Date());
			partnerStationRelMapper.updateByPrimaryKeySelective(instance);
			logger.info("sync add success, partnerInstanceId = {}, station_apply_id = {}", partnerInstanceId, stationApply.getId());

			return stationApply;
		} catch (Exception e) {
			logger.error(ERROR_MSG + ": addStationApply", e);
			return null;
		}
	}

	@Override
	public void updateStationApply(Long partnerInstanceId, SyncStationApplyEnum updateType) throws AugeServiceException {
		try {
			if (null == updateType || SyncStationApplyEnum.ADD == updateType) {
				throw new IllegalArgumentException("invalid param");
			}
			StationApply stationApply = buildStationApply(partnerInstanceId, updateType);
			DomainUtils.beforeUpdate(stationApply, DomainUtils.DEFAULT_OPERATOR);
			logger.info("sync upate to station_apply {} : {}", updateType, JSON.toJSONString(stationApply));
			stationApplyMapper.updateByPrimaryKeySelective(stationApply);
		} catch (Exception e) {
			logger.error(ERROR_MSG + ": updateStationApply", e);
		}
	}

	private StationApply buildStationApply(Long partnerInstanceId, SyncStationApplyEnum buildType) {
		StationApply stationApply = new StationApply();
		if (null == partnerInstanceId || partnerInstanceId == 0l) {
			throw new IllegalArgumentException("partnerInstanceId can not be null");
		}
		PartnerStationRel instance = partnerStationRelMapper.selectByPrimaryKey(partnerInstanceId);
		if (buildType != SyncStationApplyEnum.ADD) {
			if (null == instance.getStationApplyId()) {
				throw new AugeServiceException("[update]instance's station_apply_id is null, instance_id = " + partnerInstanceId);
			}
			stationApply.setId(instance.getStationApplyId());
			if (null == stationApplyMapper.selectByPrimaryKey(instance.getStationApplyId())) {
				throw new AugeServiceException("[update]station_apply not exists, instance_id = " + partnerInstanceId);
			}
		}

		Station station = stationMapper.selectByPrimaryKey(instance.getStationId());
		Partner partner = partnerMapper.selectByPrimaryKey(instance.getPartnerId());
		PartnerLifecycleItemsExample example = new PartnerLifecycleItemsExample();
		example.createCriteria().andCurrentStepNotEqualTo("end").andPartnerInstanceIdEqualTo(instance.getId())
				.andBusinessTypeEqualTo(instance.getState());
		List<PartnerLifecycleItems> partnerLifecycleItemsList = partnerLifecycleItemsMapper.selectByExample(example);
		PartnerLifecycleItems partnerLifecycleItems = CollectionUtil.isEmpty(partnerLifecycleItemsList) ? null
				: partnerLifecycleItemsList.iterator().next();

		// 设置状态
		String stationApplySate = convertInstanceState2StationApplyState(instance.getType(), instance.getState(), partnerLifecycleItems);
		stationApply.setState(stationApplySate);
		
		
		stationApply.setModifier(instance.getModifier());
		stationApply.setGmtModified(instance.getGmtModified());
		
		switch (buildType) {
		case UPDATE_STATE:
			break;
		case DELETE:
			stationApply.setIsDeleted("y");
			break;
		case ADD:
			buildBaseInfo(stationApply, instance, station, partner);
			break;
		case UPDATE_BASE:
			buildBaseInfo(stationApply, instance, station, partner);
			break;
		case UPDATE_ALL:
			buildBaseInfo(stationApply, instance, station, partner);
			buildProtocolAndMoneyInfo(stationApply, instance, station, partner, partnerLifecycleItems);
			break;
		}
		return stationApply;
	}

	private void buildProtocolAndMoneyInfo(StationApply stationApply, PartnerStationRel instance, Station station, Partner partner,
			PartnerLifecycleItems partnerLifecycleItems) {

		/**
		 * account_money相关信息
		 * 
		 * thaw_time,frozen_time,frozen_money
		 */
		AccountMoneyExample example = new AccountMoneyExample();
		example.createCriteria().andIsDeletedEqualTo("n").andObjectIdEqualTo(instance.getId())
				.andTypeEqualTo(AccountMoneyTypeEnum.PARTNER_BOND.getCode())
				.andTargetTypeEqualTo(TargetTypeEnum.PARTNER_INSTANCE.getCode());
		List<AccountMoney> list = accountMoneyMapper.selectByExample(example);
		if (!CollectionUtils.isEmpty(list)) {
			AccountMoney accountMoney = list.iterator().next();
			stationApply.setFrozenTime(accountMoney.getFrozenTime());
			stationApply.setFrozenMoney(String.valueOf(accountMoney.getMoney().doubleValue()));
			stationApply.setThawTime(accountMoney.getThawTime());
		}

		/**
		 * protocol相关信息
		 * 
		 * confirmed_time ,protocol_version ,quit_protocol_version,
		 * manage_protocol_version,manage_protocol_confirmed
		 */

		List<ProtocolTypeEnum> protocolTypeList = Lists.newArrayList(ProtocolTypeEnum.SETTLE_PRO, ProtocolTypeEnum.MANAGE_PRO,
				ProtocolTypeEnum.PARTNER_QUIT_PRO);
		ProtocolExample pExample = new ProtocolExample();
		pExample.createCriteria().andIsDeletedEqualTo("n");
		List<Protocol> pList = protocolMapper.selectByExample(pExample);

		for (ProtocolTypeEnum p : protocolTypeList) {
			Protocol protocol = null;
			for (Protocol item : pList) {
				if (p.getCode().equals(item.getType())) {
					protocol = item;
					break;
				}
			}
			if (protocol == null) {
				throw new AugeServiceException("protocol not exists : " + p.getCode());
			}
			PartnerProtocolRelExample relExample = new PartnerProtocolRelExample();
			relExample.createCriteria().andIsDeletedEqualTo("n").andTargetTypeEqualTo(PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE.getCode())
					.andProtocolIdEqualTo(protocol.getId()).andObjectIdEqualTo(instance.getId());

			List<PartnerProtocolRel> prList = partnerProtocolRelMapper.selectByExample(relExample);
			if (CollectionUtils.isEmpty(prList)) {
				continue;
			}
			PartnerProtocolRel rel = prList.iterator().next();
			if (ProtocolTypeEnum.SETTLE_PRO.equals(p)) {
				stationApply.setConfirmedTime(rel.getConfirmTime());
				stationApply.setProtocolVersion(String.valueOf(protocol.getVersion()));
			} else if (ProtocolTypeEnum.MANAGE_PRO.equals(p)) {
				stationApply.setManageProtocolConfirmed(rel.getConfirmTime());
				stationApply.setManageProtocolVersion(String.valueOf(protocol.getVersion()));
			} else if (ProtocolTypeEnum.PARTNER_QUIT_PRO.equals(String.valueOf(protocol.getVersion()))) {
				stationApply.setQuitProtocolVersion(String.valueOf(protocol.getVersion()));
			}

		}

		// lifecycle,protocol_confirming_step
		if(null != partnerLifecycleItems && PartnerInstanceStateEnum.SETTLING.getCode().equals(partnerLifecycleItems.getBusinessType())){
			if(PartnerLifecycleCurrentStepEnum.BOND.getCode().equals(partnerLifecycleItems.getCurrentStep())){
				stationApply.setProtocolConfirmingStep("CONFIRMED");
			}
		}
		
		if(PartnerInstanceStateEnum.DECORATING.getCode().equals(instance.getState()) || PartnerInstanceStateEnum.SERVICING.getCode().equals(instance.getState())){
			stationApply.setProtocolConfirmingStep("FROZEN");
		}

		
		// customer_level,contact_date
		// submitted_people_name

	}

	/**
	 * 设置instance、partner、stationx逆袭到station_apply
	 * 
	 * @param stationApply
	 * @param instance
	 * @param station
	 * @param partner
	 */
	private void buildBaseInfo(StationApply stationApply, PartnerStationRel instance, Station station, Partner partner) {

		// 村点信息
		stationApply.setName(station.getName());
		stationApply.setCovered(station.getCovered());
		stationApply.setProducts(station.getProducts());
		stationApply.setLogisticsState(station.getLogisticsState());
		stationApply.setDescription(station.getDescription());
		stationApply.setFormat(station.getFormat());
		stationApply.setOwnOrgId(station.getApplyOrg());
		stationApply.setStationNum(station.getStationNum());
		stationApply.setAreaType(station.getAreaType());
		stationApply.setFixedType(station.getFixedType());

		stationApply.setProvince(station.getProvince());
		stationApply.setProvinceDetail(station.getProvinceDetail());
		stationApply.setCity(station.getCity());
		stationApply.setCityDetail(station.getCityDetail());
		stationApply.setCounty(station.getCounty());
		stationApply.setCountyDetail(station.getCountyDetail());
		stationApply.setTown(station.getTown());
		stationApply.setTownDetail(station.getTownDetail());
		stationApply.setAddressDetail(station.getAddress());
		stationApply.setLat(station.getLat());
		stationApply.setLng(station.getLng());
		stationApply.setVillage(station.getVillage());
		stationApply.setVillageDetail(station.getVillageDetail());
		stationApply.setCreator(station.getCreater());
		stationApply.setModifier(station.getModifier());

		String feature = station.getFeature();
		if (StringUtils.isNotBlank(feature)) {
			Map<String, String> featureMap = FeatureUtil.toMap(feature);
			stationApply.setVillageFlag(featureMap.get("villageFlag"));
			stationApply.setPlaceFlag(featureMap.get("placeFlag"));
			// station里存的是code，station_apply存的是中文描述
			stationApply.setCategory(changeStationCategoryCode2Value(featureMap.get("category")));
			stationApply.setManagementType(featureMap.get("managementType"));
		}

		// 人员信息
		stationApply.setTaobaoUserId(partner.getTaobaoUserId());
		stationApply.setTaobaoNick(partner.getTaobaoNick());
		stationApply.setEmail(partner.getEmail());
		stationApply.setAlipayAccount(partner.getAlipayAccount());
		stationApply.setIdenNum(partner.getIdenNum());
		stationApply.setMobile(partner.getMobile());
		stationApply.setBusinessType(partner.getBusinessType());
		stationApply.setApplierName(partner.getName());

		// 实例信息
		stationApply.setServiceBeginDate(instance.getServiceBeginTime());
		stationApply.setServiceEndDate(instance.getServiceEndTime());
		stationApply.setApplyTime(instance.getApplyTime());
		stationApply.setApplierType(instance.getApplierType());
		stationApply.setApplierId(instance.getApplierId());
		stationApply.setOpenDate(instance.getOpenDate());
		stationApply.setOperatorType(instance.getType());

		stationApply.setPartnerStationId(instance.getParentStationId());
		stationApply.setQuitType(instance.getCloseType());
		// 设置station_id
		String instanceState = instance.getState();
		if (!PartnerInstanceStateEnum.TEMP.getCode().equals(instanceState)
				&& !PartnerInstanceStateEnum.SETTLING.getCode().equals(instanceState)
				&& !PartnerInstanceStateEnum.SETTLE_FAIL.getCode().equals(instanceState)) {
			stationApply.setStationId(instance.getStationId());
		}

	}

	public static String convertInstanceState2StationApplyState(String partnerType, String instatnceState,
			PartnerLifecycleItems PartnerLifecycle) {
		if (PartnerInstanceStateEnum.TEMP.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.TEMP.getCode();
		} else if (PartnerInstanceStateEnum.SETTLING.getCode().equals(instatnceState)) {
			// if (PartnerLifecycle != null) {
			if (PartnerLifecycleCurrentStepEnum.ROLE_APPROVE.getCode().equals(PartnerLifecycle.getCurrentStep())) {
				if (PartnerInstanceTypeEnum.TPA.getCode().equals(partnerType)) {
					return StationApplyStateEnum.TPA_TEMP.getCode();
				}
			} else if (PartnerLifecycleCurrentStepEnum.SETTLED_PROTOCOL.getCode().equals(PartnerLifecycle.getCurrentStep())) {
				return StationApplyStateEnum.SUMITTED.getCode();
			} else if (PartnerLifecycleCurrentStepEnum.BOND.getCode().equals(PartnerLifecycle.getCurrentStep())) {
				return StationApplyStateEnum.CONFIRMED.getCode();
			}
			// }
		} else if (PartnerInstanceStateEnum.SETTLE_FAIL.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.TPA_AUDIT_FAIL.getCode();
		} else if (PartnerInstanceStateEnum.DECORATING.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.DECORATING.getCode();
		} else if (PartnerInstanceStateEnum.SERVICING.getCode().equals(instatnceState)) {
			if (PartnerInstanceTypeEnum.TPA.getCode().equals(partnerType)) {
				return StationApplyStateEnum.TPA_SERVICING.getCode();
			} else if (PartnerInstanceTypeEnum.TP.getCode().equals(partnerType)) {
				return StationApplyStateEnum.SERVICING.getCode();
			}
		} else if (PartnerInstanceStateEnum.CLOSING.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.QUIT_APPLYING.getCode();
		} else if (PartnerInstanceStateEnum.CLOSED.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.QUIT_APPLY_CONFIRMED.getCode();
		} else if (PartnerInstanceStateEnum.QUITING.getCode().equals(instatnceState)) {
			if (PartnerLifecycle != null) {
				if (PartnerLifecycleCurrentStepEnum.ROLE_APPROVE.getCode().equals(PartnerLifecycle.getCurrentStep())) {
					return StationApplyStateEnum.QUITAUDITING.getCode();
				} else if (PartnerLifecycleCurrentStepEnum.BOND.getCode().equals(PartnerLifecycle.getCurrentStep())) {
					return StationApplyStateEnum.CLOSED_WAIT_THAW.getCode();
				}
			}
		} else if (PartnerInstanceStateEnum.QUIT.getCode().equals(instatnceState)) {
			return StationApplyStateEnum.QUIT.getCode();
		}
		return null;

	}

	private static String changeStationCategoryCode2Value(String code) {
		if (code == null) {
			return null;
		}
		if (StationCategoryEnum.ZONGHE.getCode().equals(code)) {
			return StationCategoryEnum.ZONGHE.getDesc();
		}
		if (StationCategoryEnum.NONGZI.getCode().equals(code)) {
			return StationCategoryEnum.NONGZI.getDesc();
		}
		if (StationCategoryEnum.JIADIAN.getCode().equals(code)) {
			return StationCategoryEnum.JIADIAN.getDesc();
		}
		if (StationCategoryEnum.SANCSHUMA.getCode().equals(code)) {
			return StationCategoryEnum.SANCSHUMA.getDesc();
		}
		if (StationCategoryEnum.QIMOPEI.getCode().equals(code)) {
			return StationCategoryEnum.QIMOPEI.getDesc();
		}
		if (StationCategoryEnum.JIAZHUANG.getCode().equals(code)) {
			return StationCategoryEnum.JIAZHUANG.getDesc();
		}
		if (StationCategoryEnum.FUZHUANG.getCode().equals(code)) {
			return StationCategoryEnum.FUZHUANG.getDesc();
		}
		if (StationCategoryEnum.MUYING.getCode().equals(code)) {
			return StationCategoryEnum.MUYING.getDesc();
		}
		if (StationCategoryEnum.RIBAI.getCode().equals(code)) {
			return StationCategoryEnum.RIBAI.getDesc();
		}
		if (StationCategoryEnum.CANYIN.getCode().equals(code)) {
			return StationCategoryEnum.CANYIN.getDesc();
		}
		if (StationCategoryEnum.QITA.getCode().equals(code)) {
			return StationCategoryEnum.QITA.getDesc();
		}
		return null;
	}

}
