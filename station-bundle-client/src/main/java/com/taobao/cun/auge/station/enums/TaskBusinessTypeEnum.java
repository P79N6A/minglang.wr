package com.taobao.cun.auge.station.enums;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 内外流程任务类型定义在ProcessBusinessEnum 其他任务类型的枚举在此定义
 * 
 * @author linjianke
 *
 */
public class TaskBusinessTypeEnum implements Serializable {

	private static final long serialVersionUID = -6355359207409909479L;

	private static final Map<String, TaskBusinessTypeEnum> mappings = new HashMap<String, TaskBusinessTypeEnum>();

	private String code;
	private String desc;

	public static final TaskBusinessTypeEnum SETTLING_SYS_PROCESS = new TaskBusinessTypeEnum("SETTLING_SYS_PROCESS", "入驻流程系统处理环节");

	public static final TaskBusinessTypeEnum UPDATE_SERVICING_CAINIAO = new TaskBusinessTypeEnum("UPDATE_SERVICING_CAINIAO", "服务中修改菜鸟");

	public static final TaskBusinessTypeEnum REMOVE_USER_TAG = new TaskBusinessTypeEnum("REMOVE_USER_TAG", "去UIC、旺旺标任务");
	public static final TaskBusinessTypeEnum REMOVE_PEISONG_TAG = new TaskBusinessTypeEnum("REMOVE_PEISONG_TAG", "去配送接单能力任务");


	public static final TaskBusinessTypeEnum REMOVE_LOGISTICS = new TaskBusinessTypeEnum("REMOVE_LOGISTICS", "删除物流站点任务");

	public static final TaskBusinessTypeEnum REMOVE_ALIPAY_TAG = new TaskBusinessTypeEnum("REMOVE_ALIPAY_TAG", "去支付宝标任务");

	public static final TaskBusinessTypeEnum PARTNER_INSTANCE_QUIT = new TaskBusinessTypeEnum("PARTNER_INSTANCE_QUIT", "合伙人保证金解冻及正式退出任务");

	public static final TaskBusinessTypeEnum TP_DEGRADE = new TaskBusinessTypeEnum("TP_DEGRADE", "降级流程");
	
	public static final TaskBusinessTypeEnum PARTNER_INSTANCE_QUIT_APPROVED = new TaskBusinessTypeEnum("PARTNER_INSTANCE_QUIT_APPROVED","退出审批确认环节");

	public static final TaskBusinessTypeEnum SMS = new TaskBusinessTypeEnum("SMS", "短信任务");
	
	public static final TaskBusinessTypeEnum MAIL = new TaskBusinessTypeEnum("MAIL", "邮件任务");
	
	public static final TaskBusinessTypeEnum STATION_SHUTDOWN_APPROVED = new TaskBusinessTypeEnum("STATION_SHUTDOWN_APPROVED","关闭物流站");

	public static final TaskBusinessTypeEnum CLOSE_TO_SERVICE = new TaskBusinessTypeEnum("CLOSE_TO_SERVICE", "已停业回到服务中");

	public static final TaskBusinessTypeEnum CLOSED_TO_CAINIAO = new TaskBusinessTypeEnum("CLOSED_TO_CAINIAO", "停业通知菜鸟");

	public static final TaskBusinessTypeEnum CLOSED_UM = new TaskBusinessTypeEnum("CLOSED_UM", "关闭优盟");

	public static final TaskBusinessTypeEnum QUIT_UM = new TaskBusinessTypeEnum("QUIT_UM", "退出优盟");

	public static final TaskBusinessTypeEnum INIT_LIGHT_STORE = new TaskBusinessTypeEnum("INIT_LIGHT_STORE", "初始化轻店");


	static {
		mappings.put("SETTLING_SYS_PROCESS", SETTLING_SYS_PROCESS);
		mappings.put("UPDATE_SERVICING_CAINIAO", UPDATE_SERVICING_CAINIAO);
		mappings.put("REMOVE_USER_TAG", REMOVE_USER_TAG);
		mappings.put("REMOVE_PEISONG_TAG", REMOVE_PEISONG_TAG);
		mappings.put("REMOVE_LOGISTICS", REMOVE_LOGISTICS);
		mappings.put("REMOVE_ALIPAY_TAG", REMOVE_ALIPAY_TAG);
		mappings.put("PARTNER_INSTANCE_QUIT", PARTNER_INSTANCE_QUIT);
		mappings.put("PARTNER_INSTANCE_QUIT_APPROVED", PARTNER_INSTANCE_QUIT_APPROVED);
		mappings.put("TP_DEGRADE", TP_DEGRADE);
		mappings.put("SMS", SMS);
		mappings.put("STATION_SHUTDOWN_APPROVED", STATION_SHUTDOWN_APPROVED);
		mappings.put("CLOSE_TO_SERVICE", CLOSE_TO_SERVICE);
		mappings.put("CLOSED_TO_CAINIAO", CLOSED_TO_CAINIAO);
		mappings.put("CLOSED_UM", CLOSED_UM);
		mappings.put("QUIT_UM", QUIT_UM);
		mappings.put("INIT_LIGHT_STORE", INIT_LIGHT_STORE);

	}

	public TaskBusinessTypeEnum(String code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	public TaskBusinessTypeEnum() {

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

	public static TaskBusinessTypeEnum valueof(String code) {
		if (code == null) {
            return null;
        }
		return mappings.get(code);
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
		TaskBusinessTypeEnum other = (TaskBusinessTypeEnum) obj;
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
