package com.taobao.cun.auge.level.enterrule.grade;

import org.springframework.stereotype.Component;

/**
 * A镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class AGradeRuleResolver extends DefaultGradeRuleResolver {

	protected AGradeRuleResolver() {
		super("A");
	}

}
