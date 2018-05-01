package com.taobao.cun.auge.transition.transition;

import java.util.List;

import com.alibaba.middleware.jingwei.client.custom.EventMessage;
import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;

public interface StateTransitionEventProcessor {

	/**
	 * 是否匹配
	 * @param tableName
	 * @param action
	 * @return
	 */
	public Boolean isMatched(String action,String tableName);
	
	/**
	 * 处理的业务类型
	 * @return
	 */
	public String bizType();
	
	/**
	 * 转换成状态变更元组
	 * @param event
	 */
	public List<StateTransitionTuple> mapTuple(EventMessage event);
	
	
	/**
	 * 转换成状态变更记录
	 * @param tuple
	 * @return
	 */
	List<CuntaoLifecycleTransition> mapTransition(List<StateTransitionTuple> tuple);
	
}
