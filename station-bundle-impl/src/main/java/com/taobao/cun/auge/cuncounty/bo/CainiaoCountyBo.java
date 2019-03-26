package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CainiaoCountyDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CainiaoCountyEditDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
import com.taobao.cun.auge.dal.mapper.CainiaoCountyMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 菜鸟县仓
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CainiaoCountyBo {
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	@Resource
	private CainiaoCountyMapper cainiaoCountyMapper;
	
	void save(CainiaoCountyEditDto cainiaoCountyEditDto) {
		//如果没有菜鸟县仓信息则直接返回
		if(cainiaoCountyEditDto == null) {
			return;
		}
		CainiaoCounty cainiaoCounty = cuntaoCountyExtMapper.getCainiaoCounty(cainiaoCountyEditDto.getCountyId());
		if(cainiaoCounty == null) {
			cainiaoCounty = insert(cainiaoCountyEditDto);
		}else {
			update(cainiaoCounty, cainiaoCountyEditDto);
		}
	}

	private void update(CainiaoCounty cainiaoCounty, CainiaoCountyEditDto cainiaoCountyEditDto) {
		CainiaoCounty newCainiaoCounty = BeanConvertUtils.convert(cainiaoCountyEditDto);
		newCainiaoCounty.setCreator(cainiaoCounty.getCreator());
		newCainiaoCounty.setGmtCreate(cainiaoCounty.getGmtCreate());
		newCainiaoCounty.setId(cainiaoCounty.getId());
		cainiaoCountyMapper.updateByPrimaryKey(newCainiaoCounty);
	}

	private CainiaoCounty insert(CainiaoCountyEditDto cainiaoCountyEditDto) {
		CainiaoCounty cainiaoCounty = BeanConvertUtils.convert(cainiaoCountyEditDto);
		cainiaoCountyMapper.insert(cainiaoCounty);
		return cainiaoCounty;
	}
	
	CainiaoCountyDto getCainiaoCountyDto(Long countyId) {
		return BeanConvertUtils.convert(CainiaoCountyDto.class, cuntaoCountyExtMapper.getCainiaoCounty(countyId));
	}
}
