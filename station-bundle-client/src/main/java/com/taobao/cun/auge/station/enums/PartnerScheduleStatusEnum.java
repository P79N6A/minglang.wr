package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class PartnerScheduleStatusEnum implements Serializable{

		private static final long serialVersionUID = 1L;
		public static final PartnerScheduleStatusEnum NOT_ATTEMP = new PartnerScheduleStatusEnum("NOT_ATTEMP", "未观看");
		public static final PartnerScheduleStatusEnum NOT_REFLECT = new PartnerScheduleStatusEnum("NOT_REFLECT", "已观看未反馈");
		public static final PartnerScheduleStatusEnum HAS_REFLECT = new PartnerScheduleStatusEnum("HAS_REFLECT", "已观看乙反馈");
		public static final PartnerScheduleStatusEnum LIVING = new PartnerScheduleStatusEnum("LIVING", "进行中");
		public static final PartnerScheduleStatusEnum WAIT_ATTEMP = new PartnerScheduleStatusEnum("WAIT_ATTEMP", "待观看");

		private static final Map<String, PartnerScheduleStatusEnum> mappings = new HashMap<String, PartnerScheduleStatusEnum>();

		static {
			mappings.put("NOT_ATTEMP", NOT_ATTEMP);
			mappings.put("NOT_REFLECT", NOT_REFLECT);
			mappings.put("HAS_REFLECT", HAS_REFLECT);
			mappings.put("LIVING", LIVING);
			mappings.put("WAIT_ATTEMP", WAIT_ATTEMP);
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
			if (!(obj instanceof OperatorTypeEnum)) {
                return false;
            }
			OperatorTypeEnum objType = (OperatorTypeEnum) obj;
			return objType.getCode().equals(this.getCode());
		}

		public PartnerScheduleStatusEnum(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public static PartnerScheduleStatusEnum valueof(String code) {
			if (code == null) {
                return null;
            }
			return mappings.get(code);
		}

		public PartnerScheduleStatusEnum() {

		}
}
