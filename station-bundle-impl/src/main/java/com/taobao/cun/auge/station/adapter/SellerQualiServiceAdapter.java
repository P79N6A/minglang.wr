package com.taobao.cun.auge.station.adapter;

import java.util.List;
import java.util.Optional;

import com.alibaba.pm.sc.api.quali.dto.EntityQuali;
import com.alibaba.pm.sc.api.quali.dto.ListHidByEidAndEidTypeResponse;
import com.alibaba.pm.sc.api.quali.dto.UserQualiRecord;
import com.taobao.cun.auge.dal.domain.CuntaoQualification;

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
	
	
	public Optional<EntityQuali> queryQuali(Long taobaoUserId);
	
	public Optional<EntityQuali> queryQualiById(Long qualiId,int eidType);
	
	public UserQualiRecord lastAuditQualiStatus(Long taobaoUserId);
	
	public Optional<ListHidByEidAndEidTypeResponse> queryHavanaIdByQuali(String eid, int eidType);
	
	public Optional<List<UserQualiRecord>> getUserQuailRecords(Long taobaoUserId);
	
	public void insertQualiRecord(CuntaoQualification qualification);
	
	public Optional<EntityQuali> queryEnterpriceQualiById(Long havanaId);
}
