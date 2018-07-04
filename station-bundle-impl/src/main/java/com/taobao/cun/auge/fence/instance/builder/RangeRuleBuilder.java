package com.taobao.cun.auge.fence.instance.builder;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.instance.rule.RangeFenceRule;

/**
 * 构建范围规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class RangeRuleBuilder implements RuleBuilder<RangeFenceRule> {

	@Override
	public String build(Station station, RangeFenceRule fenceRule) {
		Map<String, Object> result = Maps.newHashMap();
		if(fenceRule.getDistance() != null && fenceRule.getDistance() > 0) {
			result.put("distance", fenceRule.getDistance());
		}
		
		if(CollectionUtils.isNotEmpty(fenceRule.getTextFilters())){
			Map<String, String> textFilters = Maps.newHashMap();
			for(String text : fenceRule.getTextFilters()) {
				if(text.equals("belong")) {
					textFilters.put("belong", station.getVillageDetail());
				}
				
				if(text.equals("landmark")) {
					textFilters.put("landmark", text);
				}
				
				if(text.equals("receive")) {
					textFilters.put("receive", text);
				}
			}
			result.put("textFilters", textFilters);
		}
		
		if(Strings.isNullOrEmpty(fenceRule.getDivision())){
			Map<String, String> division = Maps.newHashMap();
			if(fenceRule.getDivision().equals("province")) {
				division.put("province", station.getProvince());
			}else if(fenceRule.getDivision().equals("city")) {
				division.put("city", station.getCity());
			}else if(fenceRule.getDivision().equals("county")) {
				division.put("county", station.getCounty());
			}else {
				division.put("town", station.getTown());
			}
			
			result.put("division", division);
		}
		return JSON.toJSONString(result);
	}
}
