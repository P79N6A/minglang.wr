package com.taobao.cun.auge.tag.service;

public interface UserTagService {

	 boolean addTag(Long taobaoUserId,String userTag);
	 
	 boolean removeTag(Long taobaoUserId,String userTag);
	 
	 boolean hasTag(Long taobaoUserId,String userTag);
}
