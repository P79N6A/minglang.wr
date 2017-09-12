package com.taobao.cun.auge.logistics.convert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

public final class ManagerUtil {
	public static final String SP = ";";

	private ManagerUtil() {

	}

	public static String toString(final Set<String> set) {
		if (CollectionUtils.isEmpty(set)) {
			return null;
		}

		final StringBuilder featureSB = new StringBuilder();
		for (String v : set) {
			if (StringUtils.isEmpty(v)) {
				// 过滤掉无效的v对
				continue;
			}

			featureSB.append(v).append(SP);
		}
		return featureSB.toString();
	}

	public static Set<String> toSet(final String mStr) {
		if (StringUtils.isBlank(mStr)) {
			return Collections.<String> emptySet();
		}

		final Set<String> set = new HashSet<String>();
		for (String v : StringUtils.split(mStr, SP)) {
			if (StringUtils.isBlank(v)) {
				// 过滤掉为空的字符串片段
				continue;
			}
			set.add(v);
		}
		return set;
	}
}
