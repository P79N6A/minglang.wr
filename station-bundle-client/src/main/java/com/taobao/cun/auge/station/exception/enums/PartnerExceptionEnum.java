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


	static {
		mappings.put("NO_RECORD", NO_RECORD);
		mappings.put("ID_IS_NULL", ID_IS_NULL);
	}

	public PartnerExceptionEnum(String code, String desc) {
		super(code, desc);
	}

	public PartnerExceptionEnum() {
	}
}
