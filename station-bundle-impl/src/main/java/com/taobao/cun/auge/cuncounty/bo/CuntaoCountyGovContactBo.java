package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContactDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContactAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContactExample;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovContactMapper;

/**
 * 县政府联系人
 * 
 * @author chengyu.zhoucy
 *
 */
@Component
public class CuntaoCountyGovContactBo {
	@Resource
	private CuntaoCountyGovContactMapper cuntaoCountyGovContactMapper;
	
	public void addContact(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		cuntaoCountyGovContactMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovContactAddDto));
	}
	
	public List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts(Long countyId){
		CuntaoCountyGovContactExample example = new CuntaoCountyGovContactExample();
		example.createCriteria().andCountyIdEqualTo(countyId).andIsDeletedEqualTo("n");
		example.setOrderByClause(" id desc ");
		return BeanConvertUtils.listConvert(CuntaoCountyGovContactDto.class, cuntaoCountyGovContactMapper.selectByExample(example));
	}
}
