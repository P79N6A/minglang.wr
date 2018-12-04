package com.taobao.cun.auge.asset.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 盘点信息表状态
 * @author quanzhu.wangqz
 *
 */
public class AssetCheckInfoStatusEnum implements Serializable {

	private static final Map<String, AssetCheckInfoStatusEnum> MAPPINGS = new HashMap<String, AssetCheckInfoStatusEnum>();

	private static final long serialVersionUID = -2325045809951928493L;

	private String code;
	private String desc;

	public static final AssetCheckInfoStatusEnum CHECKED = new AssetCheckInfoStatusEnum("CHECKED", "已盘点");
	public static final AssetCheckInfoStatusEnum TASK_DONE = new AssetCheckInfoStatusEnum("TASK_DONE", "盘点任务完成");
	public static final AssetCheckInfoStatusEnum SYS_CONFIRM = new AssetCheckInfoStatusEnum("SYS_CONFIRM", "系统确认");
	public static final AssetCheckInfoStatusEnum ZB_CONFIRM = new AssetCheckInfoStatusEnum("ZB_CONFIRM", "总部确认");
	public static final AssetCheckInfoStatusEnum ZB_BACK = new AssetCheckInfoStatusEnum("ZB_BACK", "总部退回");




	static {
		MAPPINGS.put("CHECKED", CHECKED);
		MAPPINGS.put("TASK_DONE", TASK_DONE);
		MAPPINGS.put("SYS_CONFIRM", SYS_CONFIRM);
		MAPPINGS.put("ZB_CONFIRM", ZB_CONFIRM);
		MAPPINGS.put("ZB_BACK", ZB_BACK);
	}

	public AssetCheckInfoStatusEnum(String code, String desc) {
	        this.code = code;
	        this.desc = desc;
	    }

	public AssetCheckInfoStatusEnum() {

	    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public static AssetCheckInfoStatusEnum valueof(String code) {
		if (code == null) {
			return null;
		}
		return MAPPINGS.get(code);
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AssetCheckInfoStatusEnum other = (AssetCheckInfoStatusEnum) obj;
		if (code == null) {
			if (other.code != null) {
				return false;
			}
		} else if (!code.equals(other.code)) {
			return false;
		}
		return true;
	}
}
