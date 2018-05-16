package com.taobao.cun.auge.station.bo;

import java.util.List;

import com.taobao.cun.auge.dal.domain.Partner;
import com.taobao.cun.auge.station.dto.PartnerDto;
import com.taobao.cun.auge.station.dto.PartnerFlowerNameApplyDto;

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
	public Partner getNormalPartnerByTaobaoUserId(Long taobaoUserId) ;
	
	/**
	 * 根据淘宝id得到合伙人id
	 * @param taobaoUserId
	 */
	public Long  getNormalPartnerIdByTaobaoUserId(Long taobaoUserId) ;
	/**
	 * 新增
	 * @param partnerCondition
	 * @return 主键id
	 * @
	 */
	public Long  addPartner(PartnerDto partnerDto)  ;
	
	/**
	 * 修改
	 * @param partnerCondition
	 * @return 主键id
	 * @
	 */
	public void  updatePartner(PartnerDto partnerDto)  ;
	
	/**
	 * 根据主键查询partner
	 * @param id
	 * @return partner
	 * @
	 */
	public Partner getPartnerById(Long id) ;
	
	/**
	 * 删除合伙人表
	 * @param partnerId
	 * @param operator
	 * @
	 */
	public void deletePartner(Long partnerId,String operator) ;
	
	/**
	 * 根据阿里郎用户ID查询partner
	 * @param aliLangUserId
	 * @return partner
	 * @
	 */
	public Partner getPartnerByAliLangUserId(String aliLangUserId) ;
	
	/**
	 * 根据手机号获取合伙人
	 * @param mobile
	 * @return
	 * @
	 */
	public List<Partner> getPartnerByMobile(String mobile) ;
	
	   /**
     * 根据身份证获取合伙人
     * @param idnum
     * @return
     * @
     */
    public List<Partner> getPartnerByIdnum(String idnum) ;
	
	public void applyFlowName(PartnerFlowerNameApplyDto dto);
	
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetail(Long taobaoUserId);
	
	public void auditFlowerNameApply(Long id,boolean auditResult);
	
	public PartnerFlowerNameApplyDto getFlowerNameApplyDetailById(Long id);
}
