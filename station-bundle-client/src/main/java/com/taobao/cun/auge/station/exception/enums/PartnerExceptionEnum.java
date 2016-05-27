package com.taobao.cun.auge.station.exception.enums;

/**
 * 合伙人表异常枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerExceptionEnum extends CommonExceptionEnum {

	private static final long serialVersionUID = -2325045809951918493L;

	public static final PartnerExceptionEnum NO_RECORD = new PartnerExceptionEnum(
			"NO_RECORD", "合伙人信息不存在");
	public static final PartnerExceptionEnum ID_IS_NULL = new PartnerExceptionEnum(
			"ID_IS_NULL", "合伙人id为空");
	public static final PartnerExceptionEnum PARTNER_TAOBAONICK_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_TAOBAONICK_IS_NULL", "申请人淘宝会员名不能为空");
	
	public static final PartnerExceptionEnum PARTNER_IDENNUM_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_IDENNUM_IS_NULL", "身份证号码不能为空");
	
	public static final PartnerExceptionEnum PARTNER_ALIPAYACCOUNT_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_ALIPAYACCOUNT_IS_NULL", "申请人支付宝账号不能为空");
	
	public static final PartnerExceptionEnum PARTNER_NAME_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_NAME_IS_NULL", "申请人姓名不能为空");
	public static final PartnerExceptionEnum PARTNER_ALIPAYACCOUNT_NOTEQUAL = new PartnerExceptionEnum(
			"PARTNER_ALIPAYACCOUNT_NOTEQUAL", "您录入的支付宝账号与淘宝绑定的不一致，请联系申请人核对");
	public static final PartnerExceptionEnum PARTNER_PERSION_INFO_NOTEQUAL = new PartnerExceptionEnum(
			"PARTNER_PERSION_INFO_NOTEQUAL", "目前支付宝认证的归属人，与您提交的申请人信息不符，请联系申请人核对");
	
	public static final PartnerExceptionEnum PARTNER_TAOBAOUSERID_HAS_USED = new PartnerExceptionEnum(
			"PARTNER_TAOBAOUSERID_HAS_USED", "该账号已经存在活动的加盟申请");
	

	static {
		mappings.put("NO_RECORD", NO_RECORD);
		mappings.put("ID_IS_NULL", ID_IS_NULL);
		mappings.put("PARTNER_TAOBAONICK_IS_NULL", PARTNER_TAOBAONICK_IS_NULL);
		mappings.put("PARTNER_IDENNUM_IS_NULL", PARTNER_IDENNUM_IS_NULL);
		mappings.put("PARTNER_ALIPAYACCOUNT_IS_NULL", PARTNER_ALIPAYACCOUNT_IS_NULL);
		mappings.put("PARTNER_NAME_IS_NULL", PARTNER_NAME_IS_NULL);
		mappings.put("PARTNER_ALIPAYACCOUNT_NOTEQUAL", PARTNER_ALIPAYACCOUNT_NOTEQUAL);
		mappings.put("PARTNER_PERSION_INFO_NOTEQUAL", PARTNER_PERSION_INFO_NOTEQUAL);
		mappings.put("PARTNER_TAOBAOUSERID_HAS_USED", PARTNER_TAOBAOUSERID_HAS_USED);
	}

	public PartnerExceptionEnum(String code, String desc) {
		super(code, desc);
	}

	public PartnerExceptionEnum() {
	}
}
