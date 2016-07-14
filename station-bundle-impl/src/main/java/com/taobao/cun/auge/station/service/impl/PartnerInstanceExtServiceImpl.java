package com.taobao.cun.auge.station.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CollectionUtil;

@Service("partnerInstanceExtService")
@HSFProvider(serviceInterface = PartnerInstanceExtService.class)
public class PartnerInstanceExtServiceImpl implements PartnerInstanceExtService {

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;

	@Override
	public Boolean validateChildNum(Long parentStationId) {
		ValidateUtils.notNull(parentStationId);

		PartnerStationRel parent = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
		if (parent == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		Long instanceId = parent.getId();

		List<PartnerInstanceStateEnum> validChildStates = PartnerInstanceStateEnum.getValidChildStates();
		List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(instanceId, validChildStates);
		if (CollectionUtil.isEmpty(children)) {
			return Boolean.FALSE;
		}
		
		int childrenNum = children.size();
		Integer maxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);
		if (childrenNum >= maxChildNum) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

}
