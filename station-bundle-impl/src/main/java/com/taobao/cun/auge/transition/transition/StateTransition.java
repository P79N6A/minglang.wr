package com.taobao.cun.auge.transition.transition;

import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;

import com.taobao.cun.auge.dal.domain.CuntaoLifecycleTransition;

/**
 * 提供状态变更判断和操作的抽象接口
 * @author zhenhuan.zhangzh
 *
 */
public interface StateTransition {
	
	
	/**
	 * 把状态变更信息转换给周期周期变更对象，持久化
	 * @return
	 */
	public abstract BiConsumer<StateTransitionTuple,CuntaoLifecycleTransition> getStateComsumer();
	
	/**
	 * 判断变更消息的内容是否是生命周期流转中的状态变更消息，如果是就封装成StateTransitionTuple，不是就忽略掉
	 * @return
	 */
	public abstract BiPredicate<Map<String, Serializable>,Map<String, Serializable>> getStatePredicate();
	
	
	/**
	 * 转换状态变更内容的基本字段信息
	 * @return
	 */
	public abstract Function<StateTransitionTuple,BaseTransitionInfo> getBaseTransitionInfoProvider();

}
