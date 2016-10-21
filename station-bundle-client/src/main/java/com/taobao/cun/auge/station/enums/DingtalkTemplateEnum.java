package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DingtalkTemplateEnum implements Serializable {

	private static final long serialVersionUID = 3666195829516895941L;

	private String code;
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	private DingtalkTemplateEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}
	//FIXME FHH code小写，和变量值不一样，valueof时返回null。历史人历史错误
	public static final DingtalkTemplateEnum NODE_COMMIT = new DingtalkTemplateEnum("CunTaoNodeCommit", "提交创建村点");
	public static final DingtalkTemplateEnum NODE_RECV = new DingtalkTemplateEnum("CunTaoNodeRecv", "保证金付款成功并进入装修中");
	public static final DingtalkTemplateEnum NODE_OPEN = new DingtalkTemplateEnum("CuntaoNodeOpen", "开业（启动）");
	public static final DingtalkTemplateEnum NODE_LEAVE_APPLY = new DingtalkTemplateEnum("CuntaoNodeLeaveApply",
			"停业确认");
	public static final DingtalkTemplateEnum NODE_LEAVE = new DingtalkTemplateEnum("CuntaoNodeLeave", "正式撤点成功");

	public static final Map<String, DingtalkTemplateEnum> mappings = new HashMap<String, DingtalkTemplateEnum>();

	static {
		mappings.put("NODE_COMMIT", NODE_COMMIT);
		mappings.put("NODE_RECV", NODE_RECV);
		mappings.put("NODE_OPEN", NODE_OPEN);
		mappings.put("NODE_LEAVE_APPLY", NODE_LEAVE_APPLY);
		mappings.put("NODE_LEAVE", NODE_LEAVE);
	}

	public static DingtalkTemplateEnum valueof(String code) {
		if (code == null)
			return null;
		return mappings.get(code);
	}
}
