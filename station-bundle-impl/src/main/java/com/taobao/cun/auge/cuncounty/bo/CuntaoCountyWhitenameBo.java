package com.taobao.cun.auge.cuncounty.bo;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Resource;

import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.common.utils.PageDtoUtil;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameCondition;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyWhitenameDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyWhitenameAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitename;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitenameExample;
import com.taobao.cun.auge.dal.domain.CuntaoCountyWhitenameExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyWhitenameMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 开县白名单
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyWhitenameBo {
	@Resource
	private CuntaoCountyWhitenameMapper cuntaoCountyWhitenameMapper;
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	/**
	 * 获取可开县的白名单
	 * 
	 * @return
	 */
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenames(){
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCountyIdEqualTo(0L).andStateEqualTo(CuntaoCountyWhitenameDto.STATE_ENABLED);
		return BeanConvertUtils.listConvert(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitenameMapper.selectByExample(example));
	}
	
	/**
	 * 按县CODE获取白名单
	 * @param countyCode
	 * @return
	 */
	public CuntaoCountyWhitenameDto getCuntaoCountyWhitenameByCountyCode(String countyCode){
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCountyCodeEqualTo(countyCode).andStateEqualTo(CuntaoCountyWhitenameDto.STATE_ENABLED);
		List<CuntaoCountyWhitename> cuntaoCountyWhitenames = cuntaoCountyWhitenameMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(cuntaoCountyWhitenames)) {
			return null;
		}else {
			return BeanConvertUtils.convert(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitenames.get(0));
		}
	}
	
	/**
	 * 把县点ID更新到白名单上
	 * 
	 * @param countyCode
	 * @param countyId
	 */
	public void updateCountyId(String countyCode, Long countyId) {
		CuntaoCountyWhitename record = new CuntaoCountyWhitename();
		record.setGmtModified(new Date());
		record.setCountyId(countyId);
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andCountyCodeEqualTo(countyCode);
		cuntaoCountyWhitenameMapper.updateByExampleSelective(record, example);
	}

	public void addCuntaoCountyWhitename(CuntaoCountyWhitenameAddDto cuntaoCountyWhitenameAddDto) {
		Long count = Stream.of(cuntaoCountyWhitenameAddDto)
		.filter(this::isNotExists)
		.map(BeanConvertUtils::convert)
		.peek(cuntaoCountyWhitenameMapper::insert)
		.collect(Collectors.counting());
		
		if(count == 0) {
			throw new RuntimeException("白名单中已经存在，请勿重复添加");
		}
	}
	
	private boolean isNotExists(CuntaoCountyWhitenameAddDto cuntaoCountyWhitenameAddDto) {
		String code = Strings.isNullOrEmpty(cuntaoCountyWhitenameAddDto.getCountyCode()) ? cuntaoCountyWhitenameAddDto.getCityCode() : cuntaoCountyWhitenameAddDto.getCountyCode();
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		example.createCriteria().andIsDeletedEqualTo("n").andCountyCodeEqualTo(code);
		return Iterables.isEmpty(cuntaoCountyWhitenameMapper.selectByExample(example));
	}

	public void delete(Long id, String operator) {
		Stream.of(id).map(cuntaoCountyWhitenameMapper::selectByPrimaryKey)
		.filter(c->c.getCountyId().equals(0L))
		.peek(c->{
			c.setGmtModified(new Date());
			c.setModifier(operator);
			c.setIsDeleted("y");
			cuntaoCountyWhitenameMapper.updateByPrimaryKey(c);
		}).collect(Collectors.counting());
	}

	public void toggle(Long id, String operator) {
		Stream.of(id).map(cuntaoCountyWhitenameMapper::selectByPrimaryKey).peek(c->{
			c.setGmtModified(new Date());
			c.setModifier(operator);
			c.setState(c.getState().equals(CuntaoCountyWhitenameDto.STATE_ENABLED)?CuntaoCountyWhitenameDto.STATE_DISABLED:CuntaoCountyWhitenameDto.STATE_ENABLED);
			cuntaoCountyWhitenameMapper.updateByPrimaryKey(c);
		}).collect(Collectors.counting());
	}

	public PageDto<CuntaoCountyWhitenameDto> query(CuntaoCountyWhitenameCondition condition) {
		Preconditions.checkArgument(condition.getPageSize() > 0 && condition.getPageSize() <= 50, "每页数不超过50");
		CuntaoCountyWhitenameExample example = new CuntaoCountyWhitenameExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeletedEqualTo("n");
		if(!Strings.isNullOrEmpty(condition.getProvinceCode())) {
			criteria.andProvinceCodeEqualTo(condition.getProvinceCode());
		}
		if(!Strings.isNullOrEmpty(condition.getCityCode())) {
			criteria.andCityCodeEqualTo(condition.getCityCode());
		}
		if(!Strings.isNullOrEmpty(condition.getCountyCode())) {
			criteria.andCountyCodeEqualTo(condition.getCountyCode());
		}
		if(!Strings.isNullOrEmpty(condition.getCountyName())) {
			criteria.andCountyNameLike("%" + condition.getCountyName() + "%");
		}
		criteria.andCountyIdEqualTo(0L);
		PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        PageHelper.orderBy("id desc");
        List<CuntaoCountyWhitename> cuntaoCountyWhitenames = cuntaoCountyWhitenameMapper.selectByExample(example);
        
        return PageDtoUtil.success((Page<CuntaoCountyWhitename>)cuntaoCountyWhitenames, 
        		BeanConvertUtils.listConvert(CuntaoCountyWhitenameDto.class, cuntaoCountyWhitenames));
	}
	
	public List<CuntaoCountyWhitenameDto> getCuntaoCountyWhitenamesByCodes(List<String> codes){
		if(CollectionUtils.isEmpty(codes)){
			return Lists.newArrayList();
		}else {
			return BeanConvertUtils.listConvert(CuntaoCountyWhitenameDto.class,
					cuntaoCountyExtMapper.getCuntaoCountyWhitenamesByCodes(codes));
		}
	}
}
