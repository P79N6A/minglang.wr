package com.taobao.cun.auge.transition.record.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.transition.record.AbstractLifecycleTransitionRecorder;
import com.taobao.cun.auge.transition.transition.StateTransitionTuple;
import com.taobao.cun.auge.transition.transition.StateTransitionEventProcessor;
import com.taobao.cun.auge.transition.transition.Transitions;

/**
 * 站点生命周期变更记录器
 * @author zhenhuan.zhangzh
 *
 */
@Component
public class LifecycleTransitionRecorder extends AbstractLifecycleTransitionRecorder {

	@Autowired
	private Transitions transitionConverters;
	


	@Override
	public List<CuntaoLifecycleTransition> buildTransition(EventMessage message) {
		return transitionConverters.getTransitionsByEvent(message);
	}

}
