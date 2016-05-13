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


	static {
		mappings.put("NO_RECORD", NO_RECORD);
	}

	public PartnerExceptionEnum(String code, String desc) {
		super(code, desc);
	}

	public PartnerExceptionEnum() {
	}
}
