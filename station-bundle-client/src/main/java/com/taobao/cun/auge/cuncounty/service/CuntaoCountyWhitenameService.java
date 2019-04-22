package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;

/**
 * 开县白名单
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyWhitenameService {
	/**
	 * 获取开县白名单
	 * @return
	 */
	List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenames();
}
