package com.taobao.cun.auge.common.utils;

import java.util.Collections;
import java.util.Map;

import com.alibaba.common.lang.StringUtil;
import com.alibaba.fastjson.JSON;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LatitudeUtil {

	private static final Logger logger = LoggerFactory.getLogger(LatitudeUtil.class);

	public static Map<String, String> findLatitude(String lastDivisionId, String addressDetail) {
		if (StringUtil.isEmpty(lastDivisionId)) {
			return Collections.<String, String> emptyMap();
		}
		try {
			StringBuilder url = new StringBuilder("http://lsp.wuliu.taobao.com/locationservice/addr/geo_coding.do?");
			url.append("lastDivisionId=").append(lastDivisionId).append("&addr=").append(addressDetail);
			HttpClient httpCLient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url.toString());
			HttpResponse response = httpCLient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String answer = EntityUtils.toString(entity);
			String json = answer.split("=")[1];
			return (Map<String, String>) JSON.parse(json.split("}")[0] + "}");
		} catch (Exception e) {
			logger.error("获取经纬度坐标失败，lastDivisionId = " + lastDivisionId + " ,addressDetail = " + addressDetail, e);
			return Collections.<String, String> emptyMap();
		}
	}
}
