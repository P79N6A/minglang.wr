package com.taobao.cun.auge.data.partner.service;

import java.util.List;

import com.taobao.cun.auge.data.partner.dto.PartnerAccessDto;

public interface PartnerAccessService {
	/**
	 * 查询最近的登录统计数据
	 * @param day
	 * @return
	 */
	List<PartnerAccessDto> queryPartnerAccessList(int day);
}
