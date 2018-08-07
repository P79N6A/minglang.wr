package com.taobao.cun.auge.fence.instance.builder;

import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.FenceParamException;
import com.taobao.cun.auge.fence.constant.FenceConstants;
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
		//距离
		if(fenceRule.getDistance() != null && fenceRule.getDistance() > 0) {
			result.put(FenceConstants.RANGE_DISTANCE, fenceRule.getDistance());
		}
		//文本过滤
		if(CollectionUtils.isNotEmpty(fenceRule.getMatch())){
			Map<String, String> textFilters = Maps.newHashMap();
			for(String text : fenceRule.getMatch()) {
				if(text.equals("belongVillage")) {
					if(!Strings.isNullOrEmpty(station.getVillageDetail())) {
						textFilters.put("belongVillage", station.getVillageDetail());
					}else {
						throw new FenceParamException("围栏范围->文本匹配->所在行政村为空");
					}
				}
				
				/*if(text.equals("landmark")) {
					textFilters.put("landmark", text);
				}
				
				if(text.equals("receiveVillage")) {
					textFilters.put("receiveVillage", text);
				}*/
			}
			if(!textFilters.isEmpty()) {
				result.put(FenceConstants.RANGE_MATCH, textFilters);
			}
		}
		//行政区划
		if(!Strings.isNullOrEmpty(fenceRule.getDivision())){
			Map<String, String> division = Maps.newHashMap();
			if(fenceRule.getDivision().equals("province")) {
				division.put("code", station.getProvince());
				division.put("name", station.getProvinceDetail());
			}else if(fenceRule.getDivision().equals("city")) {
				division.put("code", station.getCity());
				division.put("name", station.getCityDetail());
			}else if(fenceRule.getDivision().equals("county")) {
				if(!Strings.isNullOrEmpty(station.getCounty())) {
					division.put("code", station.getCounty());
					division.put("name", station.getCountyDetail());
				}else {//属于县级市
					division.put("code", station.getCity());
					division.put("name", station.getCityDetail());
				}
			}else {
				if(!Strings.isNullOrEmpty(station.getTown())) {
					division.put("code", station.getTown());
					division.put("name", station.getTownDetail());
				}else {
					throw new FenceParamException("围栏范围->行政区划->乡镇街道级:镇为空");
				}
			}
			
			result.put(FenceConstants.RANGE_DIVISION, division);
		}
		return JSON.toJSONString(result);
	}
}
