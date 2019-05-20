package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyWhitenameAddDto;

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
	
	/**
	 * 添加白名单
	 */
	void addCuntaoCountyWhitename(CuntaoCountyWhitenameAddDto cuntaoCountyWhitenameAddDto);
	
	/**
	 * 删除白名单，如果白名单已经开点，那么它不可删除
	 * @param id
	 * @param operator
	 */
	void delete(Long id, String operator);
	/**
	 * 开启或关闭指定白名单
	 * @param id
	 * @param operator
	 */
	void toggle(Long id, String operator);
	
	/**
	 * 分页查询没有开点的白名单
	 * 
	 * @param condition
	 * @return
	 */
	PageDto<CuntaoCountyWhitenameDto> query(CuntaoCountyWhitenameCondition condition);
	
	/**
	 * 按县或者市CODE批量查询
	 * @param countyCodes
	 * @return
	 */
	List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenamesByCodes(List<String> countyCodes);
}
