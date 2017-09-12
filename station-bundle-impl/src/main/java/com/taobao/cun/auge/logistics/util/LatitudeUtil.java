package com.taobao.cun.auge.logistics.util;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public final class LatitudeUtil {

	private static final Logger logger = LoggerFactory.getLogger(LatitudeUtil.class);

	public static Map<String, String> findLatitude(String lastDivisionId, String addressDetail) {
		if (StringUtils.isEmpty(lastDivisionId)) {
			return Collections.<String, String> emptyMap();
		}
		 CloseableHttpClient closeableHttpClient = null;
		StringBuilder url = new StringBuilder("https://lsp.wuliu.taobao.com/locationservice/addr/geo_coding.do?");
		url.append("lastDivisionId=").append(lastDivisionId).append("&addr=").append(addressDetail);
        HttpGet httpget = new HttpGet(url.toString());
		try {
			SSLContext sslContext = SSLContexts.custom().useTLS().loadTrustMaterial(null, new TrustStrategy() {

				@Override
				public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType)
						throws java.security.cert.CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
			closeableHttpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

			HttpResponse response = closeableHttpClient.execute(httpget);
			HttpEntity entity = response.getEntity();
			String answer = EntityUtils.toString(entity);
			String json = answer.split("=")[1];
			return (Map<String, String>) JSON.parse(json.split("}")[0] + "}");
		} catch (Exception e) {
			logger.error("look up geo failed ! lastDivisionId = " + lastDivisionId + " ,addressDetail = " + addressDetail, e);
			return Collections.<String, String> emptyMap();
		}finally {
			try {
				if(closeableHttpClient != null){
					closeableHttpClient.close();
				}
			} catch (IOException e) {
				logger.error("close httpClient failed!",e);
			}
		}
	}
}
