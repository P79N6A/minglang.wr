package com.taobao.cun.auge.station.service.impl;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.IdCardUtil;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerInstance;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.station.bo.AccountMoneyBO;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.CloseStationApplyBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerLifecycleBO;
import com.taobao.cun.auge.station.bo.PartnerProtocolRelBO;
import com.taobao.cun.auge.station.bo.ProtocolBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.AccountMoneyDto;
import com.taobao.cun.auge.station.dto.BondFreezingInfoDto;
import com.taobao.cun.auge.station.dto.CloseStationApplyDto;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.PartnerProtocolRelDto;
import com.taobao.cun.auge.station.dto.ProtocolDto;
import com.taobao.cun.auge.station.dto.ProtocolSigningInfoDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AccountMoneyStateEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTargetTypeEnum;
import com.taobao.cun.auge.station.enums.AccountMoneyTypeEnum;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleBusinessTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleSettledProtocolEnum;
import com.taobao.cun.auge.station.enums.PartnerProtocolRelTargetTypeEnum;
import com.taobao.cun.auge.station.enums.ProtocolTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.exception.enums.PartnerExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.security.util.SensitiveDataUtil;

@HSFProvider(serviceInterface = PartnerInstanceQueryService.class)
public class PartnerInstanceQueryServiceImpl implements PartnerInstanceQueryService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceQueryService.class);

	@Autowired
	PartnerStationRelExtMapper partnerStationRelExtMapper;

	@Autowired
	StationBO stationBO;
	@Autowired
	PartnerBO partnerBO;
	@Autowired
	ProtocolBO protocolBO;
	@Autowired
	AttachementBO attachementBO;
	@Autowired
	AccountMoneyBO accountMoneyBO;
	@Autowired
	PartnerProtocolRelBO partnerProtocolRelBO;
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	PartnerLifecycleBO partnerLifecycleBO;
	@Autowired
	CloseStationApplyBO closeStationApplyBO;

	@Override
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException {
		ValidateUtils.validateParam(condition);
		ValidateUtils.notNull(condition.getInstanceId());
		PartnerStationRel psRel = partnerInstanceBO.findPartnerInstanceById(condition.getInstanceId());
		if (psRel == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		PartnerInstanceDto insDto = PartnerInstanceConverter.convert(psRel);
		if (condition.getNeedPartnerInfo()) {
			Partner partner = partnerBO.getPartnerById(insDto.getPartnerId());
			PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
			if (condition.getNeedDesensitization()) {
				setSafedInfo(partnerDto);
			}
			partnerDto.setAttachements(attachementBO.selectAttachementList(partner.getId(), AttachementBizTypeEnum.PARTNER));
			insDto.setPartnerDto(partnerDto);
		}

		if (condition.getNeedStationInfo()) {
			Station station = stationBO.getStationById(insDto.getStationId());
			StationDto stationDto = StationConverter.toStationDto(station);
			stationDto.setAttachements(attachementBO.selectAttachementList(stationDto.getId(), AttachementBizTypeEnum.CRIUS_STATION));
			insDto.setStationDto(stationDto);
		}
		return insDto;
	}

	private void setSafedInfo(PartnerDto partnerDto) {
		if (partnerDto != null) {
			if (StringUtils.isNotBlank(partnerDto.getAlipayAccount())) {
				partnerDto.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(partnerDto.getAlipayAccount()));
			}
			if (StringUtils.isNotBlank(partnerDto.getName())) {
				partnerDto.setName(SensitiveDataUtil.customizeHide(partnerDto.getName(), 0, partnerDto.getName().length() - 1, 1));
			}
			if (StringUtil.isNotBlank(partnerDto.getIdenNum())) {
				partnerDto.setIdenNum(IdCardUtil.idCardNoHide(partnerDto.getIdenNum()));
			}
			if (StringUtil.isNotBlank(partnerDto.getTaobaoNick())) {
				partnerDto.setTaobaoNick(SensitiveDataUtil.taobaoNickHide(partnerDto.getTaobaoNick()));
			}
		}
	}

	@Override
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition) throws AugeServiceException {
		try {
			// FIXME FHH 方便测试，暂时写死
			PageHelper.startPage(1, 10);
			Page<PartnerInstance> page = partnerStationRelExtMapper
					.selectPartnerInstancesByExample(PartnerInstanceConverter.convert(pageCondition));
			PageDto<PartnerInstanceDto> result = PageDtoUtil.success(page, PartnerInstanceConverter.convert(page));

			return result;
		} catch (Exception e) {
			logger.error("queryByPage error,PartnerInstancePageCondition =" + JSON.toJSONString(pageCondition), e);
			return PageDtoUtil.unSuccess(pageCondition.getPageNum(), pageCondition.getPageSize());
		}
	}

	@Override
	public Long getPartnerInstanceId(Long stationApplyId) throws AugeServiceException {
		return partnerInstanceBO.getInstanceIdByStationApplyId(stationApplyId);
	}

	@Override
	public PartnerInstanceDto getActivePartnerInstance(Long taobaoUserId) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null == rel) {
			return null;
		}
		PartnerInstanceDto instance = PartnerInstanceConverter.convert(rel);

		Partner partner = partnerBO.getPartnerById(instance.getPartnerId());
		PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
		instance.setPartnerDto(partnerDto);

		Station station = stationBO.getStationById(instance.getStationId());
		StationDto stationDto = StationConverter.toStationDto(station);
		instance.setStationDto(stationDto);

		return instance;
	}

	@Override
	public AccountMoneyDto getAccountMoney(Long taobaoUserId, AccountMoneyTypeEnum type) {
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		if (null == rel) {
			return null;
		}
		return accountMoneyBO.getAccountMoney(type, AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, rel.getId());
	}

	@Override
	public CloseStationApplyDto getCloseStationApply(Long partnerInstanceId) throws AugeServiceException {
		return closeStationApplyBO.getCloseStationApply(partnerInstanceId);
	}

	@Override
	public ProtocolSigningInfoDto getProtocolSigningInfo(Long taobaoUserId, ProtocolTypeEnum type) throws AugeServiceException {
		ProtocolSigningInfoDto info = new ProtocolSigningInfoDto();
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
		condition.setInstanceId(rel.getId());
		PartnerInstanceDto instance = queryInfo(condition);
		ProtocolDto protocol = protocolBO.getValidProtocol(type);
		info.setPartnerInstance(instance);
		info.setProtocol(protocol);

		if (null == instance || null == protocol) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		// 走入驻生命周期表
		if (ProtocolTypeEnum.SETTLE_PRO.equals(type)) {
			PartnerLifecycleItems lifecycleItems = partnerLifecycleBO.getLifecycleItems(instance.getId(),
					PartnerLifecycleBusinessTypeEnum.SETTLING);

			// 合伙人当前不状态不为入驻中，或不存在入驻生命周期record
			if (!PartnerInstanceStateEnum.SETTLING.getCode().equals(instance.getState()) || null == lifecycleItems) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_STATE_NOT_APPLICABLE);
			}
			PartnerLifecycleSettledProtocolEnum itemState = PartnerLifecycleSettledProtocolEnum
					.valueof(lifecycleItems.getSettledProtocol());
			if (null == itemState) {
				logger.error(CommonExceptionEnum.DATA_UNNORMAL + "getProtocolSigningInfo: {},{}", taobaoUserId, type);
				throw new AugeServiceException(PartnerExceptionEnum.DATA_UNNORMAL);
			}
			info.setHasSigned(PartnerLifecycleSettledProtocolEnum.SIGNED.equals(itemState) ? true : false);
		} else if (ProtocolTypeEnum.MANAGE_PRO.equals(type)) {
			// 管理协议不走生命周期，随时可以签
			if (!PartnerInstanceStateEnum.unReSettlableStatusCodeList().contains(instance.getState())) {
				throw new AugeServiceException(PartnerExceptionEnum.PARTNER_STATE_NOT_APPLICABLE);
			}
			PartnerProtocolRelDto dto = partnerProtocolRelBO.getPartnerProtocolRelDto(type, instance.getId(),
					PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
			info.setHasSigned(null == dto ? false : true);
		}
		return info;
	}

	@Override
	public BondFreezingInfoDto getBondFreezingInfoDto(Long taobaoUserId) throws AugeServiceException {
		BondFreezingInfoDto info = new BondFreezingInfoDto();
		PartnerStationRel rel = partnerInstanceBO.getActivePartnerInstance(taobaoUserId);
		PartnerInstanceCondition condition = new PartnerInstanceCondition(true, true, false);
		condition.setInstanceId(rel.getId());
		PartnerInstanceDto instance = queryInfo(condition);
		AccountMoneyDto bondMoney = accountMoneyBO.getAccountMoney(AccountMoneyTypeEnum.PARTNER_BOND,
				AccountMoneyTargetTypeEnum.PARTNER_INSTANCE, instance.getId());
		PartnerProtocolRelDto settleProtocol = partnerProtocolRelBO.getPartnerProtocolRelDto(ProtocolTypeEnum.SETTLE_PRO, instance.getId(),
				PartnerProtocolRelTargetTypeEnum.PARTNER_INSTANCE);
		if (null == instance || null == bondMoney || null == settleProtocol || null == settleProtocol.getConfirmTime()) {
			logger.error("getBondFreezingInfoDto error, instance/bondMoney/settleProtocol is null: {}", taobaoUserId);
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		info.setPartnerInstance(instance);
		info.setAcountMoney(bondMoney);
		info.setProtocolConfirmTime(settleProtocol.getConfirmTime());
		if (AccountMoneyStateEnum.WAIT_FROZEN.getCode().equals(bondMoney.getState())) {
			info.setHasFrozen(false);
		} else if (AccountMoneyStateEnum.HAS_FROZEN.getCode().equals(bondMoney.getState())) {
			info.setHasFrozen(true);
		} else {
			logger.error(CommonExceptionEnum.DATA_UNNORMAL + "getBondFreezingInfoDto, {}", taobaoUserId);
			throw new AugeServiceException(CommonExceptionEnum.DATA_UNNORMAL);
		}
		return info;
	}

}
