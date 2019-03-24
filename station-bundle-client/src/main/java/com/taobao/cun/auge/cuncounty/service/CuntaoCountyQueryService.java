package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import javax.validation.Valid;

import com.taobao.cun.auge.common.PageOutput;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyDetailDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyListItem;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyStateCountDto;

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
	PageOutput<CuntaoCountyListItem> query(@Valid CuntaoCountyCondition condition);
	
	/**
	 * 按状态分组统计
	 * @param condition
	 * @return
	 */
	List<CuntaoCountyStateCountDto> groupCountyByState(@Valid CuntaoCountyCondition condition);
}
