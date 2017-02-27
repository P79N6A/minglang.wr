package com.taobao.cun.auge.station.adapter;

import java.util.List;
import java.util.Optional;

import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;

public interface SellerQualiServiceAdapter {

	/**
	 * 没有提交审核记录
	 */
	public static final int NO_AUDIT_RECORD = -1;
	
	/**
	 * 是否有有效淘宝资质
	 * @param taobaoUserId
	 * @return
	 */
	public boolean hasValidQuali(Long taobaoUserId);
	
	
	public Optional<EntityQuali> queryValidQuali(Long taobaoUserId);
	
	
	public UserQualiRecord lastAuditQualiStatus(Long taobaoUserId);
	
	public Optional<List<UserQualiRecord>> getUserQuailRecords(Long taobaoUserId);
	
	 Long getQualiInfoId();
}
