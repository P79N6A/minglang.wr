package com.taobao.cun.auge.station.service;

import com.taobao.cun.auge.station.dto.UserTagDto;

public interface UicTagService {
	
	/**
	 * 打标
	 * @param userTagDto
	 */
	public void addUserTag(UserTagDto userTagDto);

	/**
	 * 去标
	 * 
	 * @param userTagDto
	 */
	public void removeUserTag(UserTagDto userTagDto);
}
