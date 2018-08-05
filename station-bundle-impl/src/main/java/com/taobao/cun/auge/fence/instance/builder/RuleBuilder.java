package com.taobao.cun.auge.fence.instance.builder;

import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.rule.FenceRule;

/**
 * 规则构建
 * 
 * @author chengyu.zhoucy
 *
 * @param <R>
 */
public interface RuleBuilder<R extends FenceRule> {
	/**
	 * 按规则构建结果
	 * @param station
	 * @param fenceRule
	 * @return
	 */
	String build(Station station, R fenceRule);
}
