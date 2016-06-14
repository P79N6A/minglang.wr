package com.taobao.cun.auge.dingding;

import java.util.List;

import com.taobao.cun.auge.dingding.dto.DingTalkUserDto;

public interface DingDingService {

	public List<DingTalkUserDto> findDingTalkUser(List<String> taobaoUserIds); 
}
