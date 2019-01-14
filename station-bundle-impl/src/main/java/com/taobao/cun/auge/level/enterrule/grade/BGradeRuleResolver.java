package com.taobao.cun.auge.level.enterrule.grade;

import org.springframework.stereotype.Component;

/**
 * B镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("bgradeRuleResolver")
public class BGradeRuleResolver extends DefaultGradeRuleResolver {

	protected BGradeRuleResolver() {
		super("B");
	}

}
