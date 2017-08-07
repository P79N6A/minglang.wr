package com.taobao.cun.auge.user.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.county.CountyService;
import com.taobao.cun.auge.org.dto.CuntaoUserRole;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;

@Service("CuntaoUserOrgService")
@HSFProvider(serviceInterface = CountyService.class)
public class CuntaoUserOrgServiceImpl implements CuntaoUserOrgService{

	@Autowired
	CuntaoUserRoleService cuntaoUserRoleService;
	 
	public Boolean checkOrg(String empId, String cuntaoFullIdPath) {
		if(StringUtils.isEmpty(empId)||StringUtils.isEmpty(cuntaoFullIdPath)){
			return false;
		}
		List<CuntaoUserRole> userRoles=cuntaoUserRoleService.getCuntaoUserRoles(empId);
		if(userRoles==null||userRoles.size()==0){
			return false;
		}
		for(CuntaoUserRole userRole:userRoles){
			if(cuntaoFullIdPath.equals(userRole.getFullIdPath())){
				return true;
			}
		}
		return false;
	}

}
