package com.taobao.cun.auge.user.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taobao.cun.auge.common.PageDto;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrg;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrgExample;
import com.taobao.cun.auge.dal.domain.CuntaoUserOrgExample.Criteria;
import com.taobao.cun.auge.dal.mapper.CuntaoUserOrgMapper;
import com.taobao.cun.auge.org.dto.CuntaoUserRole;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgListDto;
import com.taobao.cun.auge.user.dto.CuntaoUserOrgQueryDto;
import com.taobao.cun.auge.user.dto.CuntaoUserStausEnum;
import com.taobao.cun.auge.user.dto.CuntaoUserTypeEnum;
import com.taobao.cun.auge.user.service.CuntaoUserOrgService;
import com.taobao.cun.auge.user.service.CuntaoUserRoleService;
import com.taobao.hsf.app.spring.util.annotation.HSFProvider;
import com.taobao.util.CollectionUtil;

@Service("CuntaoUserOrgService")
@HSFProvider(serviceInterface = CuntaoUserOrgService.class)
public class CuntaoUserOrgServiceImpl implements CuntaoUserOrgService{

	@Autowired
	CuntaoUserRoleService cuntaoUserRoleService;
	
	@Autowired
	CuntaoUserOrgMapper cuntaoUserOrgMapper;
	
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

	@Override
	public PageDto<CuntaoUserOrgListDto> queryUsersByPage(CuntaoUserOrgQueryDto queryCondition) {
		if (queryCondition.getUserType() != null
				&& queryCondition.getUserType().getCode()
						.equals(CuntaoUserTypeEnum.BUC.getCode())) {
			if (CollectionUtil.isEmpty(queryCondition.getOrgIds())) {
				throw new RuntimeException(
						"cuntaoUserOrgQueryVo.orgids is null!");
			}
		}
		if (queryCondition.getPage() < 0) {
            queryCondition.setPage(0);
        }
        if (queryCondition.getPageSize() <= 0) {
            queryCondition.setPageSize(20);
        }
        if (queryCondition.getPageSize() > 100) {
            queryCondition.setPageSize(100);
        }
		PageDto<CuntaoUserOrgListDto> returnModel= new PageDto<CuntaoUserOrgListDto>();
		CuntaoUserOrgExample example=new CuntaoUserOrgExample();
		Criteria c = example.createCriteria().andIsDeletedEqualTo("n");
		if(StringUtils.isNotEmpty(queryCondition.getLoginId())){
			c.andLoginIdEqualTo(queryCondition.getLoginId());
		}
		if(StringUtils.isNotEmpty(queryCondition.getUserName())){
			c.andUserNameEqualTo(queryCondition.getUserName());
		}
		if(queryCondition.getUserType()!=null){
			c.andUserTypeEqualTo(queryCondition.getUserType().getCode());
		}
		if(queryCondition.getUserStatuses()!=null){
			List<String> status=new ArrayList<String>();
			for(CuntaoUserStausEnum en:queryCondition.getUserStatuses()){
				status.add(en.getCode());
			}
			c.andStatusIn(status);
		}
		int total=cuntaoUserOrgMapper.countByExample(example);
		List<CuntaoUserOrg> userOrgs = cuntaoUserOrgMapper.selectByExample(example);
		List<CuntaoUserOrgListDto> rst=new ArrayList<CuntaoUserOrgListDto>();
		for(CuntaoUserOrg userOrg:userOrgs){
			rst.add(convert(userOrg));
		}
	    returnModel.setItems(rst);
		returnModel.setTotal(new Long(total));
		return returnModel;
	}

	private CuntaoUserOrgListDto convert(CuntaoUserOrg userOrg) {
		CuntaoUserOrgListDto cuntaoUserOrgVo = new CuntaoUserOrgListDto();
		cuntaoUserOrgVo.setUserName(userOrg.getUserName());
		cuntaoUserOrgVo.setId(userOrg.getId());
		cuntaoUserOrgVo.setOrgId(userOrg.getOrgId());
		cuntaoUserOrgVo.setStartTime(userOrg.getStartTime());
		cuntaoUserOrgVo.setEndTime(userOrg.getEndTime());
		cuntaoUserOrgVo.setId(userOrg.getId());
		cuntaoUserOrgVo.setUserStatus(CuntaoUserStausEnum.valueof(userOrg
				.getStatus()));
		cuntaoUserOrgVo.setWorkNo(userOrg.getLoginId());
		cuntaoUserOrgVo.setMobile(toMap(userOrg.getFeature()).get("mobile"));
		String divisionId = toMap(userOrg.getFeature()).get("divisionId");
		if (StringUtils.isNotEmpty(divisionId)) {
			cuntaoUserOrgVo.setDivisionId(new Long(divisionId));
		}
		return cuntaoUserOrgVo;
	}
	
	private Map<String,String> toMap(String feature){
		Map<String,String> valueMap=new HashMap<String, String>();
		if(!StringUtils.isBlank(feature)){
            String[] values=feature.split(";");
            if(values.length>0){
            	for(String value:values){
            		String[] keyValue=value.split(":");
            		if(keyValue.length==2){
                		valueMap.put(keyValue[0],keyValue[1]);
            		}
            	}
            }
        }
		return valueMap;
	}

}
