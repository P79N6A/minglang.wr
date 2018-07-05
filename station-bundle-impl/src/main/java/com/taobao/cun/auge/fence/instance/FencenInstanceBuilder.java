package com.taobao.cun.auge.fence.instance;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.taobao.cun.auge.dal.domain.CuntaoCainiaoStationRel;
import com.taobao.cun.auge.dal.domain.FenceEntity;
import com.taobao.cun.auge.dal.domain.Station;
import com.taobao.cun.auge.fence.dto.FenceTemplateDto;
import com.taobao.cun.auge.fence.instance.builder.CommodityRuleBuilder;
import com.taobao.cun.auge.fence.instance.builder.RangeRuleBuilder;
import com.taobao.cun.auge.fence.instance.builder.UserRuleBuilder;
import com.taobao.cun.auge.fence.instance.rule.CommodityFenceRule;
import com.taobao.cun.auge.fence.instance.rule.RangeFenceRule;
import com.taobao.cun.auge.fence.instance.rule.UserFenceRule;
import com.taobao.cun.auge.station.bo.CuntaoCainiaoStationRelBO;
import com.taobao.cun.auge.station.enums.CuntaoCainiaoStationRelTypeEnum;

@Component
public class FencenInstanceBuilder {
	@Resource
	private CommodityRuleBuilder commodityRuleBuilder;
	@Resource
	private UserRuleBuilder userRuleBuilder;
	@Resource
	private RangeRuleBuilder rangeRuleBuilder;
	@Resource
	private CuntaoCainiaoStationRelBO cuntaoCainiaoStationRelBO;
	
	public FenceEntity build(Station station, FenceTemplateDto fenceTemplateDto) {
		FenceEntity fenceEntity = new FenceEntity();
		
		String crresult = commodityRuleBuilder.build(station, createCommodityFenceRule(fenceTemplateDto));
		String urresult = userRuleBuilder.build(station, createUserFenceRule(fenceTemplateDto));
		String rrresult =  rangeRuleBuilder.build(station, createRangeFenceRule(fenceTemplateDto));
		fenceEntity.setRangeRule(rrresult);
		fenceEntity.setUserType(urresult);
		fenceEntity.setCommodity(crresult);
		fenceEntity.setState(fenceTemplateDto.getState());
		fenceEntity.setStationId(station.getId());
		fenceEntity.setGmtCreate(new Date());
		fenceEntity.setGmtModified(new Date());
		fenceEntity.setCreator("job");
		fenceEntity.setModifier("job");
		fenceEntity.setIsDeleted("n");
		fenceEntity.setProvince(station.getProvince());
		fenceEntity.setCity(station.getCity());
		fenceEntity.setCounty(station.getCounty());
		fenceEntity.setTown(station.getTown());
		fenceEntity.setDefaultCheck(fenceTemplateDto.getDefaultCheck());
		fenceEntity.setTemplateId(fenceTemplateDto.getId());
		fenceEntity.setName(fenceTemplateDto.getName());
		fenceEntity.setType(fenceTemplateDto.getTypeEnum().getCode());
		
		CuntaoCainiaoStationRel cuntaoCainiaoStationRel = cuntaoCainiaoStationRelBO.queryCuntaoCainiaoStationRel(station.getId(), CuntaoCainiaoStationRelTypeEnum.STATION);
		if(cuntaoCainiaoStationRel != null) {
			fenceEntity.setCainiaoStationId(cuntaoCainiaoStationRel.getCainiaoStationId());
		}
		return fenceEntity;
	}

	private RangeFenceRule createRangeFenceRule(FenceTemplateDto fenceTemplateDto) {
		return JSON.parseObject(fenceTemplateDto.getRangeRule(), RangeFenceRule.class);
	}

	private UserFenceRule createUserFenceRule(FenceTemplateDto fenceTemplateDto) {
		UserFenceRule userFenceRule = new UserFenceRule();
		userFenceRule.setUserType(fenceTemplateDto.getUserTypeEnum().getCode());
		return userFenceRule;
	}

	private CommodityFenceRule createCommodityFenceRule(FenceTemplateDto fenceTemplateDto) {
		if(fenceTemplateDto.getLimitCommodity().equals("Y")) {
			return JSON.parseObject(fenceTemplateDto.getCommodityRule(), CommodityFenceRule.class);
		}
		return null;
	}
}
