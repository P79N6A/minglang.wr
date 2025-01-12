package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 合伙人生命周期表，培训状态枚举
 * @author quanzhu.wangqz
 *
 */
public class PartnerLifecycleCourseStatusEnum  implements Serializable {
	
	private static final long serialVersionUID = -119918219648000754L;
	public static final PartnerLifecycleCourseStatusEnum N  = new PartnerLifecycleCourseStatusEnum("N", "未培训");
 	public static final PartnerLifecycleCourseStatusEnum Y = new PartnerLifecycleCourseStatusEnum("Y", "已培训");
 	
 	public static final PartnerLifecycleCourseStatusEnum NEW  = new PartnerLifecycleCourseStatusEnum("NEW", "待购买培训");
 	public static final PartnerLifecycleCourseStatusEnum PAY = new PartnerLifecycleCourseStatusEnum("PAY", "待培训签到");
	
	private static final Map<String, PartnerLifecycleCourseStatusEnum> mappings = new HashMap<String, PartnerLifecycleCourseStatusEnum>();
	
	static {
		mappings.put("N", N);
		mappings.put("Y", Y);
		mappings.put("NEW", NEW);
		mappings.put("PAY", PAY);
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
		if (!(obj instanceof PartnerLifecycleCourseStatusEnum)) {
            return false;
        }
		PartnerLifecycleCourseStatusEnum objType = (PartnerLifecycleCourseStatusEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleCourseStatusEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleCourseStatusEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleCourseStatusEnum() {

	}
}
