package com.taobao.cun.auge.station.bo.impl;

import com.taobao.cun.auge.common.utils.DomainUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevel;
import com.taobao.cun.auge.dal.domain.PartnerInstanceLevelExample;
import com.taobao.cun.auge.dal.mapper.PartnerInstanceLevelMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceLevelBO;
import com.taobao.cun.auge.station.convert.PartnerInstanceLevelConverter;
import com.taobao.cun.auge.station.dto.PartnerInstanceLevelDto;
import com.taobao.hsf.remoting.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by jingxiao.gjx on 2016/7/29.
 */
@Component("partnerInstanceLevelBO")
public class PartnerInstanceLevelBOImpl implements PartnerInstanceLevelBO {
	@Autowired
	PartnerInstanceLevelMapper partnerInstanceLevelMapper;

	@Override
	public PartnerInstanceLevel getPartnerInstanceLevelByPartnerInstanceId(Long partnerInstanceId) {
		PartnerInstanceLevelExample example = new PartnerInstanceLevelExample();
		PartnerInstanceLevelExample.Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(partnerInstanceId);
		criteria.andIsDeletedEqualTo("n");
		criteria.andIsValidEqualTo("y");
		List<PartnerInstanceLevel> partnerInstanceLevels = partnerInstanceLevelMapper.selectByExample(example);
		if (CollectionUtils.isEmpty(partnerInstanceLevels)) {
			return null;
		}
		return partnerInstanceLevels.get(0);
	}

	@Override
	public void addPartnerInstanceLevel(PartnerInstanceLevelDto partnerInstanceLevelDto) {
		ValidateUtils.notNull(partnerInstanceLevelDto);
		ValidateUtils.notNull(partnerInstanceLevelDto.getPartnerInstanceId());

		PartnerInstanceLevel level = PartnerInstanceLevelConverter.toPartnerInstanceLevel(partnerInstanceLevelDto);
		DomainUtils.beforeInsert(level, partnerInstanceLevelDto.getOperator());
		level.setIsValid("y");
		partnerInstanceLevelMapper.insertSelective(level);
	}

	@Override
	public void invalidatePartnerInstanceLevelBefore(PartnerInstanceLevelDto partnerInstanceLevelDto) {
		PartnerInstanceLevel level = new PartnerInstanceLevel();
		level.setIsValid("n");
		DomainUtils.beforeUpdate(level, partnerInstanceLevelDto.getOperator());

		PartnerInstanceLevelExample example = new PartnerInstanceLevelExample();
		PartnerInstanceLevelExample.Criteria criteria = example.createCriteria();
		criteria.andTaobaoUserIdEqualTo(partnerInstanceLevelDto.getTaobaoUserId());
		criteria.andStationIdEqualTo(partnerInstanceLevelDto.getStationId());
		criteria.andIsDeletedEqualTo("n");
		criteria.andIsValidEqualTo("y");
		partnerInstanceLevelMapper.updateByExampleSelective(level, example);
	}
}
