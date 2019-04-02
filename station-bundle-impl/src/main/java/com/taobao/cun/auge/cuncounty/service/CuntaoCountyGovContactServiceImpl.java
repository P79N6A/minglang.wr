package com.taobao.cun.auge.cuncounty.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyGovContactBo;
import com.taobao.cun.auge.cuncounty.dto.CuntaoCountyGovContactDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

/**
 * 县政府联系人
 * 
 * @author chengyu.zhoucy
 *
 */
@Validated
@HSFProvider(serviceInterface = CuntaoCountyGovContactService.class)
public class CuntaoCountyGovContactServiceImpl implements CuntaoCountyGovContactService {
	@Resource
	private CuntaoCountyGovContactBo cuntaoCountyGovContactBo;
	
	@Override
	public List<CuntaoCountyGovContactDto> getCuntaoCountyGovContacts(Long countyId) {
		return cuntaoCountyGovContactBo.getCuntaoCountyGovContacts(countyId);
	}

}
