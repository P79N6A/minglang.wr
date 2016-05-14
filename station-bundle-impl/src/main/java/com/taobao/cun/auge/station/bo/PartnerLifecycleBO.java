package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.station.condition.PartnerLifecycleCondition;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 生命周期扩展表 服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerLifecycleBO {

	public void addLifecycle(PartnerLifecycleCondition partnerLifecycle,String operator)throws AugeServiceException;
}
