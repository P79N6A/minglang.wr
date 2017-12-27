package com.taobao.cun.auge.common.utils;

import org.apache.commons.lang3.StringUtils;


public final class LoginIdConverter {

	private LoginIdConverter() {

	}

	/**
	 * 工号少于6位，则补全到6位
	 * 
	 * @param loginId
	 * @return
	 */
	public static String convertTo6Bit(String loginId) {
		if (StringUtils.isEmpty(loginId)) {
			return "";
		}
		StringBuilder newLoginId = new StringBuilder();
		int length = loginId.length();
		if (length < 6) {
			int bit = 6 - length;
			for (int i = 0; i < bit; i++) {
				newLoginId.append("0");
			}
		}
		newLoginId.append(loginId);
		return newLoginId.toString();
	}

	/**
	 * 删除工号前面的0
	 * 
	 * @param loginId
	 * @return
	 */
	public static String deleteFirstZero(String loginId) {
		if (StringUtils.isNotEmpty(loginId) && loginId.startsWith("0")) {
			return loginId.substring(1, loginId.length());
		}
		return loginId;
	}

}
