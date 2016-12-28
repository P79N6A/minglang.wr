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
			"PARTNER_TAOBAOUSERID_HAS_USED", "该账号已经入驻农村淘宝，请核实！");
	public static final PartnerExceptionEnum PARTNER_DELETE_FAIL = new PartnerExceptionEnum(
			"PARTNER_DELETE_FAIL", "当前状态合伙人信息不能删除");
	public static final PartnerExceptionEnum PARTNER_STATE_NOT_APPLICABLE = new PartnerExceptionEnum(
			"PARTNER_STATE_NOT_APPLICABLE", "当前合伙人的状态不允许开展该业务");
	
	public static final PartnerExceptionEnum PARTNER_MOBILE_CHECK_FAIL = new PartnerExceptionEnum(
			"PARTNER_MOBILE_CHECK_FAIL", "手机号必须是11位数字组成，且以1开头");
	
	public static final PartnerExceptionEnum PARTNER_MOBILE_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_MOBILE_IS_NULL", "手机号不能为空");
	
	public static final PartnerExceptionEnum PARTNER_NOT_FINISH_COURSE = new PartnerExceptionEnum(
			"PARTNER_NOT_FINISH_COURSE", "当前合伙人没有完成培训");
	
	public static final PartnerExceptionEnum PARTNER_SOLID_POINT_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_SOLID_POINT_IS_NULL", "固点标准化不能为空");
	
	public static final PartnerExceptionEnum PARTNER_LEASE_AREA_IS_NULL = new PartnerExceptionEnum(
			"PARTNER_LEASE_AREA_IS_NULL", "可租赁门店面积不能为空");

	public static final PartnerExceptionEnum PARTNER_DECORATE_NOT_PAY = new PartnerExceptionEnum(
			"PARTNER_NOT_FINISH_COURSE", "装修未付款");
	
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
		mappings.put("PARTNER_DELETE_FAIL", PARTNER_DELETE_FAIL);
		mappings.put("PARTNER_STATE_NOT_APPLICABLE", PARTNER_STATE_NOT_APPLICABLE);
		mappings.put("PARTNER_MOBILE_CHECK_FAIL", PARTNER_MOBILE_CHECK_FAIL);
		mappings.put("PARTNER_MOBILE_IS_NULL", PARTNER_MOBILE_IS_NULL);
		mappings.put("PARTNER_NOT_FINISH_COURSE", PARTNER_NOT_FINISH_COURSE);
		mappings.put("PARTNER_SOLID_POINT_IS_NULL", PARTNER_SOLID_POINT_IS_NULL);
		mappings.put("PARTNER_LEASE_AREA_IS_NULL", PARTNER_LEASE_AREA_IS_NULL);

	}

	public PartnerExceptionEnum(String code, String desc) {
		super(code, desc);
	}

	public PartnerExceptionEnum() {
	}
}
