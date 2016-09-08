package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务站装修表，装修状态枚举
 * @author quanzhu.wangqz
 *
 */
public class StationDecorateStatusEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final StationDecorateStatusEnum UNDECORATE  = new StationDecorateStatusEnum("UNDECORATE", "未装修");
	public static final StationDecorateStatusEnum DECORATING = new StationDecorateStatusEnum("DECORATING", "装修中");
	public static final StationDecorateStatusEnum WAIT_AUDIT = new StationDecorateStatusEnum("WAIT_AUDIT", "装修反馈待审核");
	public static final StationDecorateStatusEnum DONE = new StationDecorateStatusEnum("DONE", "已装修");
	public static final StationDecorateStatusEnum INVALID = new StationDecorateStatusEnum("INVALID", "作废");

	
	//未装修时，对应淘宝订单状态
	public static final StationDecorateStatusEnum NO_ORDER = new StationDecorateStatusEnum("NO_ORDER", "未下单");
	public static final StationDecorateStatusEnum WAIT_PAY = new StationDecorateStatusEnum("WAIT_PAY", "待付款");
	
	private static final Map<String, StationDecorateStatusEnum> mappings = new HashMap<String, StationDecorateStatusEnum>();
	
	static {
		mappings.put("UNDECORATE", UNDECORATE);
		mappings.put("DECORATING", DECORATING);
		mappings.put("WAIT_AUDIT", WAIT_AUDIT);
		mappings.put("DONE", DONE);
		mappings.put("INVALID", INVALID);

		mappings.put("NO_ORDER", NO_ORDER);
		mappings.put("WAIT_PAY", WAIT_PAY);
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
		if (!(obj instanceof StationDecorateStatusEnum))
			return false;
		StationDecorateStatusEnum objType = (StationDecorateStatusEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public StationDecorateStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static StationDecorateStatusEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private StationDecorateStatusEnum() {

	}
}
