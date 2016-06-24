package com.taobao.cun.auge.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.condition.PartnerInstancePageCondition;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.vipserver.client.utils.CollectionUtils;

import reactor.core.support.Assert;

//businessType;settledProtocol;bond;quitProtocol;logisticsApprove;currentStep;roleApprove;confirm;system;

public class PartnerLifecycleParser {
	private static final Logger logger = LoggerFactory.getLogger(PartnerLifecycleParser.class);
	private static Map<String, List<PartnerLifecycleMappingRule>> rules;
	private static Map<String, Field> fieldMap;

	static {
		loadMappingRule();
		loadFiled();
	}

	/**
	 * 解析当前某个生命周期item是否可执行
	 * 
	 * @param type
	 *            合伙人类型
	 * @param targetItem
	 *            具体需要执行的事项(冻结、签入驻协议等)
	 * @param lifecycle
	 *            生命周期对象
	 * @return
	 */
	public static PartnerLifecycleItemCheckResultEnum parseExecutable(PartnerInstanceTypeEnum type,
			PartnerLifecycleItemCheckEnum targetItem, PartnerLifecycleItems lifecycle) {
		String item = lifecycle.getBusinessType() + "." + targetItem;
		PartnerLifecycleMappingRule mappingRule = getMappingRuleByPartnerInstanceType(type.getCode(), item);
		Assert.notNull(mappingRule, "Mapping rule not exists for " + item);

		// 是否已执行
		if (CollectionUtils.isNotEmpty((mappingRule.getExecutedCondition()))) {
			// 多个规则里面只要满足其一即认为已执行
			for (Map<String, String> ruleCondition : mappingRule.getExecutedCondition()) {
				if (isMatchCondition(ruleCondition, lifecycle)) {
					return PartnerLifecycleItemCheckResultEnum.EXECUTED;
				}
			}
		}

		// 是否可执行
		if (CollectionUtils.isNotEmpty((mappingRule.getExecutableCondition()))) {
			// 多个规则里面只要满足其一即认为可执行
			for (Map<String, String> ruleCondition : mappingRule.getExecutableCondition()) {
				if (isMatchCondition(ruleCondition, lifecycle)) {
					return PartnerLifecycleItemCheckResultEnum.EXECUTABLE;
				}
			}
		}
		return PartnerLifecycleItemCheckResultEnum.NONEXCUTABLE;

	}

	private static boolean isMatchCondition(Map<String, String> ruleCondition, PartnerLifecycleItems lifecycle) {
		Iterator<String> it = ruleCondition.keySet().iterator();
		while (it.hasNext()) {

			try {
				String key = it.next();
				Field field = fieldMap.get(key);
				String ruleValue = ruleCondition.get(key);

				String currentValue = (String) field.get(lifecycle);
				// 规则里当前值需要为空时，当前实际值也需要为空
				if (null == ruleValue) {
					if (null != currentValue) {
						return false;
					}
				} else if (!ruleValue.equals(currentValue)) {
					return false;
				}

			} catch (IllegalArgumentException | IllegalAccessException e) {
				logger.error("parse field value error", e);
				throw new AugeServiceException("parse field value error");
			}

		}
		return true;
	}

	private static PartnerLifecycleMappingRule getMappingRuleByPartnerInstanceType(String code, String item) {
		List<PartnerLifecycleMappingRule> list = rules.get(code);
		for (PartnerLifecycleMappingRule rule : list) {
			if (rule.getItem().equals(item)) {
				return rule;
			}
		}
		return null;
	}

	public PartnerInstancePageCondition adaptQueryCondition(String state, PartnerInstanceTypeEnum type) {
		return null;
	}

	public static String convertInstanceState2StationApplyState(String partnerType, String instatnceState,
			PartnerLifecycleDto partnerLifecycle) {
		return null;
	}

	private static void loadFiled() {
		if (null == fieldMap) {
			fieldMap = Maps.newHashMap();
		}
		Field[] fields = PartnerLifecycleItems.class.getDeclaredFields();
		for (int j = 0; j < fields.length; j++) {
			Field field = fields[j];
			field.setAccessible(true);
			fieldMap.put(field.getName(), field);
		}

	}

	private static void loadMappingRule() {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("partner_lifecycle_mapping_rule.json");
		try {
			String content = IOUtils.toString(is);
			logger.info("load partner lifecycle mapping rule: {}", content);
			rules = JSON.parseObject(content, new TypeReference<Map<String, List<PartnerLifecycleMappingRule>>>() {
			});
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(is);
		}
	}

	public static void main(String[] args) {
		System.out.println("---");
	}

}
