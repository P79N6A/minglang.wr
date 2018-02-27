package com.taobao.cun.auge.asset.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 映射规则解析器

 *
 */
public class AssetOwnerParser {
	private static final Logger logger = LoggerFactory.getLogger(AssetOwnerParser.class);

	private static Map<String, List<AssetOwner>> assetOwnerList;
	
	static {
		loadMappingRule();
	}
	
	private static void loadMappingRule() {
		InputStream ruleStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("asset_owner.json");
		try {
			// 加载是否可执行的规则
			String ruleContent = IOUtils.toString(ruleStream);
			assetOwnerList = JSON.parseObject(ruleContent,
					new TypeReference<Map<String, List<AssetOwner>>>() {
					});

		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(ruleStream);
		}
	}
	
	public static  Map<String, List<AssetOwner>> getOwnerList() {
		return assetOwnerList;
	}
}
