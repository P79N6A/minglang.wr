package com.taobao.cun.auge.level.bo;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.TownLevel;
import com.taobao.cun.auge.dal.domain.TownLevelExample;
import com.taobao.cun.auge.dal.domain.TownLevelExample.Criteria;
import com.taobao.cun.auge.dal.mapper.TownLevelMapper;
import com.taobao.cun.auge.dal.mapper.ext.StationLevelExtMapper;
import com.taobao.cun.auge.level.dto.TownLevelCalcResult;
import com.taobao.cun.auge.level.dto.TownLevelCondition;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelStationRuleDto;
import com.taobao.cun.auge.level.levelrule.TownLevelResolver;
import com.taobao.cun.auge.level.stationrule.GradeRuleFacade;

@Component
public class TownLevelBo {
	@Resource
	private TownLevelMapper townLevelMapper;
	@Resource
	private GradeRuleFacade gradeRuleFacade;
	@Resource
	private TownLevelResolver townLevelResolver;
	@Resource
	private StationLevelExtMapper stationLevelExtMapper;
	
	private TownLevelStationRuleDto getTownLevelStationRule(TownLevelDto townLevelDto) {
		return gradeRuleFacade.getTownLevelStationRule(townLevelDto);
	}
	
	public TownLevelCalcResult calcTownLevel(Long id) {
		return calcTownLevel(getTownLevel(id));
	}
	
	public TownLevelCalcResult calcTownLevel(TownLevelDto townLevelDto) {
		TownLevelCalcResult townLevelCalcResult = townLevelResolver.levelResolve(townLevelDto);
		townLevelDto = townLevelCalcResult.getTownLevel();
		townLevelDto.setTownLevelStationRuleDto(getTownLevelStationRule(townLevelDto));
		return townLevelCalcResult;
	}
	
	public void update(TownLevelDto townLevelDto, String operator) {
		TownLevel townLevel = BeanCopy.copy(TownLevel.class, townLevelDto);
		townLevel.setModifier(operator);
		townLevel.setGmtModified(new Date());
		townLevelMapper.updateByPrimaryKeySelective(townLevel);
	}
	
	public void updatePopulation(Long id, Long population) {
		TownLevel townLevel = townLevelMapper.selectByPrimaryKey(id);
		if(townLevel != null) {
			townLevel.setTownPopulation(population);
		}
		TownLevelCalcResult townLevelCalcResult = townLevelResolver.levelResolve(BeanCopy.copy(TownLevelDto.class, townLevel));
		TownLevelDto townLevelDto = townLevelCalcResult.getTownLevel();
		townLevel.setLevel(townLevelDto.getLevel());
		townLevel.setCoverageRate(townLevelDto.getCoverageRate());
		townLevelMapper.updateByPrimaryKey(townLevel);
	}
	
	public TownLevelDto getTownLevelByTownCode(String townCode) {
		TownLevelExample example = new TownLevelExample();
		example.createCriteria().andTownCodeEqualTo(townCode);
		List<TownLevel> townLevels = townLevelMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(townLevels)) {
			return null;
		}
		TownLevelDto townLevelDto = BeanCopy.copy(TownLevelDto.class, townLevels.get(0));
		townLevelDto.setTownLevelStationRuleDto(getTownLevelStationRule(townLevelDto));
		return townLevelDto;
	}
	
	public TownLevelDto getTownLevel(Long id) {
		TownLevel townLevel = townLevelMapper.selectByPrimaryKey(id);
		if(townLevel == null) {
			return null;
		}
		TownLevelDto townLevelDto = BeanCopy.copy(TownLevelDto.class, townLevel);
		townLevelDto.setTownLevelStationRuleDto(getTownLevelStationRule(townLevelDto));
		return townLevelDto;
	}
	
	public PageDto<TownLevelDto> query(TownLevelCondition townLevelCondition){
		Preconditions.checkArgument(townLevelCondition.getPageSize() > 0 && townLevelCondition.getPageSize() <= 50, "每页数不超过50");
		TownLevelExample example = new TownLevelExample();
		Criteria criteria = example.createCriteria();
		
		if(!Strings.isNullOrEmpty(townLevelCondition.getProvinceCode())) {
			criteria.andProvinceCodeEqualTo(townLevelCondition.getProvinceCode());
		}
		if(!Strings.isNullOrEmpty(townLevelCondition.getCityCode())) {
			criteria.andCityCodeEqualTo(townLevelCondition.getCityCode());
		}
		if(!Strings.isNullOrEmpty(townLevelCondition.getCountyCode())) {
			criteria.andCountyCodeEqualTo(townLevelCondition.getCountyCode());
		}
		if(!Strings.isNullOrEmpty(townLevelCondition.getTownCode())) {
			criteria.andTownCodeEqualTo(townLevelCondition.getTownCode());
		}
		if(!Strings.isNullOrEmpty(townLevelCondition.getTownName())) {
			criteria.andTownNameLike("%" + townLevelCondition.getTownName() + "%");
		}
		PageHelper.startPage(townLevelCondition.getPageNum(), townLevelCondition.getPageSize());
        PageHelper.orderBy("province_code,city_code,county_code desc");
        List<TownLevel> townLevels = townLevelMapper.selectByExample(example);
        
        List<TownLevelDto> townLevelDtos = Lists.newArrayList();
        townLevels.forEach(t->{
        	TownLevelDto townLevelDto = BeanCopy.copy(TownLevelDto.class, t);
        	townLevelDto.setTownLevelStationRuleDto(getTownLevelStationRule(townLevelDto));
        	townLevelDtos.add(townLevelDto);
        });
        return PageDtoUtil.success((Page<TownLevel>)townLevels, townLevelDtos);
	}
	
	public int getStationNumInTown(String townCode) {
		return stationLevelExtMapper.countTownStation(townCode);
	}
}
