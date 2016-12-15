package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.ResultUtils;
import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApply;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApplyExample;
import com.taobao.cun.auge.dal.domain.PartnerTypeChangeApplyExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerTypeChangeApplyMapper;
import com.taobao.cun.auge.station.bo.PartnerTypeChangeApplyBO;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.cun.auge.station.exception.AugeSystemException;

@Component("partnerTypeChangeApplyBO")
public class PartnerTypeChangeApplyBOImpl implements PartnerTypeChangeApplyBO {

	@Autowired
	PartnerTypeChangeApplyMapper partnerTypeChangeApplyMapper;

	@Override
	public Boolean isUpgradePartnerInstance(Long nextInstanceId) throws AugeServiceException, AugeSystemException {
		ValidateUtils.notNull(nextInstanceId);

		PartnerTypeChangeApplyExample example = new PartnerTypeChangeApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andNextPartnerInstanceIdEqualTo(nextInstanceId);

		int num = partnerTypeChangeApplyMapper.countByExample(example);

		return 0 != num;
	}

	@Override
	public PartnerTypeChangeApply getPartnerTypeChangeApply(Long upgradeInstanceId)	throws AugeServiceException, AugeSystemException {
		ValidateUtils.notNull(upgradeInstanceId);

		PartnerTypeChangeApplyExample example = new PartnerTypeChangeApplyExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		criteria.andNextPartnerInstanceIdEqualTo(upgradeInstanceId);

		List<PartnerTypeChangeApply> applies = partnerTypeChangeApplyMapper.selectByExample(example);

		return ResultUtils.selectOne(applies);
	}

}
