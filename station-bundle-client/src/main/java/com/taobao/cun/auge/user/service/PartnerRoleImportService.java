package com.taobao.cun.auge.user.service;

import java.util.List;

/**
 * 导入角色
 * @author chengyu.zhoucy
 *
 */
public interface PartnerRoleImportService {
	
	/**
	 * 按服务站ID导入村小二角色
	 * @param stationIds
	 * @param orgId
	 * @param roleName
	 * @param creator
	 * @return
	 */
	String importRoleByStationIds(List<Long> stationIds, Long orgId, String roleName, String creator);
}
