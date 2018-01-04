package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 开业任务铺货枚举
 * @author quanzhu.wangqz
 *
 */

public class ReplenishStatusEnum implements Serializable{

	private static final long serialVersionUID = -2698634278143088180L;
	/**
	 * 补货金待冻结
	 */
	public static final ReplenishStatusEnum WAIT_FROZEN  = new ReplenishStatusEnum("WAIT_FROZEN", "待冻结");
	/**
	 * 补货金已冻结
	 */
    public static final ReplenishStatusEnum HAS_FROZEN = new ReplenishStatusEnum("HAS_FROZEN", "已冻结");
	/**
	 * 补货已下单  本次用不到
	 */
    //public static final ReplenishStatusEnum HAS_ORDER = new ReplenishStatusEnum("HAS_ORDER", "已下单");

    /**
	 * 补货已收货
	 */
	public static final ReplenishStatusEnum GOODS_RECEIVE = new ReplenishStatusEnum("GOODS_RECEIVE", "已收货");

	private static final Map<String, ReplenishStatusEnum> mappings = new HashMap<String, ReplenishStatusEnum>();

	static {
		mappings.put("WAIT_FROZEN", WAIT_FROZEN);
		mappings.put("HAS_FROZEN", HAS_FROZEN);
		//mappings.put("HAS_ORDER", HAS_ORDER);
		mappings.put("GOODS_RECEIVE", GOODS_RECEIVE);
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
		if (!(obj instanceof ReplenishStatusEnum)) {
            return false;
        }
		ReplenishStatusEnum objType = (ReplenishStatusEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public ReplenishStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static ReplenishStatusEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	@SuppressWarnings("unused")
	private ReplenishStatusEnum() {

	}
}
