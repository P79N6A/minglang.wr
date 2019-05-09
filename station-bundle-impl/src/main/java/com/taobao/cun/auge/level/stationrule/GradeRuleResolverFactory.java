package com.taobao.cun.auge.level.stationrule;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

/**
 * 层级准入规则解析器工厂
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class GradeRuleResolverFactory {
	@Resource
	private GradeRuleResolver agradeRuleResolver;
	@Resource
	private GradeRuleResolver bgradeRuleResolver;
	@Resource
	private GradeRuleResolver xgradeRuleResolver;
	@Resource
	private GradeRuleResolver cgradeRuleResolver;
	
	public GradeRuleResolver getGradeRuleResolver(String level) {
		switch(level) {
		case "A":
			return agradeRuleResolver;
		case "B":
			return bgradeRuleResolver;
		case "C":
			return cgradeRuleResolver;
		case "X":
			return xgradeRuleResolver;
		default:
			throw new RuntimeException("不支持的层级规则");
		}
	}
}
