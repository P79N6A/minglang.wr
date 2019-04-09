package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContactDto;

/**
 * 政府人员
 * @author chengyu.zhoucy
 *
 */
public interface CuntaoCountyGovContactService {
	/**
	 * 获取县政府全部联系人
	 * @param countyId
	 * @return
	 */
	List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts(Long countyId);
}
