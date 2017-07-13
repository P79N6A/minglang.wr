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
import com.taobao.cun.auge.common.OperatorDto;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.configuration.TpaGmvCheckConfiguration;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.convert.PartnerChildMaxNumChangeEventConverter;
import com.taobao.cun.auge.station.dto.PartnerChildMaxNumUpdateDto;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.enums.PartnerMaxChildNumChangeReasonEnum;
import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.enums.CommonExceptionEnum;
import com.taobao.cun.auge.station.service.PartnerInstanceExtService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("partnerInstanceExtService")
@HSFProvider(serviceInterface = PartnerInstanceExtService.class)
public class PartnerInstanceExtServiceImpl implements PartnerInstanceExtService {

	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceExtService.class);

	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Autowired
	PartnerInstanceExtBO partnerInstanceExtBO;
	
	@Autowired
	TpaGmvCheckConfiguration tpaGmvCheckConfiguration;

	@Override
	public Integer findPartnerMaxChildNum(Long partnerStationId) {
		ValidateUtils.notNull(partnerStationId);

		PartnerStationRel parent = partnerInstanceBO.findPartnerInstanceByStationId(partnerStationId);
		if (parent == null) {
			throw new AugeBusinessException(CommonExceptionEnum.RECORD_IS_NULL);
		}
		Long instanceId = parent.getId();
		return partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);
	}

	@Override
	public Integer findPartnerMaxChildNumByInsId(Long instanceId) {
		ValidateUtils.notNull(instanceId);

		return partnerInstanceExtBO.findPartnerMaxChildNum(instanceId);
	}

	@Override
	public Boolean validateChildNum(Long parentStationId) {
		ValidateUtils.notNull(parentStationId);

		PartnerStationRel parent = partnerInstanceBO.findPartnerInstanceByStationId(parentStationId);
		if (parent == null) {
			throw new AugeBusinessException(CommonExceptionEnum.RECORD_IS_NULL);
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
	public void initPartnerMaxChildNum(Long instanceId, Integer maxChildNum, OperatorDto operatorDto) {
		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();
		instanceExtDto.setInstanceId(instanceId);
		instanceExtDto.setMaxChildNum(maxChildNum);
		instanceExtDto.copyOperatorDto(operatorDto);
		savePartnerMaxChildNum(instanceExtDto, PartnerMaxChildNumChangeReasonEnum.INIT);
	}

	@Override
	public void updatePartnerMaxChildNum(PartnerChildMaxNumUpdateDto updateDto) {
		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();
		instanceExtDto.setInstanceId(updateDto.getInstanceId());
		instanceExtDto.setMaxChildNum(updateDto.getMaxChildNum());
		instanceExtDto.setChildNumChangDate(updateDto.getChildNumChangDate());
		instanceExtDto.copyOperatorDto(updateDto);
		savePartnerMaxChildNum(instanceExtDto, updateDto.getReason());
	}

	@Override
	public void addPartnerMaxChildNum(Long instanceId, Integer increaseNum, PartnerMaxChildNumChangeReasonEnum reason,
			OperatorDto operatorDto) {
		// 查询当前实例扩展
		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceId);

		// 当前最大配额
		Integer curMaxChildNum = null != instanceExt ? instanceExt.getMaxChildNum() : 0;

		// 已经达到最大配额10，则返回
		if (curMaxChildNum >= tpaGmvCheckConfiguration.getMaxTpaNum4Tp()) {
			return;
		}

		Integer childNum = curMaxChildNum + increaseNum;
		// 最大配额校验
		childNum = childNum >= tpaGmvCheckConfiguration.getMaxTpaNum4Tp() ? tpaGmvCheckConfiguration.getMaxTpaNum4Tp()
				: childNum;

		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

		instanceExtDto.setInstanceId(instanceId);
		instanceExtDto.setMaxChildNum(childNum);
		instanceExtDto.copyOperatorDto(operatorDto);
		savePartnerMaxChildNum(instanceExtDto, reason);
	}

	private void savePartnerMaxChildNum(PartnerInstanceExtDto instanceExtDto,
			PartnerMaxChildNumChangeReasonEnum reason) {
		ValidateUtils.notNull(instanceExtDto);
		ValidateUtils.notNull(instanceExtDto.getInstanceId());

		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceExtDto.getInstanceId());
		if (null == instanceExt) {
			partnerInstanceExtBO.addPartnerInstanceExt(instanceExtDto);
			logger.info(
					"PartnerInstanceExt isnot exist.add PartnerInstanceExtDto = " + JSON.toJSONString(instanceExtDto));
		} else {
			partnerInstanceExtBO.updatePartnerInstanceExt(instanceExtDto);
			logger.info("update PartnerInstanceExt.PartnerInstanceExtDto=" + JSON.toJSONString(instanceExtDto));
		}

		// 发送变更事件
		PartnerChildMaxNumChangeEventConverter.dispatchChangeEvent(instanceExtDto, reason);
	}

	@Override
	public void decreasePartnerMaxChildNum(Long instanceId, Integer decreaseNum, PartnerMaxChildNumChangeReasonEnum reason, OperatorDto operatorDto) {
		// 查询当前实例扩展
		PartnerInstanceExt instanceExt = partnerInstanceExtBO.findPartnerInstanceExt(instanceId);
		
		//保护
		if(null == instanceExt){
			return;
		}
		
		// 当前最大配额
		Integer curMaxChildNum =  instanceExt.getMaxChildNum();
		
		// 已经达到初始值3，则返回
		if (curMaxChildNum <= tpaGmvCheckConfiguration.getDefaultTpaNum4Tp()) {
			return;
		}
		//计算新值
		Integer childNum = curMaxChildNum - decreaseNum;
		// 最小配额校验
		childNum = childNum <= tpaGmvCheckConfiguration.getDefaultTpaNum4Tp() ? tpaGmvCheckConfiguration.getDefaultTpaNum4Tp()
				: childNum;
		
		PartnerInstanceExtDto instanceExtDto = new PartnerInstanceExtDto();

		instanceExtDto.setInstanceId(instanceId);
		instanceExtDto.setMaxChildNum(childNum);
		instanceExtDto.copyOperatorDto(operatorDto);
		savePartnerMaxChildNum(instanceExtDto, reason);
	}
}
