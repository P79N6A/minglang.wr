package com.taobao.cun.auge.failure;

public interface AugeErrorCodes {

	/**
	 * 系统异常
	 */
	public static final  String SYSTEM_ERROR_CODE = "B-00000";
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
	 * 非法权限错误码，不报警
	 */
	public static final String ILLEGAL_PRIVILEDGE_ERROR_CODE = "B-00004";
	
	/**
	 * 数据已存在错误码，不用报警
	 */
	
	public static final  String DATA_EXISTS_ERROR_CODE = "B-00005";
	
	/**
	 * 并发更新错误码,不用报警
	 */
	public static final String CONCURRENT_UPDATE_ERROR_CODE ="B-00006";

	/**
	 * 培训业务校验错误码，不报警
	 */
	public static final String PEIXUN_BUSINESS_CHECK_ERROR_CODE = "B-00007";

	
	/**
	 * 合伙人业务校验错误码，不报警
	 */
	public static final String PARTNER_BUSINESS_CHECK_ERROR_CODE = "B-00008";
	
	
	/**
	 * 村点业务校验错误码，不报警
	 */
	public static final String STATION_BUSINESS_CHECK_ERROR_CODE = "B-00009";
	
	
	/**
	 * 村点业务校验错误码，不报警
	 */
	public static final String PARTNER_INSTANCE_BUSINESS_CHECK_ERROR_CODE = "B-00010";
	

	/**
	 * 资产业务校验错误码。不报警
	 */
	public static final String ASSET_BUSINESS_ERROR_CODE = "B-00011";

	/**
	 * 订单业务校验错误码。不报警
	 */
	public static final String TRADE_BUSINESS_CHECK_ERROR_CODE = "B-00012";

	/**
	 * 支付宝业务校验错误码。不报警
	 */
	public static final String ALIPAY_BUSINESS_CHECK_ERROR_CODE = "B-00013";

	/**
	 * 装修业务校验错误码。不报警
	 */
	public static final String DECORATE_BUSINESS_CHECK_ERROR_CODE = "B-00014";

	/**
	 * 旺旺业务校验错误码，不报警
	 */
	public static final String WANGWANG_BUSINESS_CHECK_ERROR_CODE = "B-00015";
	
	/**
	 * 查询po单失败 不报警
	 */
	public static final String PURCHASE_BUSINESS_CHECK_ERROR_CODE = "B-00016";
	
	/**
	 * 签到参数校验失败不报警
	 */
	public static final String PEIXUN_ILLIGAL_BUSINESS_CHECK_ERROR_CODE = "B-00017";
	

	/**
	 * 签到失败不报警
	 */
	public static final String PEIXUN_SIGN_BUSINESS_CHECK_ERROR_CODE = "B-00018";
	
	/**
	 * 调用1688获取memberid失败 需要报警
	 */
	public static final String MEMBER_ID_GET_ERROR = "B-00020";
	
	/**
	 * 1688商城授权失败  需要报警
	 */
	public static final String CBU_MARKET_ACCESS_ERROR = "B-00021";
	
	
	/**
	 * 公司数据不存在，不需要报警
	 */
	public static final  String COMPANY_DATA_NOT_EXISTS_ERROR_CODE = "B-00022";

	/**
	 * cp数据不存在，不需要报警
	 */
	public static final  String CP_NOT_EXISTS_ERROR_CODE = "B-00026";

	/**
	 * 门店不存在
	 */
	public static final  String STORE_DATA_NOT_EXISTS_ERROR_CODE = "B-00027";

}
