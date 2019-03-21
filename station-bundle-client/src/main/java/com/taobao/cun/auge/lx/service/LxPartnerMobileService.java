package com.taobao.cun.auge.lx.service;
import com.taobao.cun.auge.common.result.Result;
import com.taobao.cun.auge.lx.dto.LxPartnerAddDto;
import com.taobao.cun.auge.lx.dto.LxPartnerListDto;

/**
 * 拉新伙伴手机端服务
 * @author quanzhu.wangqz
 *
 */
public interface LxPartnerMobileService {
	
	/**
	 * 添加拉新伙伴
	 * @param param LxPartnerDto
	 * @return
	 */
	public Result<Boolean> addLxPartner(LxPartnerAddDto param);
	
	/**
	 * 根据村小二的userid 查询下属拉新伙伴
	 * @param taobaoUserId
	 * @return
	 */
	public Result<LxPartnerListDto> listLxPartner(Long taobaoUserId);
	
//	/**
//	 * 根据pid判断拉新伙伴是否生效
//	 * @param pid
//	 * @return
//	 */
//	public Result<Boolean> isServicing(String pid);
	
	
	
	public  Result<Boolean> checkFkByTaobaoUserId(Long taobaoUserId);
}
