package com.taobao.cun.auge.transition.record;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;

/**
 * 生命周期变更记录接口
 * @author zhenhuan.zhangzh
 *
 */
public interface LifecycleTransitionRecorder {

	/**
	 * 保存生命周期变更记录
	 * @param message
	 */
	 void record(EventMessage message);
	
	
}
