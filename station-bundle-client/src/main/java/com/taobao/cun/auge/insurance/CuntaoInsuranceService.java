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
	 * 返回保险有效天数，与无线端约定
	 * 0 ：表示未购买保险
	 * 0-提醒时间:表示购买过保险单有效期在提醒时间内
	 * 365：购买过保险且有效期超过提醒时间
	 * @param taobaoUserId
	 * @return
	 */
	Integer hasInsuranceForMobile(Long taobaoUserId);

}
