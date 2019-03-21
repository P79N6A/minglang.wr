package com.taobao.cun.auge.cuncounty.service;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;

/**
 * 县服务中心查询
 * 
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyQueryService {
	/**
	 * 查询一个县服务中心详情
	 * @param countyId
	 * @return
	 */
	CuntaoCountyDetailDto getCuntaoCountyDetail(Long countyId);
}
