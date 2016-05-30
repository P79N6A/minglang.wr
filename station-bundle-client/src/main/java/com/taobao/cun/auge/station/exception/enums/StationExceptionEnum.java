package com.taobao.cun.auge.station.exception.enums;

/**
 * Created by haihu.fhh on 2016/5/4.
 */
public class StationExceptionEnum extends CommonExceptionEnum {

	private static final long serialVersionUID = -2325045809951918493L;

	public static final StationExceptionEnum STATION_NUM_IS_DUPLICATE = new StationExceptionEnum(
			"STATION_NUM_IS_DUPLICATE", "服务站编号重复");

	public static final StationExceptionEnum SIGN_SETTLE_PROTOCOL_FAIL = new StationExceptionEnum(
			"SIGN_SETTLE_PROTOCOL_FAIL", "签署入驻协议失败");
	
	public static final StationExceptionEnum HAS_CHILDREN_TPA = new StationExceptionEnum(
			"HAS_CHILDREN_TPA", "该合伙人下存在运营中的淘帮手");
	
	public static final StationExceptionEnum PARTNER_INSTANCE_NOT_EXIST = new StationExceptionEnum(
			"PARTNER_INSTANCE_NOT_EXIST", "合伙人实例不存在");
	
	public static final StationExceptionEnum STATION_APPLY_NOT_EXIST = new StationExceptionEnum(
			"STATION_APPLY_NOT_EXIST", "申请单不存在");
	
	public static final StationExceptionEnum STATION_NOT_EXIST = new StationExceptionEnum(
			"STATION_NOT_EXIST", "村点不存在");
	
	public static final StationExceptionEnum STATION_STATUS_CHANGED = new StationExceptionEnum(
			"STATION_STATUS_CHANGED", "村点状态已变更");
	
	public static final StationExceptionEnum QUIT_STATION_APPLY_EXIST = new StationExceptionEnum(
			"QUIT_STATION_APPLY_EXIST", "退出申请单已经存在");
	
	public static final StationExceptionEnum KAYE_PACKAGE_NOT_EXIST = new StationExceptionEnum(
			"KAYE_PACKAGE_NOT_EXIST", "必须存在开业包");
	
	public static final StationExceptionEnum STATION_NAME_IS_NULL = new StationExceptionEnum(
			"STATION_NAME_IS_NULL", "服务点名称不能为空");
	
	public static final StationExceptionEnum CAINIAO_STATION_NAME_TOO_LENGTH = new StationExceptionEnum(
			"CAINIAO_STATION_NAME_TOO_LENGTH", "村服务点名称超过了菜鸟驿站要求的长度");
	
	public static final StationExceptionEnum STATION_NUM_IS_NULL = new StationExceptionEnum(
			"STATION_NUM_IS_NULL", "服务站编号不能为空");
	public static final StationExceptionEnum STATION_NUM_TOO_LENGTH = new StationExceptionEnum(
			"STATION_NUM_TOO_LENGTH", "村服务站编号长度0-16位");
	public static final StationExceptionEnum STATION_NUM_ILLEGAL = new StationExceptionEnum(
			"STATION_NUM_ILLEGAL", "村服务站编号不能含有特殊字符");
	
	public static final StationExceptionEnum STATION_ADDRESS_IS_NULL = new StationExceptionEnum(
			"STATION_ADDRESS_IS_NULL", "服务站地址不能为空");
	
	public static final PartnerExceptionEnum STATION_DELETE_TO_TEMP = new PartnerExceptionEnum(
			"STATION_DELETE_TO_TEMP", "只能删除暂存数据");
	
	

	static {
		mappings.put("STATION_NUM_IS_DUPLICATE", STATION_NUM_IS_DUPLICATE);
		mappings.put("SIGN_SETTLE_PROTOCOL_FAIL", SIGN_SETTLE_PROTOCOL_FAIL);
		mappings.put("HAS_CHILDREN_TPA", HAS_CHILDREN_TPA);
		mappings.put("PARTNER_INSTANCE_NOT_EXIST", PARTNER_INSTANCE_NOT_EXIST);
		mappings.put("STATION_NOT_EXIST", STATION_NOT_EXIST);
		mappings.put("STATION_STATUS_CHANGED", STATION_STATUS_CHANGED);
		mappings.put("STATION_APPLY_NOT_EXIST", STATION_APPLY_NOT_EXIST);
		mappings.put("QUIT_STATION_APPLY_EXIST", QUIT_STATION_APPLY_EXIST);
		mappings.put("KAYE_PACKAGE_NOT_EXIST", KAYE_PACKAGE_NOT_EXIST);
		mappings.put("STATION_NAME_IS_NULL", STATION_NAME_IS_NULL);
		mappings.put("CAINIAO_STATION_NAME_TOO_LENGTH", CAINIAO_STATION_NAME_TOO_LENGTH);
		mappings.put("STATION_NUM_IS_NULL", STATION_NUM_IS_NULL);
		mappings.put("STATION_NUM_TOO_LENGTH", STATION_NUM_TOO_LENGTH);
		mappings.put("STATION_ADDRESS_IS_NULL", STATION_ADDRESS_IS_NULL);
		mappings.put("STATION_NUM_ILLEGAL", STATION_NUM_ILLEGAL);
		mappings.put("STATION_DELETE_TO_TEMP", STATION_DELETE_TO_TEMP);
	}

	public StationExceptionEnum(String code, String desc) {
		super(code, desc);
	}

	public StationExceptionEnum() {
	}
}
