package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
* 合伙人生命周期表中，当前步骤枚举
* @author quanzhu.wangqz
*
*/
public class PartnerLifecycleCurrentStepEnum  implements Serializable {
	
	private static final long serialVersionUID = -2698634278143088180L;
	/**
	 *  合伙人   
	 * 入驻步骤顺序 ：入驻协议>保证金》结束
	 * 强制停业：人员审批》结束
	 * 主动停业：小二确认》结束
	 * 退出：  人员审批》解冻保证金》结束
	 */
	/**
	 *  淘帮手   
	 * 合伙人主动提交，入驻步骤顺序 ：人员审批>入驻协议>保证金》结束
	 * 小二提交 入驻步骤顺序 ：入驻协议>保证金》结束
	 * 强制停业：人员审批》结束
	 * 主动停业：小二确认》结束
	 * 退出：  人员审批》解冻保证金》结束
	 */

	public static final PartnerLifecycleCurrentStepEnum PROCESSING = new PartnerLifecycleCurrentStepEnum("PROCESSING", "处理中");
	public static final PartnerLifecycleCurrentStepEnum END = new PartnerLifecycleCurrentStepEnum("END", "结束");
	
	
	private static final Map<String, PartnerLifecycleCurrentStepEnum> mappings = new HashMap<String, PartnerLifecycleCurrentStepEnum>();
	
	static {
		mappings.put("PROCESSING", PROCESSING);
		mappings.put("END", END);
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
		if (!(obj instanceof PartnerLifecycleCurrentStepEnum)) {
            return false;
        }
		PartnerLifecycleCurrentStepEnum objType = (PartnerLifecycleCurrentStepEnum) obj;
		return objType.getCode().equals(this.getCode());
	}

	public PartnerLifecycleCurrentStepEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public static PartnerLifecycleCurrentStepEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
	}

	
	@SuppressWarnings("unused")
	private PartnerLifecycleCurrentStepEnum() {

	}
}
