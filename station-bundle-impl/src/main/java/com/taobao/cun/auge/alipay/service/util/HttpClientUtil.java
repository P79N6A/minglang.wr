package com.taobao.cun.auge.alipay.service.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.CodingErrorAction;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.alibaba.fastjson.JSON;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.MessageConstraints;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {

	private final static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);

	private static PoolingHttpClientConnectionManager connManager = null;
	private static CloseableHttpClient httpclient = null;
	private static final int SOCKET_TIMEOUT = 10000;
	private static final int CONNECTION_TIMEOUT = 10000;
	private static final int CONNECTION_REQUEST_TIMEOUT = 10000;

	private static final RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(SOCKET_TIMEOUT)
			.setConnectTimeout(CONNECTION_TIMEOUT).setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
			.setExpectContinueEnabled(false).build();

	static {
		try {
			SSLContext sslContext = SSLContexts.custom().useTLS().build();
			sslContext.init(null, new TrustManager[] { new X509TrustManager() {

				@Override
                public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				@Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} }, null);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
					.register("http", PlainConnectionSocketFactory.INSTANCE)
					.register("https", new SSLConnectionSocketFactory(sslContext)).build();

			connManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			httpclient = HttpClients.custom().setConnectionManager(connManager).build();
			// Create socket configuration
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).build();
			connManager.setDefaultSocketConfig(socketConfig);
			// Create message constraints
			MessageConstraints messageConstraints = MessageConstraints.custom().setMaxHeaderCount(200)
					.setMaxLineLength(2000).build();
			// Create connection configuration
			ConnectionConfig connectionConfig = ConnectionConfig.custom()
					.setMalformedInputAction(CodingErrorAction.IGNORE)
					.setUnmappableInputAction(CodingErrorAction.IGNORE).setCharset(Consts.UTF_8)
					.setMessageConstraints(messageConstraints).build();
			connManager.setDefaultConnectionConfig(connectionConfig);
			connManager.setMaxTotal(200);
			connManager.setDefaultMaxPerRoute(20);
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException", e);
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException", e);
		}
	}

	public static String post(String url, Map<String, String> parameter, String encodeCharset) throws Exception {
		HttpPost method = new HttpPost(url);
		try {
			method.setConfig(requestConfig);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : parameter.entrySet()) {
				formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			if (null == encodeCharset) {
				encodeCharset = Consts.UTF_8.toString();
			}
			method.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset));

			logger.info("begin invoke http post url:" + url + " , params:" + JSON.toJSONString(parameter));
			CloseableHttpResponse response = httpclient.execute(method);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					String str = EntityUtils.toString(entity, encodeCharset);
					logger.info("http post response, url :" + url + " , response string :" + str);
					return str;
				}
			} finally {
				if (entity != null) {
					entity.getContent().close();
				}
				if (response != null) {
					response.close();
				}
			}
		} finally {
			if (method != null && !method.isAborted()) {
				method.abort();
			}
			method.releaseConnection();
		}
		return "";
	}

	public static String get(String url, Map<String, String> parameter, String encodeCharset) throws Exception {
		String completeUrl = buildHttpGetUrl(url, parameter);
		HttpGet method = new HttpGet(completeUrl);
		try {
			if (null == encodeCharset) {
				encodeCharset = Consts.UTF_8.toString();
			}
			method.setConfig(requestConfig);
			logger.info("begin invoke http get, url:" + url + " , params:" + JSON.toJSONString(parameter));
			CloseableHttpResponse response = httpclient.execute(method);
			HttpEntity entity = response.getEntity();
			try {
				if (entity != null) {
					String str = EntityUtils.toString(entity, encodeCharset);
					logger.info("http get response, url :" + url + " , response string :" + str);
					return str;
				}
			} finally {
				if (entity != null) {
					entity.getContent().close();
				}
				if (response != null) {
					response.close();
				}
			}
		} finally {
			if (method != null && !method.isAborted()) {
				method.abort();
			}
			method.releaseConnection();
		}
		return "";
	}

	@SuppressWarnings("deprecation")
	private static String buildHttpGetUrl(String url, Map<String, String> parameter) {
		StringBuilder sb = new StringBuilder();
		sb.append(url);
		int i = 0;
		for (Entry<String, String> entry : parameter.entrySet()) {
			if (i == 0 && !url.contains("?")) {
				sb.append("?");
			} else {
				sb.append("&");
			}
			sb.append(entry.getKey());
			sb.append("=");
			String value = entry.getValue();
			try {
				sb.append(URLEncoder.encode(value, "GBK"));
			} catch (UnsupportedEncodingException e) {
				logger.warn("encode http get params error, value is " + value, e);
				sb.append(URLEncoder.encode(value));
			}
			i++;
		}
		return sb.toString();
	}

}