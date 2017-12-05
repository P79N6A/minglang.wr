package com.taobao.cun.auge.company;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.taobao.cun.auge.company.dto.CuntaoServiceVendorDto;
import com.taobao.cun.auge.company.dto.CuntaoVendorType;
import com.taobao.cun.auge.dal.domain.CuntaoServiceVendor;

public class VendorConverter {

	public static  CuntaoServiceVendorDto convert2CuntaoVendorDto(CuntaoServiceVendor cuntaoServiceVendor) {
		if(cuntaoServiceVendor == null){
			return null;
		}
		CuntaoServiceVendorDto cuntaoServiceVendorDto = new CuntaoServiceVendorDto();
		cuntaoServiceVendorDto.setId(cuntaoServiceVendor.getId());
		cuntaoServiceVendorDto.setCompanyName(cuntaoServiceVendor.getCompanyName());
		cuntaoServiceVendorDto.setMobile(cuntaoServiceVendor.getMobile());
		cuntaoServiceVendorDto.setAlipayOutUser(cuntaoServiceVendor.getAlipayOutUser());
		cuntaoServiceVendorDto.setTaobaoNick(cuntaoServiceVendor.getTaobaoNick());
		cuntaoServiceVendorDto.setType(CuntaoVendorType.valueOf(cuntaoServiceVendor.getType()));
		cuntaoServiceVendorDto.setRemark(cuntaoServiceVendor.getRemark());
		return cuntaoServiceVendorDto;
	}
	
	
	public static  List<CuntaoServiceVendorDto> convert2CuntaoVendorDtoList(List<CuntaoServiceVendor> cuntaoServiceVendor) {
		if(cuntaoServiceVendor == null){
			return Lists.newArrayList();
		}
		  return cuntaoServiceVendor.stream().map(vendor -> convert2CuntaoVendorDto(vendor)).collect(Collectors.toList());
	}
}
