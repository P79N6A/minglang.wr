package com.taobao.cun.auge.data.partner.service;

import java.util.List;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;
import com.taobao.cun.auge.data.partner.dto.UnLoginPartnerDto;

public interface PartnerAccessService {
	/**
	 * 查询最近的登录统计数据
	 * @param day
	 * @return
	 */
	List<PartnerAccessDto> queryPartnerAccessList(int day);

	/**
	 * 查询最近N天未访问的用户
	 * @param day
	 * @param pageNum
	 * @return
	 */
	PageDto<UnLoginPartnerDto> queryUnLoginPartners(int day, int pageNum);
}
