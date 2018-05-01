package com.taobao.cun.auge.transition.record;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;
import com.taobao.cun.auge.dal.mapper.CuntaoLifecycleTransitionMapper;

public abstract class AbstractLifecycleTransitionRecorder implements LifecycleTransitionRecorder {

	@Autowired
	private CuntaoLifecycleTransitionMapper lifeCycleTransitionMapper;

	 
	public void record(EventMessage message) {
			List<CuntaoLifecycleTransition> transitions = buildTransition(message);
			if(transitions != null){
				for(CuntaoLifecycleTransition transition : transitions){
					lifeCycleTransitionMapper.insertSelective(transition);
				}
			}
	}
	
	public abstract List<CuntaoLifecycleTransition> buildTransition(EventMessage message);
	
}
