package com.taobao.cun.auge.event;

/**
 * 定义事件类型常量
 * 
 * @author quanzhu.wangqz
 *
 */
public final class EventConstant {

	private EventConstant() {

	}

	/**
	 * 合伙人服务站实例状态变更事件
	 */
	public static final String PARTNER_INSTANCE_STATE_CHANGE_EVENT = "PARTNER_INSTANCE_STATE_CHANGE_EVENT";
	
	/**
	 * 合伙人类型变更事件，如合伙人降级为淘帮手
	 */
	public static final String PARTNER_INSTANCE_TYPE_CHANGE_EVENT = "PARTNER_INSTANCE_TYPE_CHANGE_EVENT";

	/**
	 * 流程日志事件
	 */
	public static final String CUNTAO_FLOW_RECORD_EVENT = "CUNTAO_FLOW_RECORD_EVENT";

	/**
	 * 村点状态变更事件
	 */
	public static final String CUNTAO_STATION_STATUS_CHANGED_EVENT = "CUNTAO_STATION_STATUS_CHANGED_EVENT";

	/**
	 * station_apply信息同步
	 */
	public static final String CUNTAO_STATION_APPLY_SYNC_EVENT = "CUNTAO_STATION_APPLY_SYNC_EVENT";
	
	
	/**
	 * 手机端使用，在合伙人装修中的时候 使用
	 */
	public static final String PARTNER_STATION_STATE_CHANGE_EVENT = "PARTNER_STATION_STATE_CHANGE_EVENT";

}
