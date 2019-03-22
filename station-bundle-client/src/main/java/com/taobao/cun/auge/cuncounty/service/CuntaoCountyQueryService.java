package com.taobao.cun.auge.cuncounty.service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;

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
	
	/**
	 * 按条件查询
	 * @return
	 */
	PageDto<CuntaoCountyListItem> query(CuntaoCountyCondition condition);
}
