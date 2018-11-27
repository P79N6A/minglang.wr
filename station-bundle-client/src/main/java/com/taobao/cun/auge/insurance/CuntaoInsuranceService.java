package com.taobao.cun.auge.insurance;

import com.taobao.cun.auge.insurance.dto.BusinessInfoDto;
import com.taobao.cun.auge.insurance.dto.PersonInfoDto;

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
	 * 返回保险有效天数，与无线端约定
	 * 0 ：表示未购买保险
     * >0:距离保险止期的天数
	 * @param taobaoUserId
	 * @return
	 */
	Integer hasInsuranceForMobile(Long taobaoUserId);

}
