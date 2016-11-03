package com.taobao.cun.auge.station.check;

import com.taobao.cun.auge.station.exception.AugeBusinessException;
import com.taobao.cun.auge.station.exception.AugeSystemException;

public interface PartnerInstanceChecker {

	public void checkCloseApply(Long instanceId) throws AugeBusinessException, AugeSystemException;

	public void checkQuitApply(Long instanceId) throws AugeBusinessException, AugeSystemException;
}
