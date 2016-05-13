package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 合伙人表查询服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerBO {

	/**
	 * 根据淘宝id查询有效的合伙人信息
	 */
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 根据淘宝id得到合伙人id
	 */
	public Long  getNormalPartnerIdByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
}
