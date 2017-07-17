package com.taobao.cun.auge.failure;

public interface AugeErrorCodes {

	/**
	 * 非法参数异常码
	 */
	public static final  String ILLEGAL_PARAM_ERROR_CODE = "B-00001";
	
	/**
	 * 非法结果异常码
	 */
	public static final  String ILLEGAL_RESULT_ERROR_CODE = "B-00002";
	
	/**
	 * 非法外部调用结果异常码
	 */
	public static final  String ILLEGAL_EXT_RESULT_ERROR_CODE = "B-00003";

	/**
	 * 数据已存在错误码，不用报警
	 */
	
	public static final  String DATA_EXISTS_ERROR_CODE = "B-00004";
	
	/**
	 * 未完成订单错误码
	 */
	public static final  String NOT_END_ORDER_ERROR_CODE = "B-00005";
	
	/**
	 * 并发更新错误码,不用报警
	 */
	public static final String DUPLICATE_UPDATE_ERROR_CODE ="B-00006";

	/**
	 * 培训订单状态不正确
	 */
	public static final String PEIXUN_ORDER_STATUS_ERROR_ERROR_CODE ="B-00007";
	
	/**
	 * 培训订单退款状态不正确
	 */
	public static final String PEIXUN_ORDER_REFUND_STATUS_ERROR_CODE ="B-00008";
	
	/**
	 * 培训订单审核状态不正确
	 */
	public static final String PEIXUN_ORDER_REFUND_AUDIT_STATUS_ERROR_ERROR_CODE ="B-00009";
	
	/**
	 * 培训订单审核退款中不允许退款,不报警
	 */
	public static final String PEIXUN_ORDER_REFUNDING_ERROR_ERROR_CODE ="B-00010";
	
	/**
	 * 培训订单未签到,不报警
	 */
	public static final String PEIXUN_UNCHECK_ERROR_ERROR_CODE ="B-00011";
	
	/**
	 * 培训订单已签到,不报警
	 */
	public static final String PEIXUN_CHECK_ERROR_ERROR_CODE ="B-00012";
	
	/**
	 * 培训发票错误,不报警
	 */
	public static final String PEIXUN_INVOICE_ERROR_CODE ="B-00013";

	/**
	 * 装修出资状态错误，不报警
	 */
	public static final String DECORATE_PAYMENT_TYPE_ERROR_CODE = "B-00014";

	/**
	 * 站点退出检查合伙人是否都已经退出，不报警
	 */
	public static final String PARTNER_QUIT_ERROR_CODE = "B-00015";

	/**
	 * 村点状态校验，不报警
	 */
	public static final String STATION_STATUS_CHECK_ERROR_CODE = "B-00016";

	/**
	 * 合伙人实例状态校验，不报警
	 */
	public static final String PARTNER_INSTANCE_STATUS_CHECK_ERROR_CODE = "B-00017";

	/**
	 * 保证金已冻结，不报警
	 */
	public static final String BOND_HAS_FROZEN_ERROR_CODE = "B-00018";

	/**
	 * 超过TPA上线，不报警
	 */
	public static final String EXCEED_TPA_LIMIT_ERROR_CODE = "B-00019";

	/**
	 * 合伙人没有完成培训，不报警
	 */
	public static final String PARTNER_NOT_FINISH_COURSE_ERROR_CODE = "B-00020";

	/**
	 * 合伙人没有完成装修，不报警
	 */
	public static final String STATION_NOT_FINISH_DECORATE_ERROR_CODE = "B-00021";

	/**
	 * 合伙人生命周期错误码，不报警
	 */
	public static final String PARTNER_LIFECYCLE_CHECK_ERROR_CODE = "B-00022";

	/**
	 * 该合伙人下面还有淘帮手，请解绑所有淘帮手再做降级，不报警
	 */
	public static final String DEGRADE_PARTNER_HAS_TPA_ERROR_CODE = "B-00023";

	/**
	 * 所归属的合伙人的淘帮手超过限额，不允许绑定 ，不报警
	 */
	public static final String DEGRADE_TARGET_PARTNER_HAS_TPA_MAX_ERROR_CODE = "B-00024";

	/**
	 * 需要降级的合伙人与归属合伙人组织不在同一个县，请检查确认后再做降级，不报警
	 */
	public static final String DEGRADE_PARTNER_ORG_NOT_SAME_ERROR_CODE = "B-00025";

	/**
	 * 支付宝账号与淘宝绑定的不一致，不报警
	 */
	public static final String PARTNER_ALIPAYACCOUNT_NOTEQUAL_ERROR_CODE = "B-00026";

	/**
	 * 目前支付宝认证的归属人，与您提交的申请人信息不符，不报警
	 */
	public static final String PARTNER_PERSION_INFO_NOTEQUAL_ERROR_CODE = "B-00027";

	/**
	 * 被入驻的村点状态必须为已停业，不报警
	 */
	public static final String PARTNER_INSTANCE_MUST_BE_CLOSED_ERROR_CODE = "B-00028";

	/**
	 * 合伙人类型错误码，不报警
	 */
	public static final String PARTNER_INSTANCE_TYPE_ERROR_CODE = "B-00029";

	/**
	 * 非法权限错误码，不报警
	 */
	public static final String ILLEGAL_PRIVILEDGE_ERROR_CODE = "B-00030";

	/**
	 * 资产未回收误码，不报警
	 */
	public static final String ASSET_UN_RECYCLE_ERROR_CODE = "B-00031";

	/**
	 * 合伙人存在淘帮手，不能停业，不报警
	 */
	public static final String TP_HAS_CHILDREN_TPA_FOR_CLOSE_ERROR_CODE = "B-00032";

	/**
	 * 合伙人存在淘帮手，不能退出，不报警
	 */
	public static final String TP_HAS_CHILDREN_TPA_FOR_QUIT_ERROR_CODE = "B-00033";

	/**
	 * 村点上存在未退出的合伙人，不能撤点。不报警
	 */
	public static final String HAS_OTHER_PARTNER_QUIT_ERROR_CODE = "B-00034";

}
