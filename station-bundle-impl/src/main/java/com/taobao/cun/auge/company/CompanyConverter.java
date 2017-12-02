package com.taobao.cun.auge.company;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.company.dto.CuntaoCompanyDto;
import com.taobao.cun.auge.company.dto.CuntaoCompanyType;
import com.taobao.cun.auge.dal.domain.CuntaoCompany;

public class CompanyConverter {

	public static  CuntaoCompanyDto convert2CuntaoCompanyDto(CuntaoCompany cuntaoCompany) {
		if(cuntaoCompany == null){
			return null;
		}
		CuntaoCompanyDto cuntaoCompanyDto = new CuntaoCompanyDto();
		cuntaoCompanyDto.setCompanyName(cuntaoCompany.getCompanyName());
		cuntaoCompanyDto.setMobile(cuntaoCompany.getMobile());
		cuntaoCompanyDto.setAlipayOutUser(cuntaoCompany.getAlipayOutUser());
		cuntaoCompanyDto.setTaobaoNick(cuntaoCompany.getTaobaoNick());
		cuntaoCompanyDto.setType(CuntaoCompanyType.valueOf(cuntaoCompany.getType()));
		cuntaoCompanyDto.setRemark(cuntaoCompanyDto.getRemark());
		return cuntaoCompanyDto;
	}
	
	
	public static  List<CuntaoCompanyDto> convert2CuntaoCompanyDtoList(List<CuntaoCompany> cuntaoCompany) {
		if(cuntaoCompany == null){
			return Lists.newArrayList();
		}
		return cuntaoCompany.stream().map(company -> convert2CuntaoCompanyDto(company)).collect(Collectors.toList());
	}
}
