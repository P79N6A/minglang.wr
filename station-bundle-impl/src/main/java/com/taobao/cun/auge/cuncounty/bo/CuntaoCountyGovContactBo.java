package com.taobao.cun.auge.cuncounty.bo;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContactDto;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyGovContactAddDto;
import com.taobao.cun.auge.cuncounty.utils.BeanConvertUtils;
import com.taobao.cun.auge.dal.domain.CuntaoCountyGovContact;
import com.taobao.cun.auge.dal.mapper.CuntaoCountyGovContactMapper;
import com.taobao.cun.auge.dal.mapper.ext.CuntaoCountyExtMapper;

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
	@Resource
	private CuntaoCountyExtMapper cuntaoCountyExtMapper;
	
	@Transactional(rollbackFor=Throwable.class)
	public void save(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		if(!isExists(cuntaoCountyGovContactAddDto)) {
			cuntaoCountyGovContactMapper.insert(BeanConvertUtils.convert(cuntaoCountyGovContactAddDto));
		}
	}

	private boolean isExists(CuntaoCountyGovContactAddDto cuntaoCountyGovContactAddDto) {
		List<CuntaoCountyGovContact> list = cuntaoCountyExtMapper.getCuntaoCountyGovContacts(cuntaoCountyGovContactAddDto.getCountyId());
		if(!CollectionUtils.isEmpty(list)) {
			for(CuntaoCountyGovContact contact : list) {
				CuntaoCountyGovContactAddDto old = BeanConvertUtils.convert(CuntaoCountyGovContactAddDto.class, contact);
				if(old.isContentSame(cuntaoCountyGovContactAddDto)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts(Long countyId){
		return BeanConvertUtils.listConvert(CuntaoCountyGovContactDto.class, cuntaoCountyExtMapper.getCuntaoCountyGovContacts(countyId));
	}
}
