package com.taobao.cun.auge.level.stationrule;

import org.springframework.stereotype.Component;

/**
 * 超标镇准入规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component("xgradeRuleResolver")
public class XGradeRuleResolver extends DefaultGradeRuleResolver {

	protected XGradeRuleResolver() {
		super("X");
	}
}
