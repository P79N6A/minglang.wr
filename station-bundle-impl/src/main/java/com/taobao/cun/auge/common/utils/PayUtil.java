package com.taobao.cun.auge.common.utils;

import java.util.HashMap;
import java.util.Map;

import org.esb.nasdaq.service.security.SignatureUtil;
import org.springframework.util.StringUtils;


public class PayUtil {

	
	public static String createUrl(PayParam param){
		Map<String,String> params=new HashMap<String,String>();
		params.put("contract_no", param.getItemNum());
		if(!StringUtils.isEmpty(param.getReturnUrl())){
			params.put("return_url", param.getReturnUrl());
		}
		params.put("timestamp", String.valueOf(System.currentTimeMillis()));
		params.put("system", param.getSystem());
		String sign=SignatureUtil.sign(params, param.getKey(),"UTF-8");
		params.put("sign", sign);
		params.put("ali_id", param.getAliId());
		params.put("site", param.getSite());
		String queryString=SignatureUtil.getQueryString(params);
		StringBuilder sb=new StringBuilder(param.getGetway());
		return sb.append("?").append(queryString).toString();
	}
	
	
}
