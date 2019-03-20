package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
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
	
	public void save(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		if(!isExists(cuntaoCountyGovContactAddDto)) {
			cuntaoCountyGovContactMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovContactAddDto));
		}
	}

	private boolean isExists(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		List<CuntaoCountyGovContactDto> list = getCuntaoCountyGovContacts(cuntaoCountyGovContactAddDto.getCountyId());
		if(!CollectionUtils.isEmpty(list)) {
			for(CuntaoCountyGovContactDto dto : list) {
				CuntaoCountyGovContactAddDto old = BeanConvertUtils.convert(CuntaoCountyGovContactAddDto.class, dto);
				if(old.isContentSame(cuntaoCountyGovContactAddDto)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts(Long countyId){
		CuntaoCountyGovContactExample example = new CuntaoCountyGovContactExample();
		example.createCriteria().andCountyIdEqualTo(countyId).andIsDeletedEqualTo("n");
		example.setOrderByClause(" id desc ");
		return BeanConvertUtils.listConvert(CuntaoCountyGovContactDto.class, cuntaoCountyGovContactMapper.selectByExample(example));
	}
}
