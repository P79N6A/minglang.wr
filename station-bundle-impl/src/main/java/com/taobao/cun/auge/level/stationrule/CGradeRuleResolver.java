package com.taobao.cun.auge.level.stationrule;

import org.springframework.stereotype.Component;

/**
 * C镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("cgradeRuleResolver")
public class CGradeRuleResolver extends DefaultGradeRuleResolver {

	protected CGradeRuleResolver() {
		super("C");
	}

}
