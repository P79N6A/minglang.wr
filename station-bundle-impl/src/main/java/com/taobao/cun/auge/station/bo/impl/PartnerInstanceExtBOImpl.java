package com.taobao.cun.auge.station.bo.impl;

import java.util.Collections;
import java.util.Date;
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
		Integer maxChildNum = findPartnerCurMaxChildNum(instanceId);
		
		//没有查询到，则返回默认值
		if(null == maxChildNum){
			return DEFAULT_MAX_CHILD_NUM;
		}
		return maxChildNum;
	}
	
	@Override
	public Integer findPartnerCurMaxChildNum(Long instanceId) {
		ValidateUtils.notNull(instanceId);
		
		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		
		List<PartnerInstanceExt> instanceExts = partnerInstanceExtMapper.selectByExample(example);
		
		//没有查询到，则返回默认值
		if(CollectionUtils.isEmpty(instanceExts)){
			return null;
		}
		return instanceExts.get(0).getMaxChildNum();
	}
	
	@Override
	public List<PartnerInstanceExt> findPartnerInstanceExts(List<Long> instanceIds){
		if(CollectionUtils.isEmpty(instanceIds)){
			return Collections.<PartnerInstanceExt>emptyList();
		}
		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();
		
		criteria.andPartnerInstanceIdIn(instanceIds);
		criteria.andIsDeletedEqualTo("n");
		
		return partnerInstanceExtMapper.selectByExample(example);
	}

	@Override
	public void updatePartnerMaxChildNum(Long instanceId, Integer maxNum,String operator) {
		ValidateUtils.notNull(instanceId);
		ValidateUtils.notNull(maxNum);
		
		PartnerInstanceExtExample example = new PartnerInstanceExtExample();
		Criteria criteria = example.createCriteria();
		criteria.andPartnerInstanceIdEqualTo(instanceId);
		criteria.andIsDeletedEqualTo("n");
		
		PartnerInstanceExt record = new PartnerInstanceExt();
		record.setMaxChildNum(maxNum);
		record.setModifier(operator);
		record.setGmtModified(new Date());		
		partnerInstanceExtMapper.updateByExampleSelective(record, example);
		
	}
}
