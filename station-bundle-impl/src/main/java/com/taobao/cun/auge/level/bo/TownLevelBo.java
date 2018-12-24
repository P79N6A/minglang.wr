package com.taobao.cun.auge.level.bo;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.BeanCopy;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.dal.domain.TownLevel;
import com.taobao.cun.auge.dal.domain.TownLevelExample;
import com.taobao.cun.auge.dal.domain.TownLevelExample.Criteria;
import com.taobao.cun.auge.dal.domain.TownLevelRule;
import com.taobao.cun.auge.dal.domain.TownLevelRuleExample;
import com.taobao.cun.auge.dal.mapper.TownLevelMapper;
import com.taobao.cun.auge.dal.mapper.TownLevelRuleMapper;
import com.taobao.cun.auge.level.dto.TownLevelCondition;
import com.taobao.cun.auge.level.dto.TownLevelDto;
import com.taobao.cun.auge.level.dto.TownLevelRuleDto;

@Component
public class TownLevelBo implements InitializingBean{
	@Resource
	private TownLevelMapper townLevelMapper;
	@Resource
	private TownLevelRuleMapper townLevelRuleMapper;
	@Resource
	private TownLevelResolver townLevelResolver;
	
	private LoadingCache<String, List<TownLevelRuleDto>> loadingCache;
	
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
		TownLevelDto townLevelDto = townLevelResolver.levelResolve(BeanCopy.copy(TownLevelDto.class, townLevel));
		townLevel.setLevel(townLevelDto.getLevel());
		townLevelMapper.updateByPrimaryKey(townLevel);
	}
	
	public TownLevelDto getTownLevel(Long id) {
		TownLevel townLevel = townLevelMapper.selectByPrimaryKey(id);
		if(townLevel == null) {
			return null;
		}
		TownLevelDto townLevelDto = BeanCopy.copy(TownLevelDto.class, townLevel);
		townLevelDto.setTownLevelRuleDtos(getTownLevelRuleDtos(townLevelDto.getLevel()));
		return townLevelDto;
	}
	
	public PageDto<TownLevelDto> query(TownLevelCondition townLevelCondition){
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
			criteria.andTownNameLike(townLevelCondition.getTownName());
		}
		PageHelper.startPage(townLevelCondition.getPageNum(), townLevelCondition.getPageSize());
        PageHelper.orderBy("province_code,city_code,county_code desc");
        List<TownLevel> townLevels = townLevelMapper.selectByExample(example);
        
        List<TownLevelDto> townLevelDtos = Lists.newArrayList();
        townLevels.forEach(t->{
        	TownLevelDto townLevelDto = BeanCopy.copy(TownLevelDto.class, t);
        	townLevelDto.setTownLevelRuleDtos(getTownLevelRuleDtos(townLevelDto.getLevel()));
        	townLevelDtos.add(townLevelDto);
        });
        return PageDtoUtil.success((Page<TownLevel>)townLevels, townLevelDtos);
	}
	
	private List<TownLevelRuleDto> getTownLevelRuleDtos(String level){
		try {
			return loadingCache.get(level);
		} catch (ExecutionException e) {
			return Lists.newArrayList();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		loadingCache = CacheBuilder.newBuilder().expireAfterWrite(2, TimeUnit.MINUTES).build(new CacheLoader<String, List<TownLevelRuleDto>>() {

			@Override
			public List<TownLevelRuleDto> load(String key) throws Exception {
				TownLevelRuleExample example = new TownLevelRuleExample();
				example.createCriteria().andLevelEqualTo(key);
				List<TownLevelRule> townLevelRules = townLevelRuleMapper.selectByExample(example);
				return townLevelRules == null ? Lists.newArrayList() : BeanCopy.copyList(TownLevelRuleDto.class, townLevelRules);
			}
			
		});
	}
	
}
