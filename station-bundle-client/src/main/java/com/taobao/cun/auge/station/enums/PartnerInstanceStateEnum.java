package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人实例表 状态枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceStateEnum  implements Serializable {

	private static final long serialVersionUID = -2698634278143088180L;
	
	// 暂存
	public static final PartnerInstanceStateEnum TEMP = new PartnerInstanceStateEnum("TEMP", "暂存");
	// 入驻中
	public static final PartnerInstanceStateEnum SETTLING  = new PartnerInstanceStateEnum("SETTLING", "入驻中");
	// 入驻失败
	public static final PartnerInstanceStateEnum SETTLE_FAIL  = new PartnerInstanceStateEnum("SETTLE_FAIL ", "入驻失败");
	// 装修中
	public static final PartnerInstanceStateEnum DECORATING = new PartnerInstanceStateEnum("DECORATING", "装修中");
	// 服务中
	public static final PartnerInstanceStateEnum SERVICING = new PartnerInstanceStateEnum("SERVICING", "服务中");
	// 停业申请中
	public static final PartnerInstanceStateEnum CLOSING = new PartnerInstanceStateEnum("CLOSING", "停业申请中");
	// 已停业
	public static final PartnerInstanceStateEnum CLOSED = new PartnerInstanceStateEnum("CLOSED", "已停业");
	// 退出申请中
	public static final PartnerInstanceStateEnum QUITING = new PartnerInstanceStateEnum("QUITING", "退出申请中");
	// 已退出
	public static final PartnerInstanceStateEnum QUIT = new PartnerInstanceStateEnum("QUIT", "已退出");
	

	private static final Map<String, PartnerInstanceStateEnum> mappings = new HashMap<String, PartnerInstanceStateEnum>();
	static {
		mappings.put("TEMP", TEMP);
		mappings.put("SETTLE_FAIL", SETTLE_FAIL);
		mappings.put("SETTLING", SETTLING);
		mappings.put("DECORATING", DECORATING);
		mappings.put("SERVICING", SERVICING);
		mappings.put("CLOSING", CLOSING);
		mappings.put("CLOSED", CLOSED);
		mappings.put("QUITING", QUITING);
		mappings.put("QUIT", QUIT);
	}

	private String code;
	private String desc;

	public String getCode() {
		return code;
	}

	public String getDesc() {
		return desc;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof PartnerInstanceStateEnum))
			return false;
		PartnerInstanceStateEnum objType = (PartnerInstanceStateEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerInstanceStateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerInstanceStateEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerInstanceStateEnum() {

	}

}
