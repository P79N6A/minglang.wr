package com.taobao.cun.auge.level.stationrule;

import org.springframework.stereotype.Component;

/**
 * A镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("agradeRuleResolver")
public class AGradeRuleResolver extends DefaultGradeRuleResolver {

	protected AGradeRuleResolver() {
		super("A");
	}

}
