package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人实例表 转型状态
 * @author quanzhu.wangqz
 *
 */
public class PartnerInstanceTransStatusEnum  implements Serializable {

	private static final long serialVersionUID = -2698634278143088180L;

	public static final PartnerInstanceTransStatusEnum WAIT_TRANS = new PartnerInstanceTransStatusEnum("WAIT_TRANS", "待转型");
	public static final PartnerInstanceTransStatusEnum TRANS_ING  = new PartnerInstanceTransStatusEnum("TRANS_ING", "转型中");
	public static final PartnerInstanceTransStatusEnum TRANS_DONE  = new PartnerInstanceTransStatusEnum("TRANS_DONE", "已转型");
	

	private static final Map<String, PartnerInstanceTransStatusEnum> mappings = new HashMap<String, PartnerInstanceTransStatusEnum>();
	static {
		mappings.put("WAIT_TRANS", WAIT_TRANS);
		mappings.put("TRANS_ING", TRANS_ING);
		mappings.put("TRANS_DONE", TRANS_DONE);
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
		if (obj == null) {
            return false;
        }
		if (!(obj instanceof PartnerInstanceTransStatusEnum)) {
            return false;
        }
		PartnerInstanceTransStatusEnum objType = (PartnerInstanceTransStatusEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerInstanceTransStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerInstanceTransStatusEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private PartnerInstanceTransStatusEnum() {

	}

}
