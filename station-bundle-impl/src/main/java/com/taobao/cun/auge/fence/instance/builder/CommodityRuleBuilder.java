package com.taobao.cun.auge.fence.instance.builder;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.rule.CommodityFenceRule;

/**
 * 构建商品规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CommodityRuleBuilder implements RuleBuilder<CommodityFenceRule> {

	@Override
	public String build(Station station, CommodityFenceRule fenceRule) {
		return JSON.toJSONString(fenceRule);
	}

}
