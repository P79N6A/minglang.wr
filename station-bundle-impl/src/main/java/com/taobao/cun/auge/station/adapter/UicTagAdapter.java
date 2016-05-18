package com.taobao.cun.auge.station.adapter;

import com.taobao.cun.auge.station.dto.UserTagDto;

public interface UicTagAdapter {
	/**
	 * 用户打标，适用于合伙人、淘帮手和村拍档
	 * @param userTagDto
	 */
	public void addUserTag(UserTagDto userTagDto);

	/**
	 * 用户去标，适用于合伙人、淘帮手和村拍档
	 * @param userTagDto
	 */
	public void removeUserTag(UserTagDto userTagDto);

}
