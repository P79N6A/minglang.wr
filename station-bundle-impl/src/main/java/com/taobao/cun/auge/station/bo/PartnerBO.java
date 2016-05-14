package com.taobao.cun.auge.station.bo;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.condition.PartnerCondition;
import com.taobao.cun.auge.station.exception.AugeServiceException;

/**
 * 合伙人表查询服务
 * @author quanzhu.wangqz
 *
 */
public interface PartnerBO {

	/**
	 * 根据淘宝id查询有效的合伙人信息
	 *  @param taobaoUserId
	 */
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	
	/**
	 * 根据淘宝id得到合伙人id
	 * @param taobaoUserId
	 */
	public Long  getNormalPartnerIdByTaobaoUserId(Long taobaoUserId) throws AugeServiceException;
	/**
	 * 新增
	 * @param partnerCondition
	 * @return 主键id
	 * @throws AugeServiceException
	 */
	public Long  addPartner(PartnerCondition partnerCondition,String operator)  throws AugeServiceException;
	
	/**
	 * 修改
	 * @param partnerCondition
	 * @return 主键id
	 * @throws AugeServiceException
	 */
	public void  updatePartner(PartnerCondition partnerCondition,String operator)  throws AugeServiceException;
	
	/**
	 * 根据主键查询partner
	 * @param id
	 * @return partner
	 * @throws AugeServiceException
	 */
	public Partner getPartnerById(Long id) throws AugeServiceException;
	
}
