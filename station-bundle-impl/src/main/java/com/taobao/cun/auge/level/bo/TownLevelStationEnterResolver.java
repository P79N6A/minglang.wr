package com.taobao.cun.auge.level.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.enterrule.grade.GradeRuleResolver;

/**
 * 解析镇域开点规则
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class TownLevelStationEnterResolver {
	@Resource
	private GradeRuleResolver agradeRuleResolver;
	@Resource
	private GradeRuleResolver bgradeRuleResolver;
	@Resource
	private GradeRuleResolver xgradeRuleResolver;
	
	TownLevelStationRuleDto resolve(TownLevelDto townLevelDto) {
		return getGradeRuleResolver(townLevelDto).resolve(townLevelDto);
	}
	
	private GradeRuleResolver getGradeRuleResolver(TownLevelDto townLevelDto) {
		switch(townLevelDto.getLevel()) {
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
