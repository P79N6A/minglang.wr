package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
/**
 * 合伙人生命周期表，开业补货已收货枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleGoodsReceiptEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleGoodsReceiptEnum N  = new PartnerLifecycleGoodsReceiptEnum("N", "未收货");
 	public static final PartnerLifecycleGoodsReceiptEnum Y = new PartnerLifecycleGoodsReceiptEnum("Y", "已收货");
 	
	
	private static final Map<String, PartnerLifecycleGoodsReceiptEnum> mappings = new HashMap<String, PartnerLifecycleGoodsReceiptEnum>();
	
	static {
		mappings.put("N", N);
		mappings.put("Y", Y);
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
		if (!(obj instanceof PartnerLifecycleGoodsReceiptEnum)) {
            return false;
        }
		PartnerLifecycleGoodsReceiptEnum objType = (PartnerLifecycleGoodsReceiptEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleGoodsReceiptEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleGoodsReceiptEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleGoodsReceiptEnum() {

	}
}
