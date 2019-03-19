package com.taobao.cun.auge.cuncounty.service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import com.taobao.cun.auge.cuncounty.bo.CuntaoCountyWriteBo;
import com.taobao.cun.auge.cuncounty.dto.edit.CuntaoCountyAddDto;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Validated
@HSFProvider(serviceInterface = CuntaoCountyWriteService.class)
public class CuntaoCountyWriteServiceImpl implements CuntaoCountyWriteService{
	@Resource
	private CuntaoCountyWriteBo cuntaoCountyWriteBo;
	
	@Override
	public Long createCuntaoCounty(CuntaoCountyAddDto cuntaoCountyAddDto) {
		return cuntaoCountyWriteBo.createCuntaoCounty(cuntaoCountyAddDto);
	}

}
