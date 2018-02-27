package com.taobao.cun.auge.alipay.service.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;

public class AlipayRiskScanSignGenerator {

	private static final String UTF_8 = "UTF-8";
	public static final String HMAC_SHA1 = "HmacSHA1";

	/**
	 * 计算签名
	 * http://gitlab.alipay.net/cd-op/risk-control/wikis/riskScan-json-api-spec
	 * 
	 * @param path
	 *            待签名串的path部分
	 * @param params
	 *            请求参数map，值可以是一个String(单值参数)或一个String[](多值参数)
	 * @param secretKey
	 *            分配的密钥
	 * @return 计算好的签名值
	 */
	public static String sign(String path, Map<String, Object> params, String secretKey) {
		if (path == null || params == null || secretKey == null) {
            throw new IllegalArgumentException("Arguments should not be null.");
        }
		// path部分
		StringBuilder sb = new StringBuilder(path);

		// param部分
		Set<String> s = new TreeSet<String>();
		for (Map.Entry<String, Object> e : params.entrySet()) {
			Object v = e.getValue();
			if (v instanceof String) {
				s.add(e.getKey() + String.valueOf(v));
			} else if (v instanceof String[]) {
				String[] ss = (String[]) v;
				Arrays.sort(ss);
				StringBuilder b = new StringBuilder();
				b.append(e.getKey());
				for (String str : ss) {
                    b.append(str);
                }
				s.add(b.toString());
			} else {
				throw new IllegalArgumentException("Invalid signing value.");
			}
		}

		// 待签名串
		for (String str : s) {
            sb.append(str);
        }
		String data = sb.toString();
		try {
			byte[] ds = data.getBytes(UTF_8);
			return Hex.encodeHexString(hmacSha1(ds, secretKey.getBytes(UTF_8), 0, ds.length)).toUpperCase();
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * HMAC see：http://www.ietf.org/rfc/rfc2104.txt
	 */
	public static byte[] hmacSha1(byte[] data, byte[] key, int offset, int len) {
		SecretKeySpec signingKey = new SecretKeySpec(key, HMAC_SHA1);
		Mac mac = null;
		try {
			mac = Mac.getInstance(HMAC_SHA1);
			mac.init(signingKey);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (InvalidKeyException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		mac.update(data, offset, len);
		return mac.doFinal();
	}

}
