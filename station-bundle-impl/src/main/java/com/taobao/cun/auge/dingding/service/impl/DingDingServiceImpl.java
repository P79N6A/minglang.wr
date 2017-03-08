package com.taobao.cun.auge.dingding.service.impl;

import java.util.Collections;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.taobao.cun.auge.dal.domain.DingtalkUser;
import com.taobao.cun.auge.dal.domain.DingtalkUserExample;
import com.taobao.cun.auge.dal.domain.DingtalkUserExample.Criteria;
import com.taobao.cun.auge.dal.mapper.DingtalkUserMapper;
import com.taobao.cun.auge.dingding.DingDingService;
import com.taobao.cun.auge.dingding.convert.DingTalkUserConverter;
import com.taobao.cun.auge.dingding.dto.DingTalkUserDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@HSFProvider(serviceInterface = DingDingService.class)
public class DingDingServiceImpl implements DingDingService {

	@Autowired
	DingtalkUserMapper dingtalkUserMapper;

	@Override
	public List<DingTalkUserDto> findDingTalkUser(List<String> taobaoUserIds) {
		if (CollectionUtils.isEmpty(taobaoUserIds)) {
			return Collections.<DingTalkUserDto> emptyList();
		}
		try {
			DingtalkUserExample example = new DingtalkUserExample();
			Criteria criteria = example.createCriteria();

			criteria.andUserIdIn(taobaoUserIds);
			criteria.andIsDeletedEqualTo("n");

			List<DingtalkUser> users = dingtalkUserMapper.selectByExample(example);

			return DingTalkUserConverter.convert(users);
		} catch (Exception e) {
			return Collections.<DingTalkUserDto> emptyList();
		}
	}

}
