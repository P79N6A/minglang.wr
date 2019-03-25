package com.taobao.cun.auge.lx.bo;

import com.taobao.cun.auge.lx.dto.LxPartnerAddDto;
import com.taobao.cun.auge.lx.dto.LxPartnerListDto;

/**
 * 拉新伙伴bo
 * @author quanzhu.wangqz
 *
 */
public interface LxPartnerBO {

	/**
	 * 添加拉新伙伴
	 * @param param LxPartnerDto
	 * @return
	 */
	public Boolean addLxPartner(LxPartnerAddDto param);
	
	/**
	 * 根据村小二的userid 查询下属拉新伙伴
	 * @param taobaoUserId
	 * @return
	 */
	public LxPartnerListDto listLxPartner(Long taobaoUserId);
	/**
	 * 测试使用
	 * @param taobaoUserId
	 * @return
	 */
	public Boolean deleteByTaobaoUserId(Long taobaoUserId);
}
