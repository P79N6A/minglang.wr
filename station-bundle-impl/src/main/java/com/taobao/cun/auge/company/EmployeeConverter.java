package com.taobao.cun.auge.company;

import java.util.List;
import java.util.stream.Collectors;

import com.taobao.cun.auge.company.dto.CuntaoEmployeeDto;
import com.taobao.cun.auge.dal.domain.CuntaoEmployee;

public class EmployeeConverter {

	public static  CuntaoEmployeeDto convert2CuntaoEmployeeDto(CuntaoEmployee cuntaoEmployee){
		if(cuntaoEmployee == null){
			return null;
		}
		CuntaoEmployeeDto cuntaoEmployeeDto = new CuntaoEmployeeDto();
		cuntaoEmployeeDto.setId(cuntaoEmployee.getId());
		cuntaoEmployeeDto.setMobile(cuntaoEmployee.getMobile());
		cuntaoEmployeeDto.setName(cuntaoEmployee.getName());
		cuntaoEmployeeDto.setTaobaoUserId(cuntaoEmployee.getTaobaoUserId());
		cuntaoEmployeeDto.setTaobaoNick(cuntaoEmployee.getTaobaoNick());
		return cuntaoEmployeeDto;
	}
	
	
	public static  List<CuntaoEmployeeDto> convert2CuntaoEmployeeDtoList(List<CuntaoEmployee> cuntaoEmployees){
		if(cuntaoEmployees == null){
			return null;
		}
		return cuntaoEmployees.stream().map(employee -> convert2CuntaoEmployeeDto(employee)).collect(Collectors.toList());
	}
}
