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
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.dal.mapper.PartnerStationRelExtMapper;
import com.taobao.cun.auge.station.bo.AttachementBO;
import com.taobao.cun.auge.station.bo.PartnerBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.StationBO;
import com.taobao.cun.auge.station.condition.PartnerInstanceCondition;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.convert.PartnerConverter;
import com.taobao.cun.auge.station.convert.PartnerInstanceConverter;
import com.taobao.cun.auge.station.convert.StationConverter;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceDto;
import com.taobao.cun.auge.station.dto.StationDto;
import com.taobao.cun.auge.station.enums.AttachementBizTypeEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceQueryService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.security.util.SensitiveDataUtil;

@HSFProvider(serviceInterface = PartnerInstanceQueryService.class)
public class PartnerInstanceQueryServiceImpl implements PartnerInstanceQueryService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceQueryService.class);
	
	@Autowired
	PartnerStationRelExtMapper partnerStationRelExtMapper;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;
	@Autowired
	StationBO stationBO;
	@Autowired
	PartnerBO partnerBO;
	@Autowired
	AttachementBO attachementBO;

	@Override
	public PartnerInstanceDto queryInfo(PartnerInstanceCondition condition) throws AugeServiceException {
		ValidateUtils.validateParam(condition);
		ValidateUtils.notNull(condition.getInstanceId());
		PartnerStationRel psRel = partnerInstanceBO.findPartnerInstanceById(condition.getInstanceId());
		if (psRel == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		PartnerInstanceDto insDto= PartnerInstanceConverter.convert(psRel);
		if (condition.getNeedPartnerInfo()) {
			Partner partner = partnerBO.getPartnerById(insDto.getPartnerId());
			PartnerDto partnerDto = PartnerConverter.toPartnerDto(partner);
			if (condition.getNeedDesensitization()) {
				setSafedInfo(partnerDto);
			}
			partnerDto.setAttachements(attachementBO.selectAttachementList(partner.getId(),AttachementBizTypeEnum.PARTNER));
			insDto.setPartnerDto(partnerDto);
		}
		
		if (condition.getNeedStationInfo()) {
			Station station = stationBO.getStationById(insDto.getStationId());
			StationDto stationDto = StationConverter.toStationDto(station);
			stationDto.setAttachements(attachementBO.selectAttachementList(stationDto.getId(),AttachementBizTypeEnum.CRIUS_STATION));
			insDto.setStationDto(stationDto);
		}
		return insDto;
	}
	
	private void setSafedInfo(PartnerDto partnerDto) {
		if (partnerDto != null) {
			if (StringUtils.isNotBlank(partnerDto.getAlipayAccount())) {
				partnerDto
						.setAlipayAccount(SensitiveDataUtil.alipayLogonIdHide(partnerDto.getAlipayAccount()));
			}
			if (StringUtils.isNotBlank(partnerDto.getName())) {
				partnerDto.setName(SensitiveDataUtil.customizeHide(partnerDto.getName(),
						0, partnerDto.getName().length() - 1, 1));
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
	public PageDto<PartnerInstanceDto> queryByPage(PartnerInstancePageCondition pageCondition) {
		try {
			//FIXME FHH 方便测试，暂时写死
			PageHelper.startPage(1, 10);
			Page<PartnerInstance> page = partnerStationRelExtMapper.selectPartnerInstancesByExample(PartnerInstanceConverter.convert(pageCondition));
			PageDto<PartnerInstanceDto> result = PageDtoUtil.success(page, PartnerInstanceConverter.convert(page));

			return result;
		} catch (Exception e) {
			logger.error("queryByPage error,PartnerInstancePageCondition =" + JSON.toJSONString(pageCondition), e);
			return PageDtoUtil.unSuccess(pageCondition.getPageNum(), pageCondition.getPageSize());
		}
	}

	@Override
	public Long findPartnerInstanceId(Long stationApplyId) {
		return partnerInstanceBO.findPartnerInstanceId(stationApplyId);
	}

}
