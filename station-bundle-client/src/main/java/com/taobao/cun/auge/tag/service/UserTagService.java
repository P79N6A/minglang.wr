package com.taobao.cun.auge.tag.service;

import java.util.List;

public interface UserTagService {

	 boolean addTag(Long taobaoUserId,String userTag);
	 
	 boolean removeTag(Long taobaoUserId,String userTag);
	 
	 boolean hasTag(Long taobaoUserId,String userTag);
	 
	 boolean initTPTag();
	 
	 boolean batchAddTag(List<Long> taobaoUserId,String userTag);
}
