package com.taobao.cun.auge.station.service.impl;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.alibaba.common.lang.StringUtil;
import com.taobao.cun.attachment.enums.AttachmentBizTypeEnum;
import com.taobao.cun.attachment.service.AttachmentService;
import com.taobao.cun.auge.common.utils.IdCardUtil;
import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.partner.service.PartnerQueryService;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.security.util.SensitiveDataUtil;

/**
 * Created by jingxiao.gjx on 2016/8/31.
 */
@Service("partnerQueryService")
@HSFProvider(serviceInterface = PartnerQueryService.class)
public class PartnerQueryServiceImpl implements PartnerQueryService {

	@Autowired
	PartnerBO partnerBO;

	@Autowired
	AttachmentService criusAttachmentService;

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public PartnerDto queryPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException {
		Partner partner = partnerBO.getNormalPartnerByTaobaoUserId(taobaoUserId);
		return convertPartnerToDto(partner);
	}

	@Override
	public PartnerDto queryPartner(Long partnerId) throws AugeServiceException {
		Partner partner = partnerBO.getPartnerById(partnerId);
		return convertPartnerToDto(partner);
	}

	@Override
	public PartnerDto queryPartnerByStationId(Long stationId) throws AugeServiceException {
		PartnerStationRel rel = partnerInstanceBO.findPartnerInstanceByStationId(stationId);
		Assert.notNull(rel, "partner instance not exists");
		Long partnerId = rel.getPartnerId();
		Assert.notNull(partnerId, "partner instance type is null");
		return queryPartner(partnerId);
	}

	@NotNull
	private PartnerDto convertPartnerToDto(Partner partner) {
		Assert.notNull(partner, "partner not exist");
		PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
		if (StringUtils.isNotEmpty(partnerDto.getAlipayAccount())) {
			partnerDto.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(partnerDto.getAlipayAccount()));
		}
		if (StringUtils.isNotEmpty(partnerDto.getName())) {
			partnerDto.setName(SensitiveDataUtil.customizeHide(partnerDto.getName(), 0, partnerDto.getName().length() - 1, 1));
		}
		if (StringUtil.isNotEmpty(partnerDto.getIdenNum())) {
			partnerDto.setIdenNum(IdCardUtil.idCardNoHide(partnerDto.getIdenNum()));
		}
		if (StringUtil.isNotEmpty(partnerDto.getTaobaoNick())) {
			partnerDto.setTaobaoNick(SensitiveDataUtil.taobaoNickHide(partnerDto.getTaobaoNick()));
		}
		partnerDto.setAttachments(criusAttachmentService.getAttachmentList(partner.getId(), AttachmentBizTypeEnum.PARTNER));
		return partnerDto;
	}
}
