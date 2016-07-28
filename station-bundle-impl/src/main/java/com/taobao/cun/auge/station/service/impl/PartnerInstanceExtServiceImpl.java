package com.taobao.cun.auge.station.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.event.EventConstant;
import com.taobao.cun.auge.event.PartnerChildMaxNumChangeEvent;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.cun.crius.event.client.EventDispatcher;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceExtService")
@HSFProvider(serviceInterface = PartnerInstanceExtService.class)
public class PartnerInstanceExtServiceImpl implements PartnerInstanceExtService {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceExtService.class);
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;
	
	@Override
	public Integer findPartnerMaxChildNum(Long partnerStationId) {
		ValidateUtils.notNull(partnerStationId);

		PartnerStationRel parent = partnerInstanceBO.findPartnerInstanceByStationId(partnerStationId);
		if (parent == null) {
			throw new AugeServiceException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		Long instanceId = parent.getId();
		return partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);
	}

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

		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceExtDto.getInstanceId());
		if (null == instanceExt) {
			partnerInstanceExtBO.addPartnerInstanceExt(instanceExtDto);
			logger.info(
					"PartnerInstanceExt isnot exist.add PartnerInstanceExtDto = " + JSON.toJSONString(instanceExtDto));
		}

		partnerInstanceExtBO.updatePartnerInstanceExt(instanceExtDto);
		logger.info("update PartnerInstanceExt.PartnerInstanceExtDto=" + JSON.toJSONString(instanceExtDto));

		Integer maxChildNum = instanceExtDto.getMaxChildNum();
		//如果修改了子成员最大名额，则，发事件
		if (null != maxChildNum) {
			PartnerChildMaxNumChangeEvent event = new PartnerChildMaxNumChangeEvent();
			event.setPartnerInstanceId(instanceExtDto.getInstanceId());
			event.setChildMaxNum(maxChildNum);
			event.setBizMonth(instanceExtDto.getChildNumChangDate());
			event.copyOperatorDto(instanceExtDto);

			EventDispatcher.getInstance().dispatch(EventConstant.PARTNER_CHILD_MAX_NUM_CHANGE_EVENT, event);
		}
	}
}
