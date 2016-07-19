package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceExtService")
@HSFProvider(serviceInterface = PartnerInstanceExtService.class)
public class PartnerInstanceExtServiceImpl implements PartnerInstanceExtService {
	
	// 默认初始化配额
	private final static Integer DEFAULT_MAX_CHILD_NUM = 3;

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

		Integer childrenNum = partnerInstanceExtBO.findPartnerChildrenNum(instanceId);
		Integer maxChildNum = partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);
		return childrenNum >= maxChildNum;
	}

	@Override
	public List<PartnerInstanceExtDto> findPartnerExtInfos(List<Long> instanceIds) {
		if (CollectionUtils.isEmpty(instanceIds)) {
			return Collections.<PartnerInstanceExtDto> emptyList();
		}

		List<PartnerInstanceExt> instanceExts = partnerInstanceExtBO.findPartnerInstanceExts(instanceIds);
		List<PartnerInstanceExtDto> instanceExtDtos = new ArrayList<PartnerInstanceExtDto>(instanceExts.size());
		for (PartnerInstanceExt instanceExt : instanceExts) {
			if (null == instanceExt) {
				continue;
			}
			Long partnerInstanceId = instanceExt.getPartnerInstanceId();

			PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

			instanceExtDto.setInstanceId(partnerInstanceId);
			instanceExtDto.setMaxChildNum(instanceExt.getMaxChildNum());
			Integer childrenNum = partnerInstanceExtBO.findPartnerChildrenNum(partnerInstanceId);
			instanceExtDto.setCurChildNum(childrenNum);

			instanceExtDtos.add(instanceExtDto);

		}
		return instanceExtDtos;
	}
	
	@Override
	public void savePartnerExtInfo(PartnerInstanceExtDto instanceExtDto){
		ValidateUtils.notNull(instanceExtDto);
		ValidateUtils.notNull(instanceExtDto.getInstanceId());
		
		PartnerInstanceExt instanceExt=	partnerInstanceExtBO.findPartnerInstanceExt(instanceExtDto.getInstanceId());
		if(null == instanceExt){
			partnerInstanceExtBO.addPartnerInstanceExt(instanceExtDto);
		}
		
		partnerInstanceExtBO.updatePartnerInstanceExt(instanceExtDto);
	}
}
