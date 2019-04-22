package com.taobao.cun.auge.flow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;

/**
 * 把一个对象深度拷贝成一个Map
 * 
 * @author chengyu.zhoucy
 *
 */
public class BeanDeepCopyUtils {
	public static Object copy(Object obj){
		if(obj == null){
            return new HashMap<String, Object>();
        }
		Map<String, Object> input = new HashMap<String, Object>();
		input.put("temp", obj);
		String json = JSONObject.toJSONString(input);
		Map<String, Object> result = toMap(JSONObject.parseObject(json));
		return result.get("temp");
	}
	
	private static Map<String, Object> toMap(JSONObject jObject){
		Map<String, Object> result = new HashMap<String, Object>();
		for(Entry<String, Object> entry : jObject.entrySet()) {
			if(entry.getValue() instanceof JSONObject) {
				result.put(entry.getKey(), toMap((JSONObject) entry.getValue()));
			}else if(entry.getValue() instanceof JSONArray) {
				result.put(entry.getKey(),toList((JSONArray) entry.getValue()));
			}else {
				result.put(entry.getKey(), entry.getValue());
			}
		}
		return result;
	}
	
	private static List<Object> toList(JSONArray array){
		List<Object> list = new ArrayList<Object>();
		for(Object obj : array) {
			if(obj instanceof JSONObject) {
				list.add(toMap((JSONObject) obj));
			}else if(obj instanceof JSONArray) {
				list.add(toList((JSONArray) obj));
			}else {
				list.add(obj);
			}
		}
		return list;
	}
	
	public static void main(String[] argv) {
		System.out.println(BeanDeepCopyUtils.copy(1));
		System.out.println(BeanDeepCopyUtils.copy(Lists.newArrayList(1,2,3)));
	}
}
