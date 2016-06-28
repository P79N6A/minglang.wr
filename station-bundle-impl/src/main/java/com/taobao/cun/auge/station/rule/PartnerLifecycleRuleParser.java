package com.taobao.cun.auge.station.rule;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ali.com.google.common.collect.Lists;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.google.common.collect.Maps;
import com.taobao.cun.auge.dal.domain.PartnerLifecycleItems;
import com.taobao.cun.auge.station.convert.PartnerLifecycleConverter;
import com.taobao.cun.auge.station.dto.PartnerLifecycleDto;
import com.taobao.cun.auge.station.enums.PartnerInstanceStateEnum;
import com.taobao.cun.auge.station.enums.PartnerInstanceTypeEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckEnum;
import com.taobao.cun.auge.station.enums.PartnerLifecycleItemCheckResultEnum;
import com.taobao.cun.auge.station.exception.AugeServiceException;
import com.taobao.vipserver.client.utils.CollectionUtils;

import reactor.core.support.Assert;

/**
 * 映射规则解析器
 * 
 * @author linjianke
 *
 */
public class PartnerLifecycleRuleParser {
	private static final Logger logger = LoggerFactory.getLogger(PartnerLifecycleRuleParser.class);

	private static Map<String, List<PartnerLifecycleExecutableMappingRule>> executableMappingRules;
	private static Map<String, Field> partnerLifecycleItemsFieldMap;

	private static Map<String, List<PartnerLifecycleRuleMapping>> stateMappingRules;
	private static Map<String, Field> partnerLifecycleRuleFieldMap;

	static {
		loadFiled();
		loadMappingRule();
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
		PartnerLifecycleExecutableMappingRule mappingRule = getMappingRuleByPartnerInstanceType(type.getCode(), item);
		Assert.notNull(mappingRule, "Mapping rule not exists for " + item);

		// 是否已执行
		if (CollectionUtils.isNotEmpty((mappingRule.getExecutedCondition()))) {
			// 多个规则里面只要满足其一即认为已执行
			for (Map<String, String> ruleCondition : mappingRule.getExecutedCondition()) {
				if (isMatchExecuteCondition(ruleCondition, lifecycle)) {
					return PartnerLifecycleItemCheckResultEnum.EXECUTED;
				}
			}
		}

		// 是否可执行
		if (CollectionUtils.isNotEmpty((mappingRule.getExecutableCondition()))) {
			// 多个规则里面只要满足其一即认为可执行
			for (Map<String, String> ruleCondition : mappingRule.getExecutableCondition()) {
				if (isMatchExecuteCondition(ruleCondition, lifecycle)) {
					return PartnerLifecycleItemCheckResultEnum.EXECUTABLE;
				}
			}
		}
		return PartnerLifecycleItemCheckResultEnum.NONEXCUTABLE;

	}

	/**
	 * 根据合伙人类型和老模型状态映射新模型
	 * 
	 * @param type
	 *            合伙人类型
	 * @param stationApplyState
	 *            老模型状态
	 * @return
	 */
	public static PartnerLifecycleRule parseStationApplyState(PartnerInstanceTypeEnum type, String stationApplyState) {

		List<PartnerLifecycleRuleMapping> ruleList = stateMappingRules.get(type.getCode());
		for (PartnerLifecycleRuleMapping mapping : ruleList) {
			if (stationApplyState.equalsIgnoreCase(mapping.getStationApplyState())) {
				return mapping.getPartnerLifecycleRule();
			}
		}
		throw new AugeServiceException("PartnerLifecycleRule not exists: " + type.getCode() + " , " + stationApplyState);
	}

	/**
	 * 映射到老模型
	 * 
	 * @param partnerType
	 *            合伙人类型
	 * @param instatnceState
	 *            新模型实例状态
	 * @param partnerLifecycle
	 *            生命周期元素对象
	 * @return
	 */
	public static String convertInstanceState2StationApplyState(String partnerType, String instatnceState,
			PartnerLifecycleDto partnerLifecycle) {
		List<PartnerLifecycleRuleMapping> ruleList = stateMappingRules.get(partnerType);
		for (PartnerLifecycleRuleMapping mapping : ruleList) {
			if (isMatchPartnerLifecycleRule(mapping.getPartnerLifecycleRule(), instatnceState, partnerLifecycle)) {
				return mapping.getStationApplyState();
			}
		}
		throw new AugeServiceException("convertInstanceState2StationApplyState error: " + partnerType + " , " + instatnceState + ", "
				+ JSON.toJSONString(partnerLifecycle));
	}

	private static boolean isMatchExecuteCondition(Map<String, String> ruleCondition, PartnerLifecycleItems lifecycle) {
		Iterator<String> it = ruleCondition.keySet().iterator();
		while (it.hasNext()) {

			try {
				String key = it.next();
				Field field = partnerLifecycleItemsFieldMap.get(key);
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

	private static PartnerLifecycleExecutableMappingRule getMappingRuleByPartnerInstanceType(String code, String item) {
		List<PartnerLifecycleExecutableMappingRule> list = executableMappingRules.get(code);
		for (PartnerLifecycleExecutableMappingRule rule : list) {
			if (rule.getItem().equals(item)) {
				return rule;
			}
		}
		throw new AugeServiceException("getMappingRuleByPartnerInstanceType error");
	}

	private static boolean isMatchPartnerLifecycleRule(PartnerLifecycleRule partnerLifecycleRule, String instatnceState,
			PartnerLifecycleDto partnerLifecycle) {
		// 主状态是否匹配
		if (!partnerLifecycleRule.getState().getCode().equals(instatnceState)) {
			return false;
		}
		/**
		 * 生命周期元素是否匹配
		 * settledProtocol,bond,quitProtocol,logisticsApprove,roleApprove,
		 * confirm,system
		 */
		if (null != partnerLifecycleRule.getSettledProtocol()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getSettledProtocol();
			String itemCode = null == partnerLifecycle.getSettledProtocol() ? null : partnerLifecycle.getSettledProtocol().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}
		}

		if (null != partnerLifecycleRule.getBond()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getBond();
			String itemCode = null == partnerLifecycle.getBond() ? null : partnerLifecycle.getBond().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}
		}

		if (null != partnerLifecycleRule.getQuitProtocol()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getQuitProtocol();
			String itemCode = null == partnerLifecycle.getQuitProtocol() ? null : partnerLifecycle.getQuitProtocol().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}

		}

		if (null != partnerLifecycleRule.getLogisticsApprove()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getLogisticsApprove();
			String itemCode = null == partnerLifecycle.getLogisticsApprove() ? null : partnerLifecycle.getLogisticsApprove().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}
		}

		if (null != partnerLifecycleRule.getRoleApprove()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getRoleApprove();
			String itemCode = null == partnerLifecycle.getRoleApprove() ? null : partnerLifecycle.getRoleApprove().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}
		}

		if (null != partnerLifecycleRule.getConfirm()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getConfirm();
			String itemCode = null == partnerLifecycle.getConfirm() ? null : partnerLifecycle.getConfirm().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!(isMatch)) {
				return false;
			}
		}

		if (null != partnerLifecycleRule.getSystem()) {
			PartnerLifecycleRuleItem ruleItem = partnerLifecycleRule.getSystem();
			String itemCode = null == partnerLifecycle.getConfirm() ? null : partnerLifecycle.getConfirm().getCode();
			boolean isMatch = ruleItem.getEqual() == (ruleItem.getValue().equals(itemCode));
			if (!isMatch) {
				return false;
			}
		}

		return true;
	}

	private static void loadFiled() {
		// 加载PartnerLifecycleItems.class的field
		if (null == partnerLifecycleItemsFieldMap) {
			partnerLifecycleItemsFieldMap = Maps.newHashMap();
		}
		Field[] partnerLifecycleItemsFields = PartnerLifecycleItems.class.getDeclaredFields();
		for (int j = 0; j < partnerLifecycleItemsFields.length; j++) {
			Field field = partnerLifecycleItemsFields[j];
			field.setAccessible(true);
			partnerLifecycleItemsFieldMap.put(field.getName(), field);
		}

		// 加载PartnerLifecycleRule.class的field
		if (null == partnerLifecycleRuleFieldMap) {
			partnerLifecycleRuleFieldMap = Maps.newHashMap();
		}
		Field[] partnerLifecycleRuleFields = PartnerLifecycleRule.class.getDeclaredFields();
		for (int j = 0; j < partnerLifecycleRuleFields.length; j++) {
			Field field = partnerLifecycleRuleFields[j];
			field.setAccessible(true);
			partnerLifecycleRuleFieldMap.put(field.getName(), field);
		}

	}

	private static void loadMappingRule() {
		InputStream executableRuleStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("partner_lifecycle_executable_mapping_rule.json");
		InputStream stateRuleStream = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("partner_lifecycle_state_mapping_rule.json");

		try {
			String executableRuleContent = IOUtils.toString(executableRuleStream);
			logger.info("load partner lifecycle executable mapping rule: {}", executableRuleContent);
			executableMappingRules = JSON.parseObject(executableRuleContent,
					new TypeReference<Map<String, List<PartnerLifecycleExecutableMappingRule>>>() {
					});

			String stateRuleContent = IOUtils.toString(stateRuleStream);
			logger.info("load partner lifecycle state mapping rule: {}", stateRuleContent);

			Map<String, List<PartnerLifecycleStateMappingRule>> originalStateMappingRules = JSON.parseObject(stateRuleContent,
					new TypeReference<Map<String, List<PartnerLifecycleStateMappingRule>>>() {
					});
			stateMappingRules = adpatStateMappingRules(originalStateMappingRules);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(executableRuleStream);
		}
	}

	private static Map<String, List<PartnerLifecycleRuleMapping>> adpatStateMappingRules(
			Map<String, List<PartnerLifecycleStateMappingRule>> originalStateMappingRules) {
		Map<String, List<PartnerLifecycleRuleMapping>> resultMap = Maps.newHashMap();
		// key为TP、TPA、TPV
		for (String key : originalStateMappingRules.keySet()) {
			// 原始的规则列表
			List<PartnerLifecycleStateMappingRule> list = originalStateMappingRules.get(key);
			// 适配完的规则列表
			List<PartnerLifecycleRuleMapping> ruleList = Lists.newArrayList();
			// 遍历原始的规则列表，进行适配
			for (PartnerLifecycleStateMappingRule mappingRule : list) {

				PartnerInstanceStateEnum partnerInstanceState = PartnerInstanceStateEnum.valueof(mappingRule.getPartnerInstanceState());
				Assert.notNull(partnerInstanceState);

				PartnerLifecycleRule partnerLifecycleRule = new PartnerLifecycleRule();
				partnerLifecycleRule.setState(partnerInstanceState);
				Map<String, String> condition = mappingRule.getCondition();
				if (null != condition && !condition.isEmpty()) {
					/*
					 * 遍历condition map的元素，如"roleApprove":"!TO_AUDIT"
					 * 如果值以"!"开头表示不等于
					 * 
					 */
					for (String itemKey : condition.keySet()) {
						Field field = partnerLifecycleRuleFieldMap.get(itemKey);
						String itemValue = condition.get(itemKey);
						// 如果值以"!"开头表示不等于
						PartnerLifecycleRuleItem ruleItem = itemValue.startsWith("!")
								? new PartnerLifecycleRuleItem(itemValue.substring(1), false)
								: new PartnerLifecycleRuleItem(itemValue, true);
						try {
							field.set(partnerLifecycleRule, ruleItem);
						} catch (IllegalArgumentException | IllegalAccessException e) {
							logger.error("adpatStateMappingRules error", e);
							throw new AugeServiceException("padpatStateMappingRules error");
						}
					}
				}
				// 适配后的规则
				PartnerLifecycleRuleMapping mapping = new PartnerLifecycleRuleMapping();
				mapping.setStationApplyState(mappingRule.getStationApplyState());
				mapping.setPartnerLifecycleRule(partnerLifecycleRule);
				ruleList.add(mapping);
			}
			resultMap.put(key, ruleList);
		}

		return resultMap;
	}

	public static void main(String[] args) {
		// businessType;settledProtocol;bond;quitProtocol;logisticsApprove;currentStep;roleApprove;confirm;system;
		PartnerLifecycleItems lifecycle = new PartnerLifecycleItems();
		lifecycle.setSettledProtocol("SIGNING");
		lifecycle.setRoleApprove(null);
		lifecycle.setBusinessType("SETTLING");

		System.out.println("---" + parseExecutable(PartnerInstanceTypeEnum.TPA, PartnerLifecycleItemCheckEnum.settledProtocol, lifecycle));

		System.out.println("---" + JSON.toJSONString(parseStationApplyState(PartnerInstanceTypeEnum.TP, "SUMITTED")));

		System.out.println("---"
				+ convertInstanceState2StationApplyState("TP", "SETTLING", PartnerLifecycleConverter.toPartnerLifecycleDto(lifecycle)));

	}

}
