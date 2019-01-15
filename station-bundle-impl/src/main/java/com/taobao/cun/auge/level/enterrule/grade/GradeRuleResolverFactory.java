package com.taobao.cun.auge.level.enterrule.grade;

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
	
	public GradeRuleResolver getGradeRuleResolver(String level) {
		switch(level) {
		case "A":
			return agradeRuleResolver;
		case "B":
			return bgradeRuleResolver;
		case "X":
			return xgradeRuleResolver;
		default:
			throw new RuntimeException("不支持的层级规则");
		}
	}
}
