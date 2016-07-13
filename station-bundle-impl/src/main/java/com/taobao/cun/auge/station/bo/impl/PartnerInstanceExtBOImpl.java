package com.taobao.cun.auge.station.bo.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.taobao.cun.auge.common.utils.ValidateUtils;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExt;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExtExample;
import com.taobao.cun.auge.dal.domain.PartnerInstanceExtExample.Criteria;
import com.taobao.cun.auge.dal.mapper.PartnerInstanceExtMapper;
import com.taobao.cun.auge.station.bo.PartnerInstanceExtBO;

@Component("partnerInstanceExtBO")
public class PartnerInstanceExtBOImpl implements PartnerInstanceExtBO {
	
	//合伙人下一级淘帮手默认名额
	private final static Integer DEFAULT_MAX_CHILD_NUM = 3;

	@Autowired
	PartnerInstanceExtMapper partnerInstanceExtMapper;
	
	@Override
	public Integer findPartnerMaxChildNum(Long instanceId) {
		ValidateUtils.notNull(instanceId);
		
		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		
		List<PartnerInstanceExt> instanceExts = partnerInstanceExtMapper.selectByExample(example);
		
		//没有查询到，则返回默认值
		if(CollectionUtils.isEmpty(instanceExts) || null == instanceExts.get(0).getMaxChildNum()){
			return DEFAULT_MAX_CHILD_NUM;
		}
		return instanceExts.get(0).getMaxChildNum();
	}

}
