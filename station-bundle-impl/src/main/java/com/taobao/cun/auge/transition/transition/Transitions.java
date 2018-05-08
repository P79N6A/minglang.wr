package com.taobao.cun.auge.transition.transition;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;

@Component
public class Transitions {

	@Autowired
	private List<StateTransitionEventProcessor> transitions;

	
	public List<CuntaoLifecycleTransition> getTransitionsByEvent(EventMessage message){
		List<CuntaoLifecycleTransition> results = Lists.newArrayList();
		for(StateTransitionEventProcessor transition : transitions){
			List<StateTransitionTuple> tupleList = transition.mapTuple(message);
			if(tupleList != null  && !tupleList.isEmpty()){
				List<CuntaoLifecycleTransition> transitionResults = transition.mapTransition(tupleList);
				if(CollectionUtils.isNotEmpty(transitionResults)){
					results.addAll(transitionResults);
				}
			}
		}
		return results;
	}
}
