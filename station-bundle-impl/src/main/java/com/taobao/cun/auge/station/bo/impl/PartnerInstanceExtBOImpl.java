package com.taobao.cun.auge.station.bo.impl;

import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExtExample;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExtExample.Criteria;
import com.taobao.cun.auge.dal.domain.PartnerStationRel;
import com.taobao.cun.auge.dal.mapper.PartnerInstanceExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceBO;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceExtConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceExtDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component("partnerInstanceExtBO")
public class PartnerInstanceExtBOImpl implements PartnerInstanceExtBO {
	
	private static final Logger logger = LoggerFactory.getLogger(PartnerInstanceExtBO.class);

	@Autowired
	PartnerInstanceExtMapper partnerInstanceExtMapper;
	
	@Autowired
	PartnerInstanceBO partnerInstanceBO;

	@Override
	public Integer findPartnerMaxChildNum(Long instanceId) {
		ValidateUtils.notNull(instanceId);

		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();

		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");

		List<PartnerInstanceExt> instanceExts = partnerInstanceExtMapper.selectByExample(example);

		// 没有查询到,或者没有配额
		if (CollectionUtils.isEmpty(instanceExts) || null ==instanceExts.get(0).getMaxChildNum()) {
			return 0;
		}
		return instanceExts.get(0).getMaxChildNum();
	}
	
	@Override
	public Integer findPartnerChildrenNum(Long instanceId) {
		try {
			List<PartnerInstanceStateEnum> validChildStates = PartnerInstanceStateEnum.getValidChildStates();
			List<PartnerStationRel> children = partnerInstanceBO.findChildPartners(instanceId, validChildStates);

			return CollectionUtils.size(children);
		} catch (Exception e) {
			logger.error("查询合伙人子成员数量失败。instanceId=" + instanceId, e);
			return 0;
		}
	}

	@Override
	public List<PartnerInstanceExt> findPartnerInstanceExts(List<Long> instanceIds) {
		if (CollectionUtils.isEmpty(instanceIds)) {
			return Collections.<PartnerInstanceExt> emptyList();
		}
		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();

		criteria.andPartnerInstanceIdIn(instanceIds);
		criteria.andIsDeletedEqualTo("n");

		return partnerInstanceExtMapper.selectByExample(example);
	}
	
	@Override
	public PartnerInstanceExt findPartnerInstanceExt(Long instanceId) {
		ValidateUtils.notNull(instanceId);

		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();

		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");

		List<PartnerInstanceExt> instanceExts = partnerInstanceExtMapper.selectByExample(example);

		if (CollectionUtils.isEmpty(instanceExts)) {
			return null;
		}

		return instanceExts.get(0);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public void updatePartnerInstanceExt(PartnerInstanceExtDto instanceExtDto) {
		ValidateUtils.notNull(instanceExtDto);
		ValidateUtils.notNull(instanceExtDto.getInstanceId());

		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceExtDto.getInstanceId());
		criteria.andIsDeletedEqualTo("n");

		PartnerInstanceExt record = PartnerInstanceExtConverter.convert(instanceExtDto);
		DomainUtils.beforeUpdate(record, instanceExtDto.getOperator());
		partnerInstanceExtMapper.updateByExampleSelective(record, example);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, readOnly = false, rollbackFor = Exception.class)
	public Long addPartnerInstanceExt(PartnerInstanceExtDto instanceExtDto) {
		ValidateUtils.notNull(instanceExtDto);
		ValidateUtils.notNull(instanceExtDto.getInstanceId());

		PartnerInstanceExt instanceExt = PartnerInstanceExtConverter.convert(instanceExtDto);
		DomainUtils.beforeInsert(instanceExt, instanceExtDto.getOperator());
		partnerInstanceExtMapper.insertSelective(instanceExt);

		return instanceExt.getId();
	}
}
