package com.taobao.cun.auge.dingding.convert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.taobao.cun.auge.dal.domain.DingtalkUser;
import com.taobao.cun.auge.dingding.dto.DingTalkUserDto;
import com.taobao.vipserver.client.utils.CollectionUtils;

public final class DingTalkUserConverter {

	private DingTalkUserConverter() {

	}

	public static List<DingTalkUserDto> convert(List<DingtalkUser> users) {
		if (CollectionUtils.isEmpty(users)) {
			return Collections.<DingTalkUserDto> emptyList();
		}

		List<DingTalkUserDto> userDtos = new ArrayList<DingTalkUserDto>(users.size());
		for (DingtalkUser user : users) {

			if (null == user) {
				continue;
			}

			userDtos.add(convert(user));
		}

		return userDtos;

	}

	private static DingTalkUserDto convert(DingtalkUser user) {
		DingTalkUserDto userDto = new DingTalkUserDto();

		userDto.setUserId(user.getUserId());
		userDto.setDingUserId(user.getDingUserId());
		userDto.setType(user.getType());
		userDto.setActive(user.getActive());
		userDto.setDeptIdList(user.getDeptIdList());
		userDto.setPhone(user.getPhone());

		return userDto;
	}

}
