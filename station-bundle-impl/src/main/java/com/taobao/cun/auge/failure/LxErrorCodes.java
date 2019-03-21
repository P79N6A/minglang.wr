package com.taobao.cun.auge.failure;

public interface LxErrorCodes {

	/**
	 * 该淘宝账号不存在或状态异常，请与申请人核实!
	 */
	public static final  String TAOBAONICK_STATUS_ERROR_CODE = "B-00000";
	/**
	 * 请填写该账号绑定的手机号
	 */
	public static final  String MOBILE_CHECK_ERROR_CODE = "B-00001";
	
	/**
	 * 无法邀请该账号成为拉新伙伴，请尝试其他淘宝账号
	 */
	public static final  String TAOBAONICK_BUSI_CHECK_ERROR_CODE = "B-00002";
	
	/**
	 * 该手机号已被使用
	 */
	public static final  String MOBILE_USED_ERROR_CODE = "B-00003";

	/**
	 * 不支持邀请村小二/淘帮手/优盟/村拍档的淘宝账号成为拉新伙伴，请尝试其他淘宝账号
	 */
	public static final String TAOBAONICK_OTHER_BUSI_CHECK_ERROR_CODE = "B-00004";
	
	/**
	 * 当前邀请名额已用完，无法再邀请拉新伙伴
	 */
	
	public static final  String QUOTA_CHECK_ERROR_CODE = "B-00005";
	
	/**
	 * 并发更新错误码
	 */
	public static final String CONCURRENT_UPDATE_ERROR_CODE ="B-00006";

	/**
	 * 系统异常
	 */
	public static final String SYSTEM_ERROR_CODE = "B-00007";

	
	/**
	 * 所属村小二业务信息检查异常
	 */
	public static final String PARENT_PARTNER_INFO_CHECK_ERROR = "B-00008";
}
