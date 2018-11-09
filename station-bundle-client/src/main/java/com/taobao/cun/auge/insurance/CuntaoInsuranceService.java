package com.taobao.cun.auge.insurance;

import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;
import com.taobao.mtop.common.Result;

import java.util.Map;

public interface CuntaoInsuranceService {

	/**
	 * 查询个人信息
	 * @param taobaoUserId
	 * @param cpCode
	 * @return
	 */
	PersonInfoDto queryPersonInfo(Long taobaoUserId, String cpCode);

	/**
	 * 查询企业信息
	 * @param taobaoUserId
	 * @param cpCode
	 * @return
	 */
	BusinessInfoDto queryBusinessInfo(Long taobaoUserId, String cpCode);

	/**
	 * 是否买过保险
	 * @param taobaoUserId
	 * @return
	 */
	Boolean hasInsurance(Long taobaoUserId);

	/**
	 * 是否续签
	 * @param taobaoUserId
	 * @return
	 */
	Integer hasReInsurance(Long taobaoUserId);

	/**
	 * 包装了是否买过保险接口和是否续建接口
	 * 供无线端调用
	 * @param taobaoUserId
	 * @return
	 */
	Map<String,Object> hasInsuranceForMobile(Long taobaoUserId);

}
