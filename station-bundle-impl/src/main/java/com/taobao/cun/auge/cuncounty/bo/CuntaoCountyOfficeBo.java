package com.taobao.cun.auge.cuncounty.bo;

import java.util.Optional;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyOfficeEditDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyOffice;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyOfficeMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

/**
 * 办公场地
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyOfficeBo {
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	@Resource
	private CuntaoCountyOfficeMapper cuntaoCountyOfficeMapper;
	
	void save(CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto) {
		CuntaoCountyOffice cuntaoCountyOffice = cuntaoCountyExtMapper.getCuntaoCountyOffice(cuntaoCountyOfficeEditDto.getCountyId());
		if(cuntaoCountyOffice == null) {
			insert(cuntaoCountyOfficeEditDto);
		}else {
			update(cuntaoCountyOffice, cuntaoCountyOfficeEditDto);
		}
	}
	
	private void update(CuntaoCountyOffice cuntaoCountyOffice, CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto) {
		CuntaoCountyOffice newOffice = BeanConvertUtils.convert(cuntaoCountyOfficeEditDto);
		newOffice.setCreator(cuntaoCountyOffice.getCreator());
		newOffice.setGmtCreate(cuntaoCountyOffice.getGmtCreate());
		newOffice.setId(cuntaoCountyOffice.getId());
		cuntaoCountyOfficeMapper.updateByPrimaryKey(newOffice);
	}

	private void insert(CuntaoCountyOfficeEditDto cuntaoCountyOfficeEditDto) {
		cuntaoCountyOfficeMapper.insert(BeanConvertUtils.convert(cuntaoCountyOfficeEditDto));
	}

	void delete(Long countyId, String operator) {
		cuntaoCountyExtMapper.deleteCuntaoCountyOffice(countyId, operator);
	}
	
	Optional<CuntaoCountyOffice> getCuntaoCountyOffice(Long countyId) {
		return Optional.ofNullable(BeanConvertUtils.convert(CuntaoCountyOffice.class,cuntaoCountyExtMapper.getCuntaoCountyOffice(countyId)));
	}
}
