package com.taobao.cun.auge.cuncounty.bo;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.edit.CainiaoCountyEditDto;
import com.taobao.cun.auge.dal.domain.CainiaoCounty;
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
	
	void save(CainiaoCountyEditDto cainiaoCountyEditDto) {
		CainiaoCounty cainiaoCounty = cuntaoCountyExtMapper.getCainiaoCounty(cainiaoCountyEditDto.getCountyId());
		if(cainiaoCounty == null) {
			insert(cainiaoCountyEditDto);
		}else {
			update(cainiaoCounty, cainiaoCountyEditDto);
		}
	}

	private void update(CainiaoCounty cainiaoCounty, CainiaoCountyEditDto cainiaoCountyEditDto) {
		
	}

	private void insert(CainiaoCountyEditDto cainiaoCountyEditDto) {
		
	}
}
